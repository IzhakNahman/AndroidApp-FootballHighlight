package com.nahman.footballhighlights.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.view.TeamsAdapter;
import com.nahman.footballhighlights.model.Highlight;

import java.util.ArrayList;
import java.util.Collections;

public class TeamsFragment extends Fragment {

    ArrayList<Highlight> highlightsArray;
    ArrayList<Highlight> flexibleHighlightsArray;
    RecyclerView rvTeams;
    EditText etSearch;


    public static TeamsFragment newInstance(ArrayList<Highlight> highlights) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("highlights",highlights);
        TeamsFragment fragment = new TeamsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teams, container, false);

        rvTeams = root.findViewById(R.id.teams_rv);
        etSearch = root.findViewById(R.id.teams_etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = etSearch.getText().toString();
                if (text.length()==0){
                    rvTeams.setAdapter(new TeamsAdapter(highlightsArray,null,root.getContext()));
                    return;
                }

                System.out.println("text "+text);
                rvTeams.setAdapter(new TeamsAdapter(highlightsArray,text,root.getContext()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null)
            return;

        highlightsArray = args.getParcelableArrayList("highlights");
        System.out.println("Leagues: " + highlightsArray.toString());

        Collections.sort(highlightsArray, new Highlight.HighlightTeamsComparator());

        rvTeams.setAdapter(new TeamsAdapter(highlightsArray,null,this.getContext()));
        rvTeams.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }
}
