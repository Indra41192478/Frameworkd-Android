package com.example.wedding;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    WebView webviewku;
    WebSettings websettingku;
    private Button logoutbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        webviewku = findViewById(R.id.WebView1);

        websettingku = webviewku.getSettings();
        websettingku.setJavaScriptEnabled(true);

        webviewku.loadUrl("http://www.workshopjti.com/wedding");
        webviewku.setWebViewClient(new WebViewClient());


        logoutbutton = findViewById(R.id.logoutbtn);

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this , MainActivity.class));
            }
        });
    }

}
