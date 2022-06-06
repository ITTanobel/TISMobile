package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ForgetPass_Activity extends AppCompatActivity {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();

    EditText txtnik;
    Button btnnext;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        txtnik = findViewById(R.id.txtNIK);
        btnnext = findViewById(R.id.btnnext);

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNIK();
            }
        });
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

    void getNIK() {
        if (internetCon.checkConnection(this)) {
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "UserMenu");
                    json.put("method", "getNIK");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", txtnik.getText());
                    arr2.put(data);
                    json.put("data", arr2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-usermenu.php", json, this);
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (pDialog != null) pDialog.dismiss();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                                    JSONArray hasil = rslt.getJSONArray("hasil");

                                    if (txtnik.getText().toString().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "NIK masih belum diisi.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    } else {
                                        if (hasil.length() <= 0) {
                                            Toast toast = Toast.makeText(ForgetPass_Activity.this, "NIK tidak terdaftar / tidak ditemukan", Toast.LENGTH_SHORT);
                                            toast.show();
                                            if (pDialog != null) pDialog.dismiss();
                                            return;
                                        }
                                    }

                                    for (int h = 0; h < hasil.length(); h++) {
                                        JSONObject usermst = hasil.getJSONObject(h);
                                        sendVerifyCode(usermst.getString("email"));
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
            Toast toast = Toast.makeText(this, "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void sendVerifyCode(final String email) {
        if (internetCon.checkConnection(this)) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "UserMenu");
                    json.put("method", "sendCodeVerify");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", txtnik.getText());
                    data.put("email", email);
                    arr2.put(data);
                    json.put("data", arr2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-usermenu.php", json, this);
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                                    final int success = rslt.getInt("success");
                                    final String msg = rslt.getString("msg");

                                    if (success == 1) {
                                        Intent intent = new Intent(ForgetPass_Activity.this, ForgetPass_Verify_Activity.class);
                                        intent.putExtra("nik", txtnik.getText().toString());
                                        intent.putExtra("message", msg);
                                        startActivity(intent);
                                    }

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
            Toast toast = Toast.makeText(this, "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

