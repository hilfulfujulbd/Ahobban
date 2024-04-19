package com.toufikhasan.ahobban;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdsControllerClass {

    Context context;
    private AdRequest adRequest;
    private InterstitialAd interstitialAds;

    public AdsControllerClass(Context context) {
        this.context = context;
    }

    public void AdsBannerShow(AdView adView) {
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void AdsLoadInterstitial() {
        if (interstitialAds == null) {
            adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(context, context.getString(R.string.ads_image_id), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    interstitialAds = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    interstitialAds = null;
                    Log.d("Interstitial Ads", "Interstitial Ads load Error!");
                }
            });
        }
    }

    public void adsShowInterstitial() {
        if (interstitialAds != null) {
            interstitialAds.show((Activity) context);
        }
    }
}
