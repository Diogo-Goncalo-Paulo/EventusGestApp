package com.eventusgest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.Movement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovementJsonParser {
    public static ArrayList<Movement> parserJsonMovements(JSONArray response) {
        ArrayList<Movement> movements = new ArrayList<>();

        if (response != null) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject movement = (JSONObject) response.get(i);
                    int id = movement.getInt("id");
                    int time = movement.getInt("time");
                    int idCredential = movement.getInt("idCredential");
                    int idAccessPoint = movement.getInt("idAccessPoint");
                    int idAreaFrom = movement.getInt("idAreaFrom");
                    int idAreaTo = movement.getInt("idAreaTo");
                    int idUser = movement.getInt("idUser");
                    String nameAreaFrom = movement.getString("nameAreaFrom");
                    String nameAreaTo = movement.getString("nameAreaTo");;
                    String nameAccessPoint = movement.getString("nameAccessPoint");;
                    String nameUser = movement.getString("nameUser");;
                    String nameCredential = movement.getString("nameCredential");;



                    Movement m = new Movement(id, time, idCredential, idAccessPoint, idAreaFrom, idAreaTo, idUser,nameAreaFrom,nameAreaTo,nameAccessPoint,nameUser,nameCredential);
                    movements.add(m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return movements;
    }

    public static Movement parserJsonMovement(String response) {
        Movement auxMovement = null;

        try {
            JSONObject movement = new JSONObject(response);
            int id = movement.getInt("id");
            int time = movement.getInt("time");
            int idCredential = movement.getInt("idCredential");
            int idAccessPoint = movement.getInt("idAccessPoint");
            int idAreaFrom = movement.getInt("idAreaFrom");
            int idAreaTo = movement.getInt("idAreaTo");
            int idUser = movement.getInt("idUser");
            String nameAreaFrom = movement.getString("nameAreaFrom");
            String nameAreaTo = movement.getString("nameAreaTo");;
            String nameAccessPoint = movement.getString("nameAccessPoint");;
            String nameUser = movement.getString("nameUser");;
            String nameCredential = movement.getString("nameCredential");;

            auxMovement = new Movement(id, time, idCredential, idAccessPoint, idAreaFrom, idAreaTo, idUser,nameAreaFrom,nameAreaTo,nameAccessPoint,nameUser,nameCredential);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return auxMovement;
    }


    public static boolean isConnectionInternet(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cn.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }
}
