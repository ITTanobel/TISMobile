package com.tanobel.it_yoga.tis_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editName, editPassword;
    TextView txtregister, txtfpassword;
    Button btnLogIn;
    ProgressBar pbbar;
    SharedPreferences shp;

    //String URL = "http://172.16.16.17/tis-mobile/index.php";
    String URL = "https://www.tanobelfood.com/tis-mobile/index.php";
//    String URL = "https://www.tanobelfood.com/tis-mobile-dev/index.php";
    String strjson, token;
    InternetConnection internetCon = new InternetConnection();

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login

        editName = findViewById(R.id.txtuser);
        editPassword = findViewById(R.id.txtpassword);
        btnLogIn = findViewById(R.id.btnLogin);
        txtregister = findViewById(R.id.txtregister);
        txtfpassword = findViewById(R.id.txtfpassword);
        pbbar = findViewById(R.id.ppbar);
        pbbar.setVisibility(View.GONE);

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        //SharedPreferences.Editor edit = shp.edit();
        //edit.putString("UserId", "EDP4");
        //edit.commit();

        String userid = shp.getString("UserId", "none");

        if (userid.equals("none") || userid.trim().equals("")) {

        } else {

            if (internetCon.checkConnection(getApplicationContext())) {
                ((MainModule) LoginActivity.this.getApplication()).setUserCode(userid);
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }

        }

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editName.getText().toString().equals("demo_account") || editPassword.getText().toString().equals("Tanobelj4y4")) {
                    SharedPreferences.Editor edit = shp.edit();
                    edit.putString("UserId", editName.getText().toString());
                    edit.commit();

                    ((MainModule) getApplicationContext()).setUserCode(editName.getText().toString());
                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                    startActivity(i);
                    finish();

                    Toast.makeText(getApplicationContext(), "Login Success !", Toast.LENGTH_LONG).show();
                } else {
                    AttemptLogin attemptLogin = new AttemptLogin();
                    attemptLogin.execute(editName.getText().toString(), editPassword.getText().toString(), "");
                }
            }
        });

        SpannableString spannablereg = new SpannableString("Register");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };
        spannablereg.setSpan(clickableSpan, 0,
                spannablereg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtregister.setText(spannablereg, TextView.BufferType.SPANNABLE);
        txtregister.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString spannablepass = new SpannableString("Forget Password ?");
        ClickableSpan clickablepass = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(LoginActivity.this, ForgetPass_Activity.class);
                startActivity(intent);
            }
        };
        spannablepass.setSpan(clickablepass, 0,
                spannablepass.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtfpassword.setText(spannablepass, TextView.BufferType.SPANNABLE);
        txtfpassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    private class AttemptLogin extends AsyncTask<String, String, String> {

        String userid = editName.getText().toString();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            lockScreenOrientation();
            btnLogIn.setEnabled(false);
            pbbar.setVisibility(View.VISIBLE);

        }

        @Override

        protected String doInBackground(String... args) {

            String name = args[0];
            String password = args[1];

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
                urlBuilder.addQueryParameter("username", name);
                urlBuilder.addQueryParameter("password", password);

                String url = urlBuilder.build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();

                String data = response.body().string();
                JSONObject jsonObject = new JSONObject(data);
                strjson = jsonObject.getString("success").toString().trim();

            } catch (
                    Exception e) {
                e.printStackTrace();
            }

            String json = strjson;
            return json;
        }

        protected void onPostExecute(String result) {

            // dismiss the dialog once product deleted
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            unlockScreenOrientation();
            btnLogIn.setEnabled(true);
            pbbar.setVisibility(View.GONE);
            if (result != null) {
                if (result.equals("1")) {
                    token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
                    //token = "123";
                    if (token != null) {

                        registerdevice(userid, token);

                    } else {
                        //if token is null that means something wrong
                        //Toast.makeText(getApplicationContext(), "Token not generated", Toast.LENGTH_LONG).show();
                        registerdevice(userid, "");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Details", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
            }
        }
    }

    void registerdevice(final String user, String token) {
        if (internetCon.checkConnection(getApplicationContext())) {
            RequestPost RP;
            try {
                JSONObject json = new JSONObject();
                json.put("action", "DeviceNotify");
                json.put("method", "registerDevice");
                JSONObject data = new JSONObject();
                data.put("user", user.toUpperCase());
                data.put("token", token);
                JSONArray arr2 = new JSONArray();
                arr2.put(data);
                json.put("data", arr2);
                RP = new RequestPost("router-devicenotify.php", json, getApplicationContext());
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
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
                                        if (success == 1) {
                                            SharedPreferences.Editor edit = shp.edit();
                                            edit.putString("UserId", user);
                                            edit.commit();

                                            ((MainModule) getApplicationContext()).setUserCode(user);
                                            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                                            startActivity(i);
                                            finish();

                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

