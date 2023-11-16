package nhom2.voztify.Class;

public class Song {
    private String title;
    private String artist;
    private String image;

    public Song(String title, String artist, String image) {
        this.title = title;
        this.artist = artist;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getImage() {
        return image;
    }
}