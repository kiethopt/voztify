package nhom2.voztify.Model;

public class SongForU {
    private String title;
    private String artist;
    private String imageUrl;  // Add this field for the image URL
    private Object timestamp;

    // Empty constructor for Firebase
    public SongForU() {
    }

    public SongForU(String title, String artist, String imageUrl, Object timestamp) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }
    public SongForU(String title, String artist, String imageUrl) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        // Assuming the image identifier is stored in imageUrl
        return  imageUrl ;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
