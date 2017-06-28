package com.boardwords.utils;


import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.boardwords.R;
import com.varunest.sparkbutton.SparkButton;

@SuppressWarnings("ALL")
public class AnimationUtil implements Interpolator, Animation.AnimationListener {
    private Context ctx;
    private double mAmplitude = 1;
    private double mFrequency = 10;
    private SparkButton button = null;

    public AnimationUtil(Context ctx) {
        this.ctx = ctx;
    }

    private AnimationUtil(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    // Bounce Animation
    public void bounceAnimation(SparkButton button) {
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.bounce);
        button.startAnimation(myAnim);
    }

    // Bounce Interpolation Animation
    public void bounceInterpolationAnimation(SparkButton button) {
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        AnimationUtil interpolator = new AnimationUtil(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
    }

    public void slideInLeft(final SparkButton button) {
        this.button = button;
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_left);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        button.startAnimation(myAnim);
    }

    public void slideInRight(final SparkButton button) {
        this.button = button;
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_right);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        button.startAnimation(myAnim);
    }

    public void slideInUp(final SparkButton button) {
        this.button = button;
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_up);
        myAnim.setAnimationListener(this);
        button.startAnimation(myAnim);
    }

    public void slideOutDown(final SparkButton button) {
        this.button = button;
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_out_down);
        myAnim.setAnimationListener(this);
        button.startAnimation(myAnim);
    }

    public void slideOutLeft(final SparkButton button) {
        this.button = button;
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_out_left);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        button.startAnimation(myAnim);
    }

    public void slideOutRight(final SparkButton button) {
        this.button = button;
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_out_right);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.playAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        button.startAnimation(myAnim);
    }

    public void slideOutMicro(SparkButton button) {
        Animation myAnim = AnimationUtils.loadAnimation(ctx, R.anim.slide_out_micro);
        button.startAnimation(myAnim);
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        button.playAnimation();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        button.playAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}