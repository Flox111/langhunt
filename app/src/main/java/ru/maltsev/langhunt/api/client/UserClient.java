package ru.maltsev.langhunt.api.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserClient {
    public static final String BASE_URL = "http://192.168.0.2:8080/api/auth/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit == null){
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.2:8080/api/auth/")
                    .addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();
        }
        return retrofit;
    }
}
