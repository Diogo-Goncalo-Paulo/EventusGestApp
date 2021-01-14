package com.eventusgest.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eventusgest.R;
import com.eventusgest.listeners.CredentialListener;
import com.eventusgest.listeners.LoginListener;
import com.eventusgest.utils.CredentialJsonParser;
import com.eventusgest.utils.EventJsonParser;
import com.eventusgest.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingletonGestor {
    private static SingletonGestor instance = null;
    private ArrayList<Credential> credentials;
    private String[] events;
    private CredentialDBHelper credentialsDB = null;
    private String mUrlAPIUser = "http://192.168.1.68:8080/backend/web/api/user/username/";
    private String mUrlAPICredential = "http://192.168.1.68:8080/backend/web/api/credential";
    private static RequestQueue volleyQueue;
    private CredentialListener credentialListener;
    private LoginListener loginListener;
    private String authKey;

    private ArrayList<Movement> movements;
    private MovementDBHelper movementsDB = null;

    public static synchronized SingletonGestor getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestor(context);
        }
        return instance;
    }

    public void setCredentialListener(CredentialListener credentialListener) {
        this.credentialListener = credentialListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public SingletonGestor(Context context) {
        credentials = new ArrayList<>();
        credentialsDB = new CredentialDBHelper(context);
        movements = new ArrayList<>();
        movementsDB = new MovementDBHelper(context);
    }

    public Credential getCredential (int id) {
        for (Credential c : credentials)
            if(c.getId() == id)
                return c;
        return null;
    }

    public ArrayList<Credential> getAllCredentialsDB() {
        credentials = credentialsDB.getAllCredentialsDB();
        return credentials;
    }

    public void addCredentialDB (Credential credential) {
        credentialsDB.addCredentialDb(credential);
    }

    public void addCredentialsDB (ArrayList<Credential> credentials) {
        credentialsDB.removeAllCredentials();
        for (Credential c : credentials)
            addCredentialDB(c);
    }

    public ArrayList<Credential> getAllCredentials () {
        return new ArrayList<>(credentials);
    }

    public void getAllCredentialsApi(final Context context) {
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, mUrlAPICredential, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    credentials = CredentialJsonParser.parserJsonCredentials(response);
                    addCredentialsDB(credentials);

                    if (credentialListener != null) {
                        credentialListener.onRefreshCredentialList(credentials);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public String[] getUserEventsAPI(final Context context, String username) {
        String url = UrlAPI + APIPathUserEvents + username;
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println("JAVA É MERDA!");
                    events = EventJsonParser.parserJsonEventNames(response);
                    System.out.println(events);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("JAVA BUÉ É MERDA!");
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);

        }
        System.out.println(events);
        return events;
    }

    public String[] getUserEvents(Context context) {
        getUserEventsAPI(context, "gocaspro13");
        return events;
    }

    public void loginAPI(final String username, final String password, final Context context) {
        String credentials = username + ":" + password;
        final String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        StringRequest req = new StringRequest(Request.Method.GET, mUrlAPIUser + username, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(loginListener != null)
                    loginListener.onValidateLogin(username, password, response, base64EncodedCredentials);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    JSONArray errors = data.getJSONArray("errors");
                    JSONObject jsonMessage = errors.getJSONObject(0);
                    String message = jsonMessage.getString("message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = username + ":" + password;
                authKey = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", "Basic " + authKey);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("password", password);
                return params;
            }
        };
        volleyQueue = Volley.newRequestQueue(context);
        volleyQueue.add(req);
    }

    public Movement getMovement (int id) {
        for (Movement c : movements)
            if(c.getId() == id)
                return c;
        return null;
    }

    public ArrayList<Movement> getMovementsDB() {
        movements = movementsDB.getAllMovementsDB();
        return movements;
    }

    public void addMovementBD (Movement movement) {
        movementsDB.addMovementDb(movement);
    }

    public ArrayList<Movement> getAllMovements () {
        return new ArrayList<>(movements);
    }


}
