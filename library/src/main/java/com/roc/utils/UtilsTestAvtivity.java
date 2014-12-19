package com.roc.utils;

import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.roc.androidutils.R;
import com.roc.annotation.ContentView;
import com.roc.ui.BaseActivity;

public class UtilsTestAvtivity extends BaseActivity {
	@Override
	@ContentView(id = R.layout.activity_utils)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println(CommonUtils.getSign(this));
	}

	public void click(View v) throws IOException {
		CommonUtils.showCustomShortToast(this, R.layout.dialog_simple);
		// DialogUtils.stratProgressDialog(this, "test", true, false);
		CommonUtils.hideSoftInput(this);
		CommonUtils.backupApp(this);
	}

	/**
	 * patcher测试
	 * 
	 * @param v
	 */
	public void getNewApk(View v) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String oldApkPath = Environment.getExternalStorageDirectory() + "/old.apk";
				String newApkPath = Environment.getExternalStorageDirectory() + "/new.apk";
				String patchPath = Environment.getExternalStorageDirectory() + "/update_file.patch";
				PatcherUtils.patcher(oldApkPath, newApkPath, patchPath);
			}
		}).start();
	}
}
