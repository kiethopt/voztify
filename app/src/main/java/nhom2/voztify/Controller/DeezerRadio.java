package nhom2.voztify.Controller;

import com.google.gson.annotations.SerializedName;

public class DeezerRadio {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("picture")
    private String picture;

    @SerializedName("tracklist")
    private String tracklist;

    // Các trường khác tương ứng với JSON response

    // Getter và Setter

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTracklist() {
        return tracklist;
    }

    public void setTracklist(String tracklist) {
        this.tracklist = tracklist;
    }
}
