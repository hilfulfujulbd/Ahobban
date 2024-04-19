package com.toufikhasan.ahobban;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int UPDATE_IN_APP_CODE = 6090;
    private static final long LENGTH_MILLISECONDS_WAITE = 5000;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    InternetConnectivity internetConnectivity;
    AdView mAdView;
    LinearLayout linearLayoutAds;
    ReviewManager manager;
    ReviewInfo reviewInfo;
    AdsControllerClass adsControllerClass;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Title Text
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.Ahobban_Text);


        MobileAds.initialize(this, initializationStatus -> {
        });
        mAdView = findViewById(R.id.adView);
        linearLayoutAds = findViewById(R.id.bannerLayoutAds);



        internetConnectivity = new InternetConnectivity(this);
        adsControllerClass = new AdsControllerClass(this);

        if(internetConnectivity.isConnected()){
            bannerAdsLoad();
            countDownTimer.start();
        }

        if (!preferencesForVisibleDialogForMessageBook()) {
            JOIN_OUR_GROUP_DIALOG_BOX();
        }

        // Drawable Show & hide java program
        drawerLayout = findViewById(R.id.drawerLayout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Navigation on menu item selected
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button kacheAsarGolpo = findViewById(R.id.kacheAsarGolpo);

        kacheAsarGolpo.setOnClickListener(view -> startActivityFile("কাছে আসার গল্প", "kacheAsarGolpo.txt"));

        Button helthdyLifeStyle = findViewById(R.id.helthdyLifeStyle);

        helthdyLifeStyle.setOnClickListener(view -> startActivityFile("হেলদি লাইফস্টাইল", "helthdyLifeStyle.txt"));

        Button dehaBaron = findViewById(R.id.dehaBaron);

        dehaBaron.setOnClickListener(view -> startActivityFile("দেহাবরণ", "dehaBaron.txt"));

        Button sopnoKotha = findViewById(R.id.sopnoKotha);

        sopnoKotha.setOnClickListener(view -> startActivityFile("স্বপ্নকথা", "sopnoKotha.txt"));

        Button diptimoyTaronno = findViewById(R.id.diptimoyTaronno);

        diptimoyTaronno.setOnClickListener(view -> startActivityFile("দীপ্তিময় তারুণ্য", "diptimoyTaronno.txt"));

        Button AllahorChador = findViewById(R.id.AllahorChador);

        AllahorChador.setOnClickListener(view -> startActivityFile("আল্লাহর চাদর", "AllahorChador.txt"));

        Button spacialMediaManaros = findViewById(R.id.spacialMediaManaros);

        spacialMediaManaros.setOnClickListener(view -> startActivityFile("সোশ্যাল মিডিয়া ম্যানারস", "spacialMediaManaros.txt"));

        Button shesVorosa = findViewById(R.id.shesVorosa);

        shesVorosa.setOnClickListener(view -> startActivityFile("শেষ ভরসা", "shesVorosa.txt"));


    }

    private void bannerAdsLoad() {
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(LENGTH_MILLISECONDS_WAITE, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(internetConnectivity.isConnected()){
                    if(mAdView == null){
                        linearLayoutAds.setVisibility(View.GONE);
                    }
                    adsControllerClass.AdsBannerShow(mAdView);
                    linearLayoutAds.setVisibility(View.VISIBLE);
                }

            }
        };
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitAlertBox();
            //super.onBackPressed();
        }

    }

    public void exitAlertBox() {
        AlertDialog.Builder exitAlert = new AlertDialog.Builder(MainActivity.this);
        // Title set
        exitAlert.setTitle("Alert Dialog.");
        // Massage Set
        exitAlert.setMessage("আপনি কি এখান থেকে বের হতে চান?");
        // Icon set
        exitAlert.setIcon(R.drawable.alert_icon);

        // Positive Button
        exitAlert.setCancelable(false);

        exitAlert.setPositiveButton("হ্যাঁ", (dialogInterface, i) -> finish());
        exitAlert.setNegativeButton("না", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialog = exitAlert.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        } else if (item.getItemId() == R.id.writter) {
            startActivityFile("লেখক পরিচিতি", "writter_info.txt");
        } else if (item.getItemId() == R.id.about_us) {
            startActivityFile("আমাদের সম্পর্কে", "about.txt");
        } else if (item.getItemId() == R.id.privacy) {
            gotoUrl("https://toufikhasan.com/android-apk/book/ahobban-book/privacy-policy.html");
        } else if (item.getItemId() == R.id.update_app) {
            IN_APP_UPDATE_AVAILABLE_MESSAGE_BOOK();
        } else if (item.getItemId() == R.id.aber_vinno_kicu_app) {
            gotoUrl("https://play.google.com/store/apps/details?id=com.toufikhasan.abarvinnokichuhok");
        } else if (item.getItemId() == R.id.message_book_app) {
            gotoUrl("https://play.google.com/store/apps/details?id=com.toufikhasan.massagebook");
        } else if (item.getItemId() == R.id.moreApp) {
            gotoUrl("https://play.google.com/store/apps/dev?id=5871408368342725724");
        } else if (item.getItemId() == R.id.ratting) {
            IN_APP_REVIEW_AVAILABLE_MESSAGE_BOOK();
        } else if (item.getItemId() == R.id.contact_us) {
            startActivityFile("যোগাযোগ পেইজ", "contact.txt");
        } else if (item.getItemId() == R.id.website) {
            gotoUrl("http://toufikhasan.com");
        } else if (item.getItemId() == R.id.shair) {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ahobban App contact");
            String shareMassage = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\nওয়েবসাইটঃ https://toufikhasan.com\nকম্পানির ওয়েবসাইটঃ\nhttp://hilfulfujul.com.bd\n\nThanks for share.";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMassage);

            startActivity(Intent.createChooser(shareIntent, "ShareVia"));
        } else if (item.getItemId() == R.id.facebook_page) {
            gotoUrl("https://www.facebook.com/toufik.bd.official");
        } else if (item.getItemId() == R.id.facebook_group) {
            gotoUrl("https://www.facebook.com/groups/books.my.friend");
        } else if (item.getItemId() == R.id.youtube) {
            gotoUrl("https://www.youtube.com/channel/UCJWmYNTgEvJsDm0zqj3lIxw");
        } else if (item.getItemId() == R.id.youtube2) {
            gotoUrl("https://www.youtube.com/channel/UCSw15OyHP_dEzEyHQALwjzw");
        } else if (item.getItemId() == R.id.linkedin) {
            gotoUrl("https://www.linkedin.com/in/ownertoufikhasan/");
        }
        return false;
    }

    private void JOIN_OUR_GROUP_DIALOG_BOX() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_join_groph);
        dialog.setCancelable(false);
        Button GROUP_JOIN = dialog.findViewById(R.id.group_join_now);
        GROUP_JOIN.setOnClickListener(v -> {
            gotoUrl("https://www.facebook.com/groups/books.my.friend");
            dialog.dismiss();
        });
        TextView alreadyJOIN = dialog.findViewById(R.id.group_already_join);
        alreadyJOIN.setOnClickListener(v -> {
            preferencesSaveDataMessageBook();
            dialog.dismiss();
        });
        ImageView close = dialog.findViewById(R.id.group_closeDialog_box);
        close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private boolean preferencesForVisibleDialogForMessageBook() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("saveDialogBoxDataMessageBook", MODE_PRIVATE);
        return sharedPreferences.getBoolean("fb_group_join_save", false);
    }

    @SuppressLint("ApplySharedPref")
    private void preferencesSaveDataMessageBook() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("saveDialogBoxDataMessageBook", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("fb_group_join_save", true);
        editor.commit();
    }

    private void startActivityFile(String title, String fileName) {
        Intent intent = new Intent(MainActivity.this, ShowText.class);
        intent.putExtra(ShowText.TITLE_NAME, title);
        intent.putExtra(ShowText.FILE_NAME, fileName);
        startActivity(intent);
    }

    private void gotoUrl(String link) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    private void IN_APP_UPDATE_AVAILABLE_MESSAGE_BOOK() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_IN_APP_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(this, "এখনো আপডেট আসে নাই!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void IN_APP_REVIEW_AVAILABLE_MESSAGE_BOOK() {

        manager = ReviewManagerFactory.create(this);

        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
                assert reviewInfo != null;
                Task<Void> voidTask = manager.launchReviewFlow(this, reviewInfo);

                voidTask.addOnSuccessListener(unused -> Toast.makeText(this, "রেটিং দেওয়ার জন্য ধন্যবাদ.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Something ERROR...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_IN_APP_CODE) {
            Toast.makeText(this, "Updating now...", Toast.LENGTH_SHORT).show();
        }

    }
}