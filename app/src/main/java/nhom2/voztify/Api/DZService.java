package nhom2.voztify.Api;

import com.google.gson.JsonObject;

import nhom2.voztify.TrackData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DZService {
    @GET("/album/{album_id}/tracks")
    Call<TrackData> getAlbumTracks(@Path("album_id") String albumId);

    @GET("/editorial/0/charts")
    Call<JsonObject> getTracks();

}

