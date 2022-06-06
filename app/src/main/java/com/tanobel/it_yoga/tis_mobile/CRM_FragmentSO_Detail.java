package com.tanobel.it_yoga.tis_mobile;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_OrderDetail;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_RVAdapter;
import com.tanobel.it_yoga.tis_mobile.model.Db_CRM_SO;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CRM_FragmentSO_Detail extends Fragment {

    List<CRM_SO_OrderDetail> kt = new ArrayList<>();
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    private XRecyclerView mXRecyclerView;
    private CRM_SO_RVAdapter mAdapter;
    ArrayList<String> ArrayListDtl;
    String menutype, docno;

    Db_CRM_SO helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_so_detail, container, false);

        helper = new Db_CRM_SO(getActivity());

        mXRecyclerView = view.findViewById(R.id.rv_so_detail);
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
                            getOrderDetail();
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

        CRM_SalesOrder crm_salesOrder = (CRM_SalesOrder) getActivity();
        menutype = crm_salesOrder.menutype;
        docno = crm_salesOrder.docno;

        mAdapter = new CRM_SO_RVAdapter(this, kt, menutype);
        mXRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!menutype.equals("Input")) {
            getPSODtlByDocno();
        }
    }

    public void getOrderDetail() {
        List<CRM_SO_OrderDetail> itemList = helper.getDataAll();

        kt.clear();
        for (int i = 0; i < itemList.size(); i++) {
            CRM_SO_OrderDetail items = itemList.get(i);
            CRM_SO_OrderDetail item = new CRM_SO_OrderDetail();
            item.setId(items.getId());
            item.setKode(items.getKode());
            item.setNama(items.getNama());
            item.setQty(items.getQty());
            item.setSatuan(items.getSatuan());
            item.setTglkirim(items.getTglkirim());
            kt.add(item);
         }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("ArrayListDtl", ArrayListDtl);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayListDtl = savedInstanceState.getStringArrayList("ArrayListDtl");
        }
    }

    void getPSODtlByDocno() {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getPreSODtlByDocno");
                    JSONArray arr2 = new JSONArray();
                    data.put("docno", docno);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-crmsalesorder.php", json, getActivity().getApplicationContext());
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
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
                                    final String customerkrm[] = new String[1];

                                    for (int i=0; i<hasil.length(); i++) {
                                        JSONObject psodtl = hasil.getJSONObject(i);
                                        long id = helper.insertData(docno, psodtl.getString("cBrgcode"), psodtl.getString("cBrgname"), psodtl.getInt("nQty"), psodtl.getString("cSatcode"), psodtl.getString("tglkirim"));
                                        if (id <= 0) {
                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Ada kesalahan load detail item", Toast.LENGTH_SHORT);
                                            toast.show();
                                            break;
                                        }
                                    }

                                    getOrderDetail();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

