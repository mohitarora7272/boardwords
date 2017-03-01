package com.boardwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.boardwords.modal.User;
import com.boardwords.orm.DatabaseHelper;
import com.boardwords.preference.Preference;
import com.boardwords.utils.Utilities;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.btnLogIn)
    Button btnLogIn;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private DatabaseHelper databaseHelper = null;
    private Dao<User, Integer> userDao;

    /* DatabaseHelper */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passIntent();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnLogIn)
    public void btnLogIn() {
        if (Utilities.isEmpty(edtName)) {
            Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_name));
            return;
        }

        if (Utilities.isEmpty(edtEmail)) {
            Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_email));
            return;
        }

        if (!Utilities.isValidEmail(edtEmail.getText().toString())) {
            Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_valid_email));
            return;
        }

        saveUserCredentials(edtName.getText().toString(),
                edtEmail.getText().toString(),
                Utilities.getDeviceId(this));
    }

    // Save User Credentials in DataBase And SharePreference
    private void saveUserCredentials(String name, String email, String deviceId) {
        // Saved to database
        try {
            userDao = getHelper().getUserDao();
            if (userDao.isTableExists()) {
                long numRows = userDao.countOf();
                if (numRows != 0) {
                    QueryBuilder<User, Integer> queryBuilder = userDao.queryBuilder();
                    List<User> results = queryBuilder.query();
                    for (User users : results) {
                        if (!email.equals(users.getUser_email())) {
                            setUpUser(name, email, deviceId);

                        }
                    }
                } else {
                    setUpUser(name, email, deviceId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Saved to Preference
        Preference.setUserCredentials(this, name, email, deviceId);
        passIntent();
    }

    // SetUp User
    private void setUpUser(String name, String email, String deviceId) {
        User user = new User();
        user.setUser_name(name);
        user.setUser_email(email);
        user.setUser_device_id(deviceId);
        try {
            userDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Pass Intent To MainActivity
    private void passIntent() {
        if (Preference.getEmail(this) != null && !Preference.getEmail(this).equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
