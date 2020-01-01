package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    private String webUrl ="https://www.mixbook.com/";
    //ProgressBar progressBar;
    NumberProgressBar progressBar;
    Button btnNoIntRetry;
    RelativeLayout noInrernetChackLyout;

    SpaceNavigationView myNav;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        chackConnection();
        navBar();

    }


    private void init()
    {
        webView =(WebView) findViewById(R.id.myWebView);
        webView.loadUrl(webUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        progressBar= (NumberProgressBar) findViewById(R.id.number_progress_bar);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);

                setTitle("Loading....");
                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }


                super.onProgressChanged(view, newProgress);
            }
        });

        btnNoIntRetry = (Button) findViewById(R.id.noInternetButton);
        noInrernetChackLyout = (RelativeLayout) findViewById(R.id.noInternetLayout);


        btnNoIntRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chackConnection();
            }
        });


    }

    public void chackConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            noInrernetChackLyout.setVisibility(View.GONE);

        }else if (mobileNetwork.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            noInrernetChackLyout.setVisibility(View.GONE);

        }else {
            webView.setVisibility(View.GONE);
            noInrernetChackLyout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You want to Exit ?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    }).show();
        }

    }


    private void navBar(){
        myNav = findViewById(R.id.myNavID);

        myNav.initWithSaveInstanceState(savedInstanceState);
        myNav.addSpaceItem(new SpaceItem("", R.drawable.ic_share));
        myNav.addSpaceItem(new SpaceItem("", R.drawable.ic_back));
        myNav.addSpaceItem(new SpaceItem("", R.drawable.ic_forword));
        myNav.addSpaceItem(new SpaceItem("", R.drawable.ic_refresh));

        myNav.setSpaceOnClickListener(new SpaceOnClickListener() {


            @Override
            public void onCentreButtonClick() {
                myNav.setCentreButtonSelectable(true);
                chackConnection();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                String index= Integer.toString(itemIndex);

                switch (index){
                    case "0":               //This is Share Button
                        ui();
                        Toast.makeText(MainActivity.this, "Share Option", Toast.LENGTH_SHORT).show();
                        break;
                    case "1":               //This is Back button
                        onBackPressed();
                        break;
                    case "2":               //This is Forword Button
                        if (webView.canGoForward()) {
                            webView.goForward();
                        }

                        break;
                    case "3":               //This is Retry Button
                        chackConnection();
                        break;
                }
                
                
                
            }


            @Override
            public void onItemReselected(int itemIndex, String itemName) {

                String index= Integer.toString(itemIndex);

                switch (index){
                    case "0":               //This is Share Button
                        ui();
                        Toast.makeText(MainActivity.this, "Share Option", Toast.LENGTH_SHORT).show();
                        break;
                    case "1":               //This is Back button
                        onBackPressed();
                        break;
                    case "2":               //This is Forword Button
                        if (webView.canGoForward()) {
                            webView.goForward();
                        }

                        break;
                    case "3":               //This is Retry Button
                        chackConnection();
                        break;
                }
            }
        });


    }

    private void ui(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = webView.getUrl();
        String shareSubject = webView.getTitle();

        shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

        startActivity(Intent.createChooser(shareIntent,"Share using"));
    }

}
