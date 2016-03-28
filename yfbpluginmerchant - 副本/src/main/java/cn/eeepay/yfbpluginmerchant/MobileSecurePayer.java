package cn.eeepay.yfbpluginmerchant;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import cn.eeepay.android.yfbplugin.IRemoteServiceCallback;
import cn.eeepay.android.yfbplugin.Ieeepay;

/**
 * 
 * @author xuqingfeng77
 * @date 2013-10-15
 * @function
 */
public class MobileSecurePayer {
	static String TAG = "MobileSecurePayer";

	Integer lock = 0;
	Ieeepay mYSPay = null;
	boolean mbPaying = false;
	Handler mHandler = null;
	int inWhat = 0;
	Activity mActivity = null;

	private ServiceConnection mYSPayConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			//
			// wake up the binder to continue.
			synchronized (lock) {
				mYSPay = Ieeepay.Stub.asInterface(service);
				lock.notify();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			mYSPay = null;
		}
	};

	/**
	 * 
	 * @param strOrderInfo
	 * @param callback
	 * @param myWhat
	 * @param activity
	 * @return
	 */
	public boolean pay(final String strOrderInfo,
			final Handler callback, final int myWhat,
			final Activity activity) {
		if (mbPaying)
			return false;
		mbPaying = true;

		//
		mActivity = activity;
		mHandler = callback;
		inWhat = myWhat;
		// bind the service.
		if (mYSPay == null) {
			mActivity.getApplicationContext().bindService(
					new Intent(Ieeepay.class.getName()), mYSPayConnection,
					Context.BIND_AUTO_CREATE);
		}
		// else ok.

		new Thread(new Runnable() {
			public void run() {
				try {
					// wait for the service bind operation to completely
					// finished.
					// Note: this is important,otherwise the next mYSPay.Pay()
					// will fail.
					synchronized (lock) {
						if (mYSPay == null)
							lock.wait();
					}
					// call the MobileSecurePay service.
					String strRet = mYSPay.Pay(strOrderInfo);
					BaseHelper.log(TAG, "After Pay: " + strRet);

					// register a Callback for the service.
					mYSPay.registerCallback(mCallback);

				} catch (Exception e) {
					e.printStackTrace();
					YSPayAssist.getInstance().closeProgress();// 
					// send the result back to caller.
					Message msg = new Message();
					msg.what = myWhat;
					msg.obj = e.toString();
					mHandler.sendMessage(msg);
				}
			}
		}).start();

		return true;
	}

	/**
	 * This implementation is used to receive callbacks from the remote service.
	 *
	 */
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		/**
		 * This is called by the remote service regularly to tell us about new
		 * values. Note that IPC calls are dispatched through a thread pool
		 * running in each process, so the code executing here will NOT be
		 * running in our main thread like most other things -- so, to update
		 * the UI, we need to use a Handler to hop over there.
		 *
		 */
		public void startActivity(String packageName, String className,
				int iCallingPid, Bundle bundle) throws RemoteException {
			Intent intent = new Intent(Intent.ACTION_MAIN, null);

			if (bundle == null)
				bundle = new Bundle();
			// else ok.

			try {
				bundle.putInt("CallingPid", iCallingPid);// icallingpid
				intent.putExtras(bundle);
			} catch (Exception e) {
				e.printStackTrace();
			}

			intent.setClassName(packageName, className);
			mActivity.startActivity(intent);
		}

		/**
		 * when the msp loading dialog gone, call back this method.
		 */
		@Override
		public boolean isHideLoadingScreen() throws RemoteException {
			return false;
		}

		/**
		 * when the current trade is finished or cancelled, call back this
		 * method.
		 */
		@Override
		public void payEnd(boolean arg0, String arg1) throws RemoteException {
			// set the flag to indicate that we have finished.
			// unregister the Callback, and unbind the service.
			//这个方法是运行在子线程
			mbPaying = false;
			mYSPay.unregisterCallback(mCallback);
			mActivity.getApplicationContext().unbindService(mYSPayConnection);
			YSPayAssist.getInstance().closeProgress();//
			// send the result back to caller.
			Message msg = new Message();
			msg.what = inWhat;
			msg.obj = arg1;
			mHandler.sendMessage(msg);
		}

	};
}