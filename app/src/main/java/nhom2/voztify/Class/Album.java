package nhom2.voztify.Class;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("cover")
    private String cover;

    @SerializedName("artist")
    private Artist artist;

    @SerializedName("md5_image")
    private String md5_image;

    @SerializedName("cover_medium")
    private String cover_medium;

    // Constructor
    public Album(String id, String title, String cover) {
        this.id = id;
        this.title = title;
        this.cover = cover;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getMd5_image() {
        return md5_image;
    }

    public void setMd5_image(String md5_image) {
        this.md5_image = md5_image;
    }

    public String getCover_medium() {
        return cover_medium;
    }

    public void setCover_medium(String cover_medium) {
        this.cover_medium = cover_medium;
    }
}
