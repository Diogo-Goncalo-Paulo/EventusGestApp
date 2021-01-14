package com.eventusgest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eventusgest.adaptadores.CredentialListAdapter;
import com.eventusgest.listeners.CredentialListener;
import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.SingletonGestor;

import java.util.ArrayList;

public class CredentialFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, CredentialListener {
    private ListView lvCredentialList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CredentialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_credential, container, false);
        setHasOptionsMenu(true);

        lvCredentialList = view.findViewById(R.id.lvCredentialList);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        SingletonGestor.getInstance(getContext()).setCredentialListener(this);
        SingletonGestor.getInstance(getContext()).getAllCredentialsApi(getContext());

        return view;
    }

    @Override
    public void onRefresh() {
        SingletonGestor.getInstance(getContext()).getAllCredentialsApi(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshCredentialList(ArrayList<Credential> credentialList) {
        if (credentialList != null) {
            lvCredentialList.setAdapter(new CredentialListAdapter(getContext(),credentialList));
        }
    }
}