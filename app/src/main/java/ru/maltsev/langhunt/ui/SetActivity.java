package ru.maltsev.langhunt.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.maltsev.langhunt.R;

public class SetActivity extends AppCompatActivity {


    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView = findViewById(R.id.set_title);
        textView.setText(getIntent().getExtras().getString("Title"));

    }
}
