package com.eventusgest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements
NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private TextView tvUsername;
    public static final String USER = "USER_PREF_SHARED";
    public static final String USER_ID = "USER_ID";
    public static final String USERNAME = "USERNAME";
    public static final String DISPLAYNAME = "DISPLAYNAME";
    public static final String CURRENT_EVENT = "CURRENT_EVENT";
    public static final String CURRENT_EVENT_NAME = "CURRENT_EVENT_NAME";
    public static final String ACCESS_POINT = "ACCESS_POINT";
    public static final String ACCESS_POINT_NAME = "ACCESS_POINT_NAME";
    public static final String USER_ROLE = "USER_ROLE";

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

    private void carregarCabecalho() {
        SharedPreferences sharedPrefUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        String username = sharedPrefUser.getString(USERNAME, DISPLAYNAME);
        
        if(username == null)
            username = sharedPrefUser.getString(USERNAME, "bruh");

        View nView = navigationView.getHeaderView(0);
        tvUsername = nView.findViewById(R.id.tvUsernameDisplayName);
        tvUsername.setText(username);
    }

    private void carregarFragmentoInicial() {
        navigationView.setCheckedItem(R.id.nav_credenciais);
        Fragment fragment = new CredentialFragment();
        setTitle("Credenciais");
        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item .getItemId()) {
            case R.id.nav_movimentos:
                fragment = new CredentialFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_credenciais:
                setTitle(item.getTitle());
                break;
            case R.id.nav_settings:
                setTitle(item.getTitle());
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                System.out.println("-->Nav Estatico");
        }
        if( fragment!=null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}