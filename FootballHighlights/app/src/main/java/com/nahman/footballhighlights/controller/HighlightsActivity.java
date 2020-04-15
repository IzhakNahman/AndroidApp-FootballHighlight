package com.nahman.footballhighlights.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.nahman.footballhighlights.view.HighlightsAdapter;
import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.model.Highlight;

import java.util.ArrayList;
import java.util.Collections;

public class HighlightsActivity extends AppCompatActivity {

    ArrayList<Highlight> highlightsArray;
    ArrayList<Highlight> flexibleHighlightsArray;
    RecyclerView rvHighlights;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        Intent intent = getIntent();
        highlightsArray = intent.getParcelableArrayListExtra("highlights");
        System.out.println(highlightsArray.toString());

        rvHighlights=findViewById(R.id.highlights_rv);
        etSearch=findViewById(R.id.highlights_etSearch);

        Collections.sort(highlightsArray, new Highlight.HighlightDateComparator());

        rvHighlights.setAdapter(new HighlightsAdapter(highlightsArray));
        rvHighlights.setLayoutManager(new LinearLayoutManager(this));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = etSearch.getText().toString();
                if (text.length()==0){
                    rvHighlights.setAdapter(new HighlightsAdapter(highlightsArray));
                    return;
                }
                flexibleHighlightsArray = new ArrayList<>();
                for (int i = 0; i < highlightsArray.size(); i++) {
                    if (highlightsArray.get(i).getCompetition().toUpperCase().contains(text.toUpperCase())){
                        flexibleHighlightsArray.add(highlightsArray.get(i));
                    }
                    if (highlightsArray.get(i).getTitle().toUpperCase().contains(text.toUpperCase())){
                        flexibleHighlightsArray.add(highlightsArray.get(i));
                    }

                }
                rvHighlights.setAdapter(new HighlightsAdapter(flexibleHighlightsArray));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case  android.R.id.home :
                //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.topMenu_about:
                startActivity(new Intent(this,AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
