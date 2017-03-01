package com.boardwords.modal;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

@SuppressWarnings("all")
public class User implements Serializable {

    private static final long serialVersionUID = -222864131214757024L;

    public static final String ID = "id";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_DEVICE_ID = "user_device_id";


    @DatabaseField(generatedId = true, columnName = ID)
    public int id;
    @DatabaseField(columnName = USER_EMAIL)
    public String user_email;
    @DatabaseField(columnName = USER_DEVICE_ID)
    public String user_device_id;
    @DatabaseField(columnName = USER_NAME)
    public String user_name;

    /* Default Constructor */
    public User() {
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_device_id() {
        return user_device_id;
    }

    public void setUser_device_id(String user_device_id) {
        this.user_device_id = user_device_id;
    }
}
