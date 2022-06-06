package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.tanobel.it_yoga.tis_mobile.model.Asset_Detail;
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

public class ScanAsset_Activity extends AppCompatActivity {

    public static EditText tvresult;
    ImageButton btnscan;

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    TextInputLayout txtInputHrg,txtInputDepc,txtInputAccumDepc,txtInputNBV;
    EditText txtAssetName,txtAssetName2,txtlokasiawal,txtlokasiakhir,txtTglAqc,txtQty,txtHrg,txtDepc,txtAccumDepc,txtNBV;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_asset);

        Toolbar toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            getSupportActionBar().setTitle((String) bd.get("menu_name"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pDialog = new ProgressDialog(ScanAsset_Activity.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        tvresult = findViewById(R.id.text_barcode_value);
        btnscan= findViewById(R.id.btn_scan);

        txtAssetName = findViewById(R.id.txtAssetName);
        txtAssetName2 = findViewById(R.id.txtAssetName2);
        txtlokasiawal = findViewById(R.id.txtlokasiawal);
        txtlokasiakhir = findViewById(R.id.txtlokasiakhir);
        txtTglAqc = findViewById(R.id.txtTglAqc);
        txtQty= findViewById(R.id.txtQty);
        txtHrg = findViewById(R.id.txtHrg);
        txtDepc = findViewById(R.id.txtDepc);
        txtAccumDepc = findViewById(R.id.txtAccumDepc);
        txtNBV = findViewById(R.id.txtNBV);

        txtInputHrg = findViewById(R.id.txtInputHrg);
        txtInputDepc = findViewById(R.id.txtInputDepc);
        txtInputAccumDepc = findViewById(R.id.txtInputAccumDepc);
        txtInputNBV = findViewById(R.id.txtInputNBV);

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanAsset_Activity.this, ScannedBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 0) {
            if (requestCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String barcode = data.getStringExtra("barcode");
                    tvresult.setText(barcode);
                    getAssetData(barcode);
                } else {
                    tvresult.setText("No Barcode Found");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    void getAssetData(final String assetcode) {
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
                    json.put("method", "getDataAsset");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) this.getApplication()).getUserCode());
                    data.put("assetcode", assetcode);
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

                                    ArrayList<Asset_Detail> item = JSON.parseObject(hasil, new TypeReference<ArrayList<Asset_Detail>>() {
                                    });

                                    if (item.size()>0) {
                                        for (int i = 0; i < item.size(); i++) {
                                            Asset_Detail item_asset = item.get(i);
                                            txtAssetName.setText(item_asset.getAssetname());
                                            txtAssetName2.setText(item_asset.getAssetname2());
                                            txtlokasiawal.setText(item_asset.getBranchawal());
                                            txtlokasiakhir.setText(item_asset.getBranch());
                                            txtTglAqc.setText(item_asset.getAqcdate());
                                            txtQty.setText(item_asset.getQty());
                                            txtHrg.setText(item_asset.getAqc());
                                            txtDepc.setText(item_asset.getDepc());
                                            txtAccumDepc.setText(item_asset.getAccumdepc());
                                            txtNBV.setText(item_asset.getNbv());
                                        }
                                    }else{
                                        tvresult.setText("");
                                        txtAssetName.setText("");
                                        txtAssetName2.setText("");
                                        txtlokasiawal.setText("");
                                        txtlokasiakhir.setText("");
                                        txtTglAqc.setText("");
                                        txtQty.setText("");
                                        txtHrg.setText("");
                                        txtDepc.setText("");
                                        txtAccumDepc.setText("");
                                        txtNBV.setText("");

                                        Toast toast = Toast.makeText(getApplicationContext(), "Kode Asset Tidak Ditemukan", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                    JSONArray userassetvalue = rslt.getJSONArray("uservalueasset");
                                    for (int h = 0; h < userassetvalue.length(); h++) {
                                        JSONObject assetvalue = userassetvalue.getJSONObject(h);
                                        if (assetvalue.getString("AssetValue").equals("1")) {
                                            txtInputHrg.setVisibility(EditText.VISIBLE);
                                            txtInputDepc.setVisibility(EditText.VISIBLE);
                                            txtInputAccumDepc.setVisibility(EditText.VISIBLE);
                                            txtInputNBV.setVisibility(EditText.VISIBLE);
                                        }else{
                                            txtInputHrg.setVisibility(EditText.GONE);
                                            txtInputDepc.setVisibility(EditText.GONE);
                                            txtInputAccumDepc.setVisibility(EditText.GONE);
                                            txtInputNBV.setVisibility(EditText.GONE);
                                        }
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
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
