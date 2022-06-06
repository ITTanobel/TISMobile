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

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class CRM_SO_MasterLVAdapter extends ArrayAdapter<CRM_SO_MasterLVList> implements View.OnClickListener {

    private ArrayList<CRM_SO_MasterLVList> dataSet;
    Context mContext;
    String tipe;

    // View lookup cache
    private static class ViewHolder {
        TextView txtdocno;
        TextView txtcustkrm;
        TextView txtalamat;
        TextView txttglkirim;
        ImageView imgview;
    }

    public CRM_SO_MasterLVAdapter(ArrayList<CRM_SO_MasterLVList> data, Context context) {
        super(context, R.layout.list_item_so_view, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        CRM_SO_MasterLVList CRM_SO_Master_dataList = (CRM_SO_MasterLVList) object;
   
        switch (v.getId()) {
            case R.id.image_type:
               
                // converting String to StringBuilder
                StringBuilder builder = new StringBuilder(tipe);

                // removing first character
                builder.deleteCharAt(tipe.length()-2);

                Snackbar.make(v, builder.toString(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CRM_SO_MasterLVList CRM_SO_Master_dataList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_so_view, parent, false);
            viewHolder.txtdocno = convertView.findViewById(R.id.docno);
            viewHolder.txtcustkrm = convertView.findViewById(R.id.custkrm);
            viewHolder.txtalamat = convertView.findViewById(R.id.alamatkrm);
            viewHolder.txttglkirim = convertView.findViewById(R.id.tglkirim);
            viewHolder.imgview = convertView.findViewById(R.id.image_profil);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_buttom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();

        viewHolder.txtdocno.setText(CRM_SO_Master_dataList.getDocno());
        viewHolder.txtcustkrm.setText(CRM_SO_Master_dataList.getNama());
        viewHolder.txtalamat.setText(CRM_SO_Master_dataList.getAlamat());
        viewHolder.txttglkirim.setText(CRM_SO_Master_dataList.getTglkirim());
        viewHolder.imgview.setColorFilter(color);

        // Return the completed view to render on screen
        return convertView;
    }
}

