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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class ApproveSMP_LVAdapter extends ArrayAdapter<ApproveSMP_LVList> implements View.OnClickListener {

    private ArrayList<ApproveSMP_LVList> dataSet;
    Context mContext;
    String tipe;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCode;
        TextView txtName;
        ImageView imgprofil;
        ImageView imgtype;
    }

    public ApproveSMP_LVAdapter(ArrayList<ApproveSMP_LVList> data, Context context) {
        super(context, R.layout.list_item_appsmp, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        ApproveSMP_LVList approveSMP_dataList = (ApproveSMP_LVList) object;
        tipe = "";

        switch (v.getId()) {
            case R.id.image_type:

                if (approveSMP_dataList.getTipe().indexOf("H")!=-1) {
                    tipe = tipe + "Harga, ";
                }
                if (approveSMP_dataList.getTipe().indexOf("D")!=-1) {
                    tipe = tipe + "Diskon, ";
                }
                if (approveSMP_dataList.getTipe().indexOf("A")!=-1) {
                    tipe = tipe + "Add Cost, ";
                }
                if (approveSMP_dataList.getTipe().indexOf("B")!=-1) {
                    tipe = tipe + "Bonus Produk, ";
                }
                if (approveSMP_dataList.getTipe().indexOf("T")!=-1) {
                    tipe = tipe + "Bonus Tambahan, ";
                }

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
        ApproveSMP_LVList approveSMP_dataList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_appsmp, parent, false);
            viewHolder.txtCode = convertView.findViewById(R.id.code);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.imgprofil = convertView.findViewById(R.id.image_profil);
            viewHolder.imgtype = convertView.findViewById(R.id.image_type);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_buttom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        //get first letter of each String item
        String firstLetter = String.valueOf(approveSMP_dataList.getNama().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        // generate random color
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(firstLetter, color);

        viewHolder.txtCode.setText(approveSMP_dataList.getCode());
        viewHolder.txtName.setText(approveSMP_dataList.getNama());
        viewHolder.imgprofil.setImageDrawable(drawable);
        viewHolder.imgtype.setOnClickListener(this);
        viewHolder.imgtype.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}

