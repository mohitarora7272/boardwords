package com.boardwords;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boardwords.adapters.CharacterViewAdapter;
import com.boardwords.adapters.FoldingCellListAdapter;
import com.boardwords.adapters.RecyclerViewAdapter;
import com.boardwords.adapters.TimeAdapter;
import com.boardwords.interfaces.Constant;
import com.boardwords.interfaces.RestClient;
import com.boardwords.modal.ItemObject;
import com.boardwords.modal.TimeOption;
import com.boardwords.modal.WordsApi;
import com.boardwords.modal.WordsPOJO;
import com.boardwords.preference.Preference;
import com.boardwords.utils.TextViewUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Constant,
        RecyclerViewAdapter.ItemClickListener, CharacterViewAdapter.ItemClickListener,
        MediaPlayer.OnCompletionListener {

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

    @BindView(R.id.img_backspace)
    ImageButton img_backspace;

    @SuppressLint("StaticFieldLeak")
    private static MainActivity mActivity;

    private RecyclerViewAdapter rcAdapter;

    private CharacterViewAdapter chAdapter;

    private CountDownTimer countDownTimer;

    private Menu menu;

    private MenuItem menuTime;

    private ActionBar ab;

    private List<String> characterList;

    private List<String> allcharacterList;

    private MediaPlayer mp;

    public static MainActivity getInstance() {
        return mActivity;
    }

    private ArrayList<WordsPOJO> c_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First Getting Array List From Assets Folder
        allcharacterList = getAllCharacterList();

        setContentView(R.layout.activity_main);

        /* Retrieve the useful instance variables */
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

        // Start First List
        List<ItemObject> wordsList = getAllItemList();
        //Collections.shuffle(wordsList);
        GridLayoutManager lLayout = new GridLayoutManager(MainActivity.this, 6);

        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        rcAdapter = new RecyclerViewAdapter(MainActivity.this, wordsList);
        rcAdapter.setClickListener(this);
        rView.setAdapter(rcAdapter);

        // Start Second List
        characterList = new ArrayList<>();
        GridLayoutManager lLayout2 = new GridLayoutManager(MainActivity.this, 4);

        recycler_view_horizontal.setHasFixedSize(true);
        recycler_view_horizontal.setLayoutManager(lLayout2);

        setAdapterForCharacters(characterList);

        setRating();

        mp = MediaPlayer.create(this, R.raw.button_sound);
        mp.setOnCompletionListener(this);

        c_List = new ArrayList<>();

        if (Preference.getList(this) != null && Preference.getList(this).size() > 0) {
            c_List = Preference.getList(this);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    @OnClick(R.id.img_cancel)
    public void cancel() {
        playSoundButton();
        if (getMenuTitle().equals(getString(R.string.time) + " " + getString(R.string.time00))) {

            showTimerMessage("");

        } else if (getMenuTitle().equals(getString(R.string.time_out))) {

            showTimerMessage("");

        } else {

            startTimer();

        }

        if (Preference.getBoardName(this) != null &&
                !Preference.getBoardName(this).equals("")) {

            setActionBarTitle(Preference.getBoardName(this));

        } else {

            setActionBarTitle(getString(R.string.app_name));

        }

        relative_list.setVisibility(View.GONE);
    }

    @OnClick(R.id.img_backspace)
    public void backSpace() {
        playSoundButton();
        if (tvCharacter.getText() != null && tvCharacter.getText().toString().length() > 0) {
            TextViewUtil.removeText(tvCharacter);
            checkMatchCharacterInDataBase();
        } else {
            img_backspace.setVisibility(View.GONE);
        }
    }

    // Play Sound Button
    private void playSoundButton() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(this, R.raw.button_sound);
            }
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public final static String loadJSONFromAsset(final Context context) throws Exception {
        String json = null;
        InputStream is = context.getAssets().open("words.json");
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
        chAdapter = new CharacterViewAdapter(MainActivity.this, characterList);
        chAdapter.setClickListener(this);
        recycler_view_horizontal.setAdapter(chAdapter);
    }

    // Set ActionBar Title
    private void setActionBarTitle(String title) {
        ab.setTitle(title);
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.drawable.bw_icon);
    }

    // GetAllItemList
    private List<ItemObject> getAllItemList() {

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

    // onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu

        showHideMenuList(menu);

        menuTime = menu.findItem(R.id.time);
        menuTime.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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

    // onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time:
                if (getMenuTitle().equals(getString(R.string.time) + " " + getString(R.string.time00))) {

                    showTimerMessage("");

                } else if (getMenuTitle().equals(getString(R.string.time_out))) {

                    showTimerMessage("");

                } else {

                    startTimer();

                }

                if (Preference.getBoardName(this) != null &&
                        !Preference.getBoardName(this).equals("")) {

                    setActionBarTitle(Preference.getBoardName(this));

                } else {
                    setActionBarTitle(getString(R.string.app_name));
                }

                relative_list.setVisibility(View.GONE);

                return true;

            case R.id.board_list:

                showRecords();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        setMenuItemTitle(getString(R.string.time) + " " + getString(R.string.time00));
        showHideMenuList(menu);

        if (Preference.getRatingStar(this) >= 5) {
            showMessageCongratulationRatingDone();
            return super.onPrepareOptionsMenu(menu);
        }

        if (Preference.getBoardName(this).equals("") && Preference.getBoardTime(this) == -1) {
            showDialogForBoardName();
            return super.onPrepareOptionsMenu(menu);
        }

        if (Preference.getBoardTime(this) != -1) {

            updateTime(menu, Preference.getBoardTime(this));
            return super.onPrepareOptionsMenu(menu);
        }

        if (Preference.getBoardTime(this) == -1) {

            showDialogForTime();
            return super.onPrepareOptionsMenu(menu);
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
        img_backspace.setVisibility(View.VISIBLE);
        TextViewUtil.appendText(tvCharacter, rcAdapter.getItem(position));
        //checkCharacterRequest(tvCharacter.getText().toString().toLowerCase());
        new CheckCharacter().execute();
    }

    private void checkCharacterRequest(String text) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT + text + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestClient restClient = retrofit.create(RestClient.class);
        Call<WordsApi> call = restClient.wordsRequest();

        Callback<WordsApi> callback = new Callback<WordsApi>() {

            @Override
            public void onResponse(Call<WordsApi> call, Response<WordsApi> response) {
                WordsApi wordsApi = response.body();
                Log.e("wordsApi","wordsApi?"+wordsApi);
                int code = response.code();
                if (code == RESPONSE_CODE) {
                    if(wordsApi.getWord().equals(tvCharacter.getText().toString().toLowerCase())){
                        Log.e("equal","equal?");
                    }else{
                        Log.e("equal","not?");
                    }


                } else {
                    Log.e("equal","nottt?"+code);

                }
            }

            @Override
            public void onFailure(Call<WordsApi> call, Throwable t) {
                Log.e("onFailure", "onFailure?? " + t.getMessage());

            }
        };

        call.enqueue(callback);

    }

    // Check Character Async Task
    public class CheckCharacter extends AsyncTask<Void, Void, Void> {

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
        for (String text : allcharacterList) {
            if (text.toLowerCase().equals(tvCharacter.getText().toString().toLowerCase())) {

                stopTimer();
                //tvCharacter.setBackgroundResource(R.drawable.token_background);
                //tvCharacter.setTextColor(Color.parseColor("#FFFFFF"));
                setSpanText(tvCharacter.getText().toString());

                if (characterList != null) {
                    characterList.add(text.toUpperCase());
                    setAdapterForCharacters(characterList);
                }

                if (characterList != null && characterList.size() == 10) {
                    setWholeBoardList(Preference.getBoardName(this), (ArrayList<String>) characterList);
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
        playSoundButton();
        new LovelyStandardDialog(this)
                .setCancelable(true)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitleColor(R.color.md_white_1000)
                .setIcon(R.drawable.ic_access_time_white_24dp)
                .setTopTitle(R.string.time_out)
                .setMessage(R.string.timer_message)
                .setMessageGravity(Gravity.CENTER)
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.start, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setTimeOutRating(text);
                        playSoundButton();
                        invalidateOptionsMenu();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTimeOutRating(text);
                        playSoundButton();
                    }
                })
                .show();
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

    // Show Dialog For BoardName
    private void showDialogForBoardName() {
        playSoundButton();
        new LovelyTextInputDialog(this)
                .setCancelable(false)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitle(getString(R.string.setBoardName))
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
                        playSoundButton();

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
        playSoundButton();
        // Follow Link:- https://android-arsenal.com/details/1/3452
        ArrayAdapter<TimeOption> adapter = new TimeAdapter(this, loadTimeOptions());
        new LovelyChoiceDialog(this)
                .setCancelable(false)
                .setTopTitle(getString(R.string.setTime))
                .setTopTitleColor(R.color.md_white_1000)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_access_time_white_24dp)
                .setItems(adapter, new LovelyChoiceDialog.OnItemSelectedListener<TimeOption>() {
                    @Override
                    public void onItemSelected(int position, TimeOption item) {
                        playSoundButton();

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
        playSoundButton();
        new LovelyStandardDialog(this)
                .setCancelable(false)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitleColor(R.color.md_white_1000)
                .setIcon(R.drawable.ic_check_circle_white_24dp)
                .setTopTitle(R.string.congratulation)
                .setMessageGravity(Gravity.CENTER)
                .setMessage(R.string.congratulation_message)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.next, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSoundButton();
                        setMenuItemTitle(getString(R.string.time) + " " + getString(R.string.time00));
                        tvCharacter.setText("");
                        img_backspace.setVisibility(View.GONE);
                        tvCharacter.setBackgroundResource(0);
                        tvCharacter.setTextColor(Color.parseColor("#000000"));
                        invalidateOptionsMenu();

                    }
                })
                .show();
    }

    // Show Congratulation Message For Make Next Board
    private void showMessageCongratulationNextBoard() {
        playSoundButton();
        new LovelyStandardDialog(this)
                .setCancelable(false)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitleColor(R.color.md_white_1000)
                .setIcon(R.drawable.ic_check_circle_white_24dp)
                .setTopTitle(R.string.congratulation)
                .setMessage(R.string.congratulation_message_complete_board)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.next, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSoundButton();
                        if (Preference.getRatingStar(MainActivity.this) == -1) {
                            ratingBar.setRating(1);
                            Preference.setRatingStar(MainActivity.this, 1);
                        } else {
                            int rateStar = Preference.getRatingStar(MainActivity.this);
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
                })
                .show();
    }

    // Show Congratulation Message When 5 Star Rating is Done
    private void showMessageCongratulationRatingDone() {
        playSoundButton();
        new LovelyStandardDialog(this)
                .setCancelable(false)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitleColor(R.color.md_white_1000)
                .setIcon(R.drawable.ic_stars_white_24dp)
                .setTopTitle(R.string.level)
                .setMessage(R.string.congratulation_message_complete_five_star)
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButtonColor(getResources().getColor(R.color.colorAccent))
                .setNegativeButtonColor(getResources().getColor(R.color.colorAccent))
                .setPositiveButton(R.string.new_game, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        c_List.clear();
                        Preference.saveList(MainActivity.this, c_List);
                        playSoundButton();
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
                        invalidateOptionsMenu();

                    }
                })
                .setNegativeButton(R.string.board_words_list, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRecords();
                    }
                })
                .show();
    }

    // Show Dialog For Exit
    private void showDialogForExit() {
        new LovelyStandardDialog(this)
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
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    // On Item Click On Words
    @Override
    public void onItemClicks(View view, int position) {
        Log.e("ss", "ss???" + chAdapter.getItem(position));
        //view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
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

        public RainbowSpan(Context context) {
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

    // onBackPressed
    @Override
    public void onBackPressed() {
        showDialogForExit();
    }

    // onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preference.setBoardTime(this, -1);
        stopTimer();
    }
}
