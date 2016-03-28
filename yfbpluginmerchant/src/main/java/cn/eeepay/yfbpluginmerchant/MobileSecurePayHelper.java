package cn.eeepay.yfbpluginmerchant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.eeepay.yfbpluginmerchant.FileDownloader.IDownloadProgress;

/**
 * 
 * @author xuqingfeng77
 * @date 2013-10-15
 * @function
 */
public class MobileSecurePayHelper {
	static final String TAG = "MobileSecurePayHelper";
	private ProgressDialog mProgress = null;
	Context mContext = null;

	public MobileSecurePayHelper(Context context) {
		mContext = context;
	}

	/**
	 * 
	 * @param apkname
	 * @return
	 */
	public boolean detectMobile_sp(String apkname) {
		boolean isMobile_spExist = isMobile_spExist("cn.eeepay.android.yfbplugin");
		if (!isMobile_spExist) {
			mProgress = BaseHelper.showProgress(mContext, null, "正在请求支付...",
					false, true);

			//
			// get the cacheDir.
			// /data/data//cache
			File cacheDir = mContext.getCacheDir();
			final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
			//
			// ����װ
			retrieveApkFromAssets(mContext, apkname, cachePath);

			
			new Thread(new Runnable() {
				public void run() {
					//
					PackageInfo apkInfo = getApkInfo(mContext, cachePath);
					// String newApkdlUrl = checkNewUpdate(apkInfo);
					String newApkdlUrl = null;
					// newApkdlUrl="http://empos.posp.cn:5780/paybox.apk";//�����̱������ص�ַ

					//
					if (newApkdlUrl != null) {
						FileDownloader fd = new FileDownloader(true);// �н�����
						fd.setFileUrl(newApkdlUrl);
						fd.setSavePath(cachePath);
						fd.setProgressOutput(new IDownloadProgress() {
							@Override
							public void downloadSucess() {
								Message msg = mHandler.obtainMessage(
										YSId.RQF_INSTALL_CHECK, cachePath);
								mHandler.sendMessage(msg);
							}

							@Override
							public void downloadProgress(float progress) {

							}

							@Override
							public void downloadFail() {
								Message msg = mHandler.obtainMessage(
										YSId.RQF_DOWNLOAD_FAILED, cachePath);
								mHandler.sendMessage(msg);
							}
						});
						fd.start();
					} else {
						Message msg = mHandler.obtainMessage(
								YSId.RQF_INSTALL_WITHOUT_DOWNLOAD, cachePath);
						mHandler.sendMessage(msg);
					}

					// send the result back to caller.
					// ���ͽ��

				}
			}).start();
		}
		// else ok.

		return isMobile_spExist;
	}

	/**
	 * 判断是否安装了packagename的apk(专为安装超级刷定制)
	 * 
	 * @param apkname
	 *            //apkname：for example：YFBPlugin.apk
	 * @param packagename
	 * @return
	 */
	public boolean detectAPKisOK(String apkname, String packagename) {
		boolean isMobile_spExist = isMobile_spExist(packagename);
		if (!isMobile_spExist) {
			mProgress = BaseHelper.showProgress(mContext, null, "正在请求数据...",
					false, true);
			//
			// get the cacheDir.
			// /data/data//cache
			File cacheDir = mContext.getCacheDir();
			final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
			//
			// ����װ
			retrieveApkFromAssets(mContext, apkname, cachePath);

		
			Message msg = mHandler.obtainMessage(
					YSId.RQF_INSTALL_OTHER_APK, cachePath);
			mHandler.sendMessage(msg);
			
		}
		// else ok.

		return isMobile_spExist;
	}

	/**
	 * 
	 * @param context
	 * @param cachePath
	 */
	public void showInstallConfirmDialog(final Context context,final String msg,
			final String cachePath) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setIcon(R.drawable.info);
		builder.setTitle("提示");
		builder.setMessage(msg);

		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//
						BaseHelper.chmod("777", cachePath);

						//
						// install the apk.
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setDataAndType(Uri.parse("file://" + cachePath),
								"application/vnd.android.package-archive");
						context.startActivity(intent);
					}
				});

		builder.setNegativeButton("取消", null);

		builder.show();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isMobile_spExist(String packagename) {
		PackageManager manager = mContext.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);
		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);
			if (pI.packageName.equalsIgnoreCase(packagename))
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @param context
	 * @param fileName
	 * @param path
	 * @return
	 */
	public boolean retrieveApkFromAssets(Context context, String fileName,
			String path) {
		boolean bRet = false;

		try {
			InputStream is = context.getAssets().open(fileName);

			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}

			fos.close();
			is.close();

			bRet = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bRet;
	}

	/**
	 * 
	 * @param context
	 * @param archiveFilePath
	 * @return
	 */
	public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
				PackageManager.GET_META_DATA);
		return apkInfo;
	}

	/**
	 * 
	 * @param packageInfo
	 * @return
	 */
	public String checkNewUpdate(PackageInfo packageInfo) {
		String url = null;

		try {
			// JSONObject resp = sendCheckNewUpdate(packageInfo.versionName);
			JSONObject resp = sendCheckNewUpdate("5.0.8");
			if (resp.getString("needUpdate").equalsIgnoreCase("true")) {
				url = resp.getString("updateUrl");
			}
			// else ok.
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}

	/**
	 * 
	 * @param versionName
	 * @return
	 */
	public JSONObject sendCheckNewUpdate(String versionName) {
		JSONObject objResp = null;
		try {
			JSONObject req = new JSONObject();
			req.put(AlixDefine.action, AlixDefine.actionUpdate);

			JSONObject data = new JSONObject();
			data.put(AlixDefine.platform, "android");
			data.put(AlixDefine.VERSION, versionName);
			data.put(AlixDefine.partner, "");

			req.put(AlixDefine.data, data);
			BaseHelper.log(TAG, "req.toString()=" + req.toString());
			objResp = sendRequest(req.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return objResp;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public JSONObject sendRequest(final String content) {
		NetworkManager nM = new NetworkManager(this.mContext);

		//
		JSONObject jsonResponse = null;
		try {
			String response = null;

			synchronized (nM) {
				//
				response = nM.SendAndWaitResponse(content, Constant.server_url);
			}

			jsonResponse = new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		if (jsonResponse != null)
			BaseHelper.log(TAG, jsonResponse.toString());

		return jsonResponse;
	}

	/**
	 * 
	 * @param context
	 * @param url
	 * @param filename
	 * @return
	 */
	public boolean retrieveApkFromNet(Context context, String url,
			String filename) {
		boolean ret = false;

		try {
			NetworkManager nm = new NetworkManager(mContext);
			ret = nm.urlDownloadToFile(context, url, filename);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	//
	// close the progress bar
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

	//
	// the handler use to receive the install check result.
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				Log.e(TAG, "msg = " + msg);
				switch (msg.what) {
				case YSId.RQF_INSTALL_CHECK:
				case YSId.RQF_INSTALL_WITHOUT_DOWNLOAD:
				case YSId.RQF_DOWNLOAD_FAILED: {
					//
					Log.i(TAG, "show Install dialog");
					closeProgress();
					String cachePath = (String) msg.obj;

					showInstallConfirmDialog(mContext,"为保证您的交易安全，需要您安装移联安全支付服务，才能进行付款。\n\n点击确定，立即安装。", cachePath);
				}
					break;
					
				case	YSId.RQF_INSTALL_OTHER_APK:
					
					Log.i(TAG, "show Install dialog");
					closeProgress();
					String cachePath = (String) msg.obj;

					showInstallConfirmDialog(mContext,"为保证您的交易安全，需要您安装超级刷应用，才能进行付款。\n\n点击确定，立即安装。", cachePath);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
