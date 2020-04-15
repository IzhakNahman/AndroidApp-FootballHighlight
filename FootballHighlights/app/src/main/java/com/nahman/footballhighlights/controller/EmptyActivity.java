package com.nahman.footballhighlights.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.model.Highlight;
import com.nahman.footballhighlights.model.IO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmptyActivity extends AppCompatActivity {

    ArrayList<Highlight> highlightsArray = new ArrayList<>();

    public static String JSON_URL = "https://www.scorebat.com/video-api/v1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        getSupportActionBar().hide();


        this.runOnUiThread(new Runnable() {
            public void run() {
                ImageView ivBall1 = findViewById(R.id.emptyActivity_ivBall1);
                ImageView ivBall2 = findViewById(R.id.emptyActivity_ivBall2);
                ivBall1.setTranslationY(-2000f);
                ivBall1.animate().translationYBy(2000f).setDuration(1200);
                ivBall2.setTranslationY(2000f);
                ivBall2.animate().translationYBy(-2000f).setDuration(1200);
            }
        });


        if (IO.haveNetworkConnection(this) == true){
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(JSON_URL);
        }else{
            new AlertDialog.Builder(this)
            .setTitle("Info")
            .setMessage("Internet not available, Cross check your internet connectivity and try again")
            .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }




    }


    public class DownloadTask extends AsyncTask<String, Void ,  ArrayList<Highlight>> {
        @Override
        protected ArrayList<Highlight> doInBackground(String... urls) {
            String result = "";

            ArrayList<Highlight> highlightsArray = new ArrayList<>();
            try {
                result = IO.readURL(urls[0]);

                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String date = jsonObject.getString("date");
                    JSONObject side1 = jsonObject.getJSONObject(("side1"));
                    JSONObject side2 = jsonObject.getJSONObject(("side2"));
                    JSONObject competitionObj = jsonObject.getJSONObject(("competition"));
                    String [] teams = new String[]{side1.getString("name"),side2.getString("name") };
                    String competition = competitionObj.getString("name");
                    String imageUrl =  jsonObject.getString("thumbnail");

                    String embed;
                    JSONObject video;
                    JSONArray videos = jsonObject.getJSONArray("videos");
                    if (videos.length()>1){
                        video = videos.getJSONObject(1);
                    }else{
                        video = videos.getJSONObject(0);
                    }

                    embed = video.getString("embed");

                    Highlight highlight = new Highlight(title,embed,teams,competition,imageUrl,date);
                    highlightsArray.add(highlight);
                }

                return highlightsArray;


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Highlight> highlights) {
            super.onPostExecute(highlights);
            highlightsArray = highlights;



            Handler h = new Handler();
            h.postDelayed(()->{
                Intent intent = new Intent(EmptyActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra("highlights",highlightsArray);
                startActivity(intent);
                finish();
            }, 800);

        }
    }
}

