package com.boardwords.modal;


import android.view.View;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class WordsPOJO {

    private String boardName;
    private ArrayList<String> wordsList;
    private View.OnClickListener requestBtnClickListener;
    private float averageRating;

    public WordsPOJO() {}

    public ArrayList<String> getWordsList() {
        return wordsList;
    }

    public void setWordsList(ArrayList<String> wordsList) {
        this.wordsList = wordsList;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }
}
