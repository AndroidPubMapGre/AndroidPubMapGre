package org.mapsforge.applications.android.samples;

import java.util.ArrayList;
import java.util.Locale;

import org.mapsforge.android.maps.MapActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * A simple application which demonstrates how to use a MapView.
 */
@SuppressLint("NewApi")
public class InfoView extends MapActivity implements TextToSpeech.OnInitListener {

	public ArrayList<POI> arrayPOI;
	public TextToSpeech tts;
	public int idPOI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.info);

		tts = new TextToSpeech(this, this);

		Bundle b = getIntent().getExtras();

		this.idPOI = b.getInt("paramIdPOI");
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

	@Override
	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.info_bar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
			case R.id.action_tts:
				explainDescription();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRANCE);

			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	public void explainDescription() {
		String text = "Voici les informations pour le point numéro " + idPOI + " étant " + arrayPOI.get(idPOI).titre
				+ " voici la description du point d'interet " + arrayPOI.get(idPOI).description;
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
}
