package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.PR_Mst;
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

public class PR_Fragment_Master extends Fragment  {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    EditText txtplant,txtordertipe,txttipe,txtbayar,txtcostctr,txttglinput,txttgldoc,txttglbutuh;
    EditText txttglpost,txtinputby,txteditby,txttgledit,txtstatus,txtapv1by,txtapv2by,txtkeperluan;
    ProgressDialog pDialog;
    String plant, docno, tipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pr_master, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        PR_Detail prdetail = (PR_Detail) getActivity();
        plant = prdetail.plant;
        docno = prdetail.textTitle.getText().toString();
        tipe = prdetail.tipe;

        txtplant = view.findViewById(R.id.txtplant);
        txtordertipe = view.findViewById(R.id.txtordertipe);
        txttipe = view.findViewById(R.id.txttipe);
        txtbayar = view.findViewById(R.id.txtbayar);
        txtcostctr = view.findViewById(R.id.txtcostctr);
        txttglinput = view.findViewById(R.id.txttglinput);
        txttgldoc = view.findViewById(R.id.txttgldoc);
        txttglbutuh = view.findViewById(R.id.txttglbutuh);
        txttglpost = view.findViewById(R.id.txttglpost);
        txtinputby = view.findViewById(R.id.txtinputby);
        txteditby = view.findViewById(R.id.txteditby);
        txttgledit = view.findViewById(R.id.txttgledit);
        txtstatus = view.findViewById(R.id.txtstatus);
        txtapv1by = view.findViewById(R.id.txtapv1by);
        txtapv2by = view.findViewById(R.id.txtapv2by);
        txtkeperluan = view.findViewById(R.id.txtkeperluan);

        txtplant.setFocusable(false);
        txtordertipe.setFocusable(false);
        txttipe.setFocusable(false);
        txtbayar.setFocusable(false);
        txtcostctr.setFocusable(false);
        txttglinput.setFocusable(false);
        txttgldoc.setFocusable(false);
        txttglbutuh.setFocusable(false);
        txttglpost.setFocusable(false);
        txtinputby.setFocusable(false);
        txteditby.setFocusable(false);
        txttgledit.setFocusable(false);
        txtstatus.setFocusable(false);
        txtapv1by.setFocusable(false);
        txtapv2by.setFocusable(false);
        txtkeperluan.setFocusable(false);

        getPRMaster(view);

        // Inflate the layout for this fragment
        return view;
    }

    void getPRMaster(final View view) {
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
                    json.put("method", "getPRMst");
                    JSONArray arr2 = new JSONArray();
                    data.put("plant", plant);
                    data.put("docno", docno);
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

                                    ArrayList<PR_Mst> item = JSON.parseObject(hasil, new TypeReference<ArrayList<PR_Mst>>() {
                                    });

                                    for (int i = 0; i < item.size(); i++) {
                                        PR_Mst item_master = item.get(i);
                                        txtplant.setText(item_master.getBranchDesc());
                                        txtordertipe.setText(item_master.getPoorderdesc());
                                        txttipe.setText(item_master.getTipe());
                                        txtbayar.setText(item_master.getAP());
                                        txtcostctr.setText(item_master.getCostctr());
                                        txttglinput.setText(item_master.getTglinput());
                                        txttgldoc.setText(item_master.getTgldoc());
                                        txttglbutuh.setText(item_master.getTglbutuh());
                                        txttglpost.setText(item_master.getTglpost());
                                        txtinputby.setText(item_master.getInputby());
                                        txteditby.setText(item_master.getEditby());
                                        txttgledit.setText(item_master.getTgledit());
                                        txtstatus.setText(item_master.getStatusdesc());
                                        txtapv1by.setText(item_master.getApv1by());
                                        txtapv2by.setText(item_master.getApv2by());
                                        txtkeperluan.setText(item_master.getKeperluan());
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
