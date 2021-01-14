package com.eventusgest.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eventusgest.R;
import com.eventusgest.modelo.Credential;

import java.util.ArrayList;

public class CredentialListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Credential> credentials;

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
        private TextView tvUCID, tvAccessPoint, tvEntityName;
        private ImageView ivCarrierImg;

        public ViewHolderList(View view) {
            ivCarrierImg = view.findViewById(R.id.ivImage);
            tvUCID = view.findViewById(R.id.tvUCID);
            tvAccessPoint = view.findViewById(R.id.tvAccessPoint);
            tvEntityName = view.findViewById(R.id.tvEntityName2);
        }

        public void update(Credential credential) {
            tvUCID.setText(credential.getCarrierName() == null ? credential.getUcid() : credential.getCarrierName());
            tvAccessPoint.setText(credential.getCarrierType());
            tvEntityName.setText(credential.getEntityName());
            Glide.with(context)
                    .load(credential.getCarrierPhoto())
                    .placeholder(R.drawable.defaultuser)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivCarrierImg);
        }
    }
}
