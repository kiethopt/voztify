package nhom2.voztify.Class;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Track implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String link;

    @SerializedName("duration")
    private int duration;

    @SerializedName("preview")
    private String previewUrl;

    @SerializedName("position")
    private  String position;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("artist")
    private Artist artist;

    @SerializedName("album")
    private Album album;

    @SerializedName("md5_image")
    private String md5_image;


    // Các getter và setter cho mỗi trường
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getMd5_image() {
        return md5_image;
    }

    public void setMd5_image(String md5_image) {
        this.md5_image = md5_image;
    }

    public static class Artist implements Serializable {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

