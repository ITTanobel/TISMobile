package com.tanobel.it_yoga.tis_mobile;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_CustKirim;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_DataMaster;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_UserPlant;
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

public class CRM_FragmentSO_Master extends Fragment {

    ArrayList<CRM_SO_UserPlant> userplantList = new ArrayList<>();
    ArrayList<CRM_SO_CustKirim> custKirimList = new ArrayList<>();
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    EditText txtNomor, txtcust, txttgl, txtstatus, txtalamat, txtketerangan;
    Spinner spncust, spnusrplant;
    int spnpos, spnposusrplant;
    String menutype, docno, custkrm, alamatcust, custcode;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        spnpos = 0;
        alamatcust = "";

        View view = inflater.inflate(R.layout.fragment_so_master, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        CRM_SalesOrder crm_salesOrder = (CRM_SalesOrder) getActivity();
        menutype = crm_salesOrder.menutype;
        docno = crm_salesOrder.docno;

        txtNomor = view.findViewById(R.id.txt_so_noorder);
        spnusrplant = view.findViewById(R.id.spn_so_userplant);
        txtcust = view.findViewById(R.id.txt_so_custid);
        txttgl = view.findViewById(R.id.txt_so_tglinput);
        txtstatus = view.findViewById(R.id.txt_so_status);
        spncust = view.findViewById(R.id.spn_so_kirim);
        txtalamat = view.findViewById(R.id.txt_so_alamat);
        txtketerangan = view.findViewById(R.id.txt_so_ket);

        txtNomor.setEnabled(false);
        txtcust.setEnabled(false);
        txttgl.setEnabled(false);
        txtstatus.setEnabled(false);
        txtalamat.setEnabled(false);

        if (menutype.equals("View") || menutype.equals("Cancel")){
            spnusrplant.setEnabled(false);
            spncust.setEnabled(false);
            txtketerangan.setEnabled(false);
        }

        if (menutype.equals("Edit") || menutype.equals("View") || menutype.equals("Cancel")){
            txtNomor.setText(docno);
        }

        spnusrplant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CRM_SO_UserPlant item = userplantList.get(position);
                custcode = item.getCustCode();

                CRM_SalesOrder crm_salesOrder = (CRM_SalesOrder) getActivity();
                crm_salesOrder.custcode = custcode;

                getCustKirim(item.getCustCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spncust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CRM_SO_CustKirim item_alamat = custKirimList.get(position);
                txtalamat.setText(item_alamat.getAlamat());
                custkrm = item_alamat.getKode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (menutype.equals("Input")) {
            getOrderMaster();
        } else {
            getPSOByDocno();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("spnposusrplant", spnusrplant.getSelectedItemPosition());
        outState.putInt("spnpos", spncust.getSelectedItemPosition());
        outState.putString("alamatcust", txtalamat.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            spnposusrplant = savedInstanceState.getInt("spnposusrplant");
            spnpos = savedInstanceState.getInt("spnpos");
            alamatcust = savedInstanceState.getString("alamatcust");
        }
    }

    void getOrderMaster() {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getOrderMaster");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getActivity().getApplicationContext()).getUserCode());
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router-crmsalesorder.php", json, getActivity().getApplicationContext());
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
                                    String userplant = rslt.getString("userplant");

                                    ArrayList<CRM_SO_DataMaster> item = JSON.parseObject(hasil, new TypeReference<ArrayList<CRM_SO_DataMaster>>() {
                                    });

                                    for (int i = 0; i < item.size(); i++) {
                                        CRM_SO_DataMaster item_master = item.get(i);
                                        //txtNomor.setText(item_master.getPrefix() + item_master.getSufix());
                                        txtcust.setText(((MainModule) getActivity().getApplicationContext()).getUserCode().toUpperCase());
                                        txttgl.setText(item_master.getTgl());
                                        txtstatus.setText(item_master.getStatus());
                                    }

                                    ArrayList<CRM_SO_UserPlant> usrplant = JSON.parseObject(userplant, new TypeReference<ArrayList<CRM_SO_UserPlant>>() {
                                    });

                                    userplantList.clear();
                                    userplantList.addAll(usrplant);

                                    List<String> spinnerArray = new ArrayList<>();

                                    for (int i = 0; i < usrplant.size(); i++) {
                                        CRM_SO_UserPlant itemusrplant = usrplant.get(i);
                                        spinnerArray.add(itemusrplant.getDesc());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spnusrplant.setAdapter(adapter);

                                    spnusrplant.setSelection(spnposusrplant);
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

    void getCustKirim(String custcode) {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getCustKirimMst");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getActivity().getApplicationContext()).getUserCode());
                    data.put("custcode", custcode);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router-crmsalesorder.php", json, getActivity().getApplicationContext());
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

                                    ArrayList<CRM_SO_CustKirim> custkrm = JSON.parseObject(hasil, new TypeReference<ArrayList<CRM_SO_CustKirim>>() {
                                    });

                                    custKirimList.clear();
                                    custKirimList.addAll(custkrm);

                                    List<String> spinnerArray = new ArrayList<>();

                                    for (int i = 0; i < custkrm.size(); i++) {
                                        CRM_SO_CustKirim itemccust = custkrm.get(i);
                                        spinnerArray.add(itemccust.getNama());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spncust.setAdapter(adapter);

                                    spncust.setSelection(spnpos);
                                    txtalamat.setText(alamatcust);
                                    spnpos = 0;
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

    void getPSOByDocno() {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getPreSOMstByDocno");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getActivity().getApplicationContext()).getUserCode());
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
                                    JSONArray cust = rslt.getJSONArray("custcode");
                                    String userplant = rslt.getString("userplant");
                                    String custkirim = rslt.getString("custkirim");
                                    final String customercode[] = new String[1];
                                    final String customerkrm[] = new String[1];

                                    for (int i=0; i<hasil.length(); i++) {
                                        JSONObject psomst = hasil.getJSONObject(i);
                                        txtcust.setText(psomst.getString("custid"));
                                        txttgl.setText(psomst.getString("tglinput"));
                                        txtstatus.setText(psomst.getString("statusdesc"));
                                        txtketerangan.setText(psomst.getString("ket"));
                                        customerkrm[0] = psomst.getString("custkirim");
                                    }

                                    JSONObject custmst = cust.getJSONObject(0);
                                    customercode[0] = custmst.getString("CustCode");

                                    ArrayList<CRM_SO_UserPlant> usrplant = JSON.parseObject(userplant, new TypeReference<ArrayList<CRM_SO_UserPlant>>() {
                                    });

                                    userplantList.clear();
                                    userplantList.addAll(usrplant);

                                    List<String> spinnerArray0 = new ArrayList<>();

                                    for (int i = 0; i < usrplant.size(); i++) {
                                        CRM_SO_UserPlant itemusrplant = usrplant.get(i);
                                        spinnerArray0.add(itemusrplant.getDesc());

                                        if (itemusrplant.getCustCode().equals(customercode[0])) {
                                            spnposusrplant = i;
                                        }
                                    }

                                    ArrayAdapter<String> adapter0 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_so, spinnerArray0);
                                    adapter0.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spnusrplant.setAdapter(adapter0);

                                    spnusrplant.setSelection(spnposusrplant);

                                    ArrayList<CRM_SO_CustKirim> custkrm = JSON.parseObject(custkirim, new TypeReference<ArrayList<CRM_SO_CustKirim>>() {
                                    });

                                    custKirimList.clear();
                                    custKirimList.addAll(custkrm);

                                    List<String> spinnerArray = new ArrayList<>();

                                    for (int i = 0; i < custkrm.size(); i++) {
                                        CRM_SO_CustKirim itemcust = custkrm.get(i);
                                        spinnerArray.add(itemcust.getNama());

                                        if (itemcust.getKode().equals(customerkrm[0])) {
                                            spnpos = i;
                                        }
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spncust.setAdapter(adapter);

                                    spncust.setSelection(spnpos);
                                    txtalamat.setText(alamatcust);
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

