package com.eventusgest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eventusgest.listeners.LoginListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.modelo.User;
import com.eventusgest.utils.UserJsonParser;
import com.eventusgest.utils.Utility;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoginListener {
    public EditText etUsername;
    public EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        SingletonGestor.getInstance(getApplicationContext()).setLoginListener(this);
    }

    public void onClickLogin(View view) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (!isUsernameValid(username)) {
            etUsername.setError(getString(R.string.usename_invalido));
            return;
        }
        if (!isPasswordValid(password)) {
            etPassword.setError(getString(R.string.password_invalida));
            return;
        }

        if (!Utility.hasInternetConnection(this)) {
            Toast.makeText(this, R.string.noInternet, Toast.LENGTH_SHORT).show();
            offlineLogin(username, password);
        } else {
            SingletonGestor.getInstance(getApplicationContext()).loginAPI(username, password, getApplicationContext());
        }
    }

    /**
     * Check if password is null and if it has the minimum length of 8
     * @param password The password to check
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        if (password == null)
            return false;
        return password.length() >=8;
    }

    /**
     * Check if username is null
     * @param username The username to check
     * @return boolean
     */
    private boolean isUsernameValid(String username) {
        return username != null;
    }

    private void offlineLogin(String username, String password) {
        SharedPreferences sharedPrefUser = getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        if (sharedPrefUser != null) {
            String auth = sharedPrefUser.getString(MainActivity.AUTH, MainActivity.AUTH);
            String base64EncodedCredentials = "";
            if (auth != null) {
                String credentials = username + ":" + password;
                base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                if (auth.equals(base64EncodedCredentials)) {
                    enterApp();
                } else {
                    Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onValidateLogin(String username, String password, String response, String base64EncodedCredentials) {
        User u = UserJsonParser.parserJsonUser(response);

        String credentials = username + ":" + password;
        base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        if (username != null) {
            SharedPreferences sharedPrefUser = getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefUser.edit();
            editor.putInt(MainActivity.USER_ID, u.getId());
            editor.putString(MainActivity.USERNAME, u.getUsername());
            editor.putString(MainActivity.DISPLAYNAME, u.getDisplayName());
            editor.putInt(MainActivity.CURRENT_EVENT, u.getCurrentEvent());
            editor.putString(MainActivity.CURRENT_EVENT_NAME, u.getEventName());
            editor.putString(MainActivity.AUTH, base64EncodedCredentials);
            editor.putInt(MainActivity.ACCESS_POINT, u.getAccessPoint());
            editor.putString(MainActivity.ACCESS_POINT_NAME, u.getAccessPointName());
            editor.putString(MainActivity.USER_ROLE, u.getRole());
            editor.apply();

            enterApp();
        } else {
            Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts the Main Activity
     */
    private void enterApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}