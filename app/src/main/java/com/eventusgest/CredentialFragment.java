package com.eventusgest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.SingletonGestor;

import java.util.ArrayList;

public class CredentialFragment extends Fragment {

    public CredentialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Credential credential = new Credential(1, 1, 1, 1, 0, 0, "dasqwe21");

        SingletonGestor.getInstance(getContext()).addCredentialBD(credential);
        System.out.println(SingletonGestor.getInstance(getContext()).getAllCredentialsDB());

        return inflater.inflate(R.layout.fragment_list_credential, container, false);
    }
}