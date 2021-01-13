package com.eventusgest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eventusgest.listeners.CredentialListener;
import com.eventusgest.listeners.LoginListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.modelo.User;
import com.eventusgest.utils.CredentialJsonParser;
import com.eventusgest.utils.UserJsonParser;

import org.json.JSONException;

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

        if (!CredentialJsonParser.isConnectionInternet(this)) {
            Toast.makeText(this, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            SingletonGestor.getInstance(getApplicationContext()).loginAPI(username, password, getApplicationContext());
        }
    }

    @Override
    public void onValidateLogin(String username, String password, String response) {
        User u = UserJsonParser.parserJsonUser(response);

        if (username != null) {
            SharedPreferences sharedPrefUser = getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefUser.edit();
            editor.putInt(MainActivity.USER_ID, u.getId());
            editor.putString(MainActivity.USERNAME, u.getUsername());
            editor.putString(MainActivity.DISPLAYNAME, u.getDisplayName());
            editor.putInt(MainActivity.CURRENT_EVENT, u.getCurrentEvent());
            editor.putString(MainActivity.CURRENT_EVENT_NAME, u.getEventName());
            editor.putInt(MainActivity.ACCESS_POINT, u.getAccessPoint());
            editor.putString(MainActivity.ACCESS_POINT_NAME, u.getAccessPointName());
            editor.putString(MainActivity.USER_ROLE, u.getRole());
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, R.string.invalid_login, Toast.LENGTH_SHORT).show();
        }
    }
}