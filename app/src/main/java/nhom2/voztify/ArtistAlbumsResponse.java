package nhom2.voztify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nhom2.voztify.Class.Album;

public class ArtistAlbumsResponse {
    @SerializedName("data")
    private List<Album> data;

    public List<Album> getData() {
        return data;
    }
    public void setData(List<Album> data) {
        this.data = data;
    }
}
