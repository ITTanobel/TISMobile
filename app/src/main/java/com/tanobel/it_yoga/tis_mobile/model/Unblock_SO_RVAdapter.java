package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.R;
import com.tanobel.it_yoga.tis_mobile.Unblock_SO_Fragment_Detail;

import java.util.ArrayList;
import java.util.List;

public class Unblock_SO_RVAdapter extends RecyclerView.Adapter<Unblock_SO_RVAdapter.ViewHolder> {
    private List<Unblock_SO_Dtl> kt = new ArrayList<>();
    private Typeface font;
    private Unblock_SO_Fragment_Detail mFragment;

    public Unblock_SO_RVAdapter(Unblock_SO_Fragment_Detail mFragment, List<Unblock_SO_Dtl> kt) {
       this.mFragment = mFragment;
       this.kt = kt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_so_detail, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txtbrg.setText(kt.get(position).getBrgname());
        viewHolder.txttipe.setText(kt.get(position).getTipe());
        viewHolder.txtqty.setText(kt.get(position).getQty());
        viewHolder.txtsatuan.setText(kt.get(position).getSatcode());
        viewHolder.txtharga.setText(kt.get(position).getHarga());
        viewHolder.txtbrutto.setText(kt.get(position).getBrutto());
        viewHolder.txtdisc.setText(kt.get(position).getDisc());
        viewHolder.txtdiscval.setText(kt.get(position).getDiscval());
        viewHolder.txtnetto.setText(kt.get(position).getNetto());
    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtbrg, txttipe, txtqty, txtsatuan, txtharga, txtbrutto, txtdisc, txtdiscval, txtnetto;

        public ViewHolder(View view) {
            super(view);

            txtbrg = view.findViewById(R.id.txt_so_brg);
            txttipe = view.findViewById(R.id.txt_so_tipe);
            txtqty = view.findViewById(R.id.txt_so_qty);
            txtsatuan = view.findViewById(R.id.txt_so_satuan);
            txtharga = view.findViewById(R.id.txt_so_hrg);
            txtbrutto = view.findViewById(R.id.txt_so_brutto);
            txtdisc = view.findViewById(R.id.txt_so_disc);
            txtdiscval = view.findViewById(R.id.txt_so_discval);
            txtnetto = view.findViewById(R.id.txt_so_netto);
        }
    }
}

