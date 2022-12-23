package com.tanobel.it_yoga.tis_mobile.model;
/**
 * Created by Galeh on 08/12/2022.
 */

import android.app.ProgressDialog;
import android.graphics.Typeface;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tanobel.it_yoga.tis_mobile.GR_Fragment_Detail;
import com.tanobel.it_yoga.tis_mobile.R;

import java.util.List;
public class GR_Dtl_RVAdapter extends RecyclerView.Adapter<GR_Dtl_RVAdapter.ViewHolder>  {
    private List<GR_Dtl> kt;
    private Typeface font;
    private GR_Fragment_Detail mFragment;
    String tipe;
    ProgressDialog pDialog;

    Db_PO helper;

    public GR_Dtl_RVAdapter(GR_Fragment_Detail mFragment, List<GR_Dtl> kt, String tipe) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.tipe = tipe;
    }

    @Override
    public GR_Dtl_RVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        helper = new Db_PO(mFragment.getActivity());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_gr_detail, viewGroup, false);
        return new GR_Dtl_RVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GR_Dtl_RVAdapter.ViewHolder viewHolder, int position) {
        viewHolder.txt_nama_barang.setText(kt.get(position).getNama_barang());
        viewHolder.txt_kode_barang.setText(kt.get(position).getKode_barang());
        viewHolder.txt_no_po.setText(kt.get(position).getNo_po());
        viewHolder.txt_satuan.setText(kt.get(position).getSatuan());
        viewHolder.txt_jumlah.setText(kt.get(position).getJumlah());

        viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("ic_box", "drawable", mFragment.getContext().getPackageName()));

    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_nama_barang,txt_kode_barang,txt_no_po,txt_satuan,txt_jumlah;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            txt_nama_barang = view.findViewById(R.id.txt_nama_barang);
            txt_kode_barang = view.findViewById(R.id.txt_kode_barang);
            txt_no_po = view.findViewById(R.id.txt_no_po);
            txt_satuan = view.findViewById(R.id.txt_satuan);
            txt_jumlah = view.findViewById(R.id.txt_jumlah);

            imageView = view.findViewById(R.id.image_brg);

            CardView cardView = view.findViewById(R.id.cv);
            cardView.setOnClickListener(this);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            imageView.setColorFilter(color);
        }

        @Override
        public void onClick(View view) {
            Log.i("Click Barang","OK");
        }

    }
}
