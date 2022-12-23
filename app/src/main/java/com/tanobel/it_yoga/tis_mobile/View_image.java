package com.tanobel.it_yoga.tis_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tanobel.it_yoga.tis_mobile.model.Db_GR;
import com.tanobel.it_yoga.tis_mobile.model.GR_Dtl;
import com.tanobel.it_yoga.tis_mobile.model.Purchase_PageAdapter;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class View_image extends AppCompatActivity {
    String docno,plant,user,usage,title;
    ProgressDialog pDialog;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    //private RecyclerViewAdapter imageRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.list_image);

        pDialog = new ProgressDialog(View_image.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        usage = "image_sj";
        if (bd != null) {
            docno = (String) bd.get("docno");
            plant = (String) bd.get("plant");
            user = (String) bd.get("user");
            usage = (String) bd.get("usage");
            title = "View Image";
            if (Objects.equals(usage, "image_qc")){
                title = "Image QC Inspection";
            }else if (Objects.equals(usage, "image_incoming")){
                title = "Image Incoming Inspection";
            }
            Log.i("Usage Image",title);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setSubtitle((String) bd.get("docno"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //imagesRV = (RecyclerView) imagesRV;
        View_image_adapter imgAdapter = new View_image_adapter(View_image.this,docno);
        imagesRV.setAdapter(imgAdapter);
        imagesRV.setLayoutManager(layoutManager);
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
}