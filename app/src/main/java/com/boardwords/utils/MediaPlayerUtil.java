package com.boardwords.utils;


import android.content.Context;
import android.media.MediaPlayer;

import com.boardwords.R;

@SuppressWarnings("ALL")
public class MediaPlayerUtil implements MediaPlayer.OnCompletionListener {
    private Context ctx;
    private MediaPlayer mp;

    public MediaPlayerUtil(Context ctx) {
        this.ctx = ctx;
        initMp(ctx);
    }

    private void initMp(Context ctx) {
        mp = MediaPlayer.create(ctx, R.raw.button_sound);
        mp.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        mp.release();
    }

    // Play Sound Button
    public void playSoundButton(Context ctx) {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(ctx, R.raw.button_sound);
            }
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}