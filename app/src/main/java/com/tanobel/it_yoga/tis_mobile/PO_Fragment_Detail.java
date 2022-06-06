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
import com.tanobel.it_yoga.tis_mobile.model.Db_PO;
import com.tanobel.it_yoga.tis_mobile.model.PO_Dtl;
import com.tanobel.it_yoga.tis_mobile.model.PO_Dtl_RVAdapter;
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

public class PO_Fragment_Detail extends Fragment {
    List<PO_Dtl> kt = new ArrayList<>();
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    private XRecyclerView mXRecyclerView;
    private PO_Dtl_RVAdapter mAdapter;
    ProgressDialog pDialog;
    String plant, docno, tipe, statusnew, statusdescnew, menu_type;

    FloatingActionMenu fbuttonMenu;
    FloatingActionButton fbuttonApprove, fbuttonCancel;

    Db_PO helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_po_detail, container, false);

        helper = new Db_PO(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        PO_Detail prdetail = (PO_Detail) getActivity();
        plant = prdetail.plant;
        docno = prdetail.textTitle.getText().toString();
        tipe = prdetail.tipe;
        menu_type = prdetail.menutype;

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
                            //getOrderDetailList();
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


        mAdapter = new PO_Dtl_RVAdapter(this, kt, menu_type);
        mXRecyclerView.setAdapter(mAdapter);

        fbuttonMenu = view.findViewById(R.id.fbActionMenu_po);
        fbuttonApprove = view.findViewById(R.id.fbApproveAll_po);
        fbuttonCancel = view.findViewById(R.id.fbCancelAll_po);

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

        if (menu_type.equals("Appv2PO")) {
            fbuttonMenu.hideMenu(true);
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPODetail();
    }

    public void getOrderDetailList() {
        List<PO_Dtl> itemList = helper.getDataAll();

        kt.clear();
        for (int i = 0; i < itemList.size(); i++) {
            PO_Dtl items = itemList.get(i);
            PO_Dtl item = new PO_Dtl();
            item.setId(items.getId());
            item.setBranch(items.getBranch());
            item.setDocno(items.getDocno());
            item.setNo(items.getNo());
            item.setDocref(items.getDocref());
            item.setBranchto(items.getBranchto());
            item.setBrgcode(items.getBrgcode());
            item.setBrgname(items.getBrgname());
            item.setBrgdesc(items.getBrgdesc());
            item.setSatcode(items.getSatcode());
            item.setQty(items.getQty());
            item.setIocode(items.getIocode());
            item.setStatus(items.getStatus());
            item.setStatusdesc(items.getStatusdesc());
            item.setNoref(items.getNoref());
            item.setStatusold(items.getStatusold());
            item.setHarga1(items.getHarga1());
            item.setDisc1(items.getDisc1());
            item.setBrutto(items.getBrutto());
            item.setMdisc1(items.getMdisc1());
            item.setDpp(items.getDpp());
            item.setPpn(items.getPpn());
            item.setNetto(items.getNetto());
            kt.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

    void getPODetail() {
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
                    json.put("method", "getPODtl");
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
                                        JSONObject podtl = hasil.getJSONObject(i);
                                        long id = helper.insertData(podtl.getString("branch"), podtl.getString("docno"), podtl.getString("no"), podtl.getString("docref"), podtl.getString("branchto"), podtl.getString("brgcode"), podtl.getString("brgname"), podtl.getString("brgdesc"), podtl.getString("satcode"), podtl.getString("qty"), podtl.getString("iocode"), podtl.getString("status"), podtl.getString("statusdesc"), podtl.getString("statusold"), podtl.getString("harga1"), podtl.getString("disc1"), podtl.getString("brutto"), podtl.getString("mdisc1"), podtl.getString("dpp"), podtl.getString("ppn"), podtl.getString("netto"));
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

    void updateStatus(final List<PO_Dtl> datalist, String statustipe) {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                for (int i = 0; i < datalist.size(); i++) {
                    PO_Dtl item = datalist.get(i);

                    if (statustipe.equals("Approve")) {
                        statusnew = "R";
                        statusdescnew = "Release";
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
