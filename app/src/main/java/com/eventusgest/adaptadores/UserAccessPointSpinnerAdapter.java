package com.eventusgest.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.eventusgest.MainActivity;
import com.eventusgest.R;

import static com.eventusgest.MainActivity.ACCESS_POINT_NAME;

public class UserAccessPointSpinnerAdapter implements AdapterView.OnItemSelectedListener {
    private TextView tvAccessPoint;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences sharedPrefUser = view.getContext().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefUser.edit();
        editor.putString(ACCESS_POINT_NAME,  adapterView.getSelectedItem().toString());
        editor.apply();

        String accesspoint = sharedPrefUser.getString(ACCESS_POINT_NAME, ACCESS_POINT_NAME);

        tvAccessPoint = (TextView) ((Activity) view.getContext()).findViewById(R.id.tvUserAccessPoint);
        tvAccessPoint.setText(accesspoint);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
