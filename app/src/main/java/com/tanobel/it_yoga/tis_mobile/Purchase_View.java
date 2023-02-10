package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.View_LVAdapter;
import com.tanobel.it_yoga.tis_mobile.model.View_LVList;
import com.tanobel.it_yoga.tis_mobile.model.View_UserPlant;
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
 * Created by IT_Yoga on 06/02/2019.
 */

public class Purchase_View extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static ArrayList<View_LVList> dataList = new ArrayList<>();
    public static ArrayList<View_LVList> dataALL = new ArrayList<>();
    ListView listView;
    private static View_LVAdapter listAdapter;
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    ArrayList<View_UserPlant> userplantList = new ArrayList<>();
    Spinner spnusrplant;
    Integer spnposusrplant;
    TextView textdtl;
    SwipeRefreshLayout pullToRefresh;
    ProgressDialog pDialog;
    String Branch, menu_type, search_text;
    SearchView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            getSupportActionBar().setTitle((String) bd.get("menu_name"));
            menu_type = (String) bd.get("menu_type");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pDialog = new ProgressDialog(Purchase_View.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        textdtl = findViewById(R.id.text_view_dtl);
        textdtl.setText("Detail PO");

        spnposusrplant = 0;
        spnusrplant = findViewById(R.id.spn_plant_view);
        spnusrplant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                View_UserPlant item = userplantList.get(position);
                Branch = item.getBranch();
                spnposusrplant = position;
                getDataPurchase(Branch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        listAdapter = new View_LVAdapter(getApplicationContext(), menu_type);

        listView = findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View_LVList Unblock_SO_DataList = dataList.get(position);

                if (menu_type.equals("AppvPR")) {
                    Intent i = new Intent(Purchase_View.this, PR_Detail.class);
                    i.putExtra("menu_type", menu_type);
                    i.putExtra("docno", Unblock_SO_DataList.getDocno());
                    i.putExtra("custname", Unblock_SO_DataList.getNama());
                    i.putExtra("plant", Unblock_SO_DataList.getPlant());
                    i.putExtra("custcode", Unblock_SO_DataList.getCode());
                    i.putExtra("tipe", Unblock_SO_DataList.getTipe());
                    startActivity(i);
                }else if(menu_type.equals("AppvPO") || menu_type.equals("Appv2PO")) {
                    Intent j = new Intent(Purchase_View.this, PO_Detail.class);
                    j.putExtra("menu_type", menu_type);
                    j.putExtra("docno", Unblock_SO_DataList.getDocno());
                    j.putExtra("custname", Unblock_SO_DataList.getNama());
                    j.putExtra("plant", Unblock_SO_DataList.getPlant());
                    j.putExtra("custcode", Unblock_SO_DataList.getCode());
                    j.putExtra("tipe", Unblock_SO_DataList.getTipe());
                    startActivity(j);
                }
            }
        });

        // search view
        search_text = "";
        editsearch = findViewById(R.id.search_docno);
        setupSearchView();

        pullToRefresh = findViewById(R.id.pullToRefresh_view);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getDataPurchase(Branch);
               pullToRefresh.setRefreshing(false);
            }
        });

    }

    private void setupSearchView()
    {
        editsearch.setOnQueryTextListener(this);
        editsearch.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search_text = newText;
        listAdapter.filter(search_text);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onStart() {
        super.onStart();
        getUserPlant();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataList.clear();
        dataALL.clear();
    }

    void getUserPlant() {
        if (internetCon.checkConnection(getApplicationContext())) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "Purchase");
                    json.put("method", "getUserPlant");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getApplicationContext()).getUserCode());
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-purchase.php", json, getApplicationContext());
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(jsonData);
                                    JSONObject rslt = obj.getJSONObject("result");
                                    String userplant = rslt.getString("hasil");

                                    ArrayList<View_UserPlant> usrplant = JSON.parseObject(userplant, new TypeReference<ArrayList<View_UserPlant>>() {
                                    });

                                    userplantList.clear();
                                    userplantList.addAll(usrplant);

                                    List<String> spinnerArray = new ArrayList<>();

                                    for (int i = 0; i < usrplant.size(); i++) {
                                        View_UserPlant itemusrplant = usrplant.get(i);
                                        spinnerArray.add(itemusrplant.getDesc());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Purchase_View.this, R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spnusrplant.setAdapter(adapter);
                                    spnusrplant.setSelection(spnposusrplant);
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
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void getDataPurchase(String plant) {
        if (internetCon.checkConnection(getApplicationContext())) {
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "Purchase");
                    if (menu_type.equals("AppvPR")) {
                        json.put("method", "getPurchaseRequisition");
                    } else {
                        json.put("method", "getPurchaseOrder");
                    }
                    JSONArray arr2 = new JSONArray();
                    data.put("plant", plant);
                    data.put("user", ((MainModule) getApplicationContext()).getUserTIS());
                    data.put("tipe", menu_type);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router-purchase.php", json, getApplicationContext());
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(jsonData);
                                    JSONObject rslt = obj.getJSONObject("result");
                                    String hasil = rslt.getString("hasil");
                                    ArrayList<View_LVList> LVList = JSON.parseObject(hasil, new TypeReference<ArrayList<View_LVList>>() {
                                    });

                                    dataList.clear();
                                    dataList.addAll(LVList);

                                    dataALL.clear();
                                    dataALL.addAll(LVList);

                                    listAdapter.filter(search_text);
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
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}