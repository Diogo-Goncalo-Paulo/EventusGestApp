package com.eventusgest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eventusgest.adaptadores.UserAccessPointSpinnerAdapter;
import com.eventusgest.adaptadores.UserCurrentEventSpinnerAdapter;
import com.eventusgest.listeners.AccessPointListener;
import com.eventusgest.listeners.EventUserListener;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.EventJsonParser;
import com.eventusgest.utils.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import androidx.fragment.app.Fragment;

import static com.eventusgest.MainActivity.ACCESS_POINT_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements EventUserListener, AccessPointListener {

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

    @Override
    public void onUpdatedEvent(String event) {
        SingletonGestor.getInstance(view.getContext()).setAccessPointListener(this);
        SingletonGestor.getInstance(view.getContext()).getAccessPointsAPI(view.getContext(), EventJsonParser.parserJsonEventName(event, false));

        SharedPreferences sharedPrefUser = view.getContext().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefUser.edit();
        editor.putString(MainActivity.CURRENT_EVENT_NAME, EventJsonParser.parserJsonEventName(event, false));
        editor.apply();


        String currentevent = sharedPrefUser.getString(MainActivity.CURRENT_EVENT_NAME, MainActivity.CURRENT_EVENT_NAME);
        TextView tvCurrentEvent = (TextView) ((Activity) view.getContext()).findViewById(R.id.tvCurrentEvent);
        tvCurrentEvent.setText(String.format("%s • ", currentevent));
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

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(accessPoint));
    }

    @Override
    public void onUpdatedAccessPoint(String accessPoint) {
        SharedPreferences sharedPrefUser = view.getContext().getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefUser.edit();
        editor.putString(ACCESS_POINT_NAME,  EventJsonParser.parserJsonEventName(accessPoint, true));
        editor.apply();

        String accesspoint = sharedPrefUser.getString(ACCESS_POINT_NAME, ACCESS_POINT_NAME);

        TextView tvAccessPoint = (TextView) ((Activity) view.getContext()).findViewById(R.id.tvUserAccessPoint);
        tvAccessPoint.setText(accesspoint);
    }
}