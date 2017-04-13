package com.androidd.led.flashlight;

import android.app.Activity;
import android.app.AlertDialog;import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.androidapplite.led.flashlight.torch.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class WhiteScreen extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_white);


     
	}


}
