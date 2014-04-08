package org.mapsforge.applications.android.samples;

import org.mapsforge.android.maps.MapActivity;

import android.os.Bundle;

/**
 * A simple application which demonstrates how to use a MapView.
 */
public class InfoView extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.info);

		Bundle b = getIntent().getExtras();
		int idPoi = b.getInt("paramArrayPOI");

		System.out.println("coucou " + idPoi);

	}
}
