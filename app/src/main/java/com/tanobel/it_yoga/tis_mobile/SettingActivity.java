package com.tanobel.it_yoga.tis_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences shp;
    private TextView textViewToken;
    private EditText editPass, editPassConf;
    ListView listView;
    String SettingList[] = {"Edit Password"};
    String pass, passconf;
    InternetConnection internetCon = new InternetConnection();
    private RequestPost RP;
    TextView txtusername, txtuseremail;
    ImageView imguser;
    String genre, imggenre;

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Edit Password",
    };


    int[] listviewImage = new int[]{
            R.drawable.ic_edit_password,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_setting);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(R.id.navigation1).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation2).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation3).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation4).setChecked(true);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 1; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.imgiconsetting, R.id.txtsetting};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.list_item_setting, from, to);
        listView = findViewById(R.id.list_setting);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (position == 0) {
                    showInputDialog();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "TES", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        });

        View headerLayout = navigationView.getHeaderView(0);

        txtusername = headerLayout.findViewById(R.id.txtusername);
        txtuseremail = headerLayout.findViewById(R.id.txtuseremail);
        imguser = headerLayout.findViewById(R.id.image_user);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            txtusername.setText((String) bd.get("username"));
            txtuseremail.setText((String) bd.get("useremail"));
            genre = (String) bd.get("genre");
            byte[] bmparray = ((MainModule) this.getApplication()).getUserImage();

            if (bmparray == null) {
                if (genre.equals("MALE")) {
                    imggenre = "male_user";
                } else {
                    imggenre = "female_user";
                }

                imguser.setImageResource(getResources().getIdentifier(imggenre, "mipmap", getPackageName()));
                imguser.setBackgroundResource(R.drawable.user_shape);
            } else {
                Bitmap bmp = BitmapFactory.decodeByteArray(bmparray, 0, bmparray.length);
                imguser.setImageBitmap(bmp);
                imguser.setBackgroundResource(R.drawable.image_border);
            }
        }

        /**
         textViewToken = findViewById(R.id.txtSetting);
         String token = SharedPrefManager.getInstance(this).getDeviceToken();

         //if token is not null
         if (token != null) {
         //displaying the token
         textViewToken.setText(token);
         } else {
         //if token is null that means something wrong
         textViewToken.setText("Token not generated");
         }
         **/
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_setting);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(R.id.navigation1).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation2).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation3).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation4).setChecked(true);

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        String userid = shp.getString("UserId", "none");

        if (userid.equals("none") || userid.trim().equals("")) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                Intent h = new Intent(SettingActivity.this, MenuActivity.class);
                h.putExtra("username", txtusername.getText());
                h.putExtra("useremail", txtuseremail.getText());
                h.putExtra("genre", genre);
                startActivity(h);
                break;
            case R.id.navigation2:
                Intent i = new Intent(SettingActivity.this, ProfilActivity.class);
                i.putExtra("username", txtusername.getText());
                i.putExtra("useremail", txtuseremail.getText());
                i.putExtra("genre", genre);
                startActivity(i);
                break;
            case R.id.navigation3:
                ((MainModule) this.getApplication()).setPurpose("Menu");
                Intent j = new Intent(SettingActivity.this, MenuActivity.class);
                j.putExtra("username", txtusername.getText());
                j.putExtra("useremail", txtuseremail.getText());
                j.putExtra("genre", genre);
                startActivity(j);
                break;
            case R.id.navigation4:
                Intent k = new Intent(SettingActivity.this, SettingActivity.class);
                k.putExtra("username", txtusername.getText());
                k.putExtra("useremail", txtuseremail.getText());
                k.putExtra("genre", genre);
                startActivity(k);
                break;
            case R.id.navigation5:
                SharedPreferences.Editor edit = shp.edit();
                edit.putString("UserId", "");
                edit.commit();

                Intent l = new Intent(SettingActivity.this, LoginActivity.class);
                l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                startActivity(l);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(SettingActivity.this);
        View editpassdialog = layoutInflater.inflate(R.layout.edit_password_dialog, null);

        editPass = editpassdialog.findViewById(R.id.txteditpassword);
        editPassConf = editpassdialog.findViewById(R.id.txteditpasswordconf);

        final AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                .setView(editpassdialog)
                .setPositiveButton("Simpan", null) //Set to null. We override the onclick
                .setNegativeButton("Batal", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        pass = editPass.getText().toString().trim();
                        passconf = editPassConf.getText().toString().trim();

                        if (pass.equals("") || passconf.equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Password  / confirm password tidak boleh kosong.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            if (pass.equals(passconf)) {
                                editpassword(passconf);
                                dialog.dismiss();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Password tidak sama.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    void editpassword(String password) {
        if (internetCon.checkConnection(this)) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "UserMenu");
                    json.put("method", "editPasswordUser");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) this.getApplication()).getUserCode());
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
