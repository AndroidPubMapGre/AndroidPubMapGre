package org.mapsforge.applications.android.samples;

import android.os.Parcel;
import android.os.Parcelable;

public class POI implements Parcelable {

	protected double lat;
	protected double lon;
	protected int id;
	protected String description;
	protected String titre;
	protected String img;

	public POI(double lat, double lon, int id, String description, String titre, String img) {
		this.lat = lat;
		this.lon = lon;
		this.id = id;
		this.description = description;
		this.titre = titre;
		this.img = img;
	}

	public POI() {

	}

	public POI(Parcel source) {
		this.lat = source.readDouble();
		this.lon = source.readDouble();
		this.id = source.readInt();
		this.description = source.readString();
		this.titre = source.readString();
		this.img = source.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(lat);
		dest.writeDouble(lon);
		dest.writeInt(id);
		dest.writeString(description);
		dest.writeString(titre);
		dest.writeString(img);
	}

	public static final Parcelable.Creator<POI> CREATOR = new Parcelable.Creator<POI>() {

		public POI createFromParcel(Parcel source) {
			return new POI(source);
		}

		public POI[] newArray(int size) {
			return new POI[size];
		}

	};

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof POI)) {
			return false;
		}
		return ((POI) o).id == this.id;
	}

	@Override
	public String toString() {
		return "POI [lat=" + lat + ", lon=" + lon + ", id=" + id + ", description=" + description + ", titre=" + titre
				+ ", img=" + img + "]";
	}
}
