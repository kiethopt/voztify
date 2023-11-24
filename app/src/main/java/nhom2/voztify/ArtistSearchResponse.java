package nhom2.voztify;

import java.util.List;

import nhom2.voztify.Class.Artist;

public class ArtistSearchResponse {
    private List<Artist> data;

    public List<Artist> getData() {
        return data;
    }

    public void setData(List<Artist> data) {
        this.data = data;
    }
}
