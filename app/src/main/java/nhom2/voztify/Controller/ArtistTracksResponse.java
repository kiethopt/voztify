package nhom2.voztify.Controller;

import java.util.List;

import nhom2.voztify.Model.Track;

public class ArtistTracksResponse {
    private List<Track> data;

    public List<Track> getData() {
        return data;
    }

    public void setData(List<Track> data) {
        this.data = data;
    }
}
