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
import com.tanobel.it_yoga.tis_mobile.model.Db_PR;
import com.tanobel.it_yoga.tis_mobile.model.PR_Dtl;
import com.tanobel.it_yoga.tis_mobile.model.PR_Dtl_RVAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
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

public class PR_Fragment_Detail extends Fragment {
    List<PR_Dtl> kt = new ArrayList<>();
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    private XRecyclerView mXRecyclerView;
    private PR_Dtl_RVAdapter mAdapter;
    ProgressDialog pDialog;
    String plant, docno, tipe, statusnew, statusdescnew;

    FloatingActionMenu fbuttonMenu;
    FloatingActionButton fbuttonApprove, fbuttonCancel;
    
    Db_PR helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pr_detail, container, false);

        helper = new Db_PR(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        PR_Detail prdetail = (PR_Detail) getActivity();
        plant = prdetail.plant;
        docno = prdetail.textTitle.getText().toString();
        tipe = prdetail.tipe;

        mXRecyclerView = view.findViewById(R.id.rv_pr);
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


        mAdapter = new PR_Dtl_RVAdapter(this, kt, tipe);
        mXRecyclerView.setAdapter(mAdapter);

        fbuttonMenu = view.findViewById(R.id.fbActionMenu_pr);
        fbuttonApprove = view.findViewById(R.id.fbApproveAll_pr);
        fbuttonCancel = view.findViewById(R.id.fbCancelAll_pr);

        fbuttonApprove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateStatus(kt, "Approve");
                fbuttonMenu.close(true);
            }
        });

        fbuttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateStatus(kt, "Cancel");
                fbuttonMenu.close(true);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPRDetail();
    }

    public void getOrderDetailList() {
        List<PR_Dtl> itemList = helper.getDataAll();

        kt.clear();
        for (int i = 0; i < itemList.size(); i++) {
            PR_Dtl items = itemList.get(i);
            PR_Dtl item = new PR_Dtl();
            item.setId(items.getId());
            item.setBranch(items.getBranch());
            item.setDocno(items.getDocno());
            item.setNo(items.getNo());
            item.setDocref(items.getDocref());
            item.setBrgcode(items.getBrgcode());
            item.setBrgname(items.getBrgname());
            item.setBrgdesc(items.getBrgdesc());
            item.setSatcode(items.getSatcode());
            item.setQty(items.getQty());
            item.setSisastok(items.getSisastok());
            item.setIocode(items.getIocode());
            item.setStatus(items.getStatus());
            item.setStatusdesc(items.getStatusdesc());
            item.setNoref(items.getNoref());
            item.setTglkirim(items.getTglkirim());
            item.setApproval(items.getApproval());
            item.setUserapproval(items.getUserapproval());
            item.setStatusold(items.getStatusold());
            kt.add(item);
        }

        mAdapter.notifyDataSetChanged();
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
                    json.put("method", "getPRDtl");
                    JSONArray arr2 = new JSONArray();
                    data.put("plant", plant);
                    data.put("docno", docno);
                    data.put("user", ((MainModule) getActivity().getApplicationContext()).getUserTIS());
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
                                    JSONArray hasil = rslt.getJSONArray("hasil");

                                    helper.delete();
                                    for (int i = 0; i < hasil.length(); i++) {
                                        JSONObject prdtl = hasil.getJSONObject(i);
                                        long id = helper.insertData(prdtl.getString("branch"), prdtl.getString("docno"), prdtl.getString("no"), prdtl.getString("docref"), prdtl.getString("brgcode"), MainModule.getcharfromascii(prdtl.getString("brgname")), prdtl.getString("brgdesc"), prdtl.getString("satcode"), prdtl.getString("qty"), prdtl.getString("sisastok"), prdtl.getString("iocode"), prdtl.getString("status"), prdtl.getString("statusdesc"), prdtl.getString("noref"), prdtl.getString("tglkirim"), prdtl.getString("approval"), prdtl.getString("userapproval"), prdtl.getString("statusold"));
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

    void updateStatus(final List<PR_Dtl> datalist, String statustipe) {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                for (int i = 0; i < datalist.size(); i++) {
                    PR_Dtl item = datalist.get(i);

                    if (statustipe.equals("Approve")) {
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
                                    statusdescnew = "Need Approve 2";
                                }

                                if (item.getUserapproval().trim().equals("2")) {
                                    statusnew = "2";
                                    statusdescnew = "Need Approve 3";
                                }
                            }
                        }
                    } else {
                        statusnew = "C";
                        statusdescnew = "Cancel";
                    }

                    helper.update(String.valueOf(item.getId()), statusnew, statusdescnew);
                }
                
                getOrderDetailList();
                if (pDialog != null) pDialog.dismiss();
            } catch (Exception e) {
                if (pDialog != null) pDialog.dismiss();
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
