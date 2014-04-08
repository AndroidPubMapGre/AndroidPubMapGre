package org.mapsforge.applications.android.samples;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SearchResultsActivity extends Activity {

	public ListView lvListe;
	ArrayList<POI> arrayPOI = new ArrayList<POI>();
	ArrayList<POI> searchResult = new ArrayList<POI>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);

		lvListe = (ListView) findViewById(R.id.lvListe);

		Bundle b = getIntent().getExtras();
		arrayPOI = b.getParcelableArrayList("paramArrayPOI");
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);

			ArrayList<POI> searchResultTitreAll = new ArrayList<POI>();
			ArrayList<POI> searchResultTitrePart = new ArrayList<POI>();
			ArrayList<POI> searchResultDescription = new ArrayList<POI>();

			for (POI poi : arrayPOI) {
				String titreLower = poi.titre.toLowerCase();

				if (titreLower.equalsIgnoreCase(query)) {
					searchResultTitreAll.add(poi);
				} else if (titreLower.contains(query.toLowerCase())) {
					searchResultTitrePart.add(poi);
				} else if (poi.description != null) {
					String descriptionLower = poi.description.toLowerCase();
					if (descriptionLower.contains(query.toLowerCase())) {
						searchResultDescription.add(poi);
					}
				}
			}

			searchResult.addAll(searchResultTitreAll);
			searchResult.addAll(searchResultTitrePart);
			searchResult.addAll(searchResultDescription);

			POIadapter1 adapter = new POIadapter1(this, searchResult);

			this.lvListe.setAdapter(adapter);

			this.lvListe.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					launchInfoView(position);
					finish();
				}
			});

			adapter.notifyDataSetChanged();
		}

	}

	public void launchInfoView(int position) {

		System.out.println("coucou " + this.searchResult.get(position));
		int idPOI = this.searchResult.get(position).id;
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
}