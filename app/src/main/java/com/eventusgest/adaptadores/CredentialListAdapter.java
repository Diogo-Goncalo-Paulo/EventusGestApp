package com.eventusgest.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventusgest.MainActivity;
import com.eventusgest.R;
import com.eventusgest.modelo.Credential;
import com.eventusgest.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CredentialListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Credential> credentials;
    private String mUrlAPI;

    public CredentialListAdapter(Context context, ArrayList<Credential> credentials) {
        this.context = context;
        this.credentials = credentials;
    }

    @Override
    public int getCount() {
        return credentials.size();
    }

    @Override
    public Object getItem(int position) {
        return credentials.get(position);
    }

    @Override
    public long getItemId(int position) {
        return credentials.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mUrlAPI = Utility.APIpath;

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.credential_list_item,null);

        ViewHolderList viewHolderList = (ViewHolderList) convertView.getTag();
        if (viewHolderList == null) {
            viewHolderList = new ViewHolderList(convertView);
            convertView.setTag(viewHolderList);
        }

        viewHolderList.update(credentials.get(position));

        return convertView;
    }

    private class ViewHolderList {
        private TextView tvUCID, tvCarrierType, tvEntityName;
        private ImageView ivCarrierImg;

        public ViewHolderList(View view) {
            ivCarrierImg = view.findViewById(R.id.ivImage);
            tvUCID = view.findViewById(R.id.tvUCID);
            tvCarrierType = view.findViewById(R.id.tvCarrierType1);
            tvEntityName = view.findViewById(R.id.tvEntityName2);
        }

        public void update(Credential credential) {
            tvUCID.setText(credential.getCarrierName() == null ? credential.getUcid() : credential.getCarrierName());
            tvCarrierType.setText(credential.getCarrierType() == null ? "Sem carregador." : credential.getCarrierType());
            tvEntityName.setText(credential.getEntityName());

            if(credential.getCarrierType() != null && !credential.getCarrierPhoto().equals("null")) {
                Picasso.get()
                        .load(mUrlAPI + credential.getCarrierPhoto())
                        .into(ivCarrierImg);
            } else if (credential.getCarrierType() != null && credential.getCarrierPhoto().equals("null")) {
                Picasso.get()
                        .load(R.drawable.defaultuser)
                        .into(ivCarrierImg);
            } else {
                Picasso.get()
                        .load(mUrlAPI + credential.getQrCode())
                        .into(ivCarrierImg);
            }
        }
    }
}
