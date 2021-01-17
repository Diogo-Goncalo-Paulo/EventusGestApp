package com.eventusgest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.Movement;
import com.eventusgest.modelo.SingletonGestor;
import com.squareup.picasso.Picasso;

public class ViewMovementActivity extends AppCompatActivity {

    public static final String ID = "ID";
    private Movement movement;
    private Movement movementUpdated;
    private TextView tvUCID, tvAccessPoint, tvAreaFrom,tvTimeMov,tvPorteiro;
    private Spinner spinnerAreaTo;
    private AppCompatImageButton btnBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(ID, -1);
        movement = SingletonGestor.getInstance(getApplicationContext()).getMovement(id);

        tvUCID = findViewById(R.id.tvUCID);
        tvAccessPoint = findViewById(R.id.tvAccessPoint);
        tvAreaFrom = findViewById(R.id.tvAreaFrom);
        tvTimeMov = findViewById(R.id.tvTimeMov);
        tvPorteiro = findViewById(R.id.tvPorteiro);
        spinnerAreaTo = findViewById(R.id.spinnerAreaTo);

        carregarInfo();
    }

    private void carregarInfo () {
        tvUCID.setText(movement.getNameCredential());
        tvAccessPoint.setText(movement.getNameAccessPoint());
        tvAreaFrom.setText(movement.getNameAreaFrom());
        tvTimeMov.setText(movement.getTime());
        tvPorteiro.setText(movement.getNameUser());

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