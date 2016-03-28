/**
 * @author xuqingfeng
 * @date 2013-8-30
 * @function 
 */
package cn.eeepay.android.yfbplugin;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IRemoteServiceCallback extends IInterface
{
  public abstract void startActivity(String paramString1, String paramString2, int paramInt, Bundle paramBundle)
    throws RemoteException;

  public abstract void payEnd(boolean paramBoolean, String paramString)
    throws RemoteException;

  public abstract boolean isHideLoadingScreen()
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IRemoteServiceCallback
  {
    private static final String DESCRIPTOR = "cn.eeepay.android.yfbplugin.IRemoteServiceCallback";
    static final int TRANSACTION_startActivity = 1;
    static final int TRANSACTION_payEnd = 2;
    static final int TRANSACTION_isHideLoadingScreen = 3;

    public Stub()
    {
      attachInterface(this, "cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
    }

    public static IRemoteServiceCallback asInterface(IBinder obj)
    {
      if (obj == null) {
        return null;
      }
      IInterface iin = obj.queryLocalInterface("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
      if ((iin != null) && ((iin instanceof IRemoteServiceCallback))) {
        return (IRemoteServiceCallback)iin;
      }
      return new Proxy(obj);
    }

    public IBinder asBinder() {
      return this;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
      switch (code)
      {
      case 1598968902:
        reply.writeString("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
        return true;
      case 1:
        data.enforceInterface("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");

        String _arg0 = data.readString();

        String _arg1 = data.readString();

        int _arg2 = data.readInt();
        Bundle _arg3;
        if (data.readInt() != 0) {
          _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
        }
        else {
          _arg3 = null;
        }
        startActivity(_arg0, _arg1, _arg2, _arg3);
        reply.writeNoException();
        return true;
      case 2:
        data.enforceInterface("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");

        boolean _arg4 = data.readInt() != 0;

        String _arg5 = data.readString();
        payEnd(_arg4, _arg5);
        reply.writeNoException();
        return true;
      case 3:
        data.enforceInterface("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
        boolean _result = isHideLoadingScreen();
        reply.writeNoException();
        reply.writeInt(_result ? 1 : 0);
        return true;
      }

      return super.onTransact(code, data, reply, flags);
    }
    private static class Proxy implements IRemoteServiceCallback {
      private IBinder mRemote;

      Proxy(IBinder remote) {
        this.mRemote = remote;
      }

      public IBinder asBinder() {
        return this.mRemote;
      }

      public String getInterfaceDescriptor() {
        return "cn.eeepay.android.yfbplugin.IRemoteServiceCallback";
      }

      public void startActivity(String packageName, String className, int iCallingPid, Bundle bundle) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
          _data.writeInterfaceToken("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
          _data.writeString(packageName);
          _data.writeString(className);
          _data.writeInt(iCallingPid);
          if (bundle != null) {
            _data.writeInt(1);
            bundle.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          this.mRemote.transact(1, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }

      public void payEnd(boolean isPayOk, String resultStatus) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
          _data.writeInterfaceToken("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
          _data.writeInt(isPayOk ? 1 : 0);
          _data.writeString(resultStatus);
          this.mRemote.transact(2, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public boolean isHideLoadingScreen() throws RemoteException { Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken("cn.eeepay.android.yfbplugin.IRemoteServiceCallback");
          this.mRemote.transact(3, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt() != 0;
        }
        finally
        {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
  }
}