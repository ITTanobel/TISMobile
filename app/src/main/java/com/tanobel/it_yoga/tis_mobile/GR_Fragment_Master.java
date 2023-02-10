package com.tanobel.it_yoga.tis_mobile;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.GR_Mst;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.util.RequestPostLumen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Galeh on 08/12/2022.
 */
public class GR_Fragment_Master extends Fragment {
    private RequestPostLumen RP;
    InternetConnection internetCon = new InternetConnection();
    EditText kode_supplier,nama_supplier,alamat_supplier,branch,nama_plant,status,nama_tipe,pembayaran,keterangan,tgl_input,tgl_posting,diinput_oleh,tgl_approve1,approve1_oleh,tgl_approve2,approve2_oleh,no_po,order_po,nama_gudang,no_faktur_sj,no_ekspedisi,no_container,tgl_sj_supplier,tgl_penerimaan,mata_uang,top;
    ProgressDialog pDialog;
    String plant, docno, tipe, tipeorder,user,menutype,segment;
    Button view_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gr_master, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        GR_Detail grdetail = (GR_Detail) getActivity();
        plant = grdetail.plant;
        docno = grdetail.textTitle.getText().toString();
        tipe = grdetail.tipe;
        user = grdetail.user;
        menutype = grdetail.menutype;
        if (menutype.equals("AppvLPB")){
            segment = "approval1.lpb.view_one";
        }else if (menutype.equals("Appv2LPB")){
            segment = "approval2.lpb.view_one";
        }
        kode_supplier = view.findViewById(R.id.kode_supplier);
        nama_supplier = view.findViewById(R.id.nama_supplier);
        alamat_supplier = view.findViewById(R.id.alamat_supplier);
        branch = view.findViewById(R.id.plant);
        nama_plant = view.findViewById(R.id.nama_plant);
        status = view.findViewById(R.id.status);
        nama_tipe = view.findViewById(R.id.nama_tipe);
        pembayaran = view.findViewById(R.id.pembayaran);
        keterangan = view.findViewById(R.id.keterangan);
        tgl_input = view.findViewById(R.id.tgl_input);
        tgl_posting = view.findViewById(R.id.tgl_posting);
        diinput_oleh = view.findViewById(R.id.diinput_oleh);
        tgl_approve1 = view.findViewById(R.id.tgl_approve1);
        approve1_oleh = view.findViewById(R.id.approve1_oleh);
        tgl_approve2 = view.findViewById(R.id.tgl_approve2);
        approve2_oleh = view.findViewById(R.id.approve2_oleh);
        no_po = view.findViewById(R.id.no_po);
        order_po = view.findViewById(R.id.order_po);
        nama_gudang = view.findViewById(R.id.nama_gudang);
        no_faktur_sj = view.findViewById(R.id.no_faktur_sj);
        no_ekspedisi = view.findViewById(R.id.no_ekspedisi);
        no_container = view.findViewById(R.id.no_container);
        tgl_sj_supplier = view.findViewById(R.id.tgl_sj_supplier);
        tgl_penerimaan = view.findViewById(R.id.tgl_penerimaan);
        mata_uang = view.findViewById(R.id.mata_uang);
        top = view.findViewById(R.id.top);
        view_image = view.findViewById(R.id.view_image_btn);
        //set to false focusable
        kode_supplier.setFocusable(false);
        nama_supplier.setFocusable(false);
        alamat_supplier.setFocusable(false);
        branch.setFocusable(false);
        nama_plant.setFocusable(false);
        status.setFocusable(false);
        nama_tipe.setFocusable(false);
        pembayaran.setFocusable(false);
        keterangan.setFocusable(false);
        tgl_input.setFocusable(false);
        tgl_posting.setFocusable(false);
        diinput_oleh.setFocusable(false);
        tgl_approve1.setFocusable(false);
        approve1_oleh.setFocusable(false);
        tgl_approve2.setFocusable(false);
        approve2_oleh.setFocusable(false);
        no_po.setFocusable(false);
        order_po.setFocusable(false);
        nama_gudang.setFocusable(false);
        no_faktur_sj.setFocusable(false);
        no_ekspedisi.setFocusable(false);
        no_container.setFocusable(false);
        tgl_sj_supplier.setFocusable(false);
        tgl_penerimaan.setFocusable(false);
        mata_uang.setFocusable(false);
        top.setFocusable(false);
        getGRMaster(view);
        view_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(getActivity(),View_image.class);
                j.putExtra("docno", docno);
                j.putExtra("plant", plant);
                j.putExtra("user", user);
                j.putExtra("usage", "image_sj");
                startActivity(j);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    void getGRMaster(final View view) {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }

            try {
                JSONObject json = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("param", "master");
                    json.put("docno", docno);
                    json.put("plant", plant);
                    json.put("username", user);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPostLumen(segment, json, getActivity().getApplicationContext());
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

                                    ArrayList<GR_Mst> item = JSON.parseObject(hasil, new TypeReference<ArrayList<GR_Mst>>() {
                                    });

                                    for (int i = 0; i < item.size(); i++) {
                                        GR_Mst item_master = item.get(i);
                                        kode_supplier.setText(item_master.getKode_supplier());
                                        nama_supplier.setText(item_master.getNama_supplier());
                                        alamat_supplier.setText(item_master.getAlamat_supplier());
                                        branch.setText(item_master.getBranch());
                                        nama_plant.setText(item_master.getNama_plant());
                                        status.setText(item_master.getStatus());
                                        nama_tipe.setText(item_master.getNama_tipe());
                                        pembayaran.setText(item_master.getPembayaran());
                                        keterangan.setText(item_master.getKeterangan());
                                        tgl_input.setText(item_master.getTgl_input());
                                        tgl_posting.setText(item_master.getTgl_posting());
                                        diinput_oleh.setText(item_master.getDiinput_oleh());
                                        tgl_approve1.setText(item_master.getTgl_approve1());
                                        approve1_oleh.setText(item_master.getApprove1_oleh());
                                        tgl_approve2.setText(item_master.getTgl_approve2());
                                        approve2_oleh.setText(item_master.getApprove2_oleh());
                                        no_po.setText(item_master.getNo_po());
                                        order_po.setText(item_master.getOrder_po());
                                        nama_gudang.setText(item_master.getNama_gudang());
                                        no_faktur_sj.setText(item_master.getNo_faktur_sj());
                                        no_ekspedisi.setText(item_master.getNo_ekspedisi());
                                        no_container.setText(item_master.getNo_container());
                                        tgl_sj_supplier.setText(item_master.getTgl_sj_supplier());
                                        tgl_penerimaan.setText(item_master.getTgl_penerimaan());
                                        mata_uang.setText(item_master.getMata_uang());
                                        top.setText(item_master.getNtop());
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
