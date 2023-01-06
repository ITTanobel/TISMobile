package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.tanobel.it_yoga.tis_mobile.model.Menu_GVAdapter;
import com.tanobel.it_yoga.tis_mobile.model.Menu_GVList;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //SubMenuFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */

public class SubMenuFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Menu_GVList> dataList = new ArrayList<>();
    GridView gridView;
    private static Menu_GVAdapter listAdapter;
    private ImageButton btnback;
    private RequestPost RP;
    private ProgressDialog pDialog;
    InternetConnection internetCon = new InternetConnection();

    public SubMenuFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubMenuFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static SubMenuFragment2 newSubInstance(String param1, String param2) {
        SubMenuFragment2 fragment = new SubMenuFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MenuActivity) getActivity()).getSupportActionBar().setSubtitle(mParam2);

        btnback = getActivity().findViewById(R.id.btnback_menu);
        btnback.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        listAdapter = new Menu_GVAdapter(dataList, getActivity().getApplicationContext());

        gridView = view.findViewById(R.id.grid);
        gridView.setAdapter(listAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Menu_GVList Menu_DataList = dataList.get(position);
                String code = Menu_DataList.getCode();

                switch (code.trim()) {
                    case "APV01":
                        Intent a = new Intent(getActivity(), Purchase_View.class);
                        a.putExtra("menu_name", Menu_DataList.getNama());
                        a.putExtra("menu_type", "AppvPR");
                        startActivity(a);
                        break;
                    case "APV02":
                        Intent b = new Intent(getActivity(), Purchase_View.class);
                        b.putExtra("menu_name", Menu_DataList.getNama());
                        b.putExtra("menu_type", "AppvPO");
                        startActivity(b);
                        break;
                    case "APV03":
                        Intent c = new Intent(getActivity(), Purchase_View.class);
                        c.putExtra("menu_name", Menu_DataList.getNama());
                        c.putExtra("menu_type", "Appv2PO");
                        startActivity(c);
                        break;
                    case "APV04":
                        Intent d = new Intent(getActivity(), GoodsReceipt_View.class);
                        d.putExtra("menu_name", Menu_DataList.getNama());
                        d.putExtra("menu_type", "AppvLPB");
                        startActivity(d);
                        break;
                    case "APV05":
                        Intent e = new Intent(getActivity(), GoodsReceipt_View.class);
                        e.putExtra("menu_name", Menu_DataList.getNama());
                        e.putExtra("menu_type", "Appv2LPB");
                        startActivity(e);
                        break;
                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataList.clear();
        if (((MainModule) getActivity().getApplicationContext()).getUserCode().equals("demo_account")) {
            getItemDemo();
        } else  {
            getItem();
        }
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
                    data.put("level", "2");
                    data.put("parent", mParam2);
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
        Menu_GVList list1;
        Menu_GVList list2;
        Menu_GVList list3;

        switch (mParam1) {
            case "1":
                list1 = new Menu_GVList("APV01", "Approve PR", "ic_pr", "crm_shape", 0);
                list2 = new Menu_GVList("APV02", "Approve PO", "ic_po", "apv_sls_shape", 0);
                list3 = new Menu_GVList("APV03", "Approve 2 PO", "ic_po2", "unblck_shape", 0);

                dataList.add(list1);
                dataList.add(list2);
                dataList.add(list3);
                break;
        }

        listAdapter.notifyDataSetChanged();
    }
}
