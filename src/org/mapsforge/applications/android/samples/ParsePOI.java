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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.AsyncTask;

public class ParsePOI extends AsyncTask<String, Void, ArrayList<POI>> {

	private ArrayList<POI> arrayPOI;
	private Context context;

	public interface OnTaskCompleted {
		void onTaskCompleted(ArrayList<POI> arrayPOI);
	}

	private OnTaskCompleted listener;

	public ParsePOI(OnTaskCompleted listener, Context context) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected ArrayList<POI> doInBackground(String... param) {

		return createPOI();
	}

	protected ArrayList<POI> createPOI() {

		ArrayList<POI> poi = new ArrayList<POI>();

		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();

			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			InputStream isr = context.getApplicationContext().getAssets().open("poi.xml");
			xpp.setInput(isr, "UTF-8");
			int eventType = xpp.getEventType();

			POI currentPOI = null;
			String name = "";

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						if (name.equals("poi")) {
							currentPOI = new POI();
						}
						break;
					case XmlPullParser.TEXT:
						if (!xpp.getText().equals("") && xpp.getText() != null && name != null) {
							if (name.equals("titre")) {
								currentPOI.titre = xpp.getText();
								name = null;
							} else if (name.equals("lat")) {
								currentPOI.lat = Double.parseDouble(xpp.getText());
								name = null;
							} else if (name.equals("lon")) {
								currentPOI.lon = Double.parseDouble(xpp.getText());
								name = null;
							} else if (name.equals("description")) {
								currentPOI.description = xpp.getText();
								name = null;
							} else if (name.equals("img")) {
								currentPOI.img = xpp.getText();
								name = null;
							}
						}
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						if (name.equals("poi") && currentPOI != null) {
							currentPOI.id = poi.size();
							poi.add(currentPOI);
							currentPOI = null;
						}
						name = null;
						break;
				}

				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return poi;
	}

	protected void onPostExecute(ArrayList<POI> arrayPOI) {
		// Call the interface method
		if (listener != null) {
			listener.onTaskCompleted(arrayPOI);
		}
	}
}