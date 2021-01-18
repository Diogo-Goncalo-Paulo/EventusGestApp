package com.eventusgest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eventusgest.adaptadores.CredentialListAdapter;
import com.eventusgest.adaptadores.MovementListAdapter;
import com.eventusgest.listeners.CredentialFlagBlockListener;
import com.eventusgest.listeners.MovementListener;
import com.eventusgest.modelo.Credential;
import com.eventusgest.modelo.Movement;
import com.eventusgest.modelo.SingletonGestor;
import com.eventusgest.utils.Utility;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class ViewCredentialActivity extends AppCompatActivity implements CredentialFlagBlockListener {

    public static final String ID = "ID";
    private Credential credential;
    private ArrayList<Movement> movementList;
    private TextView tvNomeCarregador, tvTipoCarregador, tvInfo, tvNoMovements;
    private ListView lvMovementList;
    private static final int VER_MOVIMENTO = 1;
    private Button btnFlag;
    private AppCompatImageButton btnBlock;
    private ImageView profilePicture;
    private String mUrlAPI = Utility.APIpath;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credential);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int idd = getIntent().getIntExtra(ID, -1);
        credential = SingletonGestor.getInstance(getApplicationContext()).getCredential(idd);

        setTitle(credential.getUcid());

        tvNoMovements = findViewById(R.id.tvNoMovements);
        lvMovementList = findViewById(R.id.lvMovementList1);

        movementList = SingletonGestor.getInstance(getApplicationContext()).getCredentialMovements(credential.getId());

        if (!movementList.isEmpty()) {
            lvMovementList.setAdapter(new MovementListAdapter(getApplicationContext(), movementList));
        } else {
            tvNoMovements.setVisibility(View.VISIBLE);
        }

        lvMovementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewMovementActivity.class);
                intent.putExtra(ViewMovementActivity.ID, (int) id);
                //startActivity(intent);
                startActivityForResult(intent, VER_MOVIMENTO);
            }
        });

        tvNomeCarregador = findViewById(R.id.tvNomeCarregador);
        tvTipoCarregador = findViewById(R.id.tvTipoCarregador);
        tvInfo = findViewById(R.id.tvInfo1);
        btnFlag = findViewById(R.id.btn_flagged);
        btnBlock = findViewById(R.id.btn_blocked);
        profilePicture = findViewById(R.id.profilePicture);

        SingletonGestor.getInstance(getApplicationContext()).setCredentialFlagBlockListener(this);
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

    public void onClickFlag(View view) {
        SingletonGestor.getInstance(getApplicationContext()).flagCredential(getApplicationContext(), credential.getId());
    }

    public void onClickBlock(View view) {
        SingletonGestor.getInstance(getApplicationContext()).blockCredential(getApplicationContext(), credential.getId());
    }

    @Override
    public void onFlagCredential(Credential credential) {
        btnFlag.setText(""+credential.getFlagged());
    }

    @Override
    public void onBlockCredential() {
        btnFlag.setEnabled(false);
        btnBlock.setEnabled(false);
    }
}