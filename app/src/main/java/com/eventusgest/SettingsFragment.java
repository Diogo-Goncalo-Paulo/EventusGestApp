package com.eventusgest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.eventusgest.adaptadores.UserCurrentEventSpinnerAdapter;
import com.eventusgest.modelo.SingletonGestor;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        String[] pila = SingletonGestor.getInstance(getContext()).getUserEventsAPI(getContext(), "gocaspro13");
        String[] country = {"India", "USA", "China", "Japan", "Other"};

        Spinner spinner = view.findViewById(R.id.spinnerCurrentEvent);
        spinner.setOnItemSelectedListener(new UserCurrentEventSpinnerAdapter());

        System.out.println(pila[0]);
        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this.getContext(),
//                android.R.layout.simple_spinner_item,
//
//        );

        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);

        return view;
    }
}