package com.androidd.led.flashlight;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidapplite.led.flashlight.torch.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends Activity {

	ImageButton flashButton,vibrateButton,whiteButton;

	public String AD_UNIT_ID= "";
	private AdView adView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PermissionListener permissionlistener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {
				//Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPermissionDenied(ArrayList<String> deniedPermissions) {
				Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
			}


		};

		new TedPermission(this)
				.setPermissionListener(permissionlistener)
				.setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
				.setPermissions(Manifest.permission.CAMERA)
				.check();


		AD_UNIT_ID=getResources().getString(R.string.banner_id);
		 adView = new AdView(this);
		    adView.setAdSize(AdSize.BANNER);
		    adView.setAdUnitId(AD_UNIT_ID);
		    
		    LinearLayout layout = (LinearLayout)findViewById(R.id.adlayout);
		    layout.addView(adView);
	        
		    AdRequest adRequest = new AdRequest.Builder().build();

	        // Start loading the ad in the background.
	        adView.loadAd(adRequest);
	        
	        //bind views
	        flashButton = (ImageButton)findViewById(R.id.flash_light);
	        vibrateButton = (ImageButton)findViewById(R.id.vibrate);
	        whiteButton = (ImageButton)findViewById(R.id.white_screen);
	        
	        
	        flashButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,Flash.class);
					startActivity(intent);;
				}
			});
	        
	        whiteButton.setOnClickListener(new View.OnClickListener() {

	    				@Override
	    				public void onClick(View v) {
	    					Intent intent = new Intent(MainActivity.this,WhiteScreen.class);
	    					startActivity(intent);;
	    				}
	    			});
	        
	        vibrateButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,VibrateFlash.class);
					startActivity(intent);;
				}
			});
	        
	        
	}


}
