package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.model.Menu_GVAdapter;
import com.tanobel.it_yoga.tis_mobile.model.Menu_GVList;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MenuFragment extends Fragment {
    ArrayList<Menu_GVList> dataList = new ArrayList<>();
    GridView gridView;
    private static Menu_GVAdapter listAdapter;
    private ImageButton btnback;
    private RequestPost RP;
    private ProgressDialog pDialog;
    InternetConnection internetCon = new InternetConnection();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        btnback = getActivity().findViewById(R.id.btnback_menu);
        btnback.setVisibility(View.INVISIBLE);

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        listAdapter = new Menu_GVAdapter(dataList, getActivity().getApplicationContext());
        dataList.clear();

        if (((MainModule) getActivity().getApplicationContext()).getUserCode().equals("demo_account")) {
            getItemDemo();
        } else  {
            getItem();
        }

        gridView = view.findViewById(R.id.grid);
        gridView.setAdapter(listAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Menu_GVList Menu_DataList = dataList.get(position);
                SubMenuFragment1 subMenuFragment1 = SubMenuFragment1.newSubInstance(Integer.toString(position), Menu_DataList.getCode());

                //buat object fragmentkedua
                getFragmentManager().beginTransaction()
                        .replace(R.id.frame_content, subMenuFragment1)
                        //menggant fragment
                        .addToBackStack(null)
                        //menyimpan fragment
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        //transisi fragment
                        .commit();
                //mengeksekusi fragment transaction

            }
        });

        // Inflate the layout for this fragment
        return view;
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
                    json.put("action", "UserMenu");
                    json.put("method", "getResults");
                    JSONArray arr2 = new JSONArray();
                    data.put("user",  ((MainModule) getActivity().getApplicationContext()).getUserCode());
                    data.put("level", "0");
                    data.put("parent", "");
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
                RP = new RequestPost("router-usermenu.php", json, getActivity().getApplicationContext());
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
                                    ArrayList<Menu_GVList> GVList = JSON.parseObject(hasil, new TypeReference<ArrayList<Menu_GVList>>() {
                                    });
                                    dataList.addAll(GVList);
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

    void getItemDemo() {
        Menu_GVList list1 = new Menu_GVList("CRM00", "CRM", "ic_crm", "crm_shape", 0);
        Menu_GVList list2 = new Menu_GVList("APP00", "Approval", "ic_apv", "apv_shape", 0);
        Menu_GVList list3 = new Menu_GVList("UBC00", "Unblocked", "ic_unblck", "unblck_shape", 0);
        Menu_GVList list4 = new Menu_GVList("AST00", "Asset", "ic_asset", "apv_purc_shape", 0);
        Menu_GVList list5 = new Menu_GVList("UTY00", "Utility", "ic_utility", "utility_shape", 0);

        dataList.add(list1);
        dataList.add(list2);
        dataList.add(list3);
        dataList.add(list4);
        dataList.add(list5);

        listAdapter.notifyDataSetChanged();
    }
}
