package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.content.Intent;

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

public class ForgetPass_Edit_Activity extends AppCompatActivity {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();

    EditText txtpassword, txtconfpassword;
    Button btnsave;
    ProgressDialog pDialog;

    String nik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            nik = (String) bd.get("nik");
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        txtpassword = findViewById(R.id.txtpassword);
        txtconfpassword = findViewById(R.id.txtconfpassword);
        btnsave = findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtpassword.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password masih belum diisi.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if (txtconfpassword.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Confirm password masih belum diisi.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if (!txtpassword.getText().toString().equals(txtconfpassword.getText().toString())) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Password dan confirm password tidak sesuai.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                updatePassword(txtpassword.getText().toString());
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

    void updatePassword(String password) {
        if (internetCon.checkConnection(this)) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "UserMenu");
                    json.put("method", "editPasswordUser");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", nik);
                    data.put("password", password);
                    arr2.put(data);
                    json.put("data", arr2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-usermenu.php", json, this);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(jsonData);
                                    JSONObject rslt = obj.getJSONObject("result");
                                    final String msg = rslt.getString("msg");

                                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                    toast.show();

                                    Intent intent = new Intent(ForgetPass_Edit_Activity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                                    startActivity(intent);
                                    finish();
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
            Toast toast = Toast.makeText(this, "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}

