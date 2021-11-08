package ru.maltsev.langhunt.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import ru.maltsev.langhunt.ui.Card;
import ru.maltsev.langhunt.ui.RecyclerViewAdapterSets;
import ru.maltsev.langhunt.ui.SetWords;
import ru.maltsev.langhunt.ui.activity.SetActivity;

public class DictionaryFragment extends Fragment {

    private WordApiService service;
    private TokenManager tokenManager;
    private List<SetWords> lstSet;
    private RecyclerViewAdapterSets myAdapter;
    private Dialog mDialog;

    private FloatingActionButton floatingActionButton;

    private ViewGroup root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dictionary,container,false);

        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(WordApiService.class, tokenManager);

        lstSet = new ArrayList<>();

        floatingActionButton = root.findViewById(R.id.button_add_set);
        mDialog = new Dialog(this.getActivity());

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
                Dialog deleteDialog = new Dialog(root.getContext());
                showPopupDelete(deleteDialog, viewHolder.getAdapterPosition());
                myAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapterSets(getContext(), lstSet);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(myAdapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        getSets();
        return root;
    }

    private void getSets(){
        Call<List<SetWords>> call = service.getSets("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken());

        call.enqueue(new Callback<List<SetWords>>() {
            @Override
            public void onResponse(Call<List<SetWords>> call, Response<List<SetWords>> response) {
                if (response.isSuccessful()){
                    for (SetWords set: response.body()){
                        lstSet.add(new SetWords(set.getSetId(),set.getTitle(),set.getUserId()));
                    }
                    myAdapter.update(lstSet);
                }
            }

            @Override
            public void onFailure(Call<List<SetWords>> call, Throwable t) {
            }
        });
    }

    private void showPopup(){
        mDialog.setContentView(R.layout.popup_add_set);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText title = mDialog.findViewById(R.id.title);
        Button addCardButton = mDialog.findViewById(R.id.add_set_button);
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString() != ""){
                    SetWords newSet = new SetWords(title.getText().toString());
                    addSet(newSet);
                }
            }
        });

        mDialog.show();
    }

    private void addSet(SetWords newSet){
        Call<SetWords> call = service.addSet("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(), newSet);

        call.enqueue(new Callback<SetWords>() {
            @Override
            public void onResponse(Call<SetWords> call, Response<SetWords> response) {
                if (response.isSuccessful()){
                    lstSet.add(response.body());
                    myAdapter.update(lstSet);
                    mDialog.dismiss();
                }
                else{
                }
            }

            @Override
            public void onFailure(Call<SetWords> call, Throwable t) {
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
                SetWords set = myAdapter.getSet(position);
                deleteSet(deleteDialog, set, position);
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

    private void deleteSet(Dialog deleteDialog, SetWords set, int position) {
        Call<SetWords> call = service.deleteSet("Bearer " + tokenManager.getToken().getAccessToken(),
                tokenManager.getToken().getRefreshToken(), set);

        call.enqueue(new Callback<SetWords>() {
            @Override
            public void onResponse(Call<SetWords> call, Response<SetWords> response) {
                if (response.isSuccessful()) {
                    lstSet.remove(position);
                    myAdapter.update(lstSet);
                    deleteDialog.dismiss();
                } else {
                }
            }
            @Override
            public void onFailure(Call<SetWords> call, Throwable t) {
            }
        });
    }
}