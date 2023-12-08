package nhom2.voztify.Model;

public class TopRadioGenre {
    private String id;
    private String title;
    private String pictureUrl;

    public TopRadioGenre(String id, String title, String pictureUrl) {
        this.id = id;
        this.title = title;
        this.pictureUrl = pictureUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}
