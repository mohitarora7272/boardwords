package com.boardwords;

import android.app.Application;
import android.content.Context;


public class BoardWordsApplication extends Application {
    private static BoardWordsApplication sInstance;


    public static Context getContext()
    {
        return sInstance;
    }


    public BoardWordsApplication()
    {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
