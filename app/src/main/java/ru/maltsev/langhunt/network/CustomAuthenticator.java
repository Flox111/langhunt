package ru.maltsev.langhunt.network;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import ru.maltsev.langhunt.network.client.RetrofitBuilder;
import ru.maltsev.langhunt.network.model.AccessToken;
import ru.maltsev.langhunt.network.service.ApiAuthService;

public class CustomAuthenticator implements Authenticator {

    private TokenManager tokenManager;
    private static CustomAuthenticator INSTANCE;

    public CustomAuthenticator(TokenManager tokenManager){
        this.tokenManager = tokenManager;
    }

    public static synchronized CustomAuthenticator getInstance(TokenManager tokenManager){
        if (INSTANCE == null){
            INSTANCE = new CustomAuthenticator(tokenManager);
        }
        return INSTANCE;
    }


    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {

        if (responseCount(response) >= 3){
            return null;
        }

        AccessToken token = tokenManager.getToken();

        ApiAuthService service = RetrofitBuilder.createService(ApiAuthService.class);
        Call<AccessToken> call = service.refresh(token);
        retrofit2.Response<AccessToken> res = call.execute();

        if (res.isSuccessful()){
            AccessToken newToken = res.body();
            tokenManager.saveToken(newToken);

            return response.request().newBuilder().header("Authorization", "Bearer " + res.body().getAccessToken()).build();
        }
        else {
            return null;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
