package com.tanobel.it_yoga.tis_mobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.ApproveSMP_PageAdapter;
import com.tanobel.it_yoga.tis_mobile.model.UserDetail_List;
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


public class ApproveSMP_Activity extends AppCompatActivity {

    ApproveSMP_PageAdapter pagerAdapter;
    ViewPager vp;
    TabLayout tabLayout;

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    ArrayList<UserDetail_List> dataList = new ArrayList<>();

    SharedPreferences shp;
    ProgressDialog pDialog;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_smp);

        Toolbar toolbar = findViewById(R.id.toolbar_smp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Approve Sales Program");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        getDetailUser();

        //VIEWPAGER
        vp = findViewById(R.id.pager);
        this.addPages();

        //TABLAYOUT
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(vp);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
        pagerAdapter = new ApproveSMP_PageAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new ApproveSMP_FragmentCust(), "Mst");
        pagerAdapter.addFragment(new ApproveSMP_FragmentSO(), "Mst");

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
        ((MainModule) this.getApplication()).setPurpose("");
    }

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

                                            for (int i = 0; i < dataList.size(); i++) {
                                                UserDetail_List item = dataList.get(i);
                                                ((MainModule) ApproveSMP_Activity.this.getApplication()).setUserTIS(item.getUsertis());

                                                if (pDialog != null) pDialog.dismiss();
                                            }
                                        }else{
                                            Toast toast = Toast.makeText(ApproveSMP_Activity.this, "Status user non aktif", Toast.LENGTH_SHORT);
                                            toast.show();

                                            SharedPreferences.Editor edit = shp.edit();
                                            edit.putString("UserId", "");
                                            edit.commit();
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
}
