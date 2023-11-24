package nhom2.voztify;

import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Artist;

public class SearchResult {
    private Artist artist;
    private Album album;
    private boolean isArtist;

    public SearchResult(Artist artist) {
        this.artist = artist;
        this.isArtist = true;
    }

    public SearchResult(Album album) {
        this.album = album;
        this.isArtist = false;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }
}

