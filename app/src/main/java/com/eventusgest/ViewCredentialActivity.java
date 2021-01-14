package com.eventusgest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.eventusgest.listeners.EventUserListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.EventJsonParser;

import org.json.JSONArray;

public class ViewCredentialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credential);
    }
}