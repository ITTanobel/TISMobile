package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tanobel.it_yoga.tis_mobile.model.Db_GR;
import com.tanobel.it_yoga.tis_mobile.model.GR_Dtl;
import com.tanobel.it_yoga.tis_mobile.model.GR_Dtl_RVAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.util.RequestPostLumen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Galeh on 08/12/2022.
 */

public class GR_Fragment_Detail extends Fragment {
    List<GR_Dtl> kt = new ArrayList<>();
    private RequestPostLumen RP;
    InternetConnection internetCon = new InternetConnection();
    private XRecyclerView mXRecyclerView;
    private GR_Dtl_RVAdapter mAdapter;
    ProgressDialog pDialog;
    String plant, docno, tipe, statusnew, statusdescnew, menu_type,segment;

    FloatingActionMenu fbuttonMenu;
    FloatingActionButton fbuttonApprove, fbuttonCancel;

    Db_GR helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gr_detail, container, false);

        helper = new Db_GR(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        GR_Detail grdetail = (GR_Detail) getActivity();
        plant = grdetail.plant;
        docno = grdetail.textTitle.getText().toString();
        tipe = grdetail.tipe;
        menu_type = grdetail.menutype;
        if (menu_type.equals("AppvLPB")){
            segment = "approval1.lpb.view_barang";
        }else if (menu_type.equals("Appv2LPB")){
            segment = "approval2.lpb.view_barang";
        }
        mXRecyclerView = view.findViewById(R.id.rv_po);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (getActivity().getApplicationContext() != null) {
                            getOrderDetailList();
                        }
                        mXRecyclerView.refreshComplete();
                    }
                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                mXRecyclerView.refreshComplete();
            }
        });


        mAdapter = new GR_Dtl_RVAdapter(this, kt, menu_type);
        mXRecyclerView.setAdapter(mAdapter);

        if (menu_type.equals("Appv2GR")) {
            fbuttonMenu.hideMenu(true);
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getGRDetail();
    }

    public void getOrderDetailList() {
        List<GR_Dtl> itemList = helper.getDataAll();

        kt.clear();
        for (int i = 0; i < itemList.size(); i++) {
            GR_Dtl items = itemList.get(i);
            GR_Dtl item = new GR_Dtl();
            item.setNo_po(items.getNo_po());
            item.setKode_barang(items.getKode_barang());
            item.setNama_barang(items.getNama_barang());
            item.setSatuan(items.getSatuan());
            item.setJumlah(items.getJumlah());
            kt.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

    void getGRDetail() {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("plant", plant);
                    json.put("docno", docno);
                    json.put("username", ((MainModule) getActivity().getApplicationContext()).getUserTIS());
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
                                    JSONArray hasil = rslt.getJSONArray("hasil");

                                    helper.delete();
                                    for (int i = 0; i < hasil.length(); i++) {
                                        JSONObject podtl = hasil.getJSONObject(i);
                                        long id = helper.insertData(podtl.getString("branch"), podtl.getString("docno"), podtl.getString("no_po"), podtl.getString("kode_barang"), podtl.getString("nama_barang"), podtl.getString("satuan"), podtl.getString("jumlah"));
                                        if (id <= 0) {
                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Ada kesalahan load detail item", Toast.LENGTH_SHORT);
                                            toast.show();
                                            break;
                                        }
                                    }

                                    getOrderDetailList();

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
