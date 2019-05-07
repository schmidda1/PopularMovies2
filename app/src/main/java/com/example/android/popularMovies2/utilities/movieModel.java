package com.example.android.popularMovies2.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class movieModel implements Parcelable {
    private int movieId;
    private String originalTitle;
    private String moviePoster;
    private String plotSynopsis;
    private float vote_average;
    private String releaseDate;
    private String posterUrl;
    public movieModel(){

    }
    public movieModel(int movieId, String originalTitle, String moviePoster, String plotSynopsis, float vote_average,
                      String releaseDate, String posterUrl){
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.vote_average = vote_average;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
    }
    //get methods
    public int getMovieId(){return movieId;}
    public String getOriginalTitle(){
        return originalTitle;
    }
    public String getMoviePoster(){
        return moviePoster;
    }
    public String getPlotSynopsis(){
        return plotSynopsis;
    }
    public float getVote_average(){
        return vote_average;
    }
    public String getReleaseDate(){
        return releaseDate;
    }
    public String getPosterUrl(){
        return posterUrl;
    }
    //set methods
    public void setMovieId(int movieId){this.movieId = movieId;}
    public void setOriginalTitle(String originalTitle){
        this.originalTitle = originalTitle;
    }
    public void setMoviePoster(String moviePoster){
        this.moviePoster = moviePoster;
    }
    public void setPlotSynopsis(String plotSynopsis){
        this.plotSynopsis = plotSynopsis;
    }
    public void setVote_average(float vote_average){
        this.vote_average = vote_average;
    }
    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }
    public void setPosterUrl(String posterUrl){
        this.posterUrl = posterUrl;
    }
    public movieModel(Parcel in){
        this.movieId = in.readInt();
        this.originalTitle = in.readString();
        this.moviePoster = in.readString();
        this.plotSynopsis = in.readString();
        this.vote_average = in.readFloat();
        this.releaseDate = in.readString();
        this.posterUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(originalTitle);
        dest.writeString(moviePoster);
        dest.writeString(plotSynopsis);
        dest.writeFloat(vote_average);
        dest.writeString(releaseDate);
        dest.writeString(posterUrl);

    }
    public static final Parcelable.Creator<movieModel> CREATOR
            = new Parcelable.Creator<movieModel>(){
        public movieModel createFromParcel(Parcel in){
            return new movieModel(in);
        }
        public movieModel[] newArray(int size){
            return new movieModel[size];
        }
    };
}
