package com.boardwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.boardwords.adapters.CharacterViewAdapter;
import com.boardwords.adapters.FoldingCellListAdapter;
import com.boardwords.adapters.RecyclerViewAdapter;
import com.boardwords.adapters.TimeAdapter;
import com.boardwords.interfaces.Constant;
import com.boardwords.modal.ItemObject;
import com.boardwords.modal.TimeOption;
import com.boardwords.modal.WordsPOJO;
import com.boardwords.permissions.RuntimePermission;
import com.boardwords.preference.Preference;
import com.boardwords.utils.AnimationUtil;
import com.boardwords.utils.KeyboardUtils;
import com.boardwords.utils.MediaPlayerUtil;
import com.boardwords.utils.TextViewUtil;
import com.boardwords.utils.VibrationUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;
import com.varunest.sparkbutton.SparkButton;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements Constant,
        RecyclerViewAdapter.ItemClickListener, CharacterViewAdapter.ItemClickListener {

    @BindView(R.id.tvCharacter)
    TextView tvCharacter;

    @BindView(R.id.recycler_view)
    RecyclerView rView;

    @BindView(R.id.recycler_view_horizontal)
    RecyclerView recycler_view_horizontal;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.mainListView)
    ListView theListView;

    @BindView(R.id.relative_list)
    RelativeLayout relative_list;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.adView2)
    AdView mAdView2;

    @BindView(R.id.img_backspace)
    ImageButton img_backspace;

    @BindView(R.id.btnStar)
    SparkButton btnStar;

    @BindView(R.id.btnThumb)
    SparkButton btnThumb;

    @BindView(R.id.btnTrophy)
    SparkButton btnTrophy;

    @BindView(R.id.btnStart)
    SparkButton btnStart;

    @BindView(R.id.btnRecords)
    SparkButton btnRecords;

    @BindView(R.id.btnNewGame)
    SparkButton btnNewGame;

    @BindView(R.id.btnTimeOut)
    SparkButton btnTimeOut;

    @BindView(R.id.relateButtons)
    RelativeLayout relateButtons;

    @SuppressLint("StaticFieldLeak")
    private static MainActivity mActivity;

    private RecyclerViewAdapter rcAdapter;

    private CharacterViewAdapter chAdapter;

    private CountDownTimer countDownTimer;

    private Menu menu;

    private MenuItem menuTime;

    private ActionBar ab;

    private List<String> characterList;

    private List<String> allCharacterList;

    private MediaPlayerUtil mpUtil;

    public static MainActivity getInstance() {
        return mActivity;
    }

    private ArrayList<WordsPOJO> c_List;

    private InterstitialAd mInterstitialAd;

    private KeyboardUtils keyboardUtils;

    private AnimationUtil animationUtil;

    private VibrationUtil vibrationUtil;

    private Boolean isFirstTime = true;

    private String text = "";

    private FoldingCellListAdapter adapter;

    private RuntimePermission runtimePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First Getting Array List From Assets Folder
        allCharacterList = getAllCharacterList();

        setContentView(R.layout.activity_main);

        // Retrieve the useful instance variables
        mActivity = MainActivity.this;

        ButterKnife.bind(this);

        // Set Action Bar Title
        ab = getSupportActionBar();
        if (Preference.getBoardName(this) != null &&
                !Preference.getBoardName(this).equals("")) {

            setActionBarTitle(Preference.getBoardName(this));

        } else {
            setActionBarTitle(getString(R.string.app_name));
        }

        // Initialize Animation and Keyboard, Vibration And Permissions Utilities
        animationUtil = new AnimationUtil(this);
        keyboardUtils = new KeyboardUtils();
        vibrationUtil = new VibrationUtil(this);
        runtimePermission = new RuntimePermission(this);

        // Start First List
        List<ItemObject> wordsList = keyboardUtils.getListQwerty();
        GridLayoutManager lLayout = new GridLayoutManager(MainActivity.this, 6);

        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        setAdapterForWords(wordsList);

        // Start Second List
        characterList = new ArrayList<>();
        GridLayoutManager lLayout2 = new GridLayoutManager(MainActivity.this, 4);

        recycler_view_horizontal.setHasFixedSize(true);
        recycler_view_horizontal.setLayoutManager(lLayout2);

        setAdapterForCharacters(characterList);

        setRating();

        mpUtil = new MediaPlayerUtil(this);

        c_List = new ArrayList<>();

        if (Preference.getList(this) != null && Preference.getList(this).size() > 0) {
            c_List = Preference.getList(this);
        }

        // Show Banner Ad Here...
        showBannerAd();
        initInterstitialAd();
    }

    @OnClick(R.id.img_cancel)
    public void cancel() {
        if (menu != null) {
            showKeyboardMenu(menu);
            showTimeMenu(menu);
            hideShareMenu(menu);
        }

        relative_list.setVisibility(View.GONE);

        if (Preference.getBoardName(this) != null &&
                !Preference.getBoardName(this).equals("")) {
            setActionBarTitle(Preference.getBoardName(this));
        } else {
            setActionBarTitle(getString(R.string.app_name));
        }

        if (Preference.getRatingStar(this) >= 5) {
            showMessageCongratulationRatingDone();
            return;
        }

        if (getMenuTitle().equals(getString(R.string.time) + " " + getString(R.string.time00))) {
            showTimerMessage("");
        } else if (getMenuTitle().equals(getString(R.string.time_out))) {
            showTimerMessage("");
        } else {
            hideButtonAnimations();
            startTimer();
        }
    }

    @OnClick(R.id.img_backspace)
    public void backSpace() {
        if (tvCharacter.getText() != null && tvCharacter.getText().toString().length() > 0) {
            TextViewUtil.removeText(tvCharacter);
            checkMatchCharacterInDataBase();
        } else {
            img_backspace.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnStart)
    public void btnStart() {
        isFirstTime = false;
        mpUtil.playSoundButton(this);
        animationUtil.slideOutDown(btnStart);
        relateButtons.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @OnClick(R.id.btnStar)
    public void btnStar() {
        mpUtil.playSoundButton(this);
        animationUtil.slideOutDown(btnStar);
        relateButtons.setVisibility(View.GONE);
        btnStar.setVisibility(View.GONE);
        setWholeBoardList(Preference.getBoardName(this), (ArrayList<String>) characterList);

        if (Preference.getRatingStar(MainActivity.this) == -1) {
            ratingBar.setRating(1);
            Preference.setRatingStar(MainActivity.this, 1);
        } else {
            int rateStar = Preference.getRatingStar(this);
            rateStar++;
            Preference.setRatingStar(MainActivity.this, rateStar);
            ratingBar.setRating(rateStar);
        }

        if (Preference.getRatingStar(MainActivity.this) >= 5) {
            showMessageCongratulationRatingDone();
            return;
        }

        Preference.setTimeOutRating(MainActivity.this, 0);
        Preference.setBoardName(MainActivity.this, "");
        Preference.setBoardTime(MainActivity.this, -1);
        setMenuItemTitle(getString(R.string.time) + " " + getString(R.string.time00));
        tvCharacter.setText("");
        img_backspace.setVisibility(View.GONE);
        tvCharacter.setBackgroundResource(0);
        tvCharacter.setTextColor(Color.parseColor("#000000"));
        characterList.clear();
        setAdapterForCharacters(characterList);
        invalidateOptionsMenu();
    }

    @OnClick(R.id.btnThumb)
    public void btnThumb() {
        mpUtil.playSoundButton(this);
        animationUtil.slideOutDown(btnThumb);
        relateButtons.setVisibility(View.GONE);
        btnThumb.setVisibility(View.GONE);

        setMenuItemTitle(getString(R.string.time) + " " + getString(R.string.time00));
        tvCharacter.setText("");
        img_backspace.setVisibility(View.GONE);
        tvCharacter.setBackgroundResource(0);
        tvCharacter.setTextColor(Color.parseColor("#000000"));
        invalidateOptionsMenu();
    }

    @OnClick(R.id.btnTrophy)
    public void btnTrophy() {
        mpUtil.playSoundButton(this);
        btnTrophy.playAnimation();
        relateButtons.setVisibility(View.VISIBLE);
        btnTrophy.setVisibility(View.VISIBLE);
        btnRecords.setVisibility(View.VISIBLE);
        btnNewGame.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnRecords)
    public void btnRecords() {
        mpUtil.playSoundButton(this);
        animationUtil.slideInUp(btnTrophy);
        animationUtil.slideOutLeft(btnNewGame);
        animationUtil.slideOutRight(btnRecords);

        relateButtons.setVisibility(View.GONE);
        btnRecords.setVisibility(View.GONE);
        btnTrophy.setVisibility(View.GONE);
        btnNewGame.setVisibility(View.GONE);
        if (menu != null) {
            hideKeyboardMenu(menu);
            hideTimeMenu(menu);
            showShareMenu(menu);
        }
        hideButtonAnimations();
        showRecords();
    }

    @OnClick(R.id.btnNewGame)
    public void btnNewGame() {
        mpUtil.playSoundButton(this);
        animationUtil.slideOutDown(btnTrophy);
        animationUtil.slideOutLeft(btnNewGame);
        animationUtil.slideOutRight(btnRecords);

        relateButtons.setVisibility(View.GONE);
        btnRecords.setVisibility(View.GONE);
        btnTrophy.setVisibility(View.GONE);
        btnNewGame.setVisibility(View.GONE);

        c_List.clear();
        Preference.saveList(MainActivity.this, c_List);
        stopTimer();
        Preference.setTimeOutRating(MainActivity.this, 0);
        Preference.setRatingStar(MainActivity.this, -1);
        Preference.setBoardName(MainActivity.this, "");
        Preference.setBoardTime(MainActivity.this, -1);
        setMenuItemTitle(getString(R.string.time) + " " + getString(R.string.time00));
        setActionBarTitle(getString(R.string.app_name));
        ratingBar.setRating(0);
        tvCharacter.setText("");
        img_backspace.setVisibility(View.GONE);
        tvCharacter.setBackgroundResource(0);
        tvCharacter.setTextColor(Color.parseColor("#000000"));
        characterList.clear();
        setAdapterForCharacters(characterList);
        isFirstTime = true;
        invalidateOptionsMenu();
    }

    @OnClick(R.id.btnTimeOut)
    public void btnTimeOut() {
        mpUtil.playSoundButton(this);
        animationUtil.slideOutDown(btnTimeOut);
        relateButtons.setVisibility(View.GONE);
        btnTimeOut.setVisibility(View.GONE);
        setTimeOutRating(text);
        invalidateOptionsMenu();
    }

    // Set Rating
    private void setRating() {
        if (Preference.getRatingStar(MainActivity.this) == -1) {
            ratingBar.setRating(0);
        } else {
            ratingBar.setRating(Preference.getRatingStar(MainActivity.this));
        }
    }

    // Get All Character List Form DataBase
    private List<String> getAllCharacterList() {
        List<String> allItems = new ArrayList<>();
        try {
            String json = loadJSONFromAsset(MainActivity.this);
            allItems = (ArrayList<String>) fromJson(json, new TypeToken<ArrayList<String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allItems;
    }

    // Load JSON From Asset
    public static String loadJSONFromAsset(final Context context) throws Exception {
        String json;
        InputStream is = context.getAssets().open("generated.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        return json;
    }

    // From Json
    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    // Set Adapter For Characters
    private void setAdapterForCharacters(List<String> characterList) {
        chAdapter = new CharacterViewAdapter(this, characterList);
        chAdapter.setClickListener(this);
        recycler_view_horizontal.setAdapter(chAdapter);
    }

    // Set Adapter For Words
    private void setAdapterForWords(List<ItemObject> wordsList) {
        rcAdapter = new RecyclerViewAdapter(this, wordsList);
        rcAdapter.setClickListener(this);
        rView.setAdapter(rcAdapter);
    }

    // Set ActionBar Title
    private void setActionBarTitle(String title) {
        ab.setTitle(title);
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.drawable.bw_icon);
    }

    // onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        showHideMenuList(menu);
        hideShareMenu(menu);
        menuTime = menu.findItem(R.id.time);
        menuTime.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        setMenuItemTitle(getString(R.string.time) + " " + getString(R.string.time00));

        if (Preference.getRatingStar(this) >= 5) {
            showMessageCongratulationRatingDone();
            return true;
        }

        if (isFirstTime) {
            relateButtons.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            animationUtil.slideInUp(btnStart);
            return true;
        }

        if (Preference.getBoardName(this).equals("") && Preference.getBoardTime(this) == -1) {
            showDialogForBoardName();
            return true;
        }

        if (Preference.getBoardTime(this) != -1) {
            updateTime(menu, Preference.getBoardTime(this));
            return super.onPrepareOptionsMenu(menu);
        }

        if (Preference.getBoardTime(this) == -1) {
            showDialogForTime();
            return true;
        }
        return true;
    }

    // Show Hide Menu List Item
    private void showHideMenuList(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.board_list);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (c_List != null && c_List.size() > 0) {
            menuList.setVisible(true);
        } else {
            menuList.setVisible(false);
        }
    }

    // Show Keyboard Menu List Item
    private void showKeyboardMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.keyboard);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(true);
    }

    // Hide Keyboard Menu List Item
    private void hideKeyboardMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.keyboard);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(false);
    }

    // Show Keyboard Menu List Item
    private void showTimeMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.time);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(true);
    }

    // Hide Keyboard Menu List Item
    private void hideTimeMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.time);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(false);
    }

    // Show Keyboard Menu List Item
    private void showSettingMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.settings);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(true);
    }

    // Show Keyboard Menu List Item
    private void showShareMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.share);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(true);
    }

    // Show Keyboard Menu List Item
    private void hideShareMenu(Menu menu) {
        MenuItem menuList = menu.findItem(R.id.share);
        menuList.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuList.setVisible(false);
    }

    // onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time:
                if (Preference.getBoardName(this) != null &&
                        !Preference.getBoardName(this).equals("")) {
                    setActionBarTitle(Preference.getBoardName(this));
                } else {
                    setActionBarTitle(getString(R.string.app_name));
                }

                relative_list.setVisibility(View.GONE);

                if (Preference.getRatingStar(this) >= 5) {
                    showMessageCongratulationRatingDone();
                    return true;
                }

                if (isFirstTime) {
                    relateButtons.setVisibility(View.VISIBLE);
                    btnStart.setVisibility(View.VISIBLE);
                    animationUtil.slideInUp(btnStart);
                    return true;
                }

                if (getMenuTitle().equals(getString(R.string.time) + " " + getString(R.string.time00))) {
                    showTimerMessage("");
                } else if (getMenuTitle().equals(getString(R.string.time_out))) {
                    showTimerMessage("");
                } else {
                    hideButtonAnimations();
                    stopTimer();
                    showDialogForTime();
                }
                return true;

            case R.id.board_list:
                if (menu != null) {
                    hideKeyboardMenu(menu);
                    hideTimeMenu(menu);
                    showShareMenu(menu);
                }
                hideButtonAnimations();
                showRecords();
                return true;

            case R.id.keyboard:
                return true;

            case R.id.aToZ:
                setAdapterForWords(keyboardUtils.getListAlphabets());
                return true;

            case R.id.qwerty:
                setAdapterForWords(keyboardUtils.getListQwerty());
                return true;

            case R.id.shuffle:
                setAdapterForWords(keyboardUtils.getListShuffle());
                return true;

            case R.id.settings:
                stopTimer();
                showAlertSettings(this);
                return true;

            case R.id.share:
                takeScreenshot();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Hide Button Animations
    private void hideButtonAnimations() {
        tvCharacter.setText("");
        img_backspace.setVisibility(View.GONE);
        tvCharacter.setBackgroundResource(0);
        tvCharacter.setTextColor(Color.parseColor("#000000"));
        relateButtons.setVisibility(View.GONE);
        btnStar.setVisibility(View.GONE);
        btnThumb.setVisibility(View.GONE);
        btnTrophy.setVisibility(View.GONE);
        btnRecords.setVisibility(View.GONE);
        btnNewGame.setVisibility(View.GONE);
        btnStart.setVisibility(View.GONE);
        btnTimeOut.setVisibility(View.GONE);
    }

    // Show Records
    private void showRecords() {
        if (c_List != null && c_List.size() > 0) {
            stopTimer();
            setActionBarTitle(getString(R.string.board_words_list));

            if (Preference.getList(this) != null && Preference.getList(this).size() > 0) {
                c_List = Preference.getList(this);
            }

            setAdapterShowListComplete(c_List);
            relative_list.setVisibility(View.VISIBLE);
        }
    }

    // onPrepareOptionsMenu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        if (menu != null) {
            showKeyboardMenu(menu);
            showTimeMenu(menu);
            showSettingMenu(menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // Set Menu Item Title
    private void setMenuItemTitle(String ch) {
        SpannableString s = new SpannableString(ch);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        menuTime.setTitle(s);
    }

    // Get Menu Title
    public String getMenuTitle() {
        return menuTime.getTitle().toString();
    }

    // Update Time
    private void updateTime(Menu menu, int timeSelected) {
        final MenuItem menuTime = menu.findItem(R.id.time);
        menuTime.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        startTimer(timeSelected);
    }

    // Start Timer With Interval
    private void startTimer(int timeSelected) {
        // Follow Link:- https://developer.android.com/reference/android/os/CountDownTimer.html
        countDownTimer = new CountDownTimer(timeSelected, TIME_INTERVAL_1_SEC) {

            public void onTick(long millisUntilFinished) {
                setMenuItemTitle(getString(R.string.time) + " " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                setMenuItemTitle(getString(R.string.time_out));
                tvCharacter.setText("");
                showTimerMessage(getString(R.string.time_out));
                img_backspace.setVisibility(View.GONE);
            }
        }.start();
    }

    // Stop Timer
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    // Start Timer WithOut Interval
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    // Load Time Options
    private List<TimeOption> loadTimeOptions() {
        List<TimeOption> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.time_array);
        for (String op : raw) {
            result.add(new TimeOption(op));
        }
        return result;
    }

    // On Item Click On Characters
    @Override
    public void onItemClick(View view, int position) {
        vibrationUtil.doVibration();
        mpUtil.playSoundButton(this);
        if (Preference.getRatingStar(this) >= 5) {
            showMessageCongratulationRatingDone();
            return;
        }
        img_backspace.setVisibility(View.VISIBLE);
        TextViewUtil.appendText(tvCharacter, rcAdapter.getItem(position));
        new CheckCharacter().execute();
    }

    // Check Character Async Task
    private class CheckCharacter extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkMatchCharacterInDataBase();
                }
            });

            return null;
        }
    }

    // Check Match Characters In DataBase
    private void checkMatchCharacterInDataBase() {
        for (String text : allCharacterList) {
            if (text.toLowerCase().equals(tvCharacter.getText().toString().toLowerCase())) {

                stopTimer();
                //tvCharacter.setBackgroundResource(R.drawable.token_background);
                //tvCharacter.setTextColor(Color.parseColor("#FFFFFF"));
                setSpanText(tvCharacter.getText().toString());

                if (characterList != null && characterList.size() < 10) {
                    characterList.add(text.toUpperCase());
                    setAdapterForCharacters(characterList);
                }

                if (characterList != null && characterList.size() == 10) {
                    showMessageCongratulationNextBoard();
                    return;
                }

                showMessageCongratulation();
                break;

            } else {
                tvCharacter.setBackgroundResource(0);
                tvCharacter.setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    // Show Timer Message
    public void showTimerMessage(final String text) {
        this.text = text;
        mpUtil.playSoundButton(this);
        relateButtons.setVisibility(View.VISIBLE);
        btnTimeOut.setVisibility(View.VISIBLE);
        animationUtil.slideInUp(btnTimeOut);
        vibrationUtil.doVibration();
    }

    // Set TimeOut Rating
    private void setTimeOutRating(String text) {
        if (text.equals(getString(R.string.time_out))) {
            if (Preference.getTimeOutRating(MainActivity.this) == 0) {
                Preference.setTimeOutRating(MainActivity.this, 1);

            } else {
                int rating = Preference.getTimeOutRating(MainActivity.this);
                rating++;
                Preference.setTimeOutRating(MainActivity.this, rating);
            }
        }
    }

    // Show Dialog For Board Name
    private void showDialogForBoardName() {
        mpUtil.playSoundButton(this);
        new LovelyTextInputDialog(this, R.style.TintTheme)
                .setCancelable(false)
                .setTopTitle(getString(R.string.setBoardName))
                .setTopTitleColor(R.color.md_white_1000)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_board_24dp)
                .setConfirmButtonColor(getResources().getColor(R.color.colorAccent))
                .setInputFilter(R.string.setBoardName, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {

                        // Set Action Bar Title With Board Name
                        setActionBarTitle(text);

                        // Set Board Name in Preference
                        Preference.setBoardName(MainActivity.this, text);

                        // Show Dialog For Time
                        showDialogForTime();
                    }
                })
                .show();
    }

    // Show Dialog For Time
    private void showDialogForTime() {
        mpUtil.playSoundButton(this);
        // Follow Link:- https://android-arsenal.com/details/1/3452
        ArrayAdapter<TimeOption> adapter = new TimeAdapter(this, loadTimeOptions());
        new LovelyChoiceDialog(this, R.style.TintTheme)
                .setCancelable(false)
                .setTopTitle(getString(R.string.setTime))
                .setTopTitleColor(R.color.md_white_1000)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_access_time_white_24dp)
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<TimeOption>() {
                    @Override
                    public void onItemSelected(int position, TimeOption item) {

                        if (item.time.equals(getString(R.string.Sec10))) {
                            updateTime(menu, TIME_INTERVAL_10_SEC);

                            // Set Board Time in Preference
                            Preference.setBoardTime(MainActivity.this, TIME_INTERVAL_10_SEC);
                        }

                        if (item.time.equals(getString(R.string.Sec15))) {
                            updateTime(menu, TIME_INTERVAL_15_SEC);

                            // Set Board Time in Preference
                            Preference.setBoardTime(MainActivity.this, TIME_INTERVAL_15_SEC);
                        }

                        if (item.time.equals(getString(R.string.Sec20))) {
                            updateTime(menu, TIME_INTERVAL_20_SEC);

                            // Set Board Time in Preference
                            Preference.setBoardTime(MainActivity.this, TIME_INTERVAL_20_SEC);
                        }
                    }
                })
                .show();
    }

    // Show Congratulation Message
    private void showMessageCongratulation() {
        vibrationUtil.doVibration();
        mpUtil.playSoundButton(this);
        relateButtons.setVisibility(View.VISIBLE);
        btnThumb.setVisibility(View.VISIBLE);
        animationUtil.slideInUp(btnThumb);
    }

    // Show Congratulation Message For Make Next Board
    private void showMessageCongratulationNextBoard() {
        showInterstitial();
        vibrationUtil.doVibrationPatternLong();
        mpUtil.playSoundButton(this);
        relateButtons.setVisibility(View.VISIBLE);
        btnStar.setVisibility(View.VISIBLE);
        animationUtil.slideInUp(btnStar);
    }

    // Show Congratulation Message When 5 Star Rating is Done
    private void showMessageCongratulationRatingDone() {
        showInterstitial();
        vibrationUtil.doVibrationPatternLong();
        mpUtil.playSoundButton(this);
        relateButtons.setVisibility(View.VISIBLE);
        btnTrophy.setVisibility(View.VISIBLE);
        btnRecords.setVisibility(View.VISIBLE);
        btnNewGame.setVisibility(View.VISIBLE);
        animationUtil.slideInUp(btnTrophy);
        animationUtil.slideInRight(btnRecords);
        animationUtil.slideInLeft(btnNewGame);
    }

    // Show Dialog For Exit
    private void showDialogForExit() {
        new LovelyStandardDialog(this, R.style.TintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.ic_exit_to_app_white_24dp)
                .setMessage(R.string.exit_message)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startTimer();
                    }
                })
                .show();
    }

    // On Item Click On Words
    @Override
    public void onItemClicks(View view, int position) {
    }

    // Set Span Text Rainbow Animation On Text
    private void setSpanText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                SpannableString spannableString = new SpannableString(text);

                Pattern pattern = Pattern.compile(text.toLowerCase());
                Matcher matcher = pattern.matcher(text.toLowerCase());
                while (matcher.find()) {
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), 0);
                    spannableString.setSpan(new RainbowSpan(MainActivity.this), matcher.start(), matcher.end(), 0);
                }

                tvCharacter.setText(spannableString);

            }
        });
    }

    // Rainbow Span
    private static class RainbowSpan extends CharacterStyle implements UpdateAppearance {
        private final int[] colors;

        RainbowSpan(Context context) {
            colors = context.getResources().getIntArray(R.array.color_array);
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.setStyle(Paint.Style.FILL);
            Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors, null,
                    Shader.TileMode.MIRROR);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            shader.setLocalMatrix(matrix);
            paint.setShader(shader);
        }
    }

    // Set Whole Board List
    private void setWholeBoardList(String boardName, ArrayList<String> list) {
        WordsPOJO wordsPOJO = new WordsPOJO();
        wordsPOJO.setBoardName(boardName);
        wordsPOJO.setWordsList(list);
        wordsPOJO.setAverageRating(getAverageRating(list));
        c_List.add(wordsPOJO);
        Preference.saveList(this, c_List);
    }

    // Get Average Rating
    private float getAverageRating(ArrayList<String> list) {
        float avgTotal = Preference.getTimeOutRating(this) + list.size();
        float avgRatingF = Preference.getTimeOutRating(this) / avgTotal;
        float avgRatingFF = avgRatingF * 100;

        if (avgRatingFF >= 100) {

            avgRatingFF = 0.0F;

        } else if (avgRatingFF < 100 && avgRatingFF >= 80) {

            avgRatingFF = 1.0F;

        } else if (avgRatingFF < 80 && avgRatingFF >= 60) {

            avgRatingFF = 2.0F;

        } else if (avgRatingFF < 60 && avgRatingFF >= 40) {

            avgRatingFF = 3.0F;

        } else if (avgRatingFF < 40 && avgRatingFF >= 20) {

            avgRatingFF = 4.0F;

        } else if (avgRatingFF < 20 && avgRatingFF >= 00) {

            avgRatingFF = 5.0F;

        } else {

            avgRatingFF = 0.0F;
        }
        return avgRatingFF;
    }

    // Set Adapter For Showing List Complete Data
    // Link Follow:- https://android-arsenal.com/details/1/3426
    private void setAdapterShowListComplete(final ArrayList<WordsPOJO> c_List) {

        // add custom btn handler to first list item
        c_List.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
            }
        });

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, c_List);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });
    }

    // Take Screenshot...
    private void takeScreenshot() {
        if (!runtimePermission.checkPermissionForWriteExternalStorage()) {
            runtimePermission.requestPermissionForExternalStorage();
            return;
        }

        try {
            if (!relative_list.isShown()) {
                return;
            }
            View v1 = relative_list;
            v1.setDrawingCacheEnabled(true);
            Bitmap resultBitmap = Bitmap.createBitmap(v1.getDrawingCache(true));
            try {
                resultBitmap = Bitmap.createScaledBitmap(resultBitmap, v1.getMeasuredWidth(), v1.getMeasuredHeight(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            v1.setDrawingCacheEnabled(false);

            final Bitmap finalResultBitmap = resultBitmap;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    shareRecord(MainActivity.this, finalResultBitmap, getString(R.string.app_name) + System.currentTimeMillis());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Share Bitmap Image
    public static void shareRecord(Context ctx, Bitmap bitmap, String fileName) {
        try {
            String pathofBmp = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bitmap, fileName, null);
            Uri bmpUri = Uri.parse(pathofBmp);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            intent.setType("image/png");
            ctx.startActivity(Intent.createChooser(intent, "Share Using..."));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "Exception>>>" + e.getMessage());
        }
    }

    // Request Call Back Method To check permission is granted by user or not for MarshMallow
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeScreenshot();
                } else {
                    Toast.makeText(this, "Sorry you can't share record, Please enable permission first", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Show Alert Dialog For Settings
    public void showAlertSettings(final Context context) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setCancelable(false);
        View v = LayoutInflater.from(context).inflate(R.layout.setting_action, null);
        ab.setView(v);
        final Switch toggleSound = (Switch) v.findViewById(R.id.toggleSound);
        final Switch toggleVibration = (Switch) v.findViewById(R.id.toggleVibration);
        if (Preference.getSound(context)) {
            toggleSound.setChecked(true);
        } else {
            toggleSound.setChecked(false);
        }

        if (Preference.getVibration(context)) {
            toggleVibration.setChecked(true);
        } else {
            toggleVibration.setChecked(false);
        }

        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean sButton = toggleSound.isChecked();
                boolean vButton = toggleVibration.isChecked();
                Preference.setSound(context, sButton);
                Preference.setVibration(context, vButton);
                startTimer();
                dialog.dismiss();
            }
        });
        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTimer();
                dialog.dismiss();
            }
        });
        ab.create().show();
    }

    // Show Banner Ads
    private void showBannerAd() {
        // For Testing Purpose
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("9E847D99F08C0028B6613E597754B38A")
//                .build();
        mAdView.loadAd(new AdRequest.Builder().build());
        mAdView2.loadAd(new AdRequest.Builder().build());
        mAdView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("Banner", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("Banner", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Banner", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Banner", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("Banner", "onAdOpened");
            }
        });
    }

    // Initialize InterstitialAd
    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        loadInterstitialAds();
    }

    // Show Interstitial Ads
    private void showInterstitial() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Show Ads
                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    loadInterstitialAds();
                }
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("Interstitial", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("Interstitial", "onAdClosed");
                // Load the next interstitial.
                loadInterstitialAds();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Interstitial", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Interstitial", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("Interstitial", "onAdOpened");
            }
        });
    }

    // Load Interstitial Ads
    private void loadInterstitialAds() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            // For Testing Purpose
//            AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                    // Check the LogCat to get your test device ID
//                    .addTestDevice("9E847D99F08C0028B6613E597754B38A")
//                    .build();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    // onBackPressed
    @Override
    public void onBackPressed() {
        stopTimer();
        showDialogForExit();
    }

    // onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preference.setBoardTime(this, -1);
        stopTimer();

        if (mAdView != null) {
            mAdView.destroy();
        }
        if (mInterstitialAd != null) {
            mInterstitialAd = null;
        }
    }
}