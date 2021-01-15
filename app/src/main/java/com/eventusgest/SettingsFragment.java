package com.eventusgest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.eventusgest.adaptadores.UserCurrentEventSpinnerAdapter;
import com.eventusgest.listeners.EventUserListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.EventJsonParser;
import com.eventusgest.utils.Utility;

import org.json.JSONArray;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements EventUserListener {

    private View view = null;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        SingletonGestor.getInstance(getContext().getApplicationContext()).setEventUserListener(this);

        SharedPreferences sharedPrefUser = this.getActivity().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        String username = sharedPrefUser.getString(MainActivity.USERNAME, MainActivity.USERNAME);

        if (!Utility.hasInternetConnection(getActivity())) {
            Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            SingletonGestor.getInstance(getActivity().getApplicationContext()).getUserEventsAPI(getActivity().getApplicationContext(), username);
        }

        String apiUrl = sharedPrefUser.getString(MainActivity.API_URL, MainActivity.API_URL);
        if (apiUrl == null) {
//            SharedPreferences.Editor editor = sharedPrefUser.edit();
//            editor.putString(MainActivity.API_URL, "http://192.168.1.68:8080/");
//            editor.apply();
        }

        return view;
    }

    @Override
    public void onGetEvents(JSONArray events) {
        SharedPreferences sharedPrefUser = this.getActivity().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        String event = sharedPrefUser.getString(MainActivity.CURRENT_EVENT_NAME, MainActivity.CURRENT_EVENT_NAME);

        Spinner spinner = view.findViewById(R.id.spinnerCurrentEvent);
        spinner.setOnItemSelectedListener(new UserCurrentEventSpinnerAdapter());

        String[] eventss = EventJsonParser.parserJsonEventNames(events);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                eventss
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(adapter.getPosition(event));
    }
}