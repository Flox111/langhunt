package ru.maltsev.langhunt.api.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.maltsev.langhunt.api.model.LoginRequest;
import ru.maltsev.langhunt.api.model.SignupRequest;
import ru.maltsev.langhunt.api.model.User;

public interface UserService {

    @POST("signin")
    Call<User> login(@Body LoginRequest loginRequest);

    @POST("signup")
    Call<User> signup(@Body SignupRequest signupRequest);

    @GET("secretinfo")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);

}
