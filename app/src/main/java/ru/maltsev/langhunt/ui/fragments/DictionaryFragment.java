package ru.maltsev.langhunt.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.maltsev.langhunt.R;
import ru.maltsev.langhunt.ui.RecyclerViewAdapter;
import ru.maltsev.langhunt.ui.SetWords;

public class DictionaryFragment extends Fragment {

    List<SetWords> lstSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dictionary,container,false);

        lstSet = new ArrayList<>();
        lstSet.add(new SetWords("Title1"));
        lstSet.add(new SetWords("Title2"));
        lstSet.add(new SetWords("Title3"));
        lstSet.add(new SetWords("Title4"));
        lstSet.add(new SetWords("Title5"));
        lstSet.add(new SetWords("Title6"));
        lstSet.add(new SetWords("Title7"));
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(getContext(), lstSet);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(myAdapter);
        return root;
    }
}