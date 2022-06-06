package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.PO_Mst;
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

public class PO_Fragment_Master extends Fragment  {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    EditText txtplant,txttipeorder,txttipe,txtap,txtincppn,txtppn,txtsp,txtket,txttgldoc,txttglpost,txttglinput,txtinputby;
    EditText txtbtskirim,txtstatus1,txtapprove1,txtstatus2,txtapprove2,txtsuppcode,txtsuppname,txtsuppalamat,txtpurchorg;
    EditText txtdiserahkan,txtalmtdiserahkan,txtkotadiserahkan,txtmu,txtrate,txttop,txtbrutto,txtdisc,txtdpp,txtmppn,txtnetto;
    Switch swcincppn, swcppn, swcsp;
    ProgressDialog pDialog;
    String plant, docno, tipe, tipeorder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_po_master, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        PO_Detail podetail = (PO_Detail) getActivity();
        plant = podetail.plant;
        docno = podetail.textTitle.getText().toString();
        tipe = podetail.tipe;

        txtplant = view.findViewById(R.id.txtplant);
        txttipeorder = view.findViewById(R.id.txttipeorder);
        txttipe = view.findViewById(R.id.txttipe);
        txtap = view.findViewById(R.id.txtap);
        txtincppn = view.findViewById(R.id.txtincppn);
        txtppn = view.findViewById(R.id.txtppn);
        txtsp = view.findViewById(R.id.txtsp);
        txtket = view.findViewById(R.id.txtket);
        txttgldoc = view.findViewById(R.id.txttgldoc);
        txttglpost = view.findViewById(R.id.txttglpost);
        txttglinput = view.findViewById(R.id.txttglinput);
        txtinputby = view.findViewById(R.id.txtinputby);
        txtbtskirim = view.findViewById(R.id.txtbtskirim);
        txtstatus1 = view.findViewById(R.id.txtstatus1);
        txtapprove1 = view.findViewById(R.id.txtapprove1);
        txtstatus2 = view.findViewById(R.id.txtstatus2);
        txtapprove2 = view.findViewById(R.id.txtapprove2);
        txtsuppcode = view.findViewById(R.id.txtsuppcode);
        txtsuppname = view.findViewById(R.id.txtsuppname);
        txtsuppalamat = view.findViewById(R.id.txtsuppalamat);
        txtpurchorg = view.findViewById(R.id.txtpurchorg);
        txtdiserahkan = view.findViewById(R.id.txtdiserahkan);
        txtalmtdiserahkan = view.findViewById(R.id.txtalmtdiserahkan);
        txtkotadiserahkan = view.findViewById(R.id.txtkotadiserahkan);
        txtmu = view.findViewById(R.id.txtmu);
        txtrate = view.findViewById(R.id.txtrate);
        txttop = view.findViewById(R.id.txttop);
        txtbrutto = view.findViewById(R.id.txtbrutto);
        txtdisc = view.findViewById(R.id.txtdisc);
        txtdpp = view.findViewById(R.id.txtdpp);
        txtmppn = view.findViewById(R.id.txtmppn);
        txtnetto = view.findViewById(R.id.txtnetto);
        swcincppn = view.findViewById(R.id.swcincppn);
        swcppn = view.findViewById(R.id.swcppn);
        swcsp = view.findViewById(R.id.swcsp);

        txtplant.setFocusable(false);
        txttipeorder.setFocusable(false);
        txttipe.setFocusable(false);
        txtap.setFocusable(false);
        txtincppn.setFocusable(false);
        txtppn.setFocusable(false);
        txtsp.setFocusable(false);
        txtket.setFocusable(false);
        txttgldoc.setFocusable(false);
        txttglpost.setFocusable(false);
        txttglinput.setFocusable(false);
        txtinputby.setFocusable(false);
        txtbtskirim.setFocusable(false);
        txtstatus1.setFocusable(false);
        txtapprove1.setFocusable(false);
        txtstatus2.setFocusable(false);
        txtapprove2.setFocusable(false);
        txtsuppcode.setFocusable(false);
        txtsuppname.setFocusable(false);
        txtsuppalamat.setFocusable(false);
        txtpurchorg.setFocusable(false);
        txtdiserahkan.setFocusable(false);
        txtalmtdiserahkan.setFocusable(false);
        txtkotadiserahkan.setFocusable(false);
        txtmu.setFocusable(false);
        txtrate.setFocusable(false);
        txttop.setFocusable(false);
        txtbrutto.setFocusable(false);
        txtdisc.setFocusable(false);
        txtdpp.setFocusable(false);
        txtmppn.setFocusable(false);
        txtnetto.setFocusable(false);
        swcincppn.setClickable(false);
        swcppn.setClickable(false);
        swcsp.setClickable(false);

        getPOMaster(view);
      
        // Inflate the layout for this fragment
        return view;
    }

    void getPOMaster(final View view) {
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
                    json.put("method", "getPOMst");
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

                                    ArrayList<PO_Mst> item = JSON.parseObject(hasil, new TypeReference<ArrayList<PO_Mst>>() {
                                    });

                                    for (int i = 0; i < item.size(); i++) {
                                        PO_Mst item_master = item.get(i);
                                        txtplant.setText(item_master.getPlant());
                                        txttipeorder.setText(item_master.getPoorderdesc());
                                        txttipe.setText(item_master.getTipe());
                                        txtap.setText(item_master.getAp());
                                        txtket.setText(item_master.getKeterangan());
                                        txttgldoc.setText(item_master.getDocdate());
                                        txttglpost.setText(item_master.getPostdate());
                                        txttglinput.setText(item_master.getInputdate());
                                        txtinputby.setText(item_master.getInputby());
                                        txtbtskirim.setText(item_master.getBtskirimtskirim());
                                        txtstatus1.setText(item_master.getStatus1());
                                        txtapprove1.setText(item_master.getReleaseby1());
                                        txtstatus2.setText(item_master.getStatus2());
                                        txtapprove2.setText(item_master.getReleaseby());
                                        txtsuppcode.setText(item_master.getSuppcode());
                                        txtsuppname.setText(item_master.getSuppname());
                                        txtsuppalamat.setText(item_master.getSuppaddress());
                                        txtpurchorg.setText(item_master.getCompany());
                                        txtdiserahkan.setText(item_master.getBrcode());
                                        txtalmtdiserahkan.setText(item_master.getAlamatbr());
                                        txtkotadiserahkan.setText(item_master.getKotabr());
                                        txtmu.setText(item_master.getMucode());
                                        txtrate.setText(item_master.getRate());
                                        txttop.setText(item_master.getTop());
                                        txtbrutto.setText(item_master.getBrutto());
                                        txtdisc.setText(item_master.getDisc());
                                        txtdpp.setText(item_master.getDpp());
                                        txtmppn.setText(item_master.getMppn());
                                        txtnetto.setText(item_master.getNetto());

                                        if (item_master.getIncppn().equals("1")) {
                                            txtincppn.setText("YES");
                                            swcincppn.setChecked(true);
                                        } else {
                                            txtincppn.setText("NO");
                                            swcincppn.setChecked(false);
                                        }

                                        if (item_master.getPpn().equals("1")) {
                                            txtppn.setText("YES");
                                            swcppn.setChecked(true);
                                        } else {
                                            txtppn.setText("NO");
                                            swcppn.setChecked(false);
                                        }

                                        if (item_master.getSp().equals("1")) {
                                            txtsp.setText("YES");
                                            swcsp.setChecked(true);
                                        } else {
                                            txtsp.setText("NO");
                                            swcsp.setChecked(false);
                                        }

                                        tipeorder = item_master.getPoorder();

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
