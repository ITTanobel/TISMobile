package com.tanobel.it_yoga.tis_mobile.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.GoodsReceipt_View;
import com.tanobel.it_yoga.tis_mobile.Purchase_View;
import com.tanobel.it_yoga.tis_mobile.Unblock_SO;
import com.tanobel.it_yoga.tis_mobile.R;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.Locale;

public class View_LVAdapter extends BaseAdapter implements View.OnClickListener {

    LayoutInflater inflater;
    Context mContext;
    String tipe;

    // View lookup cache
    private static class ViewHolder {
        TextView txtDocno;
        TextView txtName;
        TextView txtCode;
        ImageView imgprofil;
        ImageView imgtype;
    }

    public View_LVAdapter(Context context, String menu_type) {
        this.mContext = context;
        this.tipe = menu_type;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onClick(View v) {
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View_LVList unblockSO_dataList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            //LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_view, parent, false);
            viewHolder.txtDocno = convertView.findViewById(R.id.txt_docno);
            viewHolder.txtName = convertView.findViewById(R.id.txt_custname);
            viewHolder.txtCode = convertView.findViewById(R.id.txt_custcode);
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
        String firstLetter;
        if (tipe.equals("AppvPR")) {
            firstLetter = String.valueOf(unblockSO_dataList.getCode().charAt(0));
        } else {
            firstLetter = String.valueOf(unblockSO_dataList.getNama().charAt(0));
        }

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT

        // generate random color
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(100)
                .height(100)
                .endConfig()
                .buildRound(firstLetter, color);

        viewHolder.txtDocno.setText(unblockSO_dataList.getDocno());
        viewHolder.txtName.setText(unblockSO_dataList.getNama());
        viewHolder.txtCode.setText(unblockSO_dataList.getCode());
        viewHolder.imgprofil.setImageDrawable(drawable);
        viewHolder.imgtype.setOnClickListener(this);
        viewHolder.imgtype.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        if (tipe.equals("UBCSO")) {
            Unblock_SO.dataList.clear();
            if (charText.length() == 0) {
                Unblock_SO.dataList.addAll(Unblock_SO.dataALL);
            } else {
                for (View_LVList wp : Unblock_SO.dataALL) {
                    if (wp.getDocno().toLowerCase(Locale.getDefault()).contains(charText)) {
                        Unblock_SO.dataList.add(wp);
                    }
                }
            }
        }else if(tipe.equals("APV04") || tipe.equals("APV05")){
            GoodsReceipt_View.dataList.clear();
            if (charText.length() == 0) {
                GoodsReceipt_View.dataList.addAll(GoodsReceipt_View.dataALL);
            } else {
                for (View_LVList wp : GoodsReceipt_View.dataALL) {
                    if (wp.getDocno().toLowerCase(Locale.getDefault()).contains(charText)) {
                        GoodsReceipt_View.dataList.add(wp);
                    }
                }
            }
        } else {
            Purchase_View.dataList.clear();
            if (charText.length() == 0) {
                Purchase_View.dataList.addAll(Purchase_View.dataALL);
            } else {
                for (View_LVList wp : Purchase_View.dataALL) {
                    if (wp.getDocno().toLowerCase(Locale.getDefault()).contains(charText)) {
                        Purchase_View.dataList.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (tipe.equals("UBCSO")) {
            return Unblock_SO.dataList.size();
        } else if (tipe.equals("APV04") || tipe.equals("APV05")){
            return GoodsReceipt_View.dataList.size();
        } else {
            return Purchase_View.dataList.size();
        }
    }

    @Override
    public View_LVList getItem(int position) {
        if (tipe.equals("UBCSO")) {
             return Unblock_SO.dataList.get(position);
        } else if (tipe.equals("APV04") || tipe.equals("APV05")){
            return GoodsReceipt_View.dataList.get(position);
        } else {
            return Purchase_View.dataList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

