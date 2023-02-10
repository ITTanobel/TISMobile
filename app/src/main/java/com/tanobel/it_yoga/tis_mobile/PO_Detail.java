package com.tanobel.it_yoga.tis_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.model.Db_PO;
import com.tanobel.it_yoga.tis_mobile.model.PO_Dtl;
import com.tanobel.it_yoga.tis_mobile.model.Purchase_PageAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by IT_Yoga on 04/01/2019.
 */

public class PO_Detail extends AppCompatActivity {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();

    Purchase_PageAdapter pagerAdapter;
    ViewPager vp;
    TabLayout tabLayout;
    TextView textTitle, textSubTitle;
    String menutype, plant, custcode, tipe;
    ImageButton btnsave;
    ProgressDialog pDialog;
    Boolean sendemail;

    Db_PO helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po);

        Toolbar toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        textTitle = findViewById(R.id.txt_smp_dtl_title);
        textSubTitle = findViewById(R.id.txt_smp_dtl_subtitle);
        btnsave = findViewById(R.id.btn_po_save);

        helper = new Db_PO(this);
        this.deleteDatabase("DbPO");

        pDialog = new ProgressDialog(PO_Detail.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            menutype = (String) bd.get("menu_type");
            textTitle.setText((String) bd.get("docno"));
            textSubTitle.setText((String) bd.get("custname"));
            plant = (String) bd.get("plant");
            custcode = (String) bd.get("custcode");
            tipe = (String) bd.get("tipe");

            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //VIEWPAGER
        vp = findViewById(R.id.pager_po);
        this.addPages();

        //TABLAYOUT
        tabLayout = findViewById(R.id.tab_layout_po);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        setupTabIcons();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<PO_Dtl> itemList = helper.getDataByStatusOld();
                if (menutype.equals("AppvPO") && itemList.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PO_Detail.this);
                    builder.setMessage("Masih ada item yang belum diapprove / dicancel.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create();
                    builder.show();
                } else {
                    final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(PO_Detail.this)
                            .setMessage("Proses Approve akan dilakukan ?")
                            .setPositiveButton("Ya", null) //Set to null. We override the onclick
                            .setNegativeButton("Tidak", null)
                            .create();

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                        @Override
                        public void onShow(final DialogInterface dialog) {

                            Button button = ((androidx.appcompat.app.AlertDialog) dialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                            button.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    final androidx.appcompat.app.AlertDialog dialog2 = new androidx.appcompat.app.AlertDialog.Builder(PO_Detail.this)
                                            .setMessage("Apakah akan mengirim email otomatis ke supllier ?")
                                            .setPositiveButton("Ya", null) //Set to null. We override the onclick
                                            .setNegativeButton("Tidak", null)
                                            .create();

                                    dialog2.setOnShowListener(new DialogInterface.OnShowListener() {

                                        @Override
                                        public void onShow(final DialogInterface dialog) {

                                            Button button = ((androidx.appcompat.app.AlertDialog) dialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                                            button.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    dialog2.dismiss();
                                                    sendemail = true;
                                                    updateData();
                                                }
                                            });

                                            Button button2 = ((androidx.appcompat.app.AlertDialog) dialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
                                            button2.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    dialog2.dismiss();
                                                    sendemail = false;
                                                    updateData();
                                                }
                                            });
                                        }
                                    });
                                    dialog2.show();
                                }
                            });
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private void setupTabIcons() {
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            View customView = pagerAdapter.getCustomView(this, i);
            tabLayout.getTabAt(i).setCustomView(customView);
        }
    }

    private void addPages() {
        pagerAdapter = new Purchase_PageAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new PO_Fragment_Master());
        pagerAdapter.addFragment(new PO_Fragment_Detail());
        pagerAdapter.addFragment(new PO_Fragment_DetailJdw());

        //SET ADAPTER TO VP
        vp.setAdapter(pagerAdapter);
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

    void updateData() {

        Fragment page = pagerAdapter.getRegisteredFragment(0);
        final PO_Fragment_Master fpomaster = (PO_Fragment_Master) page;

        List<PO_Dtl> ItemListMst;
        if (menutype.equals("AppvPO")) {
            if (helper.getDataGroupByStatus().size() > 0) {
                ItemListMst = helper.getDataGroupByStatus();
            } else {
                ItemListMst = helper.getDataGroupByStatusCancel();
            }
        } else {
            ItemListMst = helper.getDataGroupByStatusRelease();
        }
        List<PO_Dtl> ItemListDtl = helper.getDataAll();

        if (internetCon.checkConnection(getApplicationContext())) {
            RequestPost RP;
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                json.put("menutype", menutype);

                JSONArray arrmst = new JSONArray();
                for (int h = 0; h < ItemListMst.size(); h++) {
                    JSONObject datamst = new JSONObject();
                    PO_Dtl itemmst = ItemListMst.get(h);
                    datamst.put("user", ((MainModule) getApplicationContext()).getUserTIS());
                    datamst.put("branch", itemmst.getBranch());
                    datamst.put("docno", itemmst.getDocno());
                    datamst.put("tipe", tipe);
                    datamst.put("tipeorder", fpomaster.tipeorder);
                    datamst.put("status", itemmst.getStatus());
                    datamst.put("sendemail", sendemail);
                    arrmst.put(datamst);
                }
                json.put("datamst", arrmst);

                JSONArray arrdtl = new JSONArray();
                for (int i = 0; i < ItemListDtl.size(); i++) {
                    JSONObject datadtl = new JSONObject();
                    PO_Dtl itemdtl = ItemListDtl.get(i);
                    datadtl.put("branch", itemdtl.getBranch());
                    datadtl.put("docno", itemdtl.getDocno());
                    datadtl.put("no", itemdtl.getNo());
                    datadtl.put("tipe", tipe);
                    datadtl.put("status", itemdtl.getStatus());
                    arrdtl.put(datadtl);
                }
                json.put("datadtl", arrdtl);

                RP = new RequestPost("classes/Purchase_Update.php", json, getApplicationContext());
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (pDialog != null) pDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (pDialog != null) pDialog.dismiss();
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        final String jsonData = response.body().string();
                        try {
                            JSONObject obj = new JSONObject(jsonData);
                            JSONObject result = obj.getJSONObject("result");
                            final int success = result.getInt("success");
                            final String msg = result.getString("message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PO_Detail.this);
                                    builder.setTitle("Informasi");
                                    builder.setMessage(msg);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            if (success == 1) {
                                                onBackPressed();
                                            }
                                        }
                                    });
                                    builder.create();
                                    builder.show();
                                }
                            });

                        } catch (Exception e) {
                            if (pDialog != null) pDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                });

            } catch (Exception e) {
                if (pDialog != null) pDialog.dismiss();
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
