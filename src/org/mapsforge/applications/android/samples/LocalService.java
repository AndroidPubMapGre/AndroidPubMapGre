package org.mapsforge.applications.android.samples;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocalService extends Service {
	private NotificationManager mNM;

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = 1;
	ArrayList<POI> arrayPOI = new ArrayList<POI>();
	GPSTracker gpsT;

	/**
	 * Class for clients to access. Because we know this service always runs in the same process as its clients, we
	 * don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		LocalService getService() {
			return LocalService.this;
		}
	}

	@Override
	public void onCreate() {

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle b = intent.getExtras();

		this.arrayPOI = new ArrayList<POI>();
		this.arrayPOI = b.getParcelableArrayList("paramArrayPOI");

		gpsT = new GPSTracker(LocalService.this, this.arrayPOI);

		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		// Tell the user we stopped.
		Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Show a notification while this service is running.
	 */
	public void showNotification(int idPoi) {
		// In this sample, we'll use the same text for the ticker and the expanded notification
		CharSequence text = "Start";

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.marker_red, text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this notification
		Intent intent = new Intent();
		intent.setAction(MY_ACTION);

		intent.putExtra("DATAPASSED", idPoi);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Set the info for the views that show in the notification panel.

		sendBroadcast(intent);

		notification.setLatestEventInfo(this, this.arrayPOI.get(idPoi).id + " - " + this.arrayPOI.get(idPoi).titre,
				this.arrayPOI.get(idPoi).description.substring(0, 70), contentIntent);

		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}

	final static String MY_ACTION = "MY_ACTION";

}
