package nhom2.voztify.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeezerService {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.deezer.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static DZService getService() {
        return retrofit.create(DZService.class);
    }

}


