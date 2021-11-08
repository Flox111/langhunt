package ru.maltsev.langhunt.ui.activity;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.network.TokenManager;
import ru.maltsev.langhunt.network.client.RetrofitBuilder;
import ru.maltsev.langhunt.network.service.WordApiService;
import ru.maltsev.langhunt.ui.RecyclerViewAdapterWords;
import ru.maltsev.langhunt.ui.Word;

public class SetActivity extends AppCompatActivity {


    private List<Word> words;
    private WordApiService service;
    private TokenManager tokenManager;
    private RecyclerViewAdapterWords myAdapter;

    private TextView textView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(WordApiService.class, tokenManager);

        words = new ArrayList<>();

        textView = findViewById(R.id.set_title);
        floatingActionButton = findViewById(R.id.button_add);

        textView.setText(getIntent().getExtras().getString("Title"));

        RecyclerView recyclerView = findViewById(R.id.recyclerview_words_id);
        myAdapter = new RecyclerViewAdapterWords(SetActivity.this, words);
        recyclerView.setLayoutManager(new GridLayoutManager(SetActivity.this,1));
        recyclerView.setAdapter(myAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(SetActivity.this,"error", Toast.LENGTH_SHORT).show();
                myAdapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        getWords();
    }

    private void getWords(){
        Call<Set<Word>> call = service.getWords("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(),getIntent().getExtras().getLong("SetId"));

        call.enqueue(new Callback<Set<Word>>() {
            @Override
            public void onResponse(Call<Set<Word>> call, Response<Set<Word>> response) {
                if (response.isSuccessful()){
                    for (Word word: response.body()){
                        words.add(word);
                    }
                    myAdapter.update(words);
                }
                else{
                    //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Set<Word>> call, Throwable t) {
                //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
