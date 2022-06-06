package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.Unblock_SO_Master;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by IT_Yoga on 05/01/2019.
 */

public class Unblock_SO_Fragment_Master extends Fragment  {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    EditText txtbranch,	txttipe, txtstatus, txttglinput, txttgldoc,	txttglpost, txtinputby, txtmu;
    EditText txtrate, txtket, txtslscomp, txtslsarea, txtslschn, txtslsdiv, txtslsdist, txtsales;
    EditText txtcustkrm, txtalamatkrm, txttglkrm, txtcustbyr, txtalamatbyr, txttop;
    ProgressDialog pDialog;
    String plant, docno, custcode;
    TableLayout t1, t2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_unbso_master, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        Unblock_SO_Detail unblocksodetail = (Unblock_SO_Detail) getActivity();
        plant = unblocksodetail.plant;
        docno = unblocksodetail.textTitle.getText().toString();
        custcode = unblocksodetail.custcode;

        txtbranch = view.findViewById(R.id.txtplant);
        txttipe = view.findViewById(R.id.txttipe);
        txtstatus = view.findViewById(R.id.txtstatus);
        txttglinput = view.findViewById(R.id.txttglinput);
        txttgldoc = view.findViewById(R.id.txttgldoc);
        txttglpost = view.findViewById(R.id.txttglpost);
        txtinputby = view.findViewById(R.id.txtinputby);
        txtmu = view.findViewById(R.id.txtmu);
        txtrate = view.findViewById(R.id.txtrate);
        txtket = view.findViewById(R.id.txtket);
        txtslscomp = view.findViewById(R.id.txtslscomp);
        txtslsarea = view.findViewById(R.id.txtslsarea);
        txtslschn = view.findViewById(R.id.txtslsdistchan);
        txtslsdiv = view.findViewById(R.id.txtslsdiv);
        txtslsdist = view.findViewById(R.id.txtslsdist);
        txtsales = view.findViewById(R.id.txtsales);
        txtcustkrm = view.findViewById(R.id.txtcustkrm);
        txtalamatkrm = view.findViewById(R.id.txtalmtcustkrm);
        txttglkrm = view.findViewById(R.id.txttglkrm);
        txtcustbyr = view.findViewById(R.id.txtcustbyr);
        txtalamatbyr = view.findViewById(R.id.txtalmtcustbyr);
        txttop = view.findViewById(R.id.txttop);

        txtbranch.setFocusable(false);
        txttipe.setFocusable(false);
        txtstatus.setFocusable(false);
        txttglinput.setFocusable(false);
        txttgldoc.setFocusable(false);
        txttglpost.setFocusable(false);
        txtinputby.setFocusable(false);
        txtmu.setFocusable(false);
        txtrate.setFocusable(false);
        txtket.setFocusable(false);
        txtslscomp.setFocusable(false);
        txtslsarea.setFocusable(false);
        txtslschn.setFocusable(false);
        txtslsdiv.setFocusable(false);
        txtslsdist.setFocusable(false);
        txtsales.setFocusable(false);
        txtcustkrm.setFocusable(false);
        txtalamatkrm.setFocusable(false);
        txttglkrm.setFocusable(false);
        txtcustbyr.setFocusable(false);
        txtalamatbyr.setFocusable(false);
        txttop.setFocusable(false);

        t1 = view.findViewById(R.id.main_table);
        t2 = view.findViewById(R.id.main_table2);

        getSOMaster(t1, t2, view);

        // Inflate the layout for this fragment
        return view;
    }

    void getSOMaster(final TableLayout table1, final TableLayout table2, final View view) {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "Unblock_SO");
                    json.put("method", "getSalesOrderMst");
                    JSONArray arr2 = new JSONArray();
                    data.put("plant", plant);
                    data.put("docno", docno);
                    data.put("custcode", custcode.trim());
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router-unblockso.php", json, getActivity().getApplicationContext());
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (pDialog != null) pDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        final String jsonData = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(jsonData);
                                    JSONObject rslt = obj.getJSONObject("result");
                                    String hasil = rslt.getString("hasil");
                                    JSONArray limitar = rslt.getJSONArray("limitar");
                                    JSONArray limitgln = rslt.getJSONArray("limitgln");

                                    ArrayList<Unblock_SO_Master> item = JSON.parseObject(hasil, new TypeReference<ArrayList<Unblock_SO_Master>>() {
                                    });

                                    for (int i = 0; i < item.size(); i++) {
                                        Unblock_SO_Master item_master = item.get(i);
                                        txtbranch.setText(item_master.getBranch());
                                        txttipe.setText(item_master.getTipe());
                                        txtstatus.setText(item_master.getStatus());
                                        txttglinput.setText(item_master.getTglinput());
                                        txttgldoc.setText(item_master.getTgldoc());
                                        txttglpost.setText(item_master.getTglpost());
                                        txtinputby.setText(item_master.getInputby());
                                        txtmu.setText(item_master.getMu());
                                        txtrate.setText(String.valueOf(item_master.getRate()));
                                        txtket.setText(item_master.getKet());
                                        txtslscomp.setText(item_master.getSlscomp());
                                        txtslsarea.setText(item_master.getSlsarea());
                                        txtslschn.setText(item_master.getSlschn());
                                        txtslsdiv.setText(item_master.getSlsdiv());
                                        txtslsdist.setText(item_master.getSlsdist());
                                        txtsales.setText(item_master.getSales());
                                        txtcustkrm.setText(item_master.getCustkrm());
                                        txtalamatkrm.setText(item_master.getAlamatkrm());
                                        txttglkrm.setText(item_master.getTglkrm());
                                        txtcustbyr.setText(item_master.getCustbyr());
                                        txtalamatbyr.setText(item_master.getAlamatkrm());
                                        txttop.setText(String.valueOf(item_master.getTop()));
                                    }

                                    //Limit AR
                                    TableRow tr_head = new TableRow(view.getContext());
                                    TableRow.LayoutParams lp0 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                    tr_head.setLayoutParams(lp0);
                                    tr_head.setBackgroundColor(Color.GRAY);
                                    TextView head1 = new TextView(view.getContext());
                                    TextView head2 = new TextView(view.getContext());
                                    TextView head3 = new TextView(view.getContext());
                                    TextView head4 = new TextView(view.getContext());
                                    TextView head5 = new TextView(view.getContext());
                                    TextView head6 = new TextView(view.getContext());
                                    TextView head7 = new TextView(view.getContext());
                                    TextView head8 = new TextView(view.getContext());
                                    TextView head9 = new TextView(view.getContext());

                                    head1.setText("Plant");
                                    head1.setTextColor(Color.WHITE);
                                    head1.setPadding(10, 5, 10, 5);
                                    head2.setText("Kode Customer");
                                    head2.setTextColor(Color.WHITE);
                                    head2.setPadding(10, 5, 10, 5);
                                    head3.setText("Nama");
                                    head3.setTextColor(Color.WHITE);
                                    head3.setPadding(10, 5, 10, 5);
                                    head4.setText("Limit");
                                    head4.setTextColor(Color.WHITE);
                                    head4.setPadding(10, 5, 10, 5);
                                    head5.setText("SO");
                                    head5.setTextColor(Color.WHITE);
                                    head5.setPadding(10, 5, 10, 5);
                                    head6.setText("DO");
                                    head6.setTextColor(Color.WHITE);
                                    head6.setPadding(10, 5, 10, 5);
                                    head7.setText("AR");
                                    head7.setTextColor(Color.WHITE);
                                    head7.setPadding(10, 5, 10, 5);
                                    head8.setText("Giro");
                                    head8.setTextColor(Color.WHITE);
                                    head8.setPadding(10, 5, 10, 5);
                                    head9.setText("Sisa");
                                    head9.setTextColor(Color.WHITE);
                                    head9.setPadding(10, 5, 10, 5);

                                    tr_head.addView(head1);
                                    tr_head.addView(head2);
                                    tr_head.addView(head3);
                                    tr_head.addView(head4);
                                    tr_head.addView(head5);
                                    tr_head.addView(head6);
                                    tr_head.addView(head7);
                                    tr_head.addView(head8);
                                    tr_head.addView(head9);
                                    table1.addView(tr_head, 0);

                                    for (int i = 0; i < limitar.length(); i++) {
                                        JSONObject result = limitar.getJSONObject(i);

                                        TableRow row = new TableRow(view.getContext());
                                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                        row.setLayoutParams(lp);
                                        TextView tvplant = new TextView(view.getContext());
                                        TextView tvkodecust = new TextView(view.getContext());
                                        TextView tvnamecust = new TextView(view.getContext());
                                        TextView tvlimit = new TextView(view.getContext());
                                        TextView tvso = new TextView(view.getContext());
                                        TextView tvdo = new TextView(view.getContext());
                                        TextView tvar = new TextView(view.getContext());
                                        TextView tvgiro = new TextView(view.getContext());
                                        TextView tvsisa = new TextView(view.getContext());

                                        tvplant.setText(result.getString("cbranch"));
                                        tvplant.setPadding(10, 5, 10, 5);
                                        tvkodecust.setText(result.getString("ccustcode"));
                                        tvkodecust.setPadding(10, 5, 10, 5);
                                        tvnamecust.setText(result.getString("cnama"));
                                        tvnamecust.setPadding(10, 5, 10, 5);
                                        tvlimit.setText(result.getString("Limit"));
                                        tvlimit.setPadding(10, 5, 10, 5);
                                        tvso.setText(result.getString("SO"));
                                        tvso.setPadding(10, 5, 10, 5);
                                        tvdo.setText(result.getString("DO"));
                                        tvdo.setPadding(10, 5, 10, 5);
                                        tvar.setText(result.getString("AR"));
                                        tvar.setPadding(10, 5, 10, 5);
                                        tvgiro.setText(result.getString("Giro"));
                                        tvgiro.setPadding(10, 5, 10, 5);
                                        tvsisa.setText(result.getString("Sisa"));
                                        tvsisa.setPadding(10, 5, 10, 5);

                                        row.addView(tvplant);
                                        row.addView(tvkodecust);
                                        row.addView(tvnamecust);
                                        row.addView(tvlimit);
                                        row.addView(tvso);
                                        row.addView(tvdo);
                                        row.addView(tvar);
                                        row.addView(tvgiro);
                                        row.addView(tvsisa);

                                        table1.addView(row, i + 1);
                                    }

                                    //Limit Galon
                                    TableRow tr_head_gln = new TableRow(view.getContext());
                                    TableRow.LayoutParams lp0_gln = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                    tr_head_gln.setLayoutParams(lp0_gln);
                                    tr_head_gln.setBackgroundColor(Color.GRAY);
                                    TextView head1_gln = new TextView(view.getContext());
                                    TextView head2_gln = new TextView(view.getContext());
                                    TextView head3_gln = new TextView(view.getContext());
                                    TextView head4_gln = new TextView(view.getContext());
                                    TextView head5_gln = new TextView(view.getContext());
                                    TextView head6_gln = new TextView(view.getContext());
                                    TextView head7_gln = new TextView(view.getContext());
                                    TextView head8_gln = new TextView(view.getContext());
                                    TextView head9_gln = new TextView(view.getContext());

                                    head1_gln.setText("Plant");
                                    head1_gln.setTextColor(Color.WHITE);
                                    head1_gln.setPadding(10, 5, 10, 5);
                                    head2_gln.setText("Kode Customer");
                                    head2_gln.setTextColor(Color.WHITE);
                                    head2_gln.setPadding(10, 5, 10, 5);
                                    head3_gln.setText("Nama");
                                    head3_gln.setTextColor(Color.WHITE);
                                    head3_gln.setPadding(10, 5, 10, 5);
                                    head4_gln.setText("Produk");
                                    head4_gln.setTextColor(Color.WHITE);
                                    head4_gln.setPadding(10, 5, 10, 5);
                                    head5_gln.setText("Limit");
                                    head5_gln.setTextColor(Color.WHITE);
                                    head5_gln.setPadding(10, 5, 10, 5);
                                    head6_gln.setText("Unbill");
                                    head6_gln.setTextColor(Color.WHITE);
                                    head6_gln.setPadding(10, 5, 10, 5);
                                    head7_gln.setText("AR Galon");
                                    head7_gln.setTextColor(Color.WHITE);
                                    head7_gln.setPadding(10, 5, 10, 5);
                                    head8_gln.setText("Sisa (Value)");
                                    head8_gln.setTextColor(Color.WHITE);
                                    head8_gln.setPadding(10, 5, 10, 5);
                                    head9_gln.setText("Sisa (Qty)");
                                    head9_gln.setTextColor(Color.WHITE);
                                    head9_gln.setPadding(10, 5, 10, 5);

                                    tr_head_gln.addView(head1_gln);
                                    tr_head_gln.addView(head2_gln);
                                    tr_head_gln.addView(head3_gln);
                                    tr_head_gln.addView(head4_gln);
                                    tr_head_gln.addView(head5_gln);
                                    tr_head_gln.addView(head6_gln);
                                    tr_head_gln.addView(head7_gln);
                                    tr_head_gln.addView(head8_gln);
                                    tr_head_gln.addView(head9_gln);
                                    table2.addView(tr_head_gln, 0);

                                    for (int i = 0; i < limitgln.length(); i++) {
                                        JSONObject result = limitgln.getJSONObject(i);

                                        TableRow row = new TableRow(view.getContext());
                                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                        row.setLayoutParams(lp);
                                        TextView tvplant = new TextView(view.getContext());
                                        TextView tvkodecust = new TextView(view.getContext());
                                        TextView tvnamecust = new TextView(view.getContext());
                                        TextView tvproduk = new TextView(view.getContext());
                                        TextView tvlimit = new TextView(view.getContext());
                                        TextView tvunbill = new TextView(view.getContext());
                                        TextView tvargalon = new TextView(view.getContext());
                                        TextView tvsisavalue = new TextView(view.getContext());
                                        TextView tvsisaqty = new TextView(view.getContext());

                                        tvplant.setText(result.getString("cbranch"));
                                        tvplant.setPadding(10, 5, 10, 5);
                                        tvkodecust.setText(result.getString("ccustcode"));
                                        tvkodecust.setPadding(10, 5, 10, 5);
                                        tvnamecust.setText(result.getString("cNama"));
                                        tvnamecust.setPadding(10, 5, 10, 5);
                                        tvproduk.setText(result.getString("cGroup"));
                                        tvproduk.setPadding(10, 5, 10, 5);
                                        tvlimit.setText(result.getString("mLimitGalon"));
                                        tvlimit.setPadding(10, 5, 10, 5);
                                        tvunbill.setText(result.getString("Unbill_val"));
                                        tvunbill.setPadding(10, 5, 10, 5);
                                        tvargalon.setText(result.getString("mAR_GALON"));
                                        tvargalon.setPadding(10, 5, 10, 5);
                                        tvsisavalue.setText(result.getString("sisaLimit_value"));
                                        tvsisavalue.setPadding(10, 5, 10, 5);
                                        tvsisaqty.setText(result.getString("sisaLimit_qty"));
                                        tvsisaqty.setPadding(10, 5, 10, 5);

                                        row.addView(tvplant);
                                        row.addView(tvkodecust);
                                        row.addView(tvnamecust);
                                        row.addView(tvproduk);
                                        row.addView(tvlimit);
                                        row.addView(tvunbill);
                                        row.addView(tvargalon);
                                        row.addView(tvsisavalue);
                                        row.addView(tvsisaqty);

                                        table2.addView(row, i + 1);
                                    }

                                    if (pDialog != null) pDialog.dismiss();
                                } catch (JSONException e) {
                                    if (pDialog != null) pDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } catch (IOException e) {
                if (pDialog != null) pDialog.dismiss();
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
