package ru.maltsev.langhunt.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.network.TokenManager;
import ru.maltsev.langhunt.network.client.RetrofitBuilder;
import ru.maltsev.langhunt.network.service.WordApiService;
import ru.maltsev.langhunt.ui.RecyclerViewAdapterWords;
import ru.maltsev.langhunt.ui.Card;

public class SetActivity extends AppCompatActivity {


    private List<Card> cards;
    private WordApiService service;
    private TokenManager tokenManager;
    private RecyclerViewAdapterWords myAdapter;
    Dialog mDialog;

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

        cards = new ArrayList<>();

        textView = findViewById(R.id.set_title);

        floatingActionButton = findViewById(R.id.button_add_card);
        mDialog = new Dialog(SetActivity.this);


        textView.setText(getIntent().getExtras().getString("Title"));

        RecyclerView recyclerView = findViewById(R.id.recyclerview_words_id);
        myAdapter = new RecyclerViewAdapterWords(SetActivity.this, cards);
        recyclerView.setLayoutManager(new GridLayoutManager(SetActivity.this, 1));
        recyclerView.setAdapter(myAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Dialog deleteDialog = new Dialog(SetActivity.this);
                showPopupDelete(deleteDialog, viewHolder.getAdapterPosition());
                myAdapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        getWords();
    }

    private void getWords() {
        Call<List<Card>> call = service.getWords("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(), getIntent().getExtras().getLong("SetId"));

        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                if (response.isSuccessful()) {
                    for (Card card : response.body()) {
                        cards.add(card);
                    }
                    myAdapter.update(cards);
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
            }
        });
    }

    private void showPopup() {
        mDialog.setContentView(R.layout.popup_add_word);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText term = mDialog.findViewById(R.id.term);
        EditText definition = mDialog.findViewById(R.id.definition);
        Button addCardButton = mDialog.findViewById(R.id.add_card_button);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (term.getText().toString() != "" && definition.getText().toString() != "") {
                    Card newCard = new Card(term.getText().toString(),
                            definition.getText().toString(), getIntent().getExtras().getLong("SetId"));
                    addWord(newCard);
                }
            }
        });

        mDialog.show();
    }

    private void addWord(Card newCard) {
        Call<Card> call = service.addWord("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(), newCard);

        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful()) {
                    cards.add(response.body());
                    myAdapter.update(cards);
                    mDialog.dismiss();
                } else {
                    Toast.makeText(SetActivity.this, "Error when edding a card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupDelete(Dialog deleteDialog, int position) {
        deleteDialog.setContentView(R.layout.popup_delete);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button positive = deleteDialog.findViewById(R.id.positive);
        Button negative = deleteDialog.findViewById(R.id.negative);

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = myAdapter.getCard(position);
                deleteWord(deleteDialog, card, position);
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    private void deleteWord(Dialog deleteDialog, Card card, int position) {
        Call<Card> call = service.deleteCard("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(), card);

        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful()) {
                    cards.remove(position);
                    myAdapter.update(cards);
                    deleteDialog.dismiss();
                } else {
                    Toast.makeText(SetActivity.this, "Error when deleting a card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
