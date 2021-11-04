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
import ru.maltsev.langhunt.api.model.SignupRequest;
import ru.maltsev.langhunt.api.model.User;
import ru.maltsev.langhunt.api.service.UserService;

public class SignupTabFragment extends Fragment {

    EditText username;
    EditText email;
    EditText password;
    Button signup_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);

        username = root.findViewById(R.id.username);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        signup_btn = root.findViewById(R.id.signup_btn);

        signup_btn.setOnClickListener(v -> signup());

        return root;
    }

    private void signup(){
        UserService userService = UserClient.getClient().create(UserService.class);
        SignupRequest signupRequest;
        Call<User> call;
        if (username.getText().toString() != "" && email.getText().toString() != "" && password.getText().toString() != ""){
            signupRequest = new SignupRequest(username.getText().toString(),
                    email.getText().toString(), password.getText().toString());
            call = userService.signup(signupRequest);
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
                    Toast.makeText(getContext(),response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(), Toast.LENGTH_SHORT).show();
                System.out.println(t.toString());
            }
        });
    }
}
