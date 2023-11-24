package nhom2.voztify.Api;

import com.google.gson.JsonObject;

import nhom2.voztify.AlbumSearchResponse;
import nhom2.voztify.ArtistSearchResponse;
import nhom2.voztify.TrackData;
import nhom2.voztify.TrendingAlbumResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DZService {
    @GET("/editorial/0/charts")
    Call<JsonObject> getTracks();

    @GET("/search/artist")
    Call<ArtistSearchResponse> searchArtist(@Query("q") String artistName);

    @GET("/search/album")
    Call<AlbumSearchResponse> searchAlbum(@Query("q") String albumTitle);

    @GET("/editorial/0/selection")
    Call<TrendingAlbumResponse> getTrendingAlbums();

    @GET("/album/{album_id}/tracks")
    Call<TrackData> getAlbumTracks(@Path("album_id") String albumId);
}

