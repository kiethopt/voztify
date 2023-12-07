package nhom2.voztify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeezerResponse {
    @SerializedName("data")
    private List<DeezerRadio> data;

    public List<DeezerRadio> getData() {
        return data;
    }
}
