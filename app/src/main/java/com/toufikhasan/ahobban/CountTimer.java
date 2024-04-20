package com.toufikhasan.ahobban;

import android.os.CountDownTimer;

public class CountTimer {
    private static CountDownTimer countDownTimer;
    private static finishedCount onFinishListener;

    interface finishedCount {
        void onCountFinish();
    }

    public static void startTimer(finishedCount listener) {
        onFinishListener = listener;
        if (countDownTimer == null) {
            setCountDownTimer(50000);
            countDownTimer.start();
        }
    }

    public static void setCountDownTimer(final long milliseconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(milliseconds, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                // You can add code here for what should happen during each tick of the countdown
            }

            @Override
            public void onFinish() {
                if (onFinishListener != null) {
                    onFinishListener.onCountFinish();
                }
            }
        };
    }
}

