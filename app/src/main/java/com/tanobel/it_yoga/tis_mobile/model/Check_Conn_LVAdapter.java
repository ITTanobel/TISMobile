package com.tanobel.it_yoga.tis_mobile.model;

import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.R;

import java.util.ArrayList;

public class Check_Conn_LVAdapter extends ArrayAdapter<Check_Conn_LVList> implements View.OnClickListener{
    private ArrayList<Check_Conn_LVList> dataSet;
    Context mContext;
    String tipe;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDesc;
        TextView txtIP;
        TextView txtStatus;
        ImageView imgtype;
    }

    public Check_Conn_LVAdapter(ArrayList<Check_Conn_LVList> data, Context context) {
        super(context, R.layout.list_item_check_conn, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Check_Conn_LVList check_conn_datalist = (Check_Conn_LVList) object;
        tipe = "";

        switch (v.getId()) {
            case R.id.image_type:

                // converting String to StringBuilder
                StringBuilder builder = new StringBuilder(tipe);

                // removing first character
                builder.deleteCharAt(tipe.length()-2);

                Snackbar.make(v, "Tipe : " + builder.toString(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Check_Conn_LVList approveSMP_dataList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        Check_Conn_LVAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new Check_Conn_LVAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_check_conn, parent, false);
            viewHolder.txtDesc = convertView.findViewById(R.id.txt_desc);
            viewHolder.txtIP = convertView.findViewById(R.id.txt_ip);
            viewHolder.txtStatus = convertView.findViewById(R.id.txt_status);
            viewHolder.imgtype = convertView.findViewById(R.id.image_type);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Check_Conn_LVAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_buttom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtDesc.setText(approveSMP_dataList.getDesc());
        viewHolder.txtIP.setText(approveSMP_dataList.getIp());
        viewHolder.txtStatus.setText(approveSMP_dataList.getStatus());
        viewHolder.imgtype.setOnClickListener(this);
        viewHolder.imgtype.setTag(position);

        if (approveSMP_dataList.getStatus().equals("Connected")) {
            viewHolder.imgtype.setImageDrawable(mContext.getResources().getDrawable(R.drawable.connect));
        } else {
            viewHolder.imgtype.setImageDrawable(mContext.getResources().getDrawable(R.drawable.disconnect));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
