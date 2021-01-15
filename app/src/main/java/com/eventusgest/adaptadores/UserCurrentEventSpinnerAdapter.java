package com.eventusgest.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.eventusgest.MainActivity;
import com.eventusgest.R;
import com.eventusgest.listeners.AccessPointListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.EventJsonParser;

import org.json.JSONArray;

import static com.eventusgest.MainActivity.CURRENT_EVENT_NAME;

public class UserCurrentEventSpinnerAdapter extends Fragment implements AdapterView.OnItemSelectedListener, AccessPointListener {
    private TextView tvCurrentEvent;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SingletonGestor.getInstance(view.getContext()).setAccessPointListener(this);
        SingletonGestor.getInstance(view.getContext()).getAccessPointsAPI(view.getContext(), adapterView.getSelectedItem().toString());

        SharedPreferences sharedPrefUser = view.getContext().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefUser.edit();
        editor.putString(CURRENT_EVENT_NAME, adapterView.getSelectedItem().toString());
        editor.apply();


        String currentevent = sharedPrefUser.getString(CURRENT_EVENT_NAME, CURRENT_EVENT_NAME);
        tvCurrentEvent = (TextView) ((Activity) view.getContext()).findViewById(R.id.tvCurrentEvent);
        tvCurrentEvent.setText(String.format("%s • ", currentevent));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onGetAccessPoints(JSONArray accessPoints, Context context) {
        Spinner spinner = ((Activity)context).findViewById(R.id.spinnerAccessPoint);
        spinner.setOnItemSelectedListener(new UserAccessPointSpinnerAdapter());

        SharedPreferences sharedPrefUser = context.getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        String accessPoint = sharedPrefUser.getString(MainActivity.ACCESS_POINT_NAME, MainActivity.ACCESS_POINT_NAME);

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

        spinner.setSelection(adapter.getPosition(accessPoint));
    }
}
