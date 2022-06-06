package com.tanobel.it_yoga.tis_mobile.model;

import android.content.Context;
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

/**
 * Created by HP on 5/11/2016.
 */

public class Menu_GVAdapter extends ArrayAdapter<Menu_GVList> {
    private ArrayList<Menu_GVList> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView textView;
        TextView textViewbagde;
        ImageView imageView;
    }

    public Menu_GVAdapter(ArrayList<Menu_GVList> data, Context context) {
        super(context, R.layout.gridview_custom_layout, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Menu_GVList menu_dataList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.gridview_custom_layout, parent, false);
            viewHolder.textView = convertView.findViewById(R.id.gridview_text);
            viewHolder.textViewbagde = convertView.findViewById(R.id.txtbadge);
            viewHolder.imageView = convertView.findViewById(R.id.gridview_image);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position % 2 == 0) ? R.anim.fab_slide_in_from_left : R.anim.fab_slide_in_from_right);
        result.startAnimation(animation);

        viewHolder.textView.setText(menu_dataList.getNama());
        viewHolder.textView.setTextColor(mContext.getResources().getColor(mContext.getResources().getIdentifier(menu_dataList.getBG().trim(), "color", mContext.getPackageName())));

        viewHolder.imageView.setImageResource(mContext.getResources().getIdentifier(menu_dataList.getGambar().trim(), "drawable", mContext.getPackageName()));
        viewHolder.imageView.setBackgroundResource(mContext.getResources().getIdentifier(menu_dataList.getBG().trim(), "drawable", mContext.getPackageName()));

        if (menu_dataList.getCode().indexOf("APP01") != -1) {
            if (menu_dataList.getNotifcount() > 0) {
                viewHolder.textViewbagde.setVisibility(TextView.VISIBLE);
                viewHolder.textViewbagde.setText(String.valueOf(menu_dataList.getNotifcount()));

                Animation anim_notif = AnimationUtils.loadAnimation(mContext,R.anim.notif_anim);
                viewHolder.textViewbagde.setAnimation(anim_notif);
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }

}