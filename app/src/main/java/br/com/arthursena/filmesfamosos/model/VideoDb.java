package br.com.arthursena.filmesfamosos.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoDb implements Parcelable {

    public VideoDb() {
    }

    private String key;

    public VideoDb(Parcel in) {
        key = in.readString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
    }

    public static final Parcelable.Creator<VideoDb> CREATOR
            = new Parcelable.Creator<VideoDb>() {
        public VideoDb createFromParcel(Parcel in) {
            return new VideoDb(in);
        }

        public VideoDb[] newArray(int size) {
            return new VideoDb[size];
        }
    };
}
