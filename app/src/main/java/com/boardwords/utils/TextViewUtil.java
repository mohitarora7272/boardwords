package com.boardwords.utils;


import android.view.MenuItem;
import android.widget.TextView;

@SuppressWarnings("ALL")
public class TextViewUtil {
    public static void appendText(TextView tv, String text) {
        tv.setText(tv.getText() + text);
    }

    public static void appendMenuItemText(TextView tv, MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        tv.setText(tv.getText() + "_n" + title);
    }

    public static void emptyText(TextView tv) {
        tv.setText("");
    }

    public static void setText(TextView tv, String text) {
        tv.setText(text);
    }

    public static void removeText(TextView tv) {
        tv.setText(method(tv.getText().toString()));
    }

    private static String method(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}