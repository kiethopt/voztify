package nhom2.voztify.Class;

public class History {
    private String title;
    private String artist;
    private String imageUrl;  // Add this field for the image URL
    private long timestamp;

    // Empty constructor for Firebase
    public History() {
    }

    public History(String title, String artist, String imageUrl, long timestamp) {
        this.title = title;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
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
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
