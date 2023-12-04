package nhom2.voztify.Class;

public class Playlist {
    private int imageResource;
    private String playlistName;
    private String id;
    private String yourName;
    private String userId;
    public Playlist(int imageResource, String playlistName, String yourName, String userId) {
        this.imageResource = imageResource;
        this.playlistName = playlistName;
        this.yourName = yourName;
        this.userId = userId;
    }
    // No-argument constructor for Firebase
    public Playlist() {
        // Default constructor required for calls to DataSnapshot.getValue(Playlist.class)
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }
}
