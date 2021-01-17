package com.eventusgest.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eventusgest.R;
import com.eventusgest.modelo.Movement;

import java.util.ArrayList;

public class MovementListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Movement> movements;
    private String mUrlAPI = "http://192.168.1.68:8080";

    public MovementListAdapter(Context context, ArrayList<Movement> movements) {
        this.context = context;
        this.movements = movements;
    }

    @Override
    public int getCount() {
        return movements.size();
    }

    @Override
    public Object getItem(int position) {
        return movements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movements.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.movement_list_item,null);

        ViewHolderList viewHolderList = (ViewHolderList) convertView.getTag();
        if (viewHolderList == null) {
            viewHolderList = new ViewHolderList(convertView);
            convertView.setTag(viewHolderList);
        }

        viewHolderList.update(movements.get(position));

        return convertView;
    }

    private class ViewHolderList {
        private TextView tvUCID, tvAcessPoint, tvAreaFrom,tvAreaTo;

        public ViewHolderList(View view) {
            tvUCID = view.findViewById(R.id.tvUCID);
            tvAcessPoint = view.findViewById(R.id.tvAccessPoint);
            tvAreaFrom = view.findViewById(R.id.tvAreaFrom);
            tvAreaTo = view.findViewById(R.id.tvAreaTo);
        }

        public void update(Movement movement) {
            tvUCID.setText(movement.getNameCredential());
            tvAcessPoint.setText(movement.getNameAccessPoint());
            tvAreaFrom.setText(movement.getNameAreaFrom());
            tvAreaTo.setText(movement.getNameAreaTo());
        }
    }
}
