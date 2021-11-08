package ru.maltsev.langhunt.network.service;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.maltsev.langhunt.network.model.AccessToken;
import ru.maltsev.langhunt.ui.SetWords;
import ru.maltsev.langhunt.ui.Word;

public interface WordApiService {

    @POST("auth/refreshtoken")
    Call<AccessToken> refresh(@Body AccessToken token);

    @GET("getSets")
    Call<List<SetWords>> getSets(@Header("Authorization") String authToken, @Query(value = "refresh_token") String refreshToken);

    @GET("getWords")
    Call<List<Word>> getWords(@Header("Authorization") String authToken,
                             @Query(value = "refresh_token") String refreshToken,
                             @Query(value = "set_id") Long setId);
    @POST("addWord")
    Call<Word> addWord(@Header("Authorization") String authToken,
                       @Query(value = "refresh_token") String refreshToken,
                       @Body Word word);
}
