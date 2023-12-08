package nhom2.voztify;

import java.util.List;
import nhom2.voztify.Class.Track;

public class TrackSearchResponse {
    private List<Track> data;

    // Constructor
    public TrackSearchResponse(List<Track> data) {
        this.data = data;
    }

    // Getter
    public List<Track> getData() {
        return data;
    }

    // Setter
    public void setData(List<Track> data) {
        this.data = data;
    }
}
