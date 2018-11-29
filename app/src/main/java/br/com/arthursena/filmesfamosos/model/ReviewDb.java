package br.com.arthursena.filmesfamosos.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewDb implements Parcelable {

    private String author;
    private String content;
    private String url;

    public ReviewDb(){}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private ReviewDb(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(url);
        dest.writeString(content);
    }

    public static final Parcelable.Creator<ReviewDb> CREATOR
            = new Parcelable.Creator<ReviewDb>() {
        public ReviewDb createFromParcel(Parcel in) {
            return new ReviewDb(in);
        }

        public ReviewDb[] newArray(int size) {
            return new ReviewDb[size];
        }
    };
}
