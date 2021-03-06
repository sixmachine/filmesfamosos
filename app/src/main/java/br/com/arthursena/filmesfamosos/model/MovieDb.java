package br.com.arthursena.filmesfamosos.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import br.com.arthursena.filmesfamosos.database.MovieContract;
import br.com.arthursena.filmesfamosos.util.DateUtil;

public class MovieDb implements Parcelable {

    private int vote_count;
    private int id;
    private boolean video;
    private double vote_average;
    private String title;
    private double popularity;
    private String poster_path;
    private String backdrop_path;
    private String overview;
    private Date release_date;

    public MovieDb() {
        super();
    }

    private MovieDb(Parcel in) {
        vote_count = in.readInt();
        id = in.readInt();
        video = in.readByte() != 0;
        title = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        vote_average = in.readDouble();
        release_date = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vote_count);
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeDouble(vote_average);
        dest.writeLong(release_date.getTime());
    }

    public static final Parcelable.Creator<MovieDb> CREATOR
            = new Parcelable.Creator<MovieDb>() {
        public MovieDb createFromParcel(Parcel in) {
            return new MovieDb(in);
        }

        public MovieDb[] newArray(int size) {
            return new MovieDb[size];
        }
    };

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.MOVIE_ID, getId());
        cv.put(MovieContract.MovieEntry.MOVIE_TITLE, getTitle());
        cv.put(MovieContract.MovieEntry.MOVIE_POSTER_PATH, getPoster_path());
        cv.put(MovieContract.MovieEntry.MOVIE_BACKDROP_PATH, getBackdrop_path());
        cv.put(MovieContract.MovieEntry.MOVIE_OVERVIEW, getOverview());
        cv.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, DateUtil.formatarDataSqlite(getRelease_date()));
        cv.put(MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE, getVote_average());
        return cv;
    }
}
