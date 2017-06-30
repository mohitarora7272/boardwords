package com.boardwords.interfaces;


@SuppressWarnings("ALL")
public interface Constant {


    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "BoardWords.db";

    String MY_PREFERENCE = "BoardWordsPref";
    String KEY_EMAIL = "email";
    String KEY_NAME = "name";
    String KEY_DEVICE_ID = "deviceId";
    String KEY_BOARD_NAME = "board_name";
    String KEY_BOARD_TIME = "board_time";
    String KEY_RATING_STAR = "rating_star";
    String KEY_TIMEOUT_RATING = "timeout_rating";
    String KEY_LIST = "list";
    String KEY_VIBRATION = "vibration";
    String KEY_SOUND = "sound";

    int TIME_INTERVAL_10_SEC = 10000;
    int TIME_INTERVAL_15_SEC = 15000;
    int TIME_INTERVAL_20_SEC = 20000;
    int TIME_INTERVAL_1_SEC = 1000;
    int RESPONSE_CODE = 200;
    int WRITE_EXTERNAL_STORAGE_CODE = 1000;
}
