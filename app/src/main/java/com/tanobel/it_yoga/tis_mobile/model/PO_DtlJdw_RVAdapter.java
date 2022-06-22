package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tanobel.it_yoga.tis_mobile.PO_Fragment_DetailJdw;
import com.tanobel.it_yoga.tis_mobile.R;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;

import java.util.ArrayList;
import java.util.List;

public class PO_DtlJdw_RVAdapter extends RecyclerView.Adapter<PO_DtlJdw_RVAdapter.ViewHolder> {
    private List<PR_DtlJdw> kt = new ArrayList<>();
    private Typeface font;
    private PO_Fragment_DetailJdw mFragment;
    String tipe;

    public PO_DtlJdw_RVAdapter(PO_Fragment_DetailJdw mFragment, List<PR_DtlJdw> kt, String tipe) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.tipe = tipe;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_pr_detailjdw, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txt_nama_barang.setText(MainModule.getcharfromascii(kt.get(position).getBrgname()));
        viewHolder.txt_satuan.setText(kt.get(position).getSatcode());
        viewHolder.txt_qty.setText(kt.get(position).getQty());
        viewHolder.txt_tglkirim.setText(kt.get(position).getTglkirim());
        viewHolder.txt_ket.setText(kt.get(position).getKet());
        viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("ic_box", "drawable", mFragment.getContext().getPackageName()));

    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nama_barang, txt_satuan, txt_qty, txt_tglkirim, txt_ket;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            txt_nama_barang = view.findViewById(R.id.txt_nama_barang);
            txt_satuan = view.findViewById(R.id.txt_satuan);
            txt_qty = view.findViewById(R.id.txt_qty);
            txt_tglkirim = view.findViewById(R.id.txt_tglkirim);
            txt_ket = view.findViewById(R.id.txt_ket);

            imageView = view.findViewById(R.id.image_brg);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            imageView.setColorFilter(color);
        }

    }

}

