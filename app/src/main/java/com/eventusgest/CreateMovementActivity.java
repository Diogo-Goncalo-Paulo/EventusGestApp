package com.eventusgest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eventusgest.listeners.CreateMovementListener;
import com.eventusgest.listeners.CredentialFlagBlockListener;
import com.eventusgest.modelo.AccessPoint;
import com.eventusgest.modelo.Area;
import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.AccessPointJsonParser;
import com.eventusgest.utils.AreaJsonParser;
import com.eventusgest.utils.CredentialJsonParser;
import com.eventusgest.utils.Utility;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class CreateMovementActivity extends AppCompatActivity implements CreateMovementListener, CredentialFlagBlockListener {
    public static final String USER = "USER_PREF_SHARED";
    public static final String ACCESS_POINT = "ACCESS_POINT";
    public static final String USER_ID = "USER_ID";

    private LinearLayout credLayout,alertLayout;
    private EditText etUCID;
    private TextView tvUCID, tvInfo1, tvFlag, tvBlock, tvEntityName, tvEntityType, tvInfo, tvAreaFrom, tvAreaTo, tvAlert;
    private Button btnMovement,btnFlag;
    private ImageView credImage;
    private Area areaTo;
    private Credential credential;
    private int[] getAccessibleAreas;
    private String mUrlAPI = Utility.APIpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_movement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        credLayout = findViewById(R.id.credentialLayout);
        alertLayout = findViewById(R.id.alertLayout);
        btnMovement = findViewById(R.id.btn_movement);
        btnFlag = findViewById(R.id.btn_flag);
        etUCID = findViewById(R.id.ucidInput);
        tvUCID = findViewById(R.id.tvUCID);
        tvInfo1 = findViewById(R.id.tvInfo1);
        tvFlag = findViewById(R.id.tvFlag);
        tvBlock = findViewById(R.id.tvBlock);
        tvEntityName = findViewById(R.id.tvEntityName);
        tvEntityType = findViewById(R.id.tvEntityType);
        tvInfo = findViewById(R.id.tvInfo);
        tvAreaFrom = findViewById(R.id.tvAreaFrom);
        tvAreaTo = findViewById(R.id.tvAreaTo);
        tvAlert = findViewById(R.id.tvAlert);
        credImage = findViewById(R.id.credImage);

        credLayout.setAlpha(0.0f);

        SingletonGestor.getInstance(getApplicationContext()).setCreateMovementListener(this);


    }

    public void onClickSearchUCID(View view) {
        String ucid = etUCID.getText().toString();

        credLayout.animate().alpha(0.0f);

        if (!Utility.hasInternetConnection(this)) {
            Toast.makeText(this, R.string.noInternet, Toast.LENGTH_SHORT).show();
        } else {
            SingletonGestor.getInstance(getApplicationContext()).getCredentialAPI(getApplicationContext(), ucid);
        }
    }

    @Override
    public void onSearchUCID(String UCID) {
        credential = SingletonGestor.getInstance(getApplicationContext()).getCredentialUCID(UCID);

        if (credential != null) {
            credLayout.animate().alpha(1.0f);
            tvUCID.setText(credential.getCarrierName() == null ? credential.getUcid() : credential.getCarrierName());
            tvInfo1.setText(credential.getCarrierType() == null ? "Sem carregador" : credential.getCarrierType());
            tvFlag.setText(String.valueOf(credential.getFlagged()));
            tvBlock.setText(credential.getBlocked() > 0 ? "Sim" : "Não");
            tvEntityName.setText(credential.getEntityName());
            tvEntityType.setText(credential.getEntityTypeName());
            tvInfo.setText(credential.getCarrierInfo() == null ? "Sem informação" : credential.getCarrierInfo());
            tvAreaFrom.setText(credential.getCurrentAreaName());

            if(credential.getCarrierType() != null && !credential.getCarrierPhoto().equals("null")) {
                Picasso.get()
                        .load(mUrlAPI + credential.getCarrierPhoto())
                        .into(credImage);
            } else if (credential.getCarrierType() != null && credential.getCarrierPhoto().equals("null")) {
                Picasso.get()
                        .load(R.drawable.defaultuser)
                        .into(credImage);
            } else {
                Picasso.get()
                        .load(mUrlAPI + credential.getQrCode())
                        .into(credImage);
            }

            chooseAreas();
        }
    }

    @Override
    public void onGetAccessPoint(JSONObject accessPoint) {
        AccessPoint accessPoint1 = AccessPointJsonParser.parserJsonAccesspoint(accessPoint);
        int[] areas = accessPoint1.getAreas();
        System.out.println("Areas : "+ Arrays.toString(areas));
        System.out.println("CurrentArea : "+ credential.getIdCurrentArea());

        if(areas[0] != credential.getIdCurrentArea() && areas[1] != credential.getIdCurrentArea()){
            alertLayout.setVisibility(View.VISIBLE);
            tvAlert.setText(R.string.movimento_impossivel);

        }else{
            alertLayout.setVisibility(View.INVISIBLE);
            if(areas[0] == credential.getIdCurrentArea()){
                if(checkCredAccessAreas(areas[1])){
                    SingletonGestor.getInstance(this).getAreaAPI(this,areas[1]);
                }else{
                    alertLayout.setVisibility(View.VISIBLE);
                    tvAlert.setText(R.string.sem_acceso_area);
                }
            }else{
                if(checkCredAccessAreas(areas[0])){
                    SingletonGestor.getInstance(this).getAreaAPI(this,areas[0]);
                }else{
                    alertLayout.setVisibility(View.VISIBLE);
                    tvAlert.setText(R.string.sem_acceso_area);
                }
            }
        }


    }

    public boolean checkCredAccessAreas(int areaToId){
        boolean canAccess = false;
        for (int area:getAccessibleAreas) {
            if(areaToId == area){
                canAccess = true;
            }
        }
        return canAccess;
    }

    @Override
    public void onGetArea(JSONArray area) {
        areaTo = AreaJsonParser.parserJsonArea(area);
        System.out.println(areaTo);
        if(areaTo != null){
            tvAreaTo.setText(areaTo.getName());
            btnMovement.setEnabled(true);
        }

    }

    @Override
    public void onCreateMovement() {
        finish();
    }

    @Override
    public void onGetCredential(JSONArray credential) {
        getAccessibleAreas = CredentialJsonParser.parserJsonAccessibleArea(credential);
    }

    public void chooseAreas(){
        SharedPreferences sharedPref = this.getSharedPreferences(USER, Context.MODE_PRIVATE);

        SingletonGestor.getInstance(this).getAccessPointAPI(this,sharedPref.getInt(ACCESS_POINT,-1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                credential = null;
            } else {

                Toast.makeText(this, "Escaneado : " + result.getContents(), Toast.LENGTH_LONG).show();

                if (!Utility.hasInternetConnection(this)) {
                    Toast.makeText(this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                } else {
                    SingletonGestor.getInstance(getApplicationContext()).getCredentialAPI(getApplicationContext(), result.getContents());
                }
            }
        }
    }

    public void onClickScan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan QR code");
        integrator.setBeepEnabled(false);//Use this to set whether you need a beep sound when the QR code is scanned

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);

        integrator.initiateScan();
    }

    public void onClickFlag(View view) {
        SingletonGestor.getInstance(this).flagCredential(this, credential.getId());
    }

    @Override
    public void onFlagCredential(Credential credential) {
        tvFlag.setText(""+credential.getFlagged());
    }

    @Override
    public void onBlockCredential() {

    }

    public void onClickCreateMovement(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences(USER, Context.MODE_PRIVATE);

        String currentDate = new SimpleDateFormat("y-M-d H:m:s", Locale.getDefault()).format(new Date());
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("time", currentDate);
            jsonObject.put("idCredential", credential.getId());
            jsonObject.put("idAccessPoint", sharedPref.getInt(ACCESS_POINT,-1));
            jsonObject.put("idAreaFrom", credential.getIdCurrentArea());
            jsonObject.put("idAreaTo", areaTo.getId());
            jsonObject.put("idUser", sharedPref.getInt(USER_ID,-1));
        } catch (JSONException e) {
            e.printStackTrace();
            jsonObject = null;
        }
        System.out.println(jsonObject);
        SingletonGestor.getInstance(this).createMovementAPI(this,jsonObject);
    }
}