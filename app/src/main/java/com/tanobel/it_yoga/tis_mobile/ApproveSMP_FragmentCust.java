package com.tanobel.it_yoga.tis_mobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.ApproveSMP_LVAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.model.ApproveSMP_LVList;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ApproveSMP_FragmentCust extends Fragment {

    ArrayList<ApproveSMP_LVList> dataList = new ArrayList<>();
    ListView listView;
    private static ApproveSMP_LVAdapter listAdapter;
    private RequestPost RP;
    private AlertDialog pDialog;
    SwipeRefreshLayout pullToRefresh;
    InternetConnection internetCon = new InternetConnection();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appsmp_cust, container, false);

        pDialog = new SpotsDialog(getActivity(), R.style.Custom);
        listAdapter = new ApproveSMP_LVAdapter(dataList, getActivity().getApplicationContext());

        listView = view.findViewById(R.id.list_cust);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ApproveSMP_LVList ApproveSMP_DataList = dataList.get(position);

                Intent i = new Intent(getActivity(), ApproveSMP_Activity_Dtl.class);
                i.putExtra("Code", ApproveSMP_DataList.getCode());
                i.putExtra("Name", ApproveSMP_DataList.getNama());
                i.putExtra("Type", ApproveSMP_DataList.getTipe());
                i.putExtra("MenuType", "CK");
                i.putExtra("Chanelcode", ApproveSMP_DataList.getChanelcode());
                startActivity(i);

            }
        });

        pullToRefresh = view.findViewById(R.id.pullToRefresh_cust);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItem();
                pullToRefresh.setRefreshing(false);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getItem();
    }

    void getItem() {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "SalesProgram");
                    json.put("method", "getResults");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getActivity().getApplicationContext()).getUserCode());
                    data.put("type", "CK");
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router.php", json, getActivity().getApplicationContext());
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
                                    ArrayList<ApproveSMP_LVList> LVList = JSON.parseObject(hasil, new TypeReference<ArrayList<ApproveSMP_LVList>>() {
                                    });
                                    dataList.clear();
                                    dataList.addAll(LVList);
                                    listAdapter.notifyDataSetChanged();
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}