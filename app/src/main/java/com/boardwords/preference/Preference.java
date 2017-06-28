package com.boardwords.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.boardwords.interfaces.Constant;
import com.boardwords.modal.WordsPOJO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Preference implements Constant {

    public static void setBoardName(Context ctx, String board_name) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_BOARD_NAME, board_name).apply();
    }

    public static void setBoardTime(Context ctx, int board_time) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_BOARD_TIME, board_time).apply();
    }

    public static void setRatingStar(Context ctx, int ratingStar) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_RATING_STAR, ratingStar).apply();
    }

    public static void saveList(Context ctx, ArrayList<WordsPOJO> list) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_LIST, json);
        editor.apply();
    }

    public static void setTimeOutRating(Context ctx, int ratingStar) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_TIMEOUT_RATING, ratingStar).apply();
    }

    public static String getBoardName(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(KEY_BOARD_NAME, "");
    }

    public static int getBoardTime(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(KEY_BOARD_TIME, -1);
    }

    public static int getRatingStar(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(KEY_RATING_STAR, -1);
    }

    public static ArrayList<WordsPOJO> getList(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        String json = sharedpreferences.getString(KEY_LIST, null);
        Type type = (Type) new TypeToken<ArrayList<WordsPOJO>>() {
        }.getType();
        ArrayList<WordsPOJO> inpList = new Gson().fromJson(json, (Type) type);
        return inpList;
    }

    public static int getTimeOutRating(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(KEY_TIMEOUT_RATING, 0);
    }
}