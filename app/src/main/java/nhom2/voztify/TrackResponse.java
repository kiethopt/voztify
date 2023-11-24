package nhom2.voztify;

import com.google.gson.annotations.SerializedName;

public class TrackResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;

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
}
