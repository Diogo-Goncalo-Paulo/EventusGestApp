package com.eventusgest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eventusgest.listeners.AreasLeftListener;
import com.eventusgest.listeners.ChangeMovementListener;
import com.eventusgest.listeners.MovementListener;
import com.eventusgest.modelo.Area;
import com.eventusgest.modelo.Movement;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.AreaJsonParser;
import com.eventusgest.utils.MovementJsonParser;
import com.eventusgest.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class ViewMovementActivity extends AppCompatActivity implements AreasLeftListener, ChangeMovementListener {

    public static final String ID = "ID";
    public static final String USER = "USER_PREF_SHARED";
    public static final String USER_ID = "USER_ID";
    public static final String USER_ROLE = "USER_ROLE";
    private Movement movement;
    private Movement movementUpdated;
    private ArrayList<Area> areas;

    private TextView tvUCID, tvAccessPoint, tvAreaFrom,tvTimeMov,tvPorteiro;
    private Spinner spinnerAreaTo;
    private AppCompatImageButton btnBlock;
    private ImageView profilePicture;
    private String mUrlAPI = "http://192.168.1.107:8080";
    private int flag = 0;
    private Button btnDeleteMov, btnSaveMov;

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
        btnDeleteMov = findViewById(R.id.btnDeleteMov);
        btnSaveMov = findViewById(R.id.btnSaveMov);
        carregarInfo();
    }

    private void carregarInfo () {
        SharedPreferences sharedPref = this.getSharedPreferences(USER_ID, Context.MODE_PRIVATE);

        tvUCID.setText(movement.getNameCredential());
        tvAccessPoint.setText(movement.getNameAccessPoint());
        tvAreaFrom.setText(movement.getNameAreaFrom());
        tvTimeMov.setText(movement.getTime());
        tvPorteiro.setText(movement.getNameUser());
        if(movement.getLastMovement() == 1){
            if(sharedPref.getInt(USER_ID,-1) == movement.getIdUser())
            btnDeleteMov.setEnabled(true);
            if(sharedPref.getString(USER_ROLE, USER_ROLE).equals("admin")){
                btnSaveMov.setEnabled(true);
            }

        }

        SingletonGestor.getInstance(this).getAreasLeft(this,movement.getIdAreaFrom());

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add(movement.getNameAreaTo());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAreaTo.setAdapter(adapter);
        spinnerAreaTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(areas != null){
                    movement.setIdAreaTo(areas.get(position).getId()
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onGetAreasLeft(JSONArray areas) {
        this.areas = AreaJsonParser.parserJsonAreas(areas);
        ArrayAdapter<Area> adapter = new ArrayAdapter<Area>(
                this, android.R.layout.simple_spinner_item, this.areas);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerAreaTo.setAdapter(adapter);

        for (int i = 0; i < this.areas.size(); i++){
            if(this.areas.get(i).getId() == movement.getIdAreaTo()){
                spinnerAreaTo.setSelection(i);
            }
        }
    }

    public void onClickDeleteMovement(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Remover Movimento");
        builder.setMessage("Tem a certeza que quer remover este movimento?");
        builder.setPositiveButton(getString(R.string.confirmar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonGestor.getInstance(builder.getContext()).deleteMovementAPI(builder.getContext(),movement.getId());
                    }
                });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void onClickUpdateMovement(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences(USER_ID, Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Atualizar Movimento");
        builder.setMessage("Tem a certeza que quer atualizar este movimento?");
        builder.setPositiveButton(getString(R.string.confirmar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonGestor.getInstance(builder.getContext()).updateMovementAPI(builder.getContext(),movement.toJSON(sharedPref.getInt(USER_ID,-1)),movement.getId());
                    }
                });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onUpdateMovement(JSONObject movement) {
        this.movement = MovementJsonParser.parserJsonMovement(movement.toString());
        carregarInfo();
    }

    @Override
    public void onDeleteMovement() {
        ViewMovementActivity.this.finish();
    }
}