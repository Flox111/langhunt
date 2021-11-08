package ru.maltsev.langhunt.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.network.TokenManager;
import ru.maltsev.langhunt.network.client.RetrofitBuilder;
import ru.maltsev.langhunt.network.service.WordApiService;
import ru.maltsev.langhunt.ui.RecyclerViewAdapterSets;
import ru.maltsev.langhunt.ui.SetWords;

public class DictionaryFragment extends Fragment {

    WordApiService service;
    TokenManager tokenManager;
    List<SetWords> lstSet;
    RecyclerViewAdapterSets myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dictionary,container,false);

        tokenManager = TokenManager.getInstance(this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceWithAuth(WordApiService.class, tokenManager);

        lstSet = new ArrayList<>();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapterSets(getContext(), lstSet);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(myAdapter);

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
                else{
                    //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SetWords>> call, Throwable t) {
                //Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}