package com.toufikhasan.ahobban;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.toufikhasan.ahobban.admob.InterstitialAds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ShowText extends AppCompatActivity {
    public static final String FILE_NAME = "FILE_NAME";
    public static final String TITLE_NAME = "TITLE_NAME";
    LinearLayout bannerLayoutAds;
    private String filename;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        //        Google Ads Start
        MobileAds.initialize(this, initializationStatus -> {
        });

        bannerLayoutAds = findViewById(R.id.bannerLayoutAds);
        mAdView = findViewById(R.id.adView);

        InternetConnectivity internetConnectivity = new InternetConnectivity(getApplicationContext());

        if (internetConnectivity.isConnected()) {
            showAdsConfig();
        }

        Intent intent = getIntent();
        filename = intent.getStringExtra(FILE_NAME);
        String titleName = intent.getStringExtra(TITLE_NAME);

        // Title Change
        Objects.requireNonNull(getSupportActionBar()).setTitle(titleName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fileRead();
    }

    public void fileRead() {
        TextView showText = findViewById(R.id.showText);
        // Font Change code start
        showText.setTypeface(Typeface.createFromAsset(getAssets(), "font/Bangla_Font.ttf"));
        // Font Change code start

        String text = "";
        try {
            InputStream x = getAssets().open("text/" + filename);
            int size = x.available();
            byte[] buffer = new byte[size];
            final int read = x.read(buffer);
            x.close();
            text = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        showText.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (InterstitialAds.mInterstitialAd != null) {
            InterstitialAds.mInterstitialAd.show(this);
            InterstitialAds.mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    InterstitialAds.mInterstitialAd = null;
                    CountTimer.startTimer(() -> InterstitialAds.loadInterstitialAd(getApplicationContext()));
                }
            });
        }
        super.onBackPressed();
    }

    private void showAdsConfig() {
        if (mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    bannerLayoutAds.setVisibility(View.VISIBLE);
                }
            });
        } else {
            bannerLayoutAds.setVisibility(View.GONE);
        }
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}