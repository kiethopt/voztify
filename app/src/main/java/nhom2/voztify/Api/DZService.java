package nhom2.voztify.Api;

import com.google.gson.JsonObject;

import nhom2.voztify.Controller.AlbumSearchResponse;
import nhom2.voztify.Controller.ArtistAlbumsResponse;
import nhom2.voztify.Controller.ArtistSearchResponse;
import nhom2.voztify.Controller.ArtistTracksResponse;
import nhom2.voztify.Model.Track;
import nhom2.voztify.Controller.DeezerResponse;
import nhom2.voztify.Controller.RelatedArtistsResponse;
import nhom2.voztify.Controller.TrackResponse;
import nhom2.voztify.Controller.TrendingAlbumResponse;
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
    @GET("/radio/top")
    Call<DeezerResponse> getTopRadios();
    @GET("/track/{track_id}")
    Call<Track> getTrack(@Path("track_id") String trackId);

}

