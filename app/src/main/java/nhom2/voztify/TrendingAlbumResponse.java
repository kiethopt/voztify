package nhom2.voztify;

import java.util.List;
import nhom2.voztify.Class.Album;

public class TrendingAlbumResponse {
    private List<Album> data;

    public List<Album> getData() {
        return data;
    }

    public void setData(List<Album> data) {
        this.data = data;
    }
}
