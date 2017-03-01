package com.boardwords.orm;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.boardwords.BoardWordsApplication;
import com.boardwords.R;
import com.boardwords.interfaces.Constant;
import com.boardwords.modal.Character;
import com.boardwords.modal.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("all")
public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements Constant {

    /************************************************
     * Suggested Copy/Paste code. Everything from here to the done block.
     ************************************************/
    private static final String DATABASE_PATH = "/data/data/" + BoardWordsApplication.getContext().getPackageName() + "/databases/";
    private static DatabaseHelper databaseHelper;
    private Dao<User, Integer> userDao;
    private Dao<Character, Integer> characterDao;


    public DatabaseHelper(Context context) {
//        super(context, Environment.getExternalStorageDirectory().getPath()
//                        + File.separator + DRIVE_DB_NAME
//                        + File.separator + DATABASE_NAME,
//                null, DATABASE_VERSION, R.raw.ormlite_config);
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        databaseHelper = DatabaseHelper.this;
        if (!databaseExists()) {
            synchronized (this) {
                boolean success = copyPrepopulatedDatabase();
                Log.e("Copy", "Copy??" + success);
            }
        }
    }

    public static DatabaseHelper getInstance() {
        return databaseHelper;
    }

    /************************************************
     * Suggested Copy/Paste Done
     ************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
//        try {
//
//            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
//            TableUtils.createTable(connectionSource, User.class);
//            TableUtils.createTable(connectionSource, Character.class);
//
//        } catch (SQLException e) {
//            Log.e(DatabaseHelper.class.getName(), "Unable to create databases", e);
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
//        try {
//
//            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
//            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
//            // existing database etc.
//
//            TableUtils.dropTable(connectionSource, User.class, true);
//            TableUtils.dropTable(connectionSource, Character.class, true);
//
//            onCreate(sqliteDatabase, connectionSource);
//
//        } catch (SQLException e) {
//            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
//                    + newVer, e);
//        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<User, Integer> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    public Dao<Character, Integer> getCharacterDao() throws SQLException {
        if (characterDao == null) {
            characterDao = getDao(Character.class);
        }
        return characterDao;
    }

    public boolean databaseExists() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        boolean exists = file.exists();
        return exists;
    }

    private boolean copyPrepopulatedDatabase() {
        // copy database from assets
        try {
            // create directories
            File dir = new File(DATABASE_PATH);
            dir.mkdirs();

            // input file name
            String inputFileName = DATABASE_NAME;
            String lang = Locale.getDefault().getLanguage();
            String translatedFileName = lang + "_" + DATABASE_NAME;
            if (assetExists(translatedFileName)) {
                inputFileName = translatedFileName;
            }

            // output file name
            String outputFileName = DATABASE_PATH + DATABASE_NAME;

            // create streams
            InputStream inputStream = BoardWordsApplication.getContext().getAssets().open(inputFileName);
            OutputStream outputStream = new FileOutputStream(outputFileName);

            // write input to output
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // close streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean assetExists(String fileName) {
        AssetManager assetManager = BoardWordsApplication.getContext().getAssets();
        try {
            List<String> list = Arrays.asList(assetManager.list(""));
            return list.contains(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
