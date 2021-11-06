package ru.maltsev.langhunt.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.network.TokenManager;
import ru.maltsev.langhunt.network.client.RetrofitBuilder;
import ru.maltsev.langhunt.network.model.AccessToken;
import ru.maltsev.langhunt.network.model.LoginRequest;
import ru.maltsev.langhunt.network.model.User;
import ru.maltsev.langhunt.network.service.ApiService;
import ru.maltsev.langhunt.ui.MainActivity;

public class LoginTabFragment extends Fragment {

    EditText email;
    EditText password;
    Button login_btn;

    ApiService service;
    TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        login_btn = root.findViewById(R.id.login_btn);

        email.setTranslationX(800);
        password.setTranslationX(800);
        login_btn.setTranslationX(800);

        email.setAlpha(0);
        password.setAlpha(0);
        login_btn.setAlpha(0);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();


        login_btn.setOnClickListener(v -> login());

        if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        return root;
    }

    private void login(){

        LoginRequest loginRequest;
        Call<User> call;
        if (email.getText().toString() != "" && password.getText().toString() != ""){
            loginRequest = new LoginRequest(email.getText().toString(), password.getText().toString());
            call = service.login(loginRequest);
        }
        else{
            Toast.makeText(getContext(),"empty", Toast.LENGTH_SHORT).show();
            return;
        }

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(),response.body().getAccessToken(), Toast.LENGTH_SHORT).show();
                    tokenManager.saveToken(new AccessToken(response.body().getAccessToken(),response.body().getRefreshToken()));
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                else{
                    Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
