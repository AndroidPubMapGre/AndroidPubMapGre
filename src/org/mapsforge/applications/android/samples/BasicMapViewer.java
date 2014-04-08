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

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

/**
 * A simple application which demonstrates how to use a MapView.
 */
public class BasicMapViewer extends MapActivity {
	private static final File MAP_FILE = new File(Environment.getExternalStorageDirectory().getPath(),
			"rhone-alpes.map");
	ArrayList<POI> arrayPOI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			Bundle b = getIntent().getExtras();
			// POI[] testing = (POI[]) this.getIntent().getExtras().get("paramArrayPOI");
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

		} catch (Exception e) {
			e.printStackTrace();
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
}
