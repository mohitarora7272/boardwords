package com.boardwords.utils;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.boardwords.modal.Character;
import com.boardwords.modal.WordsPOJO;
import com.boardwords.orm.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToXML {
    private DatabaseHelper databaseHelper = null;
    private Context ctx;
    private Dao<Character, Integer> characterDao;

    public ToXML(Context ctx) {
        this.ctx = ctx;
    }

    /* DatabaseHelper */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void ge() {
        // Saved to database
        try {
            characterDao = getHelper().getCharacterDao();
            if (characterDao.isTableExists()) {
                Log.e("is", "exists??" + characterDao.isTableExists());
                long numRows = characterDao.countOf();
                if (numRows != 0) {
                    QueryBuilder<Character, Integer> queryBuilder = characterDao.queryBuilder();
                    List<Character> results = queryBuilder.query();

                } else {
                    Log.e("numRows", "numRows??" + numRows);
                    copyCharacterToDatabase();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // Copy Character To Database
    private void copyCharacterToDatabase() {
        //Find the directory for the SD Card using the API
        //*Don't* hardcode "/sdcard"
        File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.e("sdcard", "sdcard??" + sdcard);

        //Get the text file
        File file = new File(sdcard, "words.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();
        {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                Character character = null;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    Log.e("text", "text??" + line);
                    //text.append('\n');
                    character = new Character();
                    character.setCharacters(line.toString());
                    try {
                        characterDao.create(character);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                br.close();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void copyFileInSdcardJson(List<String> allItems) {
        Gson gson = new Gson();
        String json = gson.toJson(allItems);
        FileWriter fw = null;
        try {
            File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            //Get the text file
            File file = new File(sdcard, "words.json");
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readFile() {
        List<String> allItems = new ArrayList<>();
        try {
            File yourFile = new File("file:///android_asset/words.json");
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            allItems = new Gson().fromJson(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allItems;
    }


}



