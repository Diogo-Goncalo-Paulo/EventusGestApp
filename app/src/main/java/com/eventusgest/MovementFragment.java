package com.eventusgest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.eventusgest.adaptadores.CredentialListAdapter;
import com.eventusgest.adaptadores.MovementListAdapter;
import com.eventusgest.listeners.CredentialListener;
import com.eventusgest.listeners.MovementListener;
import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.Movement;
import com.eventusgest.modelo.SingletonGestor;

import java.util.ArrayList;

public class MovementFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MovementListener {
    private ListView lvMovementList;
    public static final String USER = "USER_PREF_SHARED";
    public static final String CURRENT_EVENT = "CURRENT_EVENT";
    private static final int VER_MOVIMENTO = 1;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MovementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_movement, container, false);
        setHasOptionsMenu(true);

        lvMovementList = view.findViewById(R.id.lvMovementList);

        lvMovementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ViewMovementActivity.class);
                intent.putExtra(ViewMovementActivity.ID, (int) id);
                //startActivity(intent);
                startActivityForResult(intent, VER_MOVIMENTO);
            }
        });


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        SingletonGestor.getInstance(getContext()).setMovementListener(this);
        SingletonGestor.getInstance(getContext()).getAllMovementsApi(getContext());

        return view;
    }

    @Override
    public void onRefresh() {
        SingletonGestor.getInstance(getContext()).getAllMovementsApi(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefreshMovementList(ArrayList<Movement> movementList) {
        if (movementList != null) {
            ArrayList<Movement> movementCurrentEventList = new ArrayList<>();
            SharedPreferences sharedPref = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);

            for (Movement m: movementList) {
                if(m.getIdEvent() == sharedPref.getInt(CURRENT_EVENT,-1)){
                    movementCurrentEventList.add(m);
                }
            }

            if(movementCurrentEventList != null){
                lvMovementList.setAdapter(new MovementListAdapter(getContext(),movementCurrentEventList));
            }


        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.credential_search, menu);

        final MenuItem itemPesquisa = menu.findItem(R.id.item_pesquisa);
        searchView = (SearchView) itemPesquisa.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Movement> tempMovements = new ArrayList<>();

                for (Movement m : SingletonGestor.getInstance(getContext()).getAllMovementsDB())
                    if(m.getNameCredential().toLowerCase().contains(newText.toLowerCase()))
                        tempMovements.add(m);
                lvMovementList.setAdapter(new MovementListAdapter(getContext(), tempMovements));
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}