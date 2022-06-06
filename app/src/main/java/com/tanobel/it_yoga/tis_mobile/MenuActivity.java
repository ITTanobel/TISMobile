package com.tanobel.it_yoga.tis_mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.security.identity.SessionTranscriptMismatchException;
import android.util.Base64;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.UserDetail_List;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    SharedPreferences shp;
    ImageButton btnback;

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    ArrayList<UserDetail_List> dataList = new ArrayList<>();

    public static Activity ActMain;
    private FragmentManager fragmentManager;
    public String Purpose = "";
    public int counter;
    TextView txtusername, txtuseremail;
    ImageView imguser;
    String genre, imggenre, versionname;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        ActMain = this;
        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        btnback = findViewById(R.id.btnback_menu);

        versionname = BuildConfig.VERSION_NAME;

        String userid = shp.getString("UserId", "none");

        if (!userid.equals("demo_account")) {
            getDetailUser();
        } else {
            getDetailUserDemo();
        }

        if (userid.equals("none") || userid.trim().equals("")) {
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        } else {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.nav_menu);
            navigationView.setNavigationItemSelectedListener(this);

            navigationView.getMenu().findItem(R.id.navigation1).setChecked(false);
            navigationView.getMenu().findItem(R.id.navigation2).setChecked(false);
            navigationView.getMenu().findItem(R.id.navigation3).setChecked(true);
            navigationView.getMenu().findItem(R.id.navigation4).setChecked(false);

            fragmentManager = getSupportFragmentManager();
       /* *getSupportFragmentManager = inisialisasi nilai FragmentManager untuk berinteraksi
            dengan Activity saat ini
         */
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //memulai transaction fragment manager

            MenuFragment menuFragment = new MenuFragment();
            //membuat object fragmentPertama
            transaction.add(R.id.frame_content, menuFragment);
            //menambahkan fragment
            //transaction.addToBackStack("MenuFragment");
            //dapat menyimpan fragment ke dalam state ,ketika tombol back diklik
            transaction.commit();
            //mengeksekusi fragment transaction

            initInstances();

            btnback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        fragmentManager.popBackStack();

                    }
                }
            });
        }

        if (!userid.equals("demo_account")) {
            getVersion();
        }
    }

    private void initInstances() {
        rootLayoutAndroid = findViewById(R.id.menu_layout_bar);
        collapsingToolbarLayoutAndroid = findViewById(R.id.collapsing_toolbar_android_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(R.id.navigation1).setChecked(true);
        navigationView.getMenu().findItem(R.id.navigation2).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation3).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation4).setChecked(false);

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        String userid = shp.getString("UserId", "none");

        if (userid.equals("none") || userid.trim().equals("")) {
            finish();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navigation1:
                ((MainModule) this.getApplication()).setPurpose("Menu");
                Intent h = new Intent(MenuActivity.this, MenuActivity.class);
                h.putExtra("username", txtusername.getText());
                h.putExtra("useremail", txtuseremail.getText());
                h.putExtra("genre", genre);
                startActivity(h);
                break;
            case R.id.navigation2:
                Intent i = new Intent(MenuActivity.this, ProfilActivity.class);
                i.putExtra("username", txtusername.getText());
                i.putExtra("useremail", txtuseremail.getText());
                i.putExtra("genre", genre);
                startActivity(i);
                break;
            case R.id.navigation3:
                ((MainModule) this.getApplication()).setPurpose("Menu");
                Intent j = new Intent(MenuActivity.this, MenuActivity.class);
                j.putExtra("username", txtusername.getText());
                j.putExtra("useremail", txtuseremail.getText());
                j.putExtra("genre", genre);
                break;
            case R.id.navigation4:
                Intent k = new Intent(MenuActivity.this, SettingActivity.class);
                k.putExtra("username", txtusername.getText());
                k.putExtra("useremail", txtuseremail.getText());
                k.putExtra("genre", genre);
                startActivity(k);
                break;
            case R.id.navigation5:
                SharedPreferences.Editor edit = shp.edit();
                edit.putString("UserId", "");
                edit.commit();

                Intent l = new Intent(MenuActivity.this, LoginActivity.class);
                l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                startActivity(l);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void getVersion() {
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
                    json.put("method", "getVersion");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) this.getApplication()).getUserCode());
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

                                    JSONObject mversion = hasil.getJSONObject(0);
                                    if (!versionname.equals(mversion.getString("version").trim())) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                                        builder.setTitle("Informasi");
                                        builder.setMessage("Aplikasi perlu diupdate, update sekarang?");
                                        builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                                finish();
                                            }
                                        });
                                        builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                        builder.create();
                                        builder.show();
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

    @SuppressLint("SetTextI18n")
    void getDetailUser() {
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
                    json.put("method", "getDetailUser");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) this.getApplication()).getUserCode());
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
                                    JSONArray userstatus = rslt.getJSONArray("userstatus");
                                    for (int h = 0; h < userstatus.length(); h++) {
                                        JSONObject statususer = userstatus.getJSONObject(h);
                                        if (statususer.getString("Status").equals("1")) {
                                            String hasil = rslt.getString("hasil");

                                            ArrayList<UserDetail_List> UserList = JSON.parseObject(hasil, new TypeReference<ArrayList<UserDetail_List>>() {
                                            });
                                            dataList.addAll(UserList);

                                            final String[] imggenre = {""};
                                            txtusername = findViewById(R.id.txtusername);
                                            txtuseremail = findViewById(R.id.txtuseremail);
                                            imguser = findViewById(R.id.image_user);

                                            for (int i = 0; i < dataList.size(); i++) {
                                                UserDetail_List item = dataList.get(i);
                                                txtusername.setText(item.getNama());
                                                txtuseremail.setText(item.getEmail());
                                                genre = item.getGenre().trim();

                                                if (item.getImgprofil().trim().equals("")) {
                                                    if (item.getGenre().trim().equals("MALE")) {
                                                        imggenre[0] = "male_user";
                                                    } else {
                                                        imggenre[0] = "female_user";
                                                    }

                                                    imguser.setImageResource(getResources().getIdentifier(imggenre[0], "mipmap", getPackageName()));
                                                    imguser.setBackgroundResource(R.drawable.user_shape);
                                                    ((MainModule) getApplicationContext()).setUserImage(null);
                                                } else {
                                                    byte[] decodeString = Base64.decode(item.getImgprofil().trim(), Base64.DEFAULT);
                                                    Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                                                    imguser.setImageBitmap(decodebitmap);
                                                    imguser.setBackgroundResource(R.drawable.image_border);

                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                    decodebitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                    ((MainModule) getApplicationContext()).setUserImage(stream.toByteArray());
                                                }

                                                ((MainModule) MenuActivity.this.getApplication()).setUserTIS(item.getUsertis());
                                                if (pDialog != null) pDialog.dismiss();
                                            }
                                        } else {
                                            Toast toast = Toast.makeText(MenuActivity.this, "Status user non aktif", Toast.LENGTH_SHORT);
                                            toast.show();

                                            SharedPreferences.Editor edit = shp.edit();
                                            edit.putString("UserId", "");
                                            edit.commit();

                                            Intent l = new Intent(MenuActivity.this, LoginActivity.class);
                                            l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                                            startActivity(l);
                                            finish();
                                        }
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

    void getDetailUserDemo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtusername  = findViewById(R.id.txtusername);
                txtuseremail = findViewById(R.id.txtuseremail);
                imguser = findViewById(R.id.image_user);

                txtusername.setText("demo_account");
                txtuseremail.setText("demo_account@Demo.com");
                genre = "MALE";

                imguser.setImageResource(getResources().getIdentifier("male_user", "mipmap", getPackageName()));
                imguser.setBackgroundResource(R.drawable.user_shape);
                ((MainModule) getApplicationContext()).setUserImage(null);                }
        },500);
    }
}
