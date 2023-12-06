package nhom2.voztify.Api;

import com.google.gson.JsonObject;

import java.util.List;

import nhom2.voztify.AlbumSearchResponse;
import nhom2.voztify.ArtistAlbumsResponse;
import nhom2.voztify.ArtistSearchResponse;
import nhom2.voztify.ArtistTracksResponse;
import nhom2.voztify.Class.Track;
import nhom2.voztify.RelatedArtistsResponse;
import nhom2.voztify.TrackResponse;
import nhom2.voztify.TrendingAlbumResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DZService {
    @GET("/editorial/0/charts")
    Call<JsonObject> getTopTracks();

    @GET("/editorial/0/charts")
    Call<JsonObject> getTopArtists();

    @GET("/search/artist")
    Call<ArtistSearchResponse> searchArtist(@Query("q") String artistName);

    @GET("/artist/{artist_id}/albums")
    Call<ArtistAlbumsResponse> getArtistAlbums(@Path("artist_id") String artistId);

    @GET("/artist/{artist_id}/radio")
    Call<ArtistTracksResponse> getArtistTracks(@Path("artist_id") String artistId);
    @GET("/artist/{artist_id}/related")
    Call<RelatedArtistsResponse> getRelatedArtists(@Path("artist_id") String artistId);
    
    @GET("/search/album")
    Call<AlbumSearchResponse> searchAlbum(@Query("q") String albumTitle);

    @GET("/editorial/0/selection")
    Call<TrendingAlbumResponse> getTrendingAlbums();

    @GET("/album/{album_id}/tracks")
    Call<TrackResponse> getAlbumTracks(@Path("album_id") String albumId);
    @GET("/search/track")
    Call<TrackResponse> searchTrack(@Query("q") String trackTitle);


}

