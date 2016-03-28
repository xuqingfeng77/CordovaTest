package cn.eeepay.yfbpluginmerchant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * @author xuqingfeng77
 * @date 2013-10-14
 * @function
 */
public class YSPayAssist {
	private ProgressDialog mProgress = null;
	private static YSPayAssist mYsPayAssist;
	private IActionCallBack mActionCallback;

	public static YSPayAssist getInstance() {
		if (mYsPayAssist == null) {
			mYsPayAssist = new YSPayAssist();
		}
		return mYsPayAssist;
	}

	Handler handler = new Handler() {

		/*
		 * overriding methods
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String result=(String) msg.obj;
			mActionCallback.callBack(result);
		}

	};
    /**
     * 
     * @param ctx 上下文
     * @param callback 注册回调
     * @param orderinfo 订单信息
     * @param apkname 移联支付SDK名字
     */
	public void startPay(Context ctx, IActionCallBack callback, String orderinfo,
			String apkname) {
		mActionCallback = callback;
		// check to see if the MobileSecurePay is already installed.
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(ctx);
		boolean isMobile_spExist = mspHelper.detectMobile_sp(apkname);
		if (!isMobile_spExist) {
			// mPosition = position;
			return;
		}

		// start pay for this order.
		try {
			// start the pay.
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp
					.pay(orderinfo, handler, YSId.RQF_PAY, (Activity) ctx);

			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				closeProgress();
				mProgress = BaseHelper.showProgress(ctx, null, "正在请求支付...",
						false, true);
			} else
			{
				
			}
		} catch (Exception ex) {
			mActionCallback.callBack("fail");
			Toast.makeText(ctx, "远程回调失败", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// �رս�ȿ�
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
