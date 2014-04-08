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
import java.util.List;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

@SuppressLint("NewApi")
public class MapViewCustom extends MapView {

	long startTime = 0;
	float startPosX = 0;
	float startPosY = 0;
	ArrayList<POI> arrayPOI;
	BasicMapViewer bmp;

	public MapViewCustom(Context context, ArrayList<POI> arrayPOI, BasicMapViewer bmp) {
		super(context);
		this.arrayPOI = arrayPOI;
		this.bmp = bmp;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// record the start time
			startTime = ev.getEventTime();
			startPosX = (int) ev.getX();
			startPosY = (int) ev.getY();
		}

		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if (startPosX + 5 >= ev.getX() && startPosX - 5 <= ev.getX() && startPosY + 5 >= ev.getY()
					&& startPosY - 5 <= ev.getY()) {

				MapPosition mapPosition = this.getMapViewPosition().getMapPosition();
				byte zoomLevel = mapPosition.zoomLevel;

				ListOverlay listOverlay = (ListOverlay) this.getOverlays().get(0);
				List<OverlayItem> overlayItems = listOverlay.getOverlayItems();

				GeoPoint gp = pixelsToGeoPoint(startPosX, startPosY);

				for (int i = 0; i < overlayItems.size(); i++) {
					MarkerCustom mc = (MarkerCustom) overlayItems.get(i);
					Rect bound = mc.getDrawable().copyBounds();

					int[] point = geoPointToPixels(mc.geoPoint);
					int[] point1 = point.clone();
					point1[0] -= Math.abs(bound.left);
					point1[1] -= Math.abs(bound.top);
					GeoPoint gp1 = pixelsToGeoPoint(point1[0], point1[1]);
					int[] point2 = point.clone();
					point2[0] -= Math.abs(bound.left);
					point2[1] -= Math.abs(bound.bottom);
					GeoPoint gp2 = pixelsToGeoPoint(point2[0], point2[1]);
					int[] point3 = point.clone();
					point3[0] += Math.abs(bound.right);
					point3[1] -= Math.abs(bound.top);
					GeoPoint gp3 = pixelsToGeoPoint(point3[0], point3[1]);
					int[] point4 = point.clone();
					point4[0] += Math.abs(bound.right);
					point4[1] -= Math.abs(bound.bottom);
					GeoPoint gp4 = pixelsToGeoPoint(point4[0], point4[1]);

					if (gp1.longitude < gp.longitude && gp1.latitude > gp.latitude
							&& !(gp2.longitude < gp.longitude && gp2.latitude < gp.latitude)
							&& gp3.longitude > gp.longitude && gp3.latitude > gp.latitude
							&& !(gp4.longitude > gp.longitude && gp4.latitude < gp.latitude)) {

						bmp.launchInfoView(mc.id);
						break;
					}
				}

			}

		}

		return super.onTouchEvent(ev);
	}

	/*
	 * public HitBoxGeoPoint createHitBox(byte zoomLevel, GeoPoint geoPoint) { MapPosition mapPosition =
	 * this.getMapViewPosition().getMapPosition(); double[] pixels = getPixelXAndY(mapPosition.zoomLevel, geoPoint);
	 * double pixelX = pixels[0]; double pixelY = pixels[1]; Drawable dr =
	 * getResources().getDrawable(R.drawable.marker_green); Rect drawableBounds = dr.copyBounds(); double left = pixelX
	 * - 40; double top = pixelY - 100; double right = pixelX + 40; double bottom = pixelY + 100; double lonLeft =
	 * MercatorProjection.pixelXToLongitude(left, zoomLevel); double latTop = MercatorProjection.pixelYToLatitude(top,
	 * zoomLevel); double lonRight = MercatorProjection.pixelXToLongitude(right, zoomLevel); double latBottom =
	 * MercatorProjection.pixelYToLatitude(bottom, zoomLevel); return new HitBoxGeoPoint(lonLeft, latTop, lonRight,
	 * latBottom); }
	 */

	private MarkerCustom createMarker(int resourceIdentifier, GeoPoint geoPoint, int id) {
		Drawable drawable = getResources().getDrawable(resourceIdentifier);
		return new MarkerCustom(geoPoint, Marker.boundCenterBottom(drawable), id);
	}

	private GeoPoint pixelsToGeoPoint(float x, float y) {
		MapPosition mapPosition = this.getMapViewPosition().getMapPosition();
		GeoPoint geoPoint = mapPosition.geoPoint;

		double pixelX = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
		double pixelY = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel);

		pixelX -= this.getWidth() >> 1;
		pixelY -= this.getHeight() >> 1;

		GeoPoint gp = new GeoPoint(MercatorProjection.pixelYToLatitude(pixelY + y, mapPosition.zoomLevel),
				MercatorProjection.pixelXToLongitude(pixelX + x, mapPosition.zoomLevel));

		return gp;
	}

	public int[] geoPointToPixels(GeoPoint in) {
		if (this.getWidth() <= 0 || this.getHeight() <= 0) {
			return null;
		}

		MapPosition mapPosition = this.getMapViewPosition().getMapPosition();

		// calculate the pixel coordinates of the top left corner
		GeoPoint geoPoint = mapPosition.geoPoint;
		double pixelX = MercatorProjection.longitudeToPixelX(geoPoint.longitude, mapPosition.zoomLevel);
		double pixelY = MercatorProjection.latitudeToPixelY(geoPoint.latitude, mapPosition.zoomLevel);
		pixelX -= this.getWidth() >> 1;
		pixelY -= this.getHeight() >> 1;
		int[] point = { (int) (MercatorProjection.longitudeToPixelX(in.longitude, mapPosition.zoomLevel) - pixelX),
				(int) (MercatorProjection.latitudeToPixelY(in.latitude, mapPosition.zoomLevel) - pixelY) };
		return point;
	}
}
