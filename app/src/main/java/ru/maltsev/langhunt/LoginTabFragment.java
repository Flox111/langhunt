package ru.maltsev.langhunt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    EditText email;
    EditText password;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        btn = root.findViewById(R.id.login_btn);

        email.setTranslationX(800);
        password.setTranslationX(800);
        btn.setTranslationX(800);

        email.setAlpha(0);
        password.setAlpha(0);
        btn.setAlpha(0);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

        return root;
    }
}
