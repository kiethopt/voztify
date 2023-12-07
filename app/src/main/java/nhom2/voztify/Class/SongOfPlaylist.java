package nhom2.voztify.Class;

public class SongOfPlaylist {
    String title;
    String artist;
    String img;

    public SongOfPlaylist(String title, String artist, String img) {
        this.title = title;
        this.artist = artist;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public SongOfPlaylist(){

    }
}
