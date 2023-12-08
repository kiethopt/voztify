package nhom2.voztify.Controller;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nhom2.voztify.Model.Track;

public class TrackResponse {
    @SerializedName("data")
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}