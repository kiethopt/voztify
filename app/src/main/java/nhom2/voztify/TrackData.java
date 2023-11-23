package nhom2.voztify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nhom2.voztify.Class.Track;

public class TrackData {
    @SerializedName("data")
    private List<Track> data;

    public List<Track> getData() {
        return data;
    }
}