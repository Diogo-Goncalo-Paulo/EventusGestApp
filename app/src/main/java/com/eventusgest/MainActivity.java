package com.eventusgest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.SingletonGestor;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String CHANNEL_ID = "EG";
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

        SingletonGestor.getInstance(getApplicationContext()).getAllCredentialsApi(getApplicationContext());

        createNotificationChannel();
        connectMqtt();
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
        if (displayname == null) {
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

        switch (item.getItemId()) {
            case R.id.nav_movimentos:
                fragment = new MovementFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_credenciais:
                fragment = new CredentialFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void connectMqtt() {
        String server = "tcp://127.0.0.1:1883";
        String topic = "eventusGest";
        String clientId = MqttClient.generateClientId();

        MqttAndroidClient client = new MqttAndroidClient(this.getApplicationContext(), "tcp://10.0.2.2:1883", clientId);

        try {
            IMqttToken subToken = client.connect();
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        if (client.isConnected()) {
                            client.subscribe(topic, 0);
                            client.setCallback(new MqttCallback() {
                                @Override
                                public void connectionLost(Throwable cause) {
                                }

                                @Override
                                public void messageArrived(String topic, MqttMessage message) throws Exception {
                                    Log.d("tag", "message>>" + new String(message.getPayload()));
                                    Log.d("tag", "topic>>" + topic);

                                    String json = new String(message.getPayload());
                                    JSONObject msg = new JSONObject(json);
                                    int credentialId = Integer.parseInt(msg.getString("credentialId"));

                                    SingletonGestor.getInstance(getApplicationContext()).getAllCredentialsApi(getApplicationContext());

                                    for (Credential c : SingletonGestor.getInstance(getApplicationContext()).getAllCredentialsDB()) {
                                        if(c.getId() == credentialId) {
                                            Intent intent = new Intent(getApplicationContext(), ViewCredentialActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra(ViewCredentialActivity.ID, credentialId);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), credentialId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                    .setSmallIcon(R.mipmap.eg_icon)
                                                    .setContentTitle("EventusGest")
                                                    .setContentText(msg.getString("action").equals("flag") ? "Credencial " + msg.getString("credentialId") + " marcada." : msg.getString("action").equals("block") ? "Credencial " + msg.getString("credentialId") + " bloqueada." : "Credencial " + msg.getString("credentialId") + " desbloqueada.")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setContentIntent(pendingIntent)
                                                    .setAutoCancel(true);
                                            ;
                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                                            notificationManager.notify(0, builder.build());
                                        }
                                    }
                                }

                                @Override
                                public void deliveryComplete(IMqttDeliveryToken token) {

                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.d("tag", "Error :" + e);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}