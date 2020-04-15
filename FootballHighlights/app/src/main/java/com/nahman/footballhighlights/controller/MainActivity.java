package com.nahman.footballhighlights.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.model.Highlight;
import com.nahman.footballhighlights.model.IO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Highlight> highlightsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo_tool_bar);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        Intent intent= getIntent();
        highlightsArray = intent.getParcelableArrayListExtra("highlights");
        //System.out.println("from Main" + highlightsArray.toString());

        navView.setSelectedItemId(R.id.navigation_leagues);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        change(LeaguesFragment.newInstance(highlightsArray));




    }




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_leagues:
                    change(LeaguesFragment.newInstance(highlightsArray));
                    return true;
                case R.id.navigation_teams:
                    change(TeamsFragment.newInstance(highlightsArray));
                    return true;
            }

            return false;

        }
    };


    public void change(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
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

            case R.id.topMenu_about:
                startActivity(new Intent(this,AboutActivity.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}




