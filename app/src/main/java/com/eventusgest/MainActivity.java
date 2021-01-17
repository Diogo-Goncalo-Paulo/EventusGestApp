package com.eventusgest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements
NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private TextView tvUsername;
    private TextView tvRole;
    private TextView tvAccessPoint;
    private TextView tvCurrentEvent;
    public static final String USER = "USER_PREF_SHARED";
    public static final String USER_ID = "USER_ID";
    public static final String USERNAME = "USERNAME";
    public static final String DISPLAYNAME = "DISPLAYNAME";
    public static final String CURRENT_EVENT = "CURRENT_EVENT";
    public static final String CURRENT_EVENT_NAME = "CURRENT_EVENT_NAME";
    public static final String ACCESS_POINT = "ACCESS_POINT";
    public static final String ACCESS_POINT_NAME = "ACCESS_POINT_NAME";
    public static final String USER_ROLE = "USER_ROLE";
    public static final String AUTH = "AUTH";
    public static final String API_ACCESS = "API_ACCESS";
    public static final String API_URL = "API_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.open, R.string.close);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        View nView = navigationView.getHeaderView(0);

        fragmentManager = getSupportFragmentManager();
        carregarCabecalho();
        navigationView.setNavigationItemSelectedListener(this);
        carregarFragmentoInicial();
    }

    public void carregarCabecalho() {
        SharedPreferences sharedPrefUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        String username = sharedPrefUser.getString(USERNAME, USERNAME);
        String displayname = sharedPrefUser.getString(DISPLAYNAME, DISPLAYNAME);
        String role = sharedPrefUser.getString(USER_ROLE, USER_ROLE);
        String currentevent = sharedPrefUser.getString(CURRENT_EVENT_NAME, CURRENT_EVENT_NAME);
        String accesspoint = sharedPrefUser.getString(ACCESS_POINT_NAME, ACCESS_POINT_NAME);

        View nView = navigationView.getHeaderView(0);
        tvUsername = nView.findViewById(R.id.tvUsernameDisplayName);
        if(displayname == null) {
            tvUsername.setText(username);
        } else {
            tvUsername.setText(displayname);
        }

        tvRole = nView.findViewById(R.id.tvRole);
        tvAccessPoint = nView.findViewById(R.id.tvUserAccessPoint);
        tvCurrentEvent = nView.findViewById(R.id.tvCurrentEvent);
        if (accesspoint != null)
            tvAccessPoint.setText(accesspoint);
        if (currentevent != null)
            tvCurrentEvent.setText(String.format("%s • ", currentevent));
        if (role != null)
            tvRole.setText(role);
    }

    private void carregarFragmentoInicial() {
        navigationView.setCheckedItem(R.id.nav_movimentos);
        Fragment fragment = new MovementFragment();
        setTitle("Movimentos");
        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item .getItemId()) {
            case R.id.nav_movimentos:
                fragment = new MovementFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_credenciais:
                fragment = new CredentialFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_scan:
                Intent intent = new Intent(MainActivity.this, ScanQrcodeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_logout:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        if( fragment!=null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}