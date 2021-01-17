package com.eventusgest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.SingletonGestor;
import com.squareup.picasso.Picasso;

public class ViewMovementActivity extends AppCompatActivity {

    public static final String ID = "ID";
    private Credential credential;
    private Credential credentialUpdated;
    private TextView tvNomeCarregador, tvTipoCarregador, tvInfo;
    private Button btnFlag;
    private AppCompatImageButton btnBlock;
    private ImageView profilePicture;
    private String mUrlAPI = "http://192.168.1.68:8080";
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(ID, -1);
        credential = SingletonGestor.getInstance(getApplicationContext()).getCredential(id);

        tvNomeCarregador = findViewById(R.id.tvNomeCarregador);
        tvTipoCarregador = findViewById(R.id.tvTipoCarregador);
        tvInfo = findViewById(R.id.tvInfo1);
        btnFlag = findViewById(R.id.btn_flagged);
        btnBlock = findViewById(R.id.btn_blocked);
        profilePicture = findViewById(R.id.profilePicture);

        carregarInfo();
    }

    private void carregarInfo () {
        tvNomeCarregador.setText(credential.getCarrierName() == null ? credential.getUcid() : credential.getCarrierName());
        tvTipoCarregador.setText(credential.getCarrierType() == null ? "Sem carregador." : credential.getCarrierType());
        tvInfo.setText(credential.getCarrierInfo() == null ? "Sem info." : credential.getCarrierInfo());

        btnFlag.setText(String.valueOf(credential.getFlagged()));

        if(credential.getBlocked() > 0) {
            btnFlag.setEnabled(false);
            btnBlock.setEnabled(false);
        }

        if(credential.getCarrierType() != null && !credential.getCarrierPhoto().equals("null")) {
            Picasso.get()
                    .load(mUrlAPI + credential.getCarrierPhoto())
                    .into(profilePicture);
        } else if (credential.getCarrierType() != null && credential.getCarrierPhoto().equals("null")) {
            Picasso.get()
                    .load(R.drawable.defaultuser)
                    .into(profilePicture);
        } else {
            Picasso.get()
                    .load(mUrlAPI + credential.getQrCode())
                    .into(profilePicture);
        }
    }

}