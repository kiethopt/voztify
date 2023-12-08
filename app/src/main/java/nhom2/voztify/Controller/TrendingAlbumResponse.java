package nhom2.voztify.Controller;

import java.util.List;
import nhom2.voztify.Model.Album;

public class TrendingAlbumResponse {
    private List<Album> data;

    public List<Album> getData() {
        return data;
    }

    public void setData(List<Album> data) {
        this.data = data;
    }
}
