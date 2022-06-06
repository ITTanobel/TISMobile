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
import com.tanobel.it_yoga.tis_mobile.PR_Fragment_Detail;
import com.tanobel.it_yoga.tis_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class PR_Dtl_RVAdapter extends RecyclerView.Adapter<PR_Dtl_RVAdapter.ViewHolder> {
    private List<PR_Dtl> kt = new ArrayList<>();
    private Typeface font;
    private PR_Fragment_Detail mFragment;
    String tipe, statusnew, statusdescnew;
    ProgressDialog pDialog;

    Db_PR helper;

    public PR_Dtl_RVAdapter(PR_Fragment_Detail mFragment, List<PR_Dtl> kt, String tipe) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.tipe = tipe;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        helper = new Db_PR(mFragment.getActivity());
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_pr_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txt_nama_barang.setText(kt.get(position).getBrgname());
        viewHolder.txt_docref.setText(kt.get(position).getDocref());
        viewHolder.txt_satuan.setText(kt.get(position).getSatcode());
        viewHolder.txt_qty.setText(kt.get(position).getQty());
        viewHolder.txt_sisastok.setText(kt.get(position).getSisastok());
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
        TextView txt_nama_barang, txt_docref, txt_satuan, txt_qty, txt_sisastok, txt_order, txt_spec, txt_status;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            txt_nama_barang = view.findViewById(R.id.txt_nama_barang);
            txt_docref = view.findViewById(R.id.txt_docref);
            txt_satuan = view.findViewById(R.id.txt_satuan);
            txt_qty = view.findViewById(R.id.txt_qty);
            txt_sisastok = view.findViewById(R.id.txt_sisastok);
            txt_order = view.findViewById(R.id.txt_order);
            txt_spec = view.findViewById(R.id.txt_spec);
            txt_status = view.findViewById(R.id.txt_status);

            imageView = view.findViewById(R.id.image_brg);

            CardView cardView = view.findViewById(R.id.cv);
            cardView.setOnClickListener(this);
            ImageButton approve = view.findViewById(R.id.btnapprove);
            ImageButton cancel = view.findViewById(R.id.btncancel);
            approve.setOnClickListener(this);
            cancel.setOnClickListener(this);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getRandomColor();
            imageView.setColorFilter(color);
        }

        @Override
        public void onClick(View view) {
            final PR_Dtl item = kt.get(getAdapterPosition() - 1);
            if (view.getId() == R.id.btnapprove) {
                if (tipe.equals("B")) {
                    statusnew = "R";
                    statusdescnew = "Release";
                } else {
                    if (item.getUserapproval().trim().equals(item.getApproval().trim())) {
                        statusnew = "R";
                        statusdescnew = "Release";
                    } else {
                        if (item.getUserapproval().trim().equals("1")) {
                            statusnew = "1";
                            statusdescnew = "Need Approve 2";                                        }

                        if (item.getUserapproval().trim().equals("2")) {
                            statusnew = "2";
                            statusdescnew = "Need Approve 3";
                        }
                    }
                }

                helper.update(String.valueOf(item.getId()), statusnew, statusdescnew);
                mFragment.getOrderDetailList();
             } else if (view.getId() == R.id.btncancel) {
                helper.update(String.valueOf(item.getId()),"C", "Cancel");
                mFragment.getOrderDetailList();
            }
        }

    }

}

