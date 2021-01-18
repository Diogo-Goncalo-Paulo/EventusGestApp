package com.eventusgest;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eventusgest.listeners.AreasLeftListener;
import com.eventusgest.modelo.Movement;
import com.eventusgest.modelo.SingletonGestor;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class ViewMovementActivity extends AppCompatActivity implements AreasLeftListener {

    public static final String ID = "ID";
    private Movement movement;
    private Movement movementUpdated;

    private TextView tvUCID, tvAccessPoint, tvAreaFrom,tvTimeMov,tvPorteiro;
    private Spinner spinnerAreaTo;
    private AppCompatImageButton btnBlock;
    private ImageView profilePicture;
    private String mUrlAPI = "http://192.168.1.107:8080";
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(ID, -1);
        movement = SingletonGestor.getInstance(getApplicationContext()).getMovement(id);

        SingletonGestor.getInstance(this).setAreasLeftListener(this);


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


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add(movement.getNameAreaTo());

        spinnerAreaTo.setSelection(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAreaTo.setAdapter(adapter);
    }

    @Override
    public void onGetAreasLeft(JSONArray areas) {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add(movement.getNameAreaTo());

        spinnerAreaTo.setSelection(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAreaTo.setAdapter(adapter);
    }
}