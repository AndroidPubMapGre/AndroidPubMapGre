package org.mapsforge.applications.android.samples;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class POIadapter1 extends BaseAdapter {

	List<POI> arrayPOI;
	LayoutInflater inflater;

	public POIadapter1(Context context, List<POI> arrayPOI) {
		inflater = LayoutInflater.from(context);
		this.arrayPOI = arrayPOI;
	}

	@Override
	public int getCount() {
		return arrayPOI.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayPOI.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView tvTitre;
		TextView tvId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_search, null);
			holder.tvTitre = (TextView) convertView.findViewById(R.id.tvDescription);
			holder.tvId = (TextView) convertView.findViewById(R.id.tvIdPOI);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitre.setText("" + arrayPOI.get(position).titre);
		holder.tvId.setText("n° " + arrayPOI.get(position).id);

		return convertView;
	}

}
