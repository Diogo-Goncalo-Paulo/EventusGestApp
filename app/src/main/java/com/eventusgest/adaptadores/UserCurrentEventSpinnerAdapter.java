package com.eventusgest.adaptadores;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class UserCurrentEventSpinnerAdapter extends Fragment implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Toast.makeText(view.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
