package nhom2.voztify.Controller;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nhom2.voztify.Model.Artist;

public class ArtistResponse {
    @SerializedName("data")
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
