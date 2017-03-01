package com.boardwords.preference;


import android.content.Context;
import android.content.SharedPreferences;

import com.boardwords.interfaces.Constant;
import com.boardwords.modal.WordsPOJO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


@SuppressWarnings("all")
public class Preference implements Constant {

    public static void setUserCredentials(Context ctx, String name, String email, String deviceId) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_DEVICE_ID, deviceId).commit();
    }

    public static void setBoardName(Context ctx, String board_name) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEY_BOARD_NAME, board_name).commit();
    }

    public static void setBoardTime(Context ctx, int board_time) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_BOARD_TIME, board_time).commit();
    }

    public static void setRatingStar(Context ctx, int ratingStar) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_RATING_STAR, ratingStar).commit();
    }

    public static void saveList(Context ctx, ArrayList<WordsPOJO> list) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_LIST, json);
        editor.commit();
    }

    public static void setTimeOutRating(Context ctx, int ratingStar) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_TIMEOUT_RATING, ratingStar).commit();
    }

    public static String getName(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(KEY_NAME, "");
    }

    public static String getEmail(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(KEY_EMAIL, "");
    }

    public static String getDeviceId(Context ctx) {
        SharedPreferences sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(KEY_DEVICE_ID, "");
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
