package com.tanobel.it_yoga.tis_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class RegisterActivity extends AppCompatActivity {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();

    EditText txtnik, txtname, txtemail, txtpassword, txtconfpassword;
    Spinner spngenre;
    Button btnregister;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        txtnik = findViewById(R.id.txtnik);
        txtname = findViewById(R.id.txtname);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        txtconfpassword = findViewById(R.id.txtconfpassword);
        spngenre = findViewById(R.id.spngenre);
        btnregister = findViewById(R.id.btnregister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterValidated();
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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    void RegisterValidated() {
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
                                        if (hasil.length() > 0) {
                                            Toast toast = Toast.makeText(RegisterActivity.this, "NIK sudah digunakan", Toast.LENGTH_SHORT);
                                            toast.show();
                                            if (pDialog != null) pDialog.dismiss();
                                            return;
                                        }
                                    }

                                    if (txtname.getText().toString().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Nama masih belum diisi.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    if (spngenre.getSelectedItemPosition() == 0) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Genre masih belum dipilih.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    if (txtemail.getText().toString().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Email masih belum diisi.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    if (!isEmailValid(txtemail.getText())) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Email tidak valid.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    if (txtpassword.getText().toString().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Password masih belum diisi.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    if (txtconfpassword.getText().toString().equals("")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Confirm password masih belum diisi.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    if (!txtpassword.getText().toString().equals(txtconfpassword.getText().toString())) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Password dan confirm password tidak sesuai.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        if (pDialog != null) pDialog.dismiss();
                                        return;
                                    }

                                    insertUser();

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

    void insertUser() {
        if (internetCon.checkConnection(this)) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "UserMenu");
                    json.put("method", "insertUser");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", txtnik.getText());
                    data.put("name", txtname.getText());
                    data.put("password", txtpassword.getText());
                    data.put("email", txtemail.getText());
                    data.put("genre", spngenre.getSelectedItem());
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

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

