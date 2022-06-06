package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanobel.it_yoga.tis_mobile.R;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ApproveSMP_RVAdapter extends RecyclerView.Adapter<ApproveSMP_RVAdapter.ViewHolder> {
    private List<ApproveSMP_RVList> kt = new ArrayList<>();
    private Typeface font;
    private Fragment mFragment;
    String type = "";
    String code = "";
    ProgressDialog pDialog;
    DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

    public ApproveSMP_RVAdapter(Fragment mFragment, List<ApproveSMP_RVList> kt, String type, String code) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.type = type;
        this.code = code;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_list_item_appsmp, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        viewHolder.tvnamabrg.setText(kt.get(position).getNamabrg());
        viewHolder.tvsatuan.setText(kt.get(position).getSatuan());
        viewHolder.tvtglawal.setText(kt.get(position).getTglmulai());
        viewHolder.tvtglakhir.setText(kt.get(position).getTglakhir());

        if (type.indexOf("HRG") > -1 || type.indexOf("ADDCOST") > -1 || type.indexOf("BNSTMBHN") > -1) {
            viewHolder.lblqtystart.setVisibility(TextView.GONE);
            viewHolder.lblqtyend.setVisibility(TextView.GONE);
            if (type.indexOf("HRG") > -1 || type.indexOf("BNSTMBHN") > -1)
                viewHolder.lbldisc1prcn.setVisibility(TextView.GONE);
            viewHolder.lbldisc2prcn.setVisibility(TextView.GONE);
            viewHolder.lbldisc1val.setVisibility(TextView.GONE);
            viewHolder.lbldisc2val.setVisibility(TextView.GONE);
            viewHolder.lblmultiple.setVisibility(TextView.GONE);

            viewHolder.tdqtystart.setVisibility(TextView.GONE);
            viewHolder.tdqtyend.setVisibility(TextView.GONE);
            if (type.indexOf("HRG") > -1 || type.indexOf("BNSTMBHN") > -1)
                viewHolder.tddisc1prcn.setVisibility(TextView.GONE);
            viewHolder.tddisc2prcn.setVisibility(TextView.GONE);
            viewHolder.tddisc1val.setVisibility(TextView.GONE);
            viewHolder.tddisc2val.setVisibility(TextView.GONE);
            viewHolder.tdmultiple.setVisibility(TextView.GONE);

            viewHolder.tvqtystart.setVisibility(TextView.GONE);
            viewHolder.tvqtyend.setVisibility(TextView.GONE);
            if (type.indexOf("HRG") > -1 || type.indexOf("BNSTMBHN") > -1)
                viewHolder.tvdisc1prcn.setVisibility(TextView.GONE);
            viewHolder.tvdisc2prcn.setVisibility(TextView.GONE);
            viewHolder.tvdisc1val.setVisibility(TextView.GONE);
            viewHolder.tvdisc2val.setVisibility(TextView.GONE);
            viewHolder.tvmulitiple.setVisibility(TextView.GONE);

            if (type.indexOf("HRG") > -1 || type.indexOf("ADDCOST") > -1)
                viewHolder.tvhrgbrg.setText(kursIndonesia.format(kt.get(position).getHarga()));

            if (type.indexOf("ADDCOST") > -1) {
                viewHolder.lbldisc1prcn.setText("Cust. Kirim");
                viewHolder.lblharga.setText("Charges");
                viewHolder.tvdisc1prcn.setText(kt.get(position).getKditem());
            }

            if (type.indexOf("BNSTMBHN") > -1) {
                viewHolder.lblharga.setVisibility(TextView.GONE);
                viewHolder.tdharga.setVisibility(TextView.GONE);
                viewHolder.tvhrgbrg.setVisibility(TextView.GONE);

                viewHolder.lblqtystart.setVisibility(TextView.VISIBLE);
                viewHolder.tdqtystart.setVisibility(TextView.VISIBLE);
                viewHolder.tvqtystart.setVisibility(TextView.VISIBLE);

                viewHolder.lblqtystart.setText("Qty");
                viewHolder.tvqtystart.setText(String.valueOf((int) kt.get(position).getQty1()));
            }

        } else if (type.indexOf("DISC") > -1) {
            viewHolder.lblharga.setVisibility(TextView.GONE);
            viewHolder.tdharga.setVisibility(TextView.GONE);
            viewHolder.tvhrgbrg.setVisibility(TextView.GONE);
            viewHolder.lblqtyend.setVisibility(TextView.GONE);
            viewHolder.tdqtyend.setVisibility(TextView.GONE);
            viewHolder.tvqtyend.setVisibility(TextView.GONE);
            viewHolder.lblmultiple.setVisibility(TextView.GONE);
            viewHolder.tdmultiple.setVisibility(TextView.GONE);
            viewHolder.tvmulitiple.setVisibility(TextView.GONE);

            if (kt.get(position).getQty1() > 0) {
                viewHolder.tvqtystart.setText(String.valueOf((int) kt.get(position).getQty1()) + "  s/d  " + String.valueOf((int) kt.get(position).getQty2()));
            } else {
                viewHolder.lblqtystart.setVisibility(TextView.GONE);
                viewHolder.tdqtystart.setVisibility(TextView.GONE);
                viewHolder.tvqtystart.setVisibility(TextView.GONE);
            }

            //if (kt.get(position).getDisc1pcn() > 0) {
                viewHolder.tvdisc1prcn.setText(String.valueOf(kt.get(position).getDisc1pcn()) + " %");
            //}

            //if (kt.get(position).getDisc2pcn() > 0) {
                viewHolder.tvdisc2prcn.setText(String.valueOf(kt.get(position).getDisc2pcn()) + " %");
            //}

           // if (kt.get(position).getDisc1val() > 0) {
                viewHolder.tvdisc1val.setText(kursIndonesia.format(kt.get(position).getDisc1val()));
            //}

            //if (kt.get(position).getDisc2val() > 0) {
                viewHolder.tvdisc2val.setText(kursIndonesia.format(kt.get(position).getDisc2val()));
            //}

        } else if (type.indexOf("BNSPRD") > -1) {

            if (kt.get(position).getKditem6().isEmpty()) {
                viewHolder.lblharga.setVisibility(TextView.GONE);
                viewHolder.tdharga.setVisibility(TextView.GONE);
                viewHolder.tvhrgbrg.setVisibility(TextView.GONE);
            } else {
                viewHolder.lblharga.setText("District");
                viewHolder.tvhrgbrg.setText(kt.get(position).getKditem6());
            }

            if (kt.get(position).getQty1() > 0) {
                viewHolder.tvqtystart.setText(String.valueOf((int) kt.get(position).getQty1()) + "  s/d  " + String.valueOf((int) kt.get(position).getQty2()));
            } else {
                viewHolder.lblqtystart.setVisibility(TextView.GONE);
                viewHolder.tdqtystart.setVisibility(TextView.GONE);
                viewHolder.tvqtystart.setVisibility(TextView.GONE);
            }

            viewHolder.lblqtyend.setText("Pilihan");
            viewHolder.lbldisc1prcn.setText("Barang Bonus");
            viewHolder.lbldisc2prcn.setText("Satuan Bonus");
            viewHolder.lbldisc1val.setText("Qty Valid");
            viewHolder.lbldisc2val.setText("Qty Bonus");
            viewHolder.lblmultiple.setText("Kelipatan");

            String multiple = "";
            if (kt.get(position).getKditem5().equals("1")) {
                multiple = "YA";
            } else {
                multiple = "TIDAK";
            }

            viewHolder.tvqtyend.setText(kt.get(position).getKditem4());
            viewHolder.tvdisc1prcn.setText(kt.get(position).getKditem());
            viewHolder.tvdisc2prcn.setText(kt.get(position).getKditem2());
            viewHolder.tvdisc1val.setText(String.valueOf((int) kt.get(position).getDisc1pcn()) + "  s/d  " + String.valueOf((int) kt.get(position).getDisc2pcn()));
            viewHolder.tvdisc2val.setText(String.valueOf((int) kt.get(position).getKditem3()));
            viewHolder.tvmulitiple.setText(multiple);

        }

        if (type.indexOf("HRG") > -1) {
            viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("price", "drawable", mFragment.getContext().getPackageName()));
        } else if (type.indexOf("DISC") > -1) {
            viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("discount", "drawable", mFragment.getContext().getPackageName()));
        } else if (type.indexOf("ADDCOST") > -1) {
            viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("addcost", "drawable", mFragment.getContext().getPackageName()));
        } else if (type.indexOf("BNSPRD") > -1) {
            viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("bonusproduk", "drawable", mFragment.getContext().getPackageName()));
        } else if (type.indexOf("BNSTMBHN") > -1) {
            viewHolder.imageView.setImageResource(mFragment.getResources().getIdentifier("bonustambahan", "drawable", mFragment.getContext().getPackageName()));
        }
    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvnamabrg, tvhrgbrg, tvsatuan, tvtglawal, tvtglakhir, lblharga, lblqtystart, lblqtyend,
                lbldisc1prcn, lbldisc2prcn, lbldisc1val, lbldisc2val, lblmultiple, tvqtystart, tvqtyend, tvdisc1prcn,
                tvdisc2prcn, tvdisc1val, tvdisc2val, tvmulitiple, tdharga, tdqtystart, tdqtyend, tddisc1prcn, tddisc2prcn,
                tddisc1val, tddisc2val, tdmultiple;

        ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            lblharga = view.findViewById(R.id.lbl_harga);
            lblqtystart = view.findViewById(R.id.lbl_qtystart);
            lblqtyend = view.findViewById(R.id.lbl_qtyend);
            lbldisc1prcn = view.findViewById(R.id.lbl_disc1_prcn);
            lbldisc2prcn = view.findViewById(R.id.lbl_disc2_prcn);
            lbldisc1val = view.findViewById(R.id.lbl_disc1_val);
            lbldisc2val = view.findViewById(R.id.lbl_disc2_val);
            lblmultiple = view.findViewById(R.id.lbl_multiple);

            tdharga = view.findViewById(R.id.td_harga);
            tdqtystart = view.findViewById(R.id.td_qtystart);
            tdqtyend = view.findViewById(R.id.td_qtyend);
            tddisc1prcn = view.findViewById(R.id.td_disc1_prcn);
            tddisc2prcn = view.findViewById(R.id.td_disc2_prcn);
            tddisc1val = view.findViewById(R.id.td_disc1_val);
            tddisc2val = view.findViewById(R.id.td_disc2_val);
            tdmultiple = view.findViewById(R.id.td_multiple);

            tvnamabrg = view.findViewById(R.id.txt_nama_barang);
            tvhrgbrg = view.findViewById(R.id.txt_harga);
            tvsatuan = view.findViewById(R.id.txt_satuan);
            tvtglawal = view.findViewById(R.id.txt_tgl_awal);
            tvtglakhir = view.findViewById(R.id.txt_tgl_akhir);
            tvqtystart = view.findViewById(R.id.txt_qtystart);
            tvqtyend = view.findViewById(R.id.txt_qtyend);
            tvdisc1prcn = view.findViewById(R.id.txt_disc1_prcn);
            tvdisc2prcn = view.findViewById(R.id.txt_disc2_prcn);
            tvdisc1val = view.findViewById(R.id.txt_disc1_val);
            tvdisc2val = view.findViewById(R.id.txt_disc2_val);
            tvmulitiple = view.findViewById(R.id.txt_multiple);

            imageView = view.findViewById(R.id.image_brg);

            CardView cardView = view.findViewById(R.id.cv);
            cardView.setOnClickListener(this);
            ImageButton approve = view.findViewById(R.id.btnapprove);
            ImageButton cancel = view.findViewById(R.id.btncancel);
            approve.setOnClickListener(this);
            cancel.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final ApproveSMP_RVList item = kt.get(getAdapterPosition() - 1);
            if (view.getId() == R.id.btnapprove) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getActivity());
                builder.setMessage("Yakin mau approve?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateRecord(code, item.getKdbrg(), item.getTglmulai(), item.getTglakhir(), item.getKditem8(), item.getKditem7(), item.getKditem9(), "1");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            } else if (view.getId() == R.id.btncancel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getActivity());
                builder.setMessage("Yakin mau cancel?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateRecord(code, item.getKdbrg(), item.getTglmulai(), item.getTglakhir(), item.getKditem8(), item.getKditem7(), item.getKditem9(), "C");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        }

    }

    void updateRecord(String param1, String param2, String param3, String param4, String param5, String param6, String param7, String status) {
        RequestPost RP;
        if (!mFragment.isRemoving()) {
            pDialog = new ProgressDialog(mFragment.getActivity());
            pDialog.setMessage("Please wait....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        try {
            JSONObject json = new JSONObject();
            json.put("action", "SalesProgram");
            json.put("method", "updateRecord");
            JSONObject data = new JSONObject();
            data.put("type", type);
            data.put("param1", param1);
            data.put("param2", param2);
            data.put("param3", param3);
            data.put("param4", param4);

            if (type.indexOf("CK_ADDCOST") > -1 || type.indexOf("CK_BNSPRD") > -1) {
                data.put("param5", param5);
            } else if (type.indexOf("SO_HRG") > -1 || type.indexOf("SO_DISC") > -1) {
                data.put("param6", param6);
            } else if (type.indexOf("SO_BNSPRD") > -1) {
                data.put("param5", param5);
                data.put("param6", param6);
                data.put("param7", param7);
            }

            data.put("status", status);
            data.put("approveby", ((MainModule) mFragment.getActivity().getApplicationContext()).getUserTIS().toUpperCase());
            data.put("message", "");
            JSONArray arr2 = new JSONArray();
            arr2.put(data);
            json.put("data", arr2);
            RP = new RequestPost("router.php", json, mFragment.getActivity().getApplicationContext());
            RP.execPostCall(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (pDialog != null) pDialog.dismiss();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    if (response.isSuccessful()) {
                        final String jsonData = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(jsonData);
                            JSONObject result = obj.getJSONObject("result");
                            final String msg = result.getString("msg");
                            mFragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mFragment.getActivity());
                                    builder.setTitle("Informasi");
                                    builder.setMessage(msg);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.create();
                                    builder.show();
                                    mFragment.onStart();
                                }
                            });
                        } catch (Exception e) {
                            if (pDialog != null) pDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                }
            });
        } catch (Exception e) {
            if (pDialog != null) pDialog.dismiss();
            e.printStackTrace();
        }

    }
}

