package nhom2.voztify.Controller;

import java.util.List;

import nhom2.voztify.Model.Artist;

public class ArtistSearchResponse {
    private List<Artist> data;

    public List<Artist> getData() {
        return data;
    }

    public void setData(List<Artist> data) {
        this.data = data;
    }
}
