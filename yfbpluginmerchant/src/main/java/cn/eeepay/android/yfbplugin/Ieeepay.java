/**
 * @author xuqingfeng
 * @date 2013-8-30
 * @function 
 */
package cn.eeepay.android.yfbplugin;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface Ieeepay extends IInterface {
	public abstract String Pay(String paramString) throws RemoteException;

	public abstract String test() throws RemoteException;

	public abstract void registerCallback(
			IRemoteServiceCallback paramIRemoteServiceCallback)
			throws RemoteException;

	public abstract void unregisterCallback(
			IRemoteServiceCallback paramIRemoteServiceCallback)
			throws RemoteException;

	public abstract String prePay(String paramString) throws RemoteException;

	public static abstract class Stub extends Binder implements Ieeepay {
		private static final String DESCRIPTOR = "cn.eeepay.android.yfbplugin.Ieeepay";
		static final int TRANSACTION_Pay = 1;
		static final int TRANSACTION_test = 2;
		static final int TRANSACTION_registerCallback = 3;
		static final int TRANSACTION_unregisterCallback = 4;
		static final int TRANSACTION_prePay = 5;

		public Stub() {
			attachInterface(this, "cn.eeepay.android.yfbplugin.Ieeepay");
		}

		public static Ieeepay asInterface(IBinder obj) {
			if (obj == null) {
				return null;
			}
			IInterface iin = obj
					.queryLocalInterface("cn.eeepay.android.yfbplugin.Ieeepay");
			if ((iin != null) && ((iin instanceof Ieeepay))) {
				return (Ieeepay) iin;
			}
			return new Proxy(obj);
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			switch (code) {
			case 1598968902:
				reply.writeString("cn.eeepay.android.yfbplugin.Ieeepay");
				return true;
			case 1:
				data.enforceInterface("cn.eeepay.android.yfbplugin.Ieeepay");

				String _arg0 = data.readString();
				String _result = Pay(_arg0);
				reply.writeNoException();
				reply.writeString(_result);
				return true;
			case 2:
				data.enforceInterface("cn.eeepay.android.yfbplugin.Ieeepay");
				String _result2 = test();
				reply.writeNoException();
				reply.writeString(_result2);
				return true;
			case 3:
				data.enforceInterface("cn.eeepay.android.yfbplugin.Ieeepay");

				IRemoteServiceCallback _arg1 = IRemoteServiceCallback.Stub
						.asInterface(data.readStrongBinder());
				registerCallback(_arg1);
				reply.writeNoException();
				return true;
			case 4:
				data.enforceInterface("cn.eeepay.android.yfbplugin.Ieeepay");

				IRemoteServiceCallback _arg2 = IRemoteServiceCallback.Stub
						.asInterface(data.readStrongBinder());
				unregisterCallback(_arg2);
				reply.writeNoException();
				return true;
			case 5:
				data.enforceInterface("cn.eeepay.android.yfbplugin.Ieeepay");

				String _arg3 = data.readString();
				String _result3 = prePay(_arg3);
				reply.writeNoException();
				reply.writeString(_result3);
				return true;
			}

			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements Ieeepay {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				this.mRemote = remote;
			}

			public IBinder asBinder() {
				return this.mRemote;
			}

			public String getInterfaceDescriptor() {
				return "cn.eeepay.android.yfbplugin.Ieeepay";
			}

			public String Pay(String strInfo) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				String _result;
				try {
					_data.writeInterfaceToken("cn.eeepay.android.yfbplugin.Ieeepay");
					_data.writeString(strInfo);
					this.mRemote.transact(1, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public String test() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				String _result;
				try {
					_data.writeInterfaceToken("cn.eeepay.android.yfbplugin.Ieeepay");
					this.mRemote.transact(2, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public void registerCallback(IRemoteServiceCallback cb)
					throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken("cn.eeepay.android.yfbplugin.Ieeepay");
					_data.writeStrongBinder(cb != null ? cb.asBinder() : null);
					this.mRemote.transact(3, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			public void unregisterCallback(IRemoteServiceCallback cb)
					throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken("cn.eeepay.android.yfbplugin.Ieeepay");
					_data.writeStrongBinder(cb != null ? cb.asBinder() : null);
					this.mRemote.transact(4, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			public String prePay(String orderInfo) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				String _result;
				try {
					_data.writeInterfaceToken("cn.eeepay.android.yfbplugin.Ieeepay");
					_data.writeString(orderInfo);
					this.mRemote.transact(5, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}
		}
	}
}