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
}
