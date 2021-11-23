package ru.maltsev.langhunt.network.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.maltsev.langhunt.network.service.ApiInterfaceImages;

public class ApiUtilities {
    public static final String BASE_URL = "https://api.unsplash.com";
    public static final String API_KEY = "u6yQr_ZhkpGgh7qs_7BcWEGKutcf3Ox7ID5vhU3Adtc";

    public static Retrofit retrofit = null;

    public static ApiInterfaceImages getApiInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterfaceImages.class);
    }
}
