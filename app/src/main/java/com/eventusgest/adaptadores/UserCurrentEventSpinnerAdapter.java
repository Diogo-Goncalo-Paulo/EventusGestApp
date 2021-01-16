package com.eventusgest.adaptadores;

import android.view.View;
import android.widget.AdapterView;

import com.eventusgest.modelo.SingletonGestor;

import androidx.fragment.app.Fragment;

public class UserCurrentEventSpinnerAdapter extends Fragment implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SingletonGestor.getInstance(view.getContext()).updateUserEvent(view.getContext(),adapterView.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
