package org.mapsforge.applications.android.samples;

import java.util.ArrayList;

import org.mapsforge.android.maps.MapActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * A simple application which demonstrates how to use a MapView.
 */
@SuppressLint("NewApi")
public class InfoView extends MapActivity {

	public ArrayList<POI> arrayPOI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.info);

		Bundle b = getIntent().getExtras();

		int idPOI = b.getInt("paramIdPOI");
		arrayPOI = new ArrayList<POI>();
		arrayPOI = b.getParcelableArrayList("paramArrayPOI");

		System.out.println("coucou " + idPOI);

		TextView et = (TextView) findViewById(R.id.titreInfo);
		et.setText("" + arrayPOI.get(idPOI).titre);
		et.setActivated(false);

		TextView tv = (TextView) findViewById(R.id.descriptionInfo);
		tv.setText(arrayPOI.get(idPOI).description);
		tv.setMovementMethod(new ScrollingMovementMethod());

		this.setTitle("#" + idPOI + " - " + arrayPOI.get(idPOI).titre);
	}
}
