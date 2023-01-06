package com.tanobel.it_yoga.tis_mobile.model;
/**
 * Created by Galeh on 08/12/2022.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tanobel.it_yoga.tis_mobile.GR_Fragment_Detail;
import com.tanobel.it_yoga.tis_mobile.GR_Fragment_Inspection;
import com.tanobel.it_yoga.tis_mobile.R;
import com.tanobel.it_yoga.tis_mobile.View_image;

import java.util.List;
public class GR_Inspection extends RecyclerView.Adapter<GR_Inspection.ViewHolder> {
    private List<GR_Dtl> kt;
    private Typeface font;
    private GR_Fragment_Inspection mFragment;
    String tipe;
    ProgressDialog pDialog;
    ImageButton img_qc,img_incoming;
    String plant, docno, user;
    Db_GR helper;

    public GR_Inspection(GR_Fragment_Inspection mFragment, List<GR_Dtl> kt, String tipe,String plant,String docno,String user) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.tipe = tipe;
        this.plant = plant;
        this.docno = docno;
        this.user = user;

    }

    @Override
    public GR_Inspection.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        helper = new Db_GR(mFragment.getActivity());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_gr_inspection, viewGroup, false);
        return new GR_Inspection.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GR_Inspection.ViewHolder viewHolder, int position) {
        viewHolder.txt_nama_barang.setText(kt.get(position).getNama_barang());
        viewHolder.txt_kode_barang.setText(kt.get(position).getKode_barang());
        viewHolder.txt_no_po.setText(kt.get(position).getNo_po());

        viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("ic_inspection", "drawable", mFragment.getContext().getPackageName()));

    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_nama_barang,txt_kode_barang,txt_no_po;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            txt_nama_barang = view.findViewById(R.id.txt_nama_barang);
            txt_kode_barang = view.findViewById(R.id.txt_kode_barang);
            txt_no_po = view.findViewById(R.id.txt_no_po);
            img_qc = view.findViewById(R.id.btn_image_qc);
            img_incoming = view.findViewById(R.id.btn_image_incoming);

            imageView = view.findViewById(R.id.image_brg);

            CardView cardView = view.findViewById(R.id.cv);
            cardView.setOnClickListener(this);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            imageView.setColorFilter(color);
            img_qc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j = new Intent(view.getContext(),View_image.class);
                    j.putExtra("docno", docno);
                    j.putExtra("plant", plant);
                    j.putExtra("user", user);
                    j.putExtra("usage", "image_qc");
                    view.getContext().startActivity(j);
                }
            });
            img_incoming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j = new Intent(v.getContext(),View_image.class);
                    j.putExtra("docno", docno);
                    j.putExtra("plant", plant);
                    j.putExtra("user", user);
                    j.putExtra("usage", "image_incoming");
                    v.getContext().startActivity(j);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Log.i("Click Barang", String.valueOf(view.getId()));
            final GR_Dtl item = kt.get(getAdapterPosition() - 1);
            if (view.getId() == R.id.btn_image_qc) {

            } else if (view.getId() == R.id.btn_image_incoming) {

            }
        }

    }
}
