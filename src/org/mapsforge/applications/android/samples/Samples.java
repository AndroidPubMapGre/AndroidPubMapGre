/*
 * Copyright 2010, 2011, 2012 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.applications.android.samples;

import java.util.ArrayList;

import org.mapsforge.applications.android.samples.ParsePOI.OnTaskCompleted;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * A simple start screen for the sample activities.
 */
public class Samples extends Activity {

	public int nextStepCount;
	public boolean gpsOn;
	public boolean poiOk;
	public ArrayList<POI> arrayPOI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splashscreen);
		checkGPS();
		new ParsePOI(listener, this).execute("Testing");
	}

	private OnTaskCompleted listener = new OnTaskCompleted() {
		@Override
		public void onTaskCompleted(ArrayList<POI> arrayPOI) {
			Toast.makeText(Samples.this, "Async task end", Toast.LENGTH_SHORT).show();
			poiOk = true;
			nextStep(arrayPOI);
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		checkGPS();
	}

	private void checkGPS() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, R.string.activate_device, Toast.LENGTH_SHORT).show();
			this.gpsOn = true;
			nextStep(arrayPOI);
			System.err.println("I call gps to nextStep");
		} else {
			showGPSDisabledAlertToUser();
		}

	}

	private void nextStep(ArrayList<POI> arrayPOI) {
		if (this.gpsOn && this.poiOk) {
			SystemClock.sleep(3000);
			Intent i = new Intent(this, BasicMapViewer.class);

			i.putParcelableArrayListExtra("paramArrayPOI", arrayPOI);
			startActivity(i);
		}
	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(R.string.not_activate_gps).setCancelable(false)
				.setPositiveButton(R.string.activate_gps, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent callGPSSettingIntent = new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(callGPSSettingIntent);
					}
				});
		alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
