package com.nahman.footballhighlights.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahman.footballhighlights.R;
import com.nahman.footballhighlights.model.Highlight;
import com.squareup.picasso.Picasso;

public class VideoActivity extends AppCompatActivity {

    Highlight highlight;
    ConstraintLayout constraintLayout;
    TextView tvTitle, tvCompetition, tvDate;
    ImageView ivImage;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int newOrientation = newConfig.orientation;

        System.out.println("Here");
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout.setVisibility(View.GONE);
        }else{
            constraintLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        highlight = intent.getParcelableExtra("video");
        constraintLayout = findViewById(R.id.constraintLayout);
        ivImage = findViewById(R.id.video_ivImage);
        tvTitle = findViewById(R.id.title);
        tvDate = findViewById(R.id.about_tvDate);
        tvCompetition = findViewById(R.id.about_tvCompetition);

        Picasso.get()
                .load(highlight.getImageUrl())
                .placeholder(R.drawable.blackrec)
                .error(R.drawable.blackrec)
                .into(ivImage);
        tvTitle.setText(highlight.getTitle());
        tvCompetition.setText(highlight.getCompetition());
        tvDate.setText(highlight.getDate());


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            constraintLayout.setVisibility(View.GONE);
        } else {
            constraintLayout.setVisibility(View.VISIBLE);
        }



        WebView webView = findViewById(R.id.video_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new myChrome());

        //webView.loadUrl("https://www.scorebat.com/embed/g/865715/?s=2");
        //webView.loadData("<div style='width:100%;height:0px;position:relative;padding-bottom:45.250%;'><iframe src='https://www.scorebat.com/embed/v/5e6578c94bdc7/?s=2' frameborder='0' width='100%' height='100%' allowfullscreen allow='autoplay; fullscreen' style='width:100%;height:100%;position:absolute;left:0px;top:0px;overflow:hidden;'></iframe></div>","text/html","UTF-8");

        System.out.println("getEmbed "+highlight.getEmbed());
        webView.loadData(highlight.getEmbed(),"text/html","UTF-8");

    }

    private class myChrome extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        //protected FrameLayout mFullscreenContainer;
        //private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        myChrome() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }
}
