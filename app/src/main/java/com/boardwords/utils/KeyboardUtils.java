package com.boardwords.utils;

import com.boardwords.R;
import com.boardwords.modal.ItemObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyboardUtils {

    public KeyboardUtils() {
    }

    // Get All Item List Alphabets
    public List<ItemObject> getListAlphabets() {
        List<ItemObject> allItems = new ArrayList<>();
        allItems.add(new ItemObject("A", R.color.md_red_500));
        allItems.add(new ItemObject("B", R.color.md_pink_500));
        allItems.add(new ItemObject("C", R.color.md_purple_500));
        allItems.add(new ItemObject("D", R.color.md_deep_purple_500));
        allItems.add(new ItemObject("E", R.color.md_indigo_500));
        allItems.add(new ItemObject("F", R.color.md_blue_500));
        allItems.add(new ItemObject("G", R.color.md_light_blue_500));
        allItems.add(new ItemObject("H", R.color.md_cyan_500));
        allItems.add(new ItemObject("I", R.color.md_teal_500));
        allItems.add(new ItemObject("J", R.color.md_green_500));
        allItems.add(new ItemObject("K", R.color.md_light_green_500));
        allItems.add(new ItemObject("L", R.color.md_lime_500));
        allItems.add(new ItemObject("M", R.color.md_yellow_500));
        allItems.add(new ItemObject("N", R.color.md_amber_500));
        allItems.add(new ItemObject("O", R.color.md_orange_500));
        allItems.add(new ItemObject("P", R.color.md_deep_orange_500));
        allItems.add(new ItemObject("Q", R.color.md_brown_500));
        allItems.add(new ItemObject("R", R.color.md_grey_500));
        allItems.add(new ItemObject("S", R.color.md_blue_grey_500));
        allItems.add(new ItemObject("T", R.color.md_red_500));
        allItems.add(new ItemObject("U", R.color.md_pink_500));
        allItems.add(new ItemObject("V", R.color.md_purple_500));
        allItems.add(new ItemObject("W", R.color.md_deep_purple_500));
        allItems.add(new ItemObject("X", R.color.md_indigo_500));
        allItems.add(new ItemObject("Y", R.color.md_blue_500));
        allItems.add(new ItemObject("Z", R.color.md_light_blue_500));
        return allItems;
    }

    // Get All Item List Qwerty
    public List<ItemObject> getListQwerty() {
        List<ItemObject> allItems = new ArrayList<>();
        allItems.add(new ItemObject("Q", R.color.md_red_500));
        allItems.add(new ItemObject("W", R.color.md_pink_500));
        allItems.add(new ItemObject("E", R.color.md_purple_500));
        allItems.add(new ItemObject("R", R.color.md_deep_purple_500));
        allItems.add(new ItemObject("T", R.color.md_indigo_500));
        allItems.add(new ItemObject("Y", R.color.md_blue_500));
        allItems.add(new ItemObject("U", R.color.md_light_blue_500));
        allItems.add(new ItemObject("I", R.color.md_cyan_500));
        allItems.add(new ItemObject("O", R.color.md_teal_500));
        allItems.add(new ItemObject("P", R.color.md_green_500));
        allItems.add(new ItemObject("A", R.color.md_light_green_500));
        allItems.add(new ItemObject("S", R.color.md_lime_500));
        allItems.add(new ItemObject("D", R.color.md_yellow_500));
        allItems.add(new ItemObject("F", R.color.md_amber_500));
        allItems.add(new ItemObject("G", R.color.md_orange_500));
        allItems.add(new ItemObject("H", R.color.md_deep_orange_500));
        allItems.add(new ItemObject("J", R.color.md_brown_500));
        allItems.add(new ItemObject("K", R.color.md_grey_500));
        allItems.add(new ItemObject("L", R.color.md_blue_grey_500));
        allItems.add(new ItemObject("Z", R.color.md_red_500));
        allItems.add(new ItemObject("X", R.color.md_pink_500));
        allItems.add(new ItemObject("C", R.color.md_purple_500));
        allItems.add(new ItemObject("V", R.color.md_deep_purple_500));
        allItems.add(new ItemObject("B", R.color.md_indigo_500));
        allItems.add(new ItemObject("N", R.color.md_blue_500));
        allItems.add(new ItemObject("M", R.color.md_light_blue_500));
        return allItems;
    }

    // Get All Item List Shuffle
    public List<ItemObject> getListShuffle() {
        List<ItemObject> wordsList = getListAlphabets();
        Collections.shuffle(wordsList);
        return wordsList;
    }
}