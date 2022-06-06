package com.tanobel.it_yoga.tis_mobile;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.UserDetail_List;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences shp;
    TextView txtusername, txtuseremail;
    EditText txtuserprofil, txtusertis, txtnameprofil, txtemailprofil;
    ImageView imguser, imgeditprofil;
    CircleImageView imgprofil;
    String genre, imggenre;
    InternetConnection internetCon = new InternetConnection();
    private RequestPost RP;
    ArrayList<UserDetail_List> dataList = new ArrayList<>();
    ProgressDialog pDialog;
    byte[] byteArray, bmparray;
    String encodedImage;

    private int requestCode;
    private int grantResults[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //if you dont have required permissions ask for it (only required for API 23+)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);


            onRequestPermissionsResult(requestCode, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        setSupportActionBar(toolbar);

        txtuserprofil = findViewById(R.id.txtuser_profil);
        txtusertis = findViewById(R.id.txtuser_tis);
        txtnameprofil = findViewById(R.id.txtnama_profil);
        txtemailprofil = findViewById(R.id.txtemail_profil);
        imgprofil = findViewById(R.id.imguser_profil);
        imgeditprofil = findViewById(R.id.imgedit_profil);

        imgprofil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        imgeditprofil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });

        String userid = shp.getString("UserId", "none");

        if (!userid.equals("demo_account")) {
            getDetailUser();
        } else {
            getDetailUserDemo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_profil);
        navigationView.setNavigationItemSelectedListener(this);

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

            bmparray = ((MainModule) this.getApplication()).getUserImage();

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

        navigationView.getMenu().findItem(R.id.navigation1).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation2).setChecked(true);
        navigationView.getMenu().findItem(R.id.navigation3).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation4).setChecked(false);

    }

    public void ChooseImage() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(
                Environment.MEDIA_CHECKING)) {
            Intent intent_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent_gallery, 1);
        } else {
            Toast.makeText(ProfilActivity.this,
                    "No activity found to perform this task",
                    Toast.LENGTH_SHORT).show();

        }
    }

    private static int getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (width < height) {
            return height;
        }else{
            return width;
        }
    }

    protected void showInputDialog() {

        ImageView image = new ImageView(this);
        bmparray = ((MainModule) this.getApplication()).getUserImage();

        if (bmparray == null) {
            if (genre.equals("MALE")) {
                imggenre = "male_user";
            } else {
                imggenre = "female_user";
            }

            image.setImageResource(getResources().getIdentifier(imggenre, "mipmap", getPackageName()));
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(bmparray, 0, bmparray.length);
            image.setImageBitmap(bmp);
        }

        Dialog d = new Dialog(this, R.style.DialogTheme);
        d.setContentView(image);
        d.getWindow().setLayout((int) (getScreenResolution(this) * 0.5),(int) (getScreenResolution(this) * 0.5));
        d.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            //Toast.makeText(ProfilActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(
                        selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);

            } catch (FileNotFoundException e) {
                Toast.makeText(ProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            if (originBitmap != null) {

                Bitmap resized;
                double newWidth;

                if (originBitmap.getWidth() > 280 && originBitmap.getWidth() > originBitmap.getHeight()) {
                    newWidth = 280d / (double) originBitmap.getWidth();
                    resized = Bitmap.createScaledBitmap(originBitmap, (int) (originBitmap.getWidth() * newWidth), (int) (originBitmap.getHeight() * newWidth), true);
                } else if (originBitmap.getWidth() > 280 && originBitmap.getHeight() > originBitmap.getWidth()) {
                    newWidth = 280d / (double) originBitmap.getHeight();
                    resized = Bitmap.createScaledBitmap(originBitmap, (int) (originBitmap.getWidth() * newWidth), (int) (originBitmap.getHeight() * newWidth), true);
                } else {
                    resized = originBitmap;
                }

                this.imgprofil.setImageBitmap(resized);
                this.imguser.setImageBitmap(resized);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                ((MainModule) getApplicationContext()).setUserImage(byteArray);

                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                updateImageProfil();
                //Toast.makeText(ProfilActivity.this, encodedImage.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d("permission", "granted");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.uujm
                    Toast.makeText(ProfilActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

                    //app cannot function without this permission for now so close it...
                    onDestroy();
                }
                return;
            }

            // other 'case' line to check fosr other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_profil);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(R.id.navigation1).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation2).setChecked(true);
        navigationView.getMenu().findItem(R.id.navigation3).setChecked(false);
        navigationView.getMenu().findItem(R.id.navigation4).setChecked(false);

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
                Intent h = new Intent(ProfilActivity.this, MenuActivity.class);
                h.putExtra("username", txtusername.getText());
                h.putExtra("useremail", txtuseremail.getText());
                h.putExtra("genre", genre);
                startActivity(h);
                break;
            case R.id.navigation2:
                Intent i = new Intent(ProfilActivity.this, ProfilActivity.class);
                i.putExtra("username", txtusername.getText());
                i.putExtra("useremail", txtuseremail.getText());
                i.putExtra("genre", genre);
                startActivity(i);
                break;
            case R.id.navigation3:
                ((MainModule) this.getApplication()).setPurpose("Menu");
                Intent j = new Intent(ProfilActivity.this, MenuActivity.class);
                j.putExtra("username", txtusername.getText());
                j.putExtra("useremail", txtuseremail.getText());
                j.putExtra("genre", genre);
                startActivity(j);
                break;
            case R.id.navigation4:
                Intent k = new Intent(ProfilActivity.this, SettingActivity.class);
                k.putExtra("username", txtusername.getText());
                k.putExtra("useremail", txtuseremail.getText());
                k.putExtra("genre", genre);
                startActivity(k);
                break;
            case R.id.navigation5:
                SharedPreferences.Editor edit = shp.edit();
                edit.putString("UserId", "");
                edit.commit();

                Intent l = new Intent(ProfilActivity.this, LoginActivity.class);
                l.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY); // To clean up all activities
                startActivity(l);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                                    String hasil = rslt.getString("hasil");
                                    ArrayList<UserDetail_List> UserList = JSON.parseObject(hasil, new TypeReference<ArrayList<UserDetail_List>>() {
                                    });
                                    dataList.addAll(UserList);

                                    final String[] imggenreprofil = {""};
                                    for (int i = 0; i < dataList.size(); i++) {
                                        UserDetail_List item = dataList.get(i);
                                        txtuserprofil.setText(item.getCode());
                                        txtusertis.setText(item.getUsertis());
                                        txtnameprofil.setText(item.getNama());
                                        txtemailprofil.setText(item.getEmail());

                                        if (item.getImgprofil().trim().equals("")) {
                                            if (item.getGenre().trim().equals("MALE")) {
                                                imggenreprofil[0] = "male_user";
                                            } else {
                                                imggenreprofil[0] = "female_user";
                                            }

                                            imgprofil.setImageResource(getResources().getIdentifier(imggenreprofil[0], "mipmap", getPackageName()));
                                            imgprofil.setBackgroundResource(R.drawable.user_shape);
                                        } else {
                                            byte[] decodeString = Base64.decode(item.getImgprofil().trim(), Base64.DEFAULT);
                                            Bitmap decodebitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                                            imgprofil.setImageBitmap(decodebitmap);
                                            imgprofil.setBackgroundResource(R.drawable.image_border);
                                        }

                                        if (pDialog != null) pDialog.dismiss();
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
                txtuserprofil.setText("demo_account");
                txtusertis.setText("demo_account");
                txtnameprofil.setText("demo_account");
                txtemailprofil.setText("demo_account@Demo.com");

                imgprofil.setImageResource(getResources().getIdentifier("male_user", "mipmap", getPackageName()));
                imgprofil.setBackgroundResource(R.drawable.user_shape);
            }
        },500);
    }

    void updateImageProfil() {
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
                    json.put("method", "editImageProfil");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) this.getApplication()).getUserCode());
                    data.put("image", encodedImage);
                    arr2.put(data);
                    json.put("data", arr2);
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
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
                                    final String msg = rslt.getString("msg");

                                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                    toast.show();

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
}
