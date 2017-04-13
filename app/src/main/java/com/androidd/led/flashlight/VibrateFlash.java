package com.androidd.led.flashlight;

import com.androidapplite.led.flashlight.torch.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VibrateFlash extends Activity {

	ImageButton btnSwitch;
	private Camera camera;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters params;
	MediaPlayer mp;
	public String AD_UNIT_ID= "";
	private AdView adView;
	int freq;
	StroboRunner sr;
	Thread t;
	SeekBar skBar;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vibrate_flash);
		
		 AD_UNIT_ID=getResources().getString(R.string.banner_id);
		 adView = new AdView(this);
		    adView.setAdSize(AdSize.BANNER);
		    adView.setAdUnitId(AD_UNIT_ID);
		    
		    LinearLayout layout = (LinearLayout)findViewById(R.id.adlayout);
		    layout.addView(adView);
	        
		    AdRequest adRequest = new AdRequest.Builder().build();

	        // Start loading the ad in the background.
	        adView.loadAd(adRequest); 

		// flash switch button and seek bar
		btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
		skBar = (SeekBar) findViewById(R.id.seekBar);

		/*
		 * First check if device is supporting flashlight or not
		 */
		hasFlash = getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

		if (!hasFlash) {
			// device doesn't support flash
			// Show alert message and close the application
			AlertDialog alert = new AlertDialog.Builder(VibrateFlash.this)
					.create();
			alert.setTitle("Error");
			alert.setMessage("Sorry, your device doesn't support flash light!");
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// closing the application
					finish();
				}
			});
			alert.show();
			return;
		}

		// get the camera
		getCamera();

		// displaying button image
		toggleButtonImage();

		/*
		 * Switch button click event to toggle flash on/off
		 */
		btnSwitch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFlashOn) {
					// turn off flash
					turnOffFlash(false);
				} else {
					// turn on flash
					turnOnFlash(false);
				}
			}
		});
		
			skBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				freq = progress;
				if (isFlashOn) {
					// turn off flash
					turnOffFlash(true);
					// turn on flash
					turnOnFlash(true);
				}
			}
		});
	}

	/*
	 * Get the camera
	 */
	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	/*
	 * Turning On flash
	 */
	private void turnOnFlash(boolean restart) {
		if (!isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			if(!restart)
			playSound();


			isFlashOn = true;
			if(freq != 0) {
				sr = new StroboRunner();
				sr.freq = freq;
				t = new Thread(sr);
				t.start();
				return;
			} else {
				params = camera.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(params);
				camera.startPreview();
			}

			// changing button/switch image
			toggleButtonImage();
		}

	}

	/*
	 * Turning Off flash
	 */
	private void turnOffFlash(boolean restart) {
		if (isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			if(!restart)
			playSound();
			
			isFlashOn = false;
			if(t != null) {
				sr.stopRunning = true;
				t = null;
				return;
			} else {
				params = camera.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(params);
				camera.stopPreview();
			}

			// changing button/switch image
			toggleButtonImage();
		}
	}

	/*
	 * Playing sound will play button toggle sound on flash on / off
	 */
	private void playSound() {
		if (isFlashOn) {
			mp = MediaPlayer.create(VibrateFlash.this, R.raw.light_switch_off);
		} else {
			mp = MediaPlayer.create(VibrateFlash.this, R.raw.light_switch_on);
		}
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.release();
			}
		});
		mp.start();
	}

	/*
	 * Toggle switch button images changing image states to on / off
	 */
	private void toggleButtonImage() {
		if (isFlashOn) {
			btnSwitch.setImageResource(R.drawable.btn_switch_on);
		} else {
			btnSwitch.setImageResource(R.drawable.btn_switch_off);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// on pause turn off the flash
		turnOffFlash(false);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// on resume turn on the flash
		if (hasFlash)
			turnOnFlash(false);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// on starting the app get the camera params
		getCamera();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// on stop release the camera
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}
	
	private class StroboRunner implements Runnable {
		int freq;
		boolean stopRunning = false;
		
		@Override
		public void run() {
			Camera.Parameters paramsOn = camera.getParameters();
			Camera.Parameters paramsOff = params;
			paramsOn.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			paramsOff.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			
			try {
				while(!stopRunning) {
					camera.setParameters(paramsOn);
					camera.startPreview();
					Thread.sleep(1000 - freq);
					camera.setParameters(paramsOff);
					camera.startPreview();
					Thread.sleep(1000 - freq);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
