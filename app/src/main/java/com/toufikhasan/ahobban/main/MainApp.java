package com.toufikhasan.ahobban.main;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.toufikhasan.ahobban.admob.InterstitialAds;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, initializationStatus -> {

        });

        InterstitialAds.loadInterstitialAd(this);
    }
}
