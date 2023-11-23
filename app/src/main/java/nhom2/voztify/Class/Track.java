package nhom2.voztify.Class;

import com.google.gson.annotations.SerializedName;

public class Track {
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

    public static class Artist {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Album {
        @SerializedName("cover_small")
        private String cover_small;

        @SerializedName("cover_medium")
        private String cover_medium;

        @SerializedName("cover_big")
        private String cover_big;

        public String getCover_small() {
            return cover_small;
        }

        public void setCover_small(String cover_small) {
            this.cover_small = cover_small;
        }

        public String getCover_medium() {
            return cover_medium;
        }

        public void setCover_medium(String cover_medium) {
            this.cover_medium = cover_medium;
        }

        public String getCover_big() {
            return cover_big;
        }

        public void setCover_big(String cover_big) {
            this.cover_big = cover_big;
        }
    }

}

