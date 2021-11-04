package ru.maltsev.langhunt.fragments;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.api.client.UserClient;
import ru.maltsev.langhunt.api.model.LoginRequest;
import ru.maltsev.langhunt.api.model.User;
import ru.maltsev.langhunt.api.service.UserService;

public class LoginTabFragment extends Fragment {

    EditText email;
    EditText password;
    Button login_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

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
        return root;
    }

    private void login(){
        UserService userService = UserClient.getClient().create(UserService.class);
        LoginRequest loginRequest;
        Call<User> call;
        if (email.getText().toString() != "" && password.getText().toString() != ""){
            loginRequest = new LoginRequest(email.getText().toString(), password.getText().toString());
            call = userService.login(loginRequest);
        }
        else{
            Toast.makeText(getContext(),"empty", Toast.LENGTH_SHORT).show();
            return;
        }

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(),response.body().getToken(), Toast.LENGTH_SHORT).show();
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
