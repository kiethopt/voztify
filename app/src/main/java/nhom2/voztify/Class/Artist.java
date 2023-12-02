package nhom2.voztify.Class;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Artist implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("picture")
    private String picture;

    @SerializedName("picture_small")
    private String picture_small;

    @SerializedName("picture_medium")
    private String picture_medium;

    @SerializedName("picture_big")
    private String picture_big;


    // Other fields and methods

    // Constructor
    public Artist(String id, String name, String picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture_small() {
        return picture_small;
    }

    public void setPicture_small(String picture_small) {
        this.picture_small = picture_small;
    }

    public String getPicture_medium() {
        return picture_medium;
    }

    public void setPicture_medium(String picture_medium) {
        this.picture_medium = picture_medium;
    }

    public String getPicture_big() {
        return picture_big;
    }

    public void setPicture_big(String picture_big) {
        this.picture_big = picture_big;
    }
}
