package com.eventusgest.adaptadores;

import android.view.View;
import android.widget.AdapterView;

import com.eventusgest.modelo.SingletonGestor;

public class UserAccessPointSpinnerAdapter implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SingletonGestor.getInstance(view.getContext()).updateUserAccessPoint(view.getContext(),adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
