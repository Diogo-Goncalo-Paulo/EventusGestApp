package com.eventusgest.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.eventusgest.R;
import com.eventusgest.listeners.AccessPointListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.EventJsonParser;

import org.json.JSONArray;

public class UserCurrentEventSpinnerAdapter extends Fragment implements AdapterView.OnItemSelectedListener, AccessPointListener {

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SingletonGestor.getInstance(getContext()).setAccessPointListener(this);
        SingletonGestor.getInstance(view.getContext()).getAccessPointsAPI(view.getContext(), adapterView.getSelectedItem().toString());
        Toast.makeText(view.getContext(), adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onGetAccessPoints(JSONArray accessPoints, Context context) {
        Spinner spinner = ((Activity)context).findViewById(R.id.spinnerAccessPoint);
        spinner.setOnItemSelectedListener(new UserAccessPointSpinnerAdapter());

        System.out.println("bruh");

        String[] accessPointss = EventJsonParser.parserJsonEventNames(accessPoints);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                accessPointss
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}
