package com.tanobel.it_yoga.tis_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.model.ApproveSMP_RVList;
import com.tanobel.it_yoga.tis_mobile.model.ApproveSMP_RVAdapter;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by IT_Yoga on 24/09/2018.
 */

public class ApproveSMP_FragmentBnsTmbhn extends Fragment {

    private XRecyclerView mXRecyclerView;
    private ApproveSMP_RVAdapter mAdapter;
    private ArrayList<ApproveSMP_RVList> kt = new ArrayList<>();
    private int refreshTime = 0;
    private int times = 0;
    int currentPage = 0;
    private int totalqty = 0;
    private int limit = 50;
    private RequestPost RP;
    boolean next;
    ProgressDialog pDialog;
    String ccustcode, menutype, message;
    InternetConnection internetCon = new InternetConnection();

    FloatingActionMenu fbuttonMenu;
    FloatingActionButton fbuttonApprove, fbuttonCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_appsmp_bnstmbhn, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        TextView textTitle = getActivity().findViewById(R.id.txt_smp_dtl_title);
        TextView textMenuType = getActivity().findViewById(R.id.txt_smp_dtl_tipe);

        menutype = textMenuType.getText().toString().trim();
        ccustcode = textTitle.getText().toString().trim();

        mXRecyclerView = view.findViewById(R.id.rv_bnstmbhn);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        kt.clear();
                        //Log.i("onRefresh","dipanggil");
                        setCurrentPage(0);
                        if (getActivity().getApplicationContext() != null) {
                            getData();
                        }
                        mAdapter.notifyDataSetChanged();
                        mXRecyclerView.refreshComplete();
                    }
                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if ((totalqty / limit) > getCurrentPage()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            setCurrentPage(getCurrentPage() + 1);
                            if (getActivity().getApplicationContext() != null) {
                                getData();
                            }
                            mXRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                            mXRecyclerView.refreshComplete();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            mXRecyclerView.loadMoreComplete();
                        }
                    }, 1000);
                }
                times++;
            }
        });

        mAdapter = new ApproveSMP_RVAdapter(this    , kt, menutype + "_BNSTMBHN", ccustcode);
        mXRecyclerView.setAdapter(mAdapter);

        fbuttonMenu = view.findViewById(R.id.fbActionMenu_bnstmbhn);
        fbuttonApprove = view.findViewById(R.id.fbApproveAll_bnstmbhn);
        fbuttonCancel = view.findViewById(R.id.fbCancelAll_bnstmbhn);

        fbuttonApprove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Yakin mau approve semua ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateRecord(kt, "1");
                                dialog.dismiss();
                                fbuttonMenu.close(true);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        fbuttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Yakin mau cancel semua ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateRecord(kt, "C");
                                dialog.dismiss();
                                fbuttonMenu.close(true);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    void getData() {
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
                    data.put("type", menutype + "_BNSTMBHN");
                    data.put("custcode",ccustcode);
                    data.put("chanelcode","");
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
                                    ArrayList<ApproveSMP_RVList> RVList = JSON.parseObject(hasil, new TypeReference<ArrayList<ApproveSMP_RVList>>() {
                                    });
                                    kt.clear();
                                    kt.addAll(RVList);
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

    void updateRecord(final ArrayList<ApproveSMP_RVList> datalist, String status) {
        if (internetCon.checkConnection(getActivity().getApplicationContext())) {
            RequestPost RP;
            if (!isRemoving()) {
                pDialog.show();
            }
            try {
                next = true;
                for (int i = 0; i < datalist.size(); i++) {
                    ApproveSMP_RVList item = datalist.get(i);

                    if (i==datalist.size()-1) {
                        message = "YES";
                    }else {
                        message = "NO";
                    }

                    JSONObject json = new JSONObject();
                    json.put("action", "SalesProgram");
                    json.put("method", "updateRecord");
                    JSONObject data = new JSONObject();
                    data.put("type", menutype + "_BNSTMBHN");
                    data.put("param1", ccustcode);
                    data.put("param2", item.getKdbrg());
                    data.put("param3", item.getTglmulai());
                    data.put("param4", item.getTglakhir());
                    data.put("status", status);
                    data.put("approveby", ((MainModule) getActivity().getApplicationContext()).getUserTIS().toUpperCase());
                    data.put("message", message);
                    JSONArray arr2 = new JSONArray();
                    arr2.put(data);
                    json.put("data", arr2);
                    RP = new RequestPost("router.php", json, getActivity().getApplicationContext());
                    RP.execPostCall(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (pDialog != null) pDialog.dismiss();
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);
                            if (response.isSuccessful()) {
                                final String jsonData = response.body().string();
                                try {
                                    JSONObject obj = new JSONObject(jsonData);
                                    JSONObject result = obj.getJSONObject("result");
                                    final int success = result.getInt("success");
                                    final String vmsg = result.getString("message");
                                    final String msg = result.getString("msg");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if((success == 1) && (vmsg.indexOf("Y")>-1)){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Informasi");
                                                builder.setMessage(msg);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.create();
                                                builder.show();

                                                getData();
                                            }else if (success == 0 && next) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Informasi");
                                                builder.setMessage("Terdapat kesalahan update data, beberapa data tidak terupdate");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.create();
                                                builder.show();

                                                getData();
                                                next = false;
                                            }
                                        }
                                    });

                                } catch (Exception e) {
                                    if (pDialog != null) pDialog.dismiss();
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                }

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
