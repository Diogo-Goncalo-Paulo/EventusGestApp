package com.eventusgest;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eventusgest.listeners.CreateMovementListener;
import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.Utility;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class createMovementActivity extends AppCompatActivity implements CreateMovementListener {
    private LinearLayout credLayout;
    private EditText etUCID;
    private TextView tvUCID, tvInfo1, tvFlag, tvBlock, tvEntityName, tvEntityType, tvInfo, tvAreaFrom, tvAreaTo;
    private ImageView credImage;

    private Credential credential;
    private String mUrlAPI = "http://192.168.1.68:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_movement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        credLayout = findViewById(R.id.credentialLayout);
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
        }
    }
}