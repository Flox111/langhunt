package ru.maltsev.langhunt.network.service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.maltsev.langhunt.network.model.AccessToken;
import ru.maltsev.langhunt.network.model.LoginRequest;
import ru.maltsev.langhunt.network.model.SignupRequest;
import ru.maltsev.langhunt.network.model.User;

public interface ApiAuthService {

    @POST("auth/signin")
    Call<User> login(@Body LoginRequest loginRequest);

    @POST("auth/signup")
    Call<User> signup(@Body SignupRequest signupRequest);

    @POST("auth/refreshtoken")
    Call<AccessToken> refresh(@Body AccessToken token);

    @GET("test")
    Call<List<String>> getTests(@Header("Authorization") String authToken);

}
