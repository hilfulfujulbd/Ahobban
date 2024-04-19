package com.toufikhasan.ahobban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class WellcomeScreen extends AppCompatActivity {

    LinearLayout developerInfo;
    Animation developerAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_screen);

        //Status bar color
        Window window = this.getWindow();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        window.setStatusBarColor(ContextCompat.getColor(this,R.color.welcome_status_bar_color));

        developerInfo = findViewById(R.id.developer_info);

        // ProgressBar Animation
        developerAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dev_anim_bottom);

        developerInfo.setAnimation(developerAnimation);

        Thread thread = new Thread(this::doWorking);
        thread.start();
    }
    public void doWorking(){
        int progress;
        for (progress = 0; progress <= 100; progress = progress + 1){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runApp();
    }
    public void runApp(){
        startActivity(new Intent(WellcomeScreen.this,MainActivity.class));
        finish();
    }
}