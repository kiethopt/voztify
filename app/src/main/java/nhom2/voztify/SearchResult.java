package nhom2.voztify;

import nhom2.voztify.Class.Album;
import nhom2.voztify.Class.Artist;
import nhom2.voztify.Class.Track;

public class SearchResult {
    private Artist artist;
    private Album album;
    private Track track;

    // Constructors
    public SearchResult(Artist artist) {
        this.artist = artist;
    }

    public SearchResult(Album album) {
        this.album = album;
    }

    public SearchResult(Track track) {
        this.track = track;
    }

    // Getters
    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }

    public Track getTrack() {
        return track;
    }

    // Method to check type
    public boolean isArtist() {
        return artist != null;
    }

    public boolean isAlbum() {
        return album != null;
    }

    public boolean isTrack() {
        return track != null;
    }
}

