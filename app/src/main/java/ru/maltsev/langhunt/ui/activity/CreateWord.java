package ru.maltsev.langhunt.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.network.TokenManager;
import ru.maltsev.langhunt.network.client.ApiUtilities;
import ru.maltsev.langhunt.network.client.RetrofitBuilder;
import ru.maltsev.langhunt.network.model.images.ImageModel;
import ru.maltsev.langhunt.network.model.images.SearchModel;
import ru.maltsev.langhunt.network.service.WordApiService;
import ru.maltsev.langhunt.ui.Card;
import ru.maltsev.langhunt.ui.ImageAdapter;

public class CreateWord extends AppCompatActivity {

    EditText term;
    EditText definition;
    Button addCardButton;
    ImageView imageView;

    private RecyclerView recyclerView;
    private ArrayList<ImageModel> list;
    private LinearLayoutManager manager;
    private ImageAdapter adapter;
    private int page = 1;
    private int pageSize = 30;
    private boolean isLastPage;

    private WordApiService service;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add word");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(WordApiService.class, tokenManager);

        term = findViewById(R.id.term);
        definition = findViewById(R.id.definition);
        imageView = findViewById(R.id.image_create_view);
        addCardButton = findViewById(R.id.add_card_button);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term.getText().toString() != "" && definition.getText().toString() != "") {
                    Card newCard = new Card(term.getText().toString(),
                            definition.getText().toString(), getIntent().getExtras().getLong("SetId"),
                            adapter.getImageModel().getUrls().getRegular());
                    addWord(newCard);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerViewImages);
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list,imageView);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPos = manager.findFirstVisibleItemPosition();

                if (!isLastPage) {
                    if ((visibleItem + firstVisibleItemPos >= totalItem)
                            && firstVisibleItemPos >= 0 && totalItem >= pageSize) {
                        page++;
                        getData();
                    }
                }
            }
        });

        term.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData(charSequence.toString());
                Toast.makeText(getApplicationContext(),charSequence.toString(), Toast.LENGTH_SHORT);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getData() {
        ApiUtilities.getApiInterface().getImages(page, 30)
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                        if (response.body() != null) {
                            list.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }

                        if (list.size() > 0) {
                            isLastPage = list.size() < pageSize;
                        } else isLastPage = true;
                    }

                    @Override
                    public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                        Toast.makeText(CreateWord.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
    }


    private void addWord(Card newCard) {
        Call<Card> call = service.addWord("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(), newCard);

        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateWord.this, "Card created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateWord.this, "Error when adding a card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void searchData(String query) {
        ApiUtilities.getApiInterface().searchImage(query)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        list.clear();
                        list.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {

                    }
                });
    }
}