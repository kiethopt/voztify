package nhom2.voztify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nhom2.voztify.Class.Artist;

public class ArtistData {
    @SerializedName("data")
    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }
    public void setArtists(List<Artist> tracks) {
        this.artists = tracks;
    }
}
