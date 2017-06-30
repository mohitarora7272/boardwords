package com.boardwords.utils;

import android.content.Context;
import android.os.Vibrator;

import com.boardwords.preference.Preference;

public class VibrationUtil {
    private Context ctx;

    public VibrationUtil(Context ctx) {
        this.ctx = ctx;
    }

    public void doVibration() {
        if (Preference.getVibration(ctx)) {
            Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(100);
        }
    }

    public void doVibrationPattern() {
        if (Preference.getVibration(ctx)) {
            Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 100, 1000};
            // 0 for repeat vibration
            // -1 for once vibration
            v.vibrate(pattern, -1);
        }
    }

    public void doVibrationPatternLong() {
        if (Preference.getVibration(ctx)) {
            Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {100, 1000, 1000};
            // 0 for repeat vibration
            // -1 for once vibration
            v.vibrate(pattern, -1);
        }
    }
}