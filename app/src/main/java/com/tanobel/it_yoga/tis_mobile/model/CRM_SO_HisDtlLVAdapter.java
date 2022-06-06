package com.tanobel.it_yoga.tis_mobile.model;

import android.content.Context;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tanobel.it_yoga.tis_mobile.R;

import java.util.ArrayList;

/**
 * Created by IT_Yoga on 24/01/2019.
 */

public class CRM_SO_HisDtlLVAdapter extends ArrayAdapter<CRM_SO_HisDtlLVList> implements View.OnClickListener {

    private ArrayList<CRM_SO_HisDtlLVList> dataSet;
    Context mContext;
    String tipe;

    // View lookup cache
    private static class ViewHolder {
        TextView txtbrgname, txtqty, txtsatuan, txtharga, txtdiskon, txtnetto, txttglkirim;
        TextView lblharga, lbldiskon, lblnetto, lbltglkrm;
        TextView sprharga, sprdiskon, sprnetto, sprtglkrm;
        ImageView imgview;
    }

    public CRM_SO_HisDtlLVAdapter(ArrayList<CRM_SO_HisDtlLVList> data, Context context, String tipe) {
        super(context, R.layout.list_item_crmhisdtl, data);
        this.dataSet = data;
        this.mContext = context;
        this.tipe = tipe;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        CRM_SO_HisDtlLVList CRM_SO_Master_dataList = (CRM_SO_HisDtlLVList) object;

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
        CRM_SO_HisDtlLVList CRM_SO_Master_dataList = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_crmhisdtl, parent, false);
            viewHolder.txtbrgname = convertView.findViewById(R.id.brgname);
            viewHolder.txtqty = convertView.findViewById(R.id.qty);
            viewHolder.txtsatuan = convertView.findViewById(R.id.satuan);
            viewHolder.txtharga = convertView.findViewById(R.id.harga);
            viewHolder.txtdiskon = convertView.findViewById(R.id.diskon);
            viewHolder.txtnetto = convertView.findViewById(R.id.netto);
            viewHolder.txttglkirim = convertView.findViewById(R.id.tglkirim);
            viewHolder.imgview = convertView.findViewById(R.id.image_profil);

            viewHolder.lblharga = convertView.findViewById(R.id.lblharga);
            viewHolder.lbldiskon = convertView.findViewById(R.id.lbldiskon);
            viewHolder.lblnetto = convertView.findViewById(R.id.lblnetto);
            viewHolder.lbltglkrm = convertView.findViewById(R.id.lbltglkrm);

            viewHolder.sprharga = convertView.findViewById(R.id.sprharga);
            viewHolder.sprdiskon = convertView.findViewById(R.id.sprdiskon);
            viewHolder.sprnetto = convertView.findViewById(R.id.sprnetto);
            viewHolder.sprtglkrm = convertView.findViewById(R.id.sprtglkrm);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getRandomColor();

        viewHolder.txtbrgname.setText(CRM_SO_Master_dataList.getBrgname());
        viewHolder.txtqty.setText(String.valueOf(CRM_SO_Master_dataList.getQty()));
        viewHolder.txtsatuan.setText(CRM_SO_Master_dataList.getSatuan());
        viewHolder.txtharga.setText(CRM_SO_Master_dataList.getHarga());
        viewHolder.txtdiskon.setText(CRM_SO_Master_dataList.getDiskon());
        viewHolder.txtnetto.setText(CRM_SO_Master_dataList.getNetto());
        viewHolder.txttglkirim.setText(CRM_SO_Master_dataList.getTglkrm());
        viewHolder.imgview.setColorFilter(color);

        switch (tipe) {
            case "PSO":
                viewHolder.txtharga.setVisibility(TextView.GONE);
                viewHolder.txtdiskon.setVisibility(TextView.GONE);
                viewHolder.txtnetto.setVisibility(TextView.GONE);

                viewHolder.lblharga.setVisibility(TextView.GONE);
                viewHolder.lbldiskon.setVisibility(TextView.GONE);
                viewHolder.lblnetto.setVisibility(TextView.GONE);

                viewHolder.sprharga.setVisibility(TextView.GONE);
                viewHolder.sprdiskon.setVisibility(TextView.GONE);
                viewHolder.sprnetto.setVisibility(TextView.GONE);
                break;
            case "DO":
                viewHolder.txtnetto.setVisibility(TextView.GONE);
                viewHolder.lblnetto.setVisibility(TextView.GONE);
                viewHolder.sprnetto.setVisibility(TextView.GONE);

                viewHolder.lblharga.setText("Qty Kirim");
                viewHolder.lbldiskon.setText("Qty Outstd.");
                break;
            case "SJ":
                viewHolder.txtharga.setVisibility(TextView.GONE);
                viewHolder.txtdiskon.setVisibility(TextView.GONE);
                viewHolder.txtnetto.setVisibility(TextView.GONE);
                viewHolder.txttglkirim.setVisibility(TextView.GONE);

                viewHolder.lblharga.setVisibility(TextView.GONE);
                viewHolder.lbldiskon.setVisibility(TextView.GONE);
                viewHolder.lblnetto.setVisibility(TextView.GONE);
                viewHolder.lbltglkrm.setVisibility(TextView.GONE);

                viewHolder.sprharga.setVisibility(TextView.GONE);
                viewHolder.sprdiskon.setVisibility(TextView.GONE);
                viewHolder.sprnetto.setVisibility(TextView.GONE);
                viewHolder.sprtglkrm.setVisibility(TextView.GONE);
                break;
            case "BILL":
                viewHolder.txttglkirim.setVisibility(TextView.GONE);
                viewHolder.lbltglkrm.setVisibility(TextView.GONE);
                viewHolder.sprtglkrm.setVisibility(TextView.GONE);
                break;
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
