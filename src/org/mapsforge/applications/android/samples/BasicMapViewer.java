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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * A simple application which demonstrates how to use a MapView.
 */
@SuppressLint("NewApi")
public class BasicMapViewer extends MapActivity {
	private static final File MAP_FILE = new File(Environment.getExternalStorageDirectory().getPath(),
			"rhone-alpes.map");
	private static final int UPDATE_DISTANCE = 0;
	private static final int UPDATE_INTERVAL = 1000;
	ArrayList<POI> arrayPOI;
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			Bundle b = getIntent().getExtras();

			arrayPOI = new ArrayList<POI>();
			arrayPOI = b.getParcelableArrayList("paramArrayPOI");

			MapViewCustom mapView = new MapViewCustom(this, arrayPOI, this);
			mapView.setClickable(true);
			mapView.setBuiltInZoomControls(true);
			FileOpenResult fileOpenResult = mapView.setMapFile(MAP_FILE);
			if (!fileOpenResult.isSuccess()) {
				Toast.makeText(this, fileOpenResult.getErrorMessage(), Toast.LENGTH_LONG).show();
				finish();
			}
			setContentView(mapView);

			ListOverlay listOverlay = new ListOverlay();
			List<OverlayItem> overlayItems = listOverlay.getOverlayItems();

			for (POI poi : arrayPOI) {
				GeoPoint gp = new GeoPoint(poi.lat, poi.lon);
				MarkerCustom marker1 = createMarker(R.drawable.marker_red, gp, poi.id);
				overlayItems.add(marker1);
			}

			GeoPoint geoPoint = new GeoPoint(45.187672, 5.726871);
			MapPosition newMapPosition = new MapPosition(geoPoint, (byte) 15);

			mapView.getMapViewPosition().setMapPosition(newMapPosition);
			mapView.getOverlays().add(listOverlay);

			// overlayItems = listOverlay.getOverlayItems();

			// Activation du GPS
			Drawable drawable = getResources().getDrawable(R.drawable.marker_green);
			gps = new GPSTracker(BasicMapViewer.this, mapView, drawable);
			gps.enableMyLocation(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_bar_menu, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void startActivity(Intent intent) {
		// check if search intent
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			intent.putParcelableArrayListExtra("paramArrayPOI", arrayPOI);
		}

		super.startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
			case R.id.action_search:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void launchInfoView(int idPOI) {
		if (arrayPOI.get(idPOI) != null) {
			System.out.println("coucou " + idPOI);
			Intent i = new Intent(this, InfoView.class);
			i.putExtra("paramIdPOI", idPOI);
			i.putParcelableArrayListExtra("paramArrayPOI", arrayPOI);
			startActivity(i);
		} else {
			Toast.makeText(this, "Aucun POI associé", Toast.LENGTH_LONG).show();
		}
	}

	private MarkerCustom createMarker(int resourceIdentifier, GeoPoint geoPoint, int id) {
		Drawable drawable = getResources().getDrawable(resourceIdentifier);
		return new MarkerCustom(geoPoint, Marker.boundCenterBottom(drawable), id);
	}

	public static GeoPoint locationToLatLong(Location location) {
		return new GeoPoint(location.getLatitude(), location.getLongitude());
	}

	/*
	 * private synchronized boolean enableBestAvailableProvider() { disableMyLocation(); Criteria criteria = new
	 * Criteria(); criteria.setAccuracy(Criteria.ACCURACY_FINE); String bestAvailableProvider =
	 * this.locationManager.getBestProvider(criteria, true); if (bestAvailableProvider == null) { return false; }
	 * this.locationManager.requestLocationUpdates(bestAvailableProvider, UPDATE_INTERVAL, UPDATE_DISTANCE, this);
	 * this.myLocationEnabled = true; return true; }
	 */
}
