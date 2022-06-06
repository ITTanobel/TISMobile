package com.tanobel.it_yoga.tis_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.model.CRM_SalesOrder_PageAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by IT_Yoga on 04/01/2019.
 */

public class Unblock_SO_Detail extends AppCompatActivity {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();

    CRM_SalesOrder_PageAdapter pagerAdapter;
    ViewPager vp;
    TabLayout tabLayout;
    TextView textTitle, textSubTitle;
    String plant, custcode;
    ImageButton btnsave;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock_so);

        Toolbar toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        textTitle = findViewById(R.id.txt_smp_dtl_title);
        textSubTitle = findViewById(R.id.txt_smp_dtl_subtitle);
        btnsave = findViewById(R.id.btn_unblock_so_save);

        pDialog = new ProgressDialog(Unblock_SO_Detail.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            textTitle.setText((String) bd.get("docno"));
            textSubTitle.setText((String) bd.get("custname"));
            plant = (String) bd.get("plant");
            custcode = (String) bd.get("custcode");

            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //VIEWPAGER
        vp = findViewById(R.id.pager_unblock_so);
        this.addPages();

        //TABLAYOUT
        tabLayout = findViewById(R.id.tab_layout_unblock_so);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        setupTabIcons();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO something when floating action menu first item clicked
                AlertDialog.Builder builder = new AlertDialog.Builder(Unblock_SO_Detail.this);
                builder.setMessage("Proses unblock akan dilakukan ?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateData(plant, textTitle.getText().toString(), "R");
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateData(plant, textTitle.getText().toString(), "C");
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                builder.create();
                builder.show();
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
        pagerAdapter = new CRM_SalesOrder_PageAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new Unblock_SO_Fragment_Master());
        pagerAdapter.addFragment(new Unblock_SO_Fragment_Detail());

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

    void updateData(String plant, String docno, String status) {
        if (internetCon.checkConnection(getApplicationContext())) {
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "Unblock_SO");
                    json.put("method", "updateStatus");
                    JSONArray arr2 = new JSONArray();
                    data.put("plant", plant);
                    data.put("docno", docno);
                    data.put("status", status);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-unblockso.php", json, getApplicationContext());
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
                        if (response.isSuccessful()) {
                            final String jsonData = response.body().string();
                            try {
                                JSONObject obj = new JSONObject(jsonData);
                                JSONObject result = obj.getJSONObject("result");
                                final int success = result.getInt("success");
                                final String msg = result.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Unblock_SO_Detail.this);
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
