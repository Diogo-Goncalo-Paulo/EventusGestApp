package com.eventusgest.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.eventusgest.MainActivity;
import com.eventusgest.R;
import com.eventusgest.listeners.AccessPointListener;
import com.eventusgest.listeners.CreateMovementListener;
import com.eventusgest.listeners.CredentialFlagBlockListener;
import com.eventusgest.listeners.CredentialListener;
import com.eventusgest.listeners.EventUserListener;
import com.eventusgest.listeners.LoginListener;
import com.eventusgest.listeners.MovementListener;
import com.eventusgest.utils.CredentialJsonParser;
import com.eventusgest.utils.MovementJsonParser;
import com.eventusgest.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.eventusgest.MainActivity.CURRENT_EVENT_NAME;

public class SingletonGestor {
    private static SingletonGestor instance = null;
    private ArrayList<Credential> credentials;
    private ArrayList<Movement> movements;
    private String[] events;
    private DBHelper dbHelper = null;


    private String APIUrl = null;
    private final static String APIPathUser = "/user/username/";
    private final static String APIPAthUpdateUserEvent = "/user/event/";
    private final static String APIPAthUpdateUserAccessPoint = "/user/accesspoint/";
    private final static String APIPathCredential = "/credential";
    private final static String APIPathMovements = "/movement";
    private final static String APIPathUserEvents = "/event/user/";
    private final static String APIPathAccessPointEvent = "/accesspoint/event/";

    private static RequestQueue volleyQueue;
    private CredentialListener credentialListener;
    private CredentialFlagBlockListener credentialFlagBlockListener;
    private MovementListener movementListener;
    private LoginListener loginListener;
    private EventUserListener eventUserListener;
    private AccessPointListener accessPointListener;
    private CreateMovementListener createMovementListener;
    private String authKey;

    public static synchronized SingletonGestor getInstance(Context context) {
        if (instance == null) {
            instance = new SingletonGestor(context);
            volleyQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return instance;
    }

    public void setAPIUrl(Context context) {
        SharedPreferences sharedPrefUser = context.getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        if (sharedPrefUser != null) {
            if (!sharedPrefUser.contains(MainActivity.API_URL)) {
                SharedPreferences.Editor editor = sharedPrefUser.edit();
                editor.putString(MainActivity.API_URL, "http://192.168.1.97:8080/backend/web/api");
                editor.apply();
            }
            APIUrl = sharedPrefUser.getString(MainActivity.API_URL, MainActivity.API_URL);
        }
    }

    public void setAccessPointListener(AccessPointListener accessPointListener) {
        this.accessPointListener = accessPointListener;
    }

    public void setCredentialListener(CredentialListener credentialListener) {
        this.credentialListener = credentialListener;
    }

    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setEventUserListener(EventUserListener eventUserListener) {
        this.eventUserListener = eventUserListener;
    }

    public void setCredentialFlagBlockListener(CredentialFlagBlockListener credentialFlagBlockListener) {
        this.credentialFlagBlockListener = credentialFlagBlockListener;
    }

    public void setCreateMovementListener(CreateMovementListener createMovementListener) {
        this.createMovementListener = createMovementListener;
    }

    public SingletonGestor(Context context) {
        credentials = new ArrayList<>();
        movements = new ArrayList<>();
        dbHelper = new DBHelper(context);

    }

    public Credential getCredential(int id) {
        for (Credential c : dbHelper.getAllCredentialsDB())
            if (c.getId() == id) {
                return c;
            }
        return null;
    }

    public Credential getCredentialUCID(String UCID) {
        for (Credential c : dbHelper.getAllCredentialsDB())
            if (c.getUcid().equals(UCID)) {
                return c;
            }
        return null;
    }

    public ArrayList<Credential> getAllCredentialsDB() {
        credentials = dbHelper.getAllCredentialsDB();
        return credentials;
    }

    public void addCredentialDB(Credential credential) {
        dbHelper.addCredentialDb(credential);
    }

    public void addCredentialsDB(ArrayList<Credential> credentials) {
        dbHelper.removeAllCredentials();
        for (Credential c : credentials)
            addCredentialDB(c);
    }

    public void flagCredentialDB(int id, Credential credential) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flagged", credential.getFlagged());

        db.update("credentials", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void blockCredentialDB(int id, Credential credential) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("blocked", credential.getBlocked());

        db.update("credentials", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Credential> getAllCredentials() {
        return new ArrayList<>(credentials);
    }

    public void getAllCredentialsApi(final Context context) {
        SharedPreferences sharedPrefUser = context.getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        String currentevent = sharedPrefUser.getString(CURRENT_EVENT_NAME, CURRENT_EVENT_NAME);
        if (APIUrl == null)
            setAPIUrl(context);

        if (!Utility.hasInternetConnection(context)) {
            if (credentialListener != null) {
                credentialListener.onRefreshCredentialList(getAllCredentialsDB());
            }
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            final JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, APIUrl + APIPathCredential + "/event/" + currentevent, null, new Response.Listener<JSONArray>() {
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
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void getCredentialAPI(final Context context, String ucid) {
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, APIUrl + APIPathCredential + "/search?q=" + ucid, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println(response);
                    credentials = CredentialJsonParser.parserJsonCredentials(response);
                    addCredentialsDB(credentials);

                    if (createMovementListener != null) {
                        createMovementListener.onSearchUCID(credentials.get(0).getUcid());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void flagCredential(final Context context, final int credentialId) {
        if (APIUrl == null)
            setAPIUrl(context);
        String url = APIUrl + APIPathCredential + "/flag/" + credentialId;
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.PUT, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    credentials = CredentialJsonParser.parserJsonCredentials(response);
                    flagCredentialDB(credentialId, credentials.get(0));

                    if (credentialFlagBlockListener != null) {
                        credentialFlagBlockListener.onFlagCredential(credentials.get(0));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void blockCredential(final Context context, final int credentialId) {
        if (APIUrl == null)
            setAPIUrl(context);
        String url = APIUrl + APIPathCredential + "/block/" + credentialId;
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.PUT, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    credentials = CredentialJsonParser.parserJsonCredentials(response);
                    blockCredentialDB(credentialId, credentials.get(0));

                    if (credentialFlagBlockListener != null) {
                        credentialFlagBlockListener.onBlockCredential();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void getAllMovementsApi(final Context context) {
        SharedPreferences sharedPrefUser = context.getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
        if (APIUrl == null)
            setAPIUrl(context);

        if (!Utility.hasInternetConnection(context)) {
            if (movementListener != null) {
                movementListener.onRefreshMovementList(getAllMovementsDB());
            }
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            final JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, APIUrl + APIPathMovements, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    movements = MovementJsonParser.parserJsonMovements(response);
                    addMovementsDB(movements);

                    if (movementListener != null) {
                        movementListener.onRefreshMovementList(movements);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void getUserEventsAPI(final Context context, String username) {
        if (APIUrl == null)
            setAPIUrl(context);
        String url = APIUrl + APIPathUserEvents + username;
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (eventUserListener != null)
                        eventUserListener.onGetEvents(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Basic " + authKey);
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void getAccessPointsAPI(final Context context, String event) {
        if (APIUrl == null)
            setAPIUrl(context);
        String url = APIUrl + APIPathAccessPointEvent + event;
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    if (accessPointListener != null)
                        accessPointListener.onGetAccessPoints(response, context);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Ocurreu um erro!", Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + authKey);
                    headers.put("Content-Type: ", "application/json charset=utf-8");
                    return headers;
                }
            };
            volleyQueue.add(req);
        }
    }

    public void updateUserEvent(final Context context, final String event) {
        if (APIUrl == null)
            setAPIUrl(context);
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPrefUser = context.getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
            int userID = sharedPrefUser.getInt(MainActivity.USER_ID, 0);

            String url = APIUrl + APIPAthUpdateUserEvent + userID;
            StringRequest req = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (eventUserListener != null)
                        eventUserListener.onUpdatedEvent(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Ocurreu um erro!", Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + authKey);
                    headers.put("Content-Type: ", "application/json charset=utf-8");
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("eventName", event);
                    return params;
                }
            };

            volleyQueue.add(req);
        }
    }

    public void updateUserAccessPoint(final Context context, final String accessPoint) {
        if (APIUrl == null)
            setAPIUrl(context);
        if (!Utility.hasInternetConnection(context)) {
            Toast.makeText(context, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPrefUser = context.getSharedPreferences(MainActivity.USER, Context.MODE_PRIVATE);
            int userID = sharedPrefUser.getInt(MainActivity.USER_ID, 0);

            String url = APIUrl + APIPAthUpdateUserAccessPoint + userID;
            StringRequest req = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (accessPointListener != null)
                        accessPointListener.onUpdatedAccessPoint(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Ocurreu um erro!", Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                }
            }) {
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Basic " + authKey);
                    headers.put("Content-Type: ", "application/json charset=utf-8");
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accessPointName", accessPoint);
                    return params;
                }
            };

            volleyQueue.add(req);
        }
    }

    public void loginAPI(final String username, final String password, final Context context) {
        if (APIUrl == null)
            setAPIUrl(context);
        String credentials = username + ":" + password;
        final String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        StringRequest req = new StringRequest(Request.Method.GET, APIUrl + APIPathUser + username, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (loginListener != null)
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
        volleyQueue.add(req);
    }

    public Movement getMovement(int id) {
        for (Movement c : movements)
            if (c.getId() == id)
                return c;
        return null;
    }

    public ArrayList<Movement> getAllMovementsDB() {
        movements = dbHelper.getAllMovementsDB();
        return movements;
    }

    public void addMovementDB(Movement movement) {
        dbHelper.addMovementDb(movement);
    }

    public void addMovementsDB(ArrayList<Movement> movements) {
        dbHelper.removeAllMovements();
        for (Movement m : movements)
            addMovementDB(m);
    }

    public ArrayList<Movement> getAllMovements() {
        return new ArrayList<>(movements);
    }

}
