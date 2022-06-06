package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

import android.app.ProgressDialog;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.tanobel.it_yoga.tis_mobile.PO_Fragment_Detail;
import com.tanobel.it_yoga.tis_mobile.R;

import java.util.List;

public class PO_Dtl_RVAdapter extends RecyclerView.Adapter<PO_Dtl_RVAdapter.ViewHolder> {
    private List<PO_Dtl> kt;
    private Typeface font;
    private PO_Fragment_Detail mFragment;
    String tipe;
    ProgressDialog pDialog;

    Db_PO helper;

    public PO_Dtl_RVAdapter(PO_Fragment_Detail mFragment, List<PO_Dtl> kt, String tipe) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.tipe = tipe;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        helper = new Db_PO(mFragment.getActivity());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_po_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txt_nama_barang.setText(kt.get(position).getBrgname());
        viewHolder.txt_qty.setText(kt.get(position).getQty() + " " + kt.get(position).getSatcode());
        viewHolder.txt_harga.setText(kt.get(position).getHarga1());
        viewHolder.txt_discp.setText(kt.get(position).getDisc1());
        viewHolder.txt_brutto.setText(kt.get(position).getBrutto());
        viewHolder.txt_disc.setText(kt.get(position).getMdisc1());
        viewHolder.txt_dpp.setText(kt.get(position).getDpp());
        viewHolder.txt_ppn.setText(kt.get(position).getPpn());
        viewHolder.txt_netto.setText(kt.get(position).getNetto());
        viewHolder.txt_plant.setText(kt.get(position).getBranchto());
        viewHolder.txt_docref.setText(kt.get(position).getDocref());
        viewHolder.txt_order.setText(kt.get(position).getIocode());
        viewHolder.txt_spec.setText(kt.get(position).getBrgdesc());
        viewHolder.txt_status.setText(kt.get(position).getStatusdesc());
        viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("ic_box", "drawable", mFragment.getContext().getPackageName()));

    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_nama_barang,txt_qty,txt_harga,txt_discp,txt_brutto,txt_disc,txt_dpp,txt_ppn,txt_netto,txt_plant,txt_docref,txt_order,txt_spec,txt_status;
        TextView txt_btnapprove,txt_btncancel;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            txt_nama_barang = view.findViewById(R.id.txt_nama_barang);
            txt_qty = view.findViewById(R.id.txt_qty);
            txt_harga = view.findViewById(R.id.txt_harga);
            txt_discp = view.findViewById(R.id.txt_discp);
            txt_brutto = view.findViewById(R.id.txt_brutto);
            txt_disc = view.findViewById(R.id.txt_disc);
            txt_dpp = view.findViewById(R.id.txt_dpp);
            txt_ppn = view.findViewById(R.id.txt_ppn);
            txt_netto = view.findViewById(R.id.txt_netto);
            txt_plant = view.findViewById(R.id.txt_plant);
            txt_docref = view.findViewById(R.id.txt_docref);
            txt_order = view.findViewById(R.id.txt_order);
            txt_spec = view.findViewById(R.id.txt_spec);
            txt_status = view.findViewById(R.id.txt_status);
            txt_btnapprove = view.findViewById(R.id.txt_btnapprove);
            txt_btncancel = view.findViewById(R.id.txt_btncancel);

            imageView = view.findViewById(R.id.image_brg);

            CardView cardView = view.findViewById(R.id.cv);
            cardView.setOnClickListener(this);
            ImageButton approve = view.findViewById(R.id.btnapprove);
            ImageButton cancel = view.findViewById(R.id.btncancel);
            approve.setOnClickListener(this);
            cancel.setOnClickListener(this);

            if (tipe.equals("Appv2PO")) {
                approve.setVisibility(ImageButton.GONE);
                cancel.setVisibility(ImageButton.GONE);
                txt_btnapprove.setVisibility(TextView.GONE);
                txt_btncancel.setVisibility(TextView.GONE);
            }

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            imageView.setColorFilter(color);
        }

        @Override
        public void onClick(View view) {
            final PO_Dtl item = kt.get(getAdapterPosition() - 1);
            if (view.getId() == R.id.btnapprove) {
                helper.update(String.valueOf(item.getId()), "R", "Release");
                mFragment.getOrderDetailList();
            } else if (view.getId() == R.id.btncancel) {
                helper.update(String.valueOf(item.getId()),"C", "Cancel");
                mFragment.getOrderDetailList();
            }
        }

    }

}

