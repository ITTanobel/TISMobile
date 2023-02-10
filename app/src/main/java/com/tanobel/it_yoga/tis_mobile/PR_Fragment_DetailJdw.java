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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tanobel.it_yoga.tis_mobile.model.PR_DtlJdw;
import com.tanobel.it_yoga.tis_mobile.model.PR_DtlJdw_RVAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

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
 * Created by IT_Yoga on 05/01/2019.
 */

public class PR_Fragment_DetailJdw extends Fragment {
    List<PR_DtlJdw> kt = new ArrayList<>();
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    private XRecyclerView mXRecyclerView;
    private PR_DtlJdw_RVAdapter mAdapter;
    ProgressDialog pDialog;
    String plant, docno, tipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pr_detailjdw, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        PR_Detail prdetail = (PR_Detail) getActivity();
        plant = prdetail.plant;
        docno = prdetail.textTitle.getText().toString();
        tipe = prdetail.tipe;

        mXRecyclerView = view.findViewById(R.id.rv_pr_jdw);
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
                            getPRDetail();
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


        mAdapter = new PR_DtlJdw_RVAdapter(this, kt, tipe);
        mXRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPRDetail();
    }

    void getPRDetail() {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "Purchase");
                    json.put("method", "getPRDtlJdw");
                    JSONArray arr2 = new JSONArray();
                    data.put("plant", plant);
                    data.put("docno", docno);
                    data.put("tipe", "PR");
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router-purchase.php", json, getActivity().getApplicationContext());
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

                                    ArrayList<PR_DtlJdw> item = JSON.parseObject(hasil, new TypeReference<ArrayList<PR_DtlJdw>>() {
                                    });

                                    kt.clear();
                                    kt.addAll(item);
                                    mAdapter.notifyDataSetChanged();
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
