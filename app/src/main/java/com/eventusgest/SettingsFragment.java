package com.eventusgest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.eventusgest.adaptadores.UserCurrentEventSpinnerAdapter;
import com.eventusgest.listeners.EventUserListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.EventJsonParser;
import com.eventusgest.utils.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements EventUserListener {

    private View view = null;
    private EditText edapiUrl;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        SingletonGestor.getInstance(getContext().getApplicationContext()).setEventUserListener(this);

        final SharedPreferences sharedPrefUser = this.getActivity().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        String username = sharedPrefUser.getString(MainActivity.USERNAME, MainActivity.USERNAME);
        String apiUrl = sharedPrefUser.getString(MainActivity.API_URL, MainActivity.API_URL);

        if (!Utility.hasInternetConnection(getActivity())) {
            Toast.makeText(getContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            SingletonGestor.getInstance(getActivity().getApplicationContext()).getUserEventsAPI(getActivity().getApplicationContext(), username);
        }

        edapiUrl = view.findViewById(R.id.etAPIUrl);
        if (apiUrl != null) {
            edapiUrl.setText(apiUrl);
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPrefUser.edit();
                editor.putString(MainActivity.API_URL, edapiUrl.getText().toString());
                editor.apply();
                SingletonGestor.getInstance(getActivity().getApplicationContext()).setAPIUrl(getActivity().getApplicationContext());
                Toast.makeText(getContext(), "Opções de desenvolvedor guardadas", Toast.LENGTH_SHORT).show();
            }
        });

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