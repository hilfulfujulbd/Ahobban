package com.toufikhasan.ahobban;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ShowText extends AppCompatActivity {
    public static final String FILE_NAME = "FILE_NAME";
    public static final String TITLE_NAME = "TITLE_NAME";
    LinearLayout linearLayoutAds;
    AdsControllerClass adsControllerClass;
    CountDownTimer countDownTimer;
    private String filename;
    private AdView mAdView;

    final int ADS_SHOW_TIME_CONTROLLER = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        //        Google Ads Start
        MobileAds.initialize(this, initializationStatus -> {
        });

        linearLayoutAds = findViewById(R.id.bannerLayoutAds);
        mAdView = findViewById(R.id.adView);

        adsControllerClass = new AdsControllerClass(this);
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

    private void showAdsConfig() {

        if (isAdsReadyVisible()) {
            saveAdsDataVisible();
            adsControllerClass.AdsLoadInterstitial();

            if (mAdView != null) {
                adsControllerClass.AdsBannerShow(mAdView);
                linearLayoutAds.setVisibility(View.VISIBLE);
            } else {
                linearLayoutAds.setVisibility(View.GONE);
            }
        }
        adsShowSomeTimeAfter();
        countDownTimer.start();
    }

    private void adsShowSomeTimeAfter() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(ADS_SHOW_TIME_CONTROLLER, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                updateAdsDataVisible();
            }
        };
    }

    private boolean isAdsReadyVisible() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("INTERSTITIAL", MODE_PRIVATE);
        return sharedPreferences.getBoolean("SHOW", true);
    }

    @SuppressLint("ApplySharedPref")
    private void saveAdsDataVisible() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("INTERSTITIAL", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SHOW", false);
        editor.commit();
    }

    @SuppressLint("ApplySharedPref")
    private void updateAdsDataVisible() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("INTERSTITIAL", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SHOW", true);
        editor.commit();

        if (linearLayoutAds.getVisibility() == View.GONE) {
            showAdsConfig();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        adsControllerClass.adsShowInterstitial();
        super.onBackPressed();
    }
}