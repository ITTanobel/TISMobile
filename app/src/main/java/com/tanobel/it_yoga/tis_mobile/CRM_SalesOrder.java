package com.tanobel.it_yoga.tis_mobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_Barang;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_OrderDetail;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SalesOrder_PageAdapter;
import com.tanobel.it_yoga.tis_mobile.model.Db_CRM_SO;
import com.tanobel.it_yoga.tis_mobile.util.DateInputMask;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by IT_Yoga on 14/11/2018.
 */

public class CRM_SalesOrder extends AppCompatActivity {

    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    ArrayList<CRM_SO_Barang> custBarangList = new ArrayList<>();

    CRM_SalesOrder_PageAdapter pagerAdapter;
    ViewPager vp;
    TabLayout tabLayout;
    ImageButton btnadd, btnsave;
    EditText editdate, qty;
    TextView textTitle, textSubTitle;
    ImageButton btntanggal;
    Spinner spnbrg, spnsatuan;
    private XRecyclerView mXRecyclerView;
    ProgressDialog pDialog;
    String title, subtitle, menutype, docno;
    public String custcode;
    Fragment mContent;
    Date nowdate, getdate;
    String strndate, strgdate;
    int maxday, minday, diffday;

    Db_CRM_SO helper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_so);

        Toolbar toolbar = findViewById(R.id.toolbar_so);
        setSupportActionBar(toolbar);

        textTitle = findViewById(R.id.txt_smp_dtl_title);
        textSubTitle = findViewById(R.id.txt_smp_dtl_subtitle);

        helper = new Db_CRM_SO(this);
        this.deleteDatabase("DbCRM");

        btnadd = findViewById(R.id.btn_so_add);
        btnsave = findViewById(R.id.btn_so_save);

        pDialog = new ProgressDialog(CRM_SalesOrder.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            textTitle.setText((String) bd.get("menu_name"));
            menutype = (String) bd.get("menu_type");
            docno = (String) bd.get("docno");

            if (menutype.equals("Input")) {
                textSubTitle.setText("");
            } else {
                textSubTitle.setText((String) bd.get("docno"));
            }
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (menutype.equals("View")) {
            btnadd.setVisibility(ImageButton.INVISIBLE);
            btnsave.setVisibility(ImageButton.INVISIBLE);
        }

        if (menutype.equals("Cancel")) {
            btnadd.setVisibility(ImageButton.INVISIBLE);
        }

        //VIEWPAGER
        vp = findViewById(R.id.pager_so);
        this.addPages();

        //TABLAYOUT
        tabLayout = findViewById(R.id.tab_layout_so);
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


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1);
                showInputDialog();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment page = pagerAdapter.getRegisteredFragment(0);
                CRM_FragmentSO_Master fsomaster = (CRM_FragmentSO_Master) page;

                Fragment page2 = pagerAdapter.getRegisteredFragment(1);
                CRM_FragmentSO_Detail fsodtl = (CRM_FragmentSO_Detail) page2;

                if (fsomaster.spncust.getSelectedItemPosition() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Harap pilih customer kirim", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (fsodtl.kt.size() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Harap masukkan detail item", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (menutype.equals("Input") || menutype.equals("Edit")) {
                        insertData();
                    }

                    if (menutype.equals("Cancel")) {
                        cancelData();
                    }
                }
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
        pagerAdapter = new CRM_SalesOrder_PageAdapter(this.getSupportFragmentManager());
        pagerAdapter.addFragment(new CRM_FragmentSO_Master());
        pagerAdapter.addFragment(new CRM_FragmentSO_Detail());

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
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CRM_SalesOrder.this);
        View editpassdialog = layoutInflater.inflate(R.layout.add_sales_order, null);

        getBarang(editpassdialog, custcode);

        spnbrg = editpassdialog.findViewById(R.id.spn_so_barang);
        spnsatuan = editpassdialog.findViewById(R.id.spn_so_satuan);

        spnbrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CRM_SO_Barang item_alamat = custBarangList.get(position);

                List<String> spinnerArray = new ArrayList<>();
                spinnerArray.add(item_alamat.getSatcode());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CRM_SalesOrder.this, R.layout.spinner_item_so, spinnerArray);
                adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                spnsatuan.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        qty = editpassdialog.findViewById(R.id.txt_so_qty);
        editdate = editpassdialog.findViewById(R.id.txt_so_tglkirim);
        editdate.setFocusable(false);

        new DateInputMask(editdate);

        btntanggal = editpassdialog.findViewById(R.id.btn_so_tglkirim);
        btntanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_SalesOrder.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                            nowdate = mdformat.parse(mdformat.format(calendar.getTime()));
                            strndate = mdformat.format(calendar.getTime());

                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                            editdate.setText(dateFormatter.format(newDate.getTime()));
                            getdate = mdformat.parse(mdformat.format(newDate.getTime()));
                            strgdate = mdformat.format(newDate.getTime());

                            getMaxMinDay();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(CRM_SalesOrder.this)
                .setView(editpassdialog)
                .setPositiveButton("Tambahkan", null) //Set to null. We override the onclick
                .setNegativeButton("Batal", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((androidx.appcompat.app.AlertDialog) dialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (spnbrg.getSelectedItemPosition() == 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Harap pilih barang", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (qty.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Harap isi quantity", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (editdate.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Harap isi tanggal kirim", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (getdate.before(nowdate)) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Tanggal kirim tidak boleh kurang dari tanggal hari ini.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (diffday > maxday) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Tanggal kirim maksimal " + String.valueOf(maxday) + " hari dari tanggal hari ini.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (diffday < minday) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Tanggal kirim minimal " + String.valueOf(minday) + " hari dari tanggal hari ini.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else  {
                            CRM_SO_Barang item = custBarangList.get(spnbrg.getSelectedItemPosition());
                            List<CRM_SO_OrderDetail> itemList = helper.getDataByBrgCodeTglKrm(item.getKode(), String.valueOf(editdate.getText()));
                            if (itemList.size()==0) {
                                if (menutype.equals("Input")) {
                                    long id = helper.insertData("0001", item.getKode(), item.getNama(), Integer.parseInt(String.valueOf(qty.getText())), String.valueOf(spnsatuan.getSelectedItem()), String.valueOf(editdate.getText()));
                                    if (id <= 0) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Ada kegagalan tambah item", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        Fragment page = pagerAdapter.getRegisteredFragment(1);
                                        CRM_FragmentSO_Detail fsodetail = (CRM_FragmentSO_Detail) page;
                                        fsodetail.getOrderDetail();
                                    }
                                    dialog.dismiss();
                                }

                                if (menutype.equals("Edit")) {
                                    List<CRM_SO_OrderDetail> itemtglkrm = helper.getDataByTglKrm(String.valueOf(editdate.getText()));
                                    if (itemtglkrm.size()>0) {
                                        long id = helper.insertData("0001", item.getKode(), item.getNama(), Integer.parseInt(String.valueOf(qty.getText())), String.valueOf(spnsatuan.getSelectedItem()), String.valueOf(editdate.getText()));
                                        if (id <= 0) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Ada kegagalan tambah item", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            Fragment page = pagerAdapter.getRegisteredFragment(1);
                                            CRM_FragmentSO_Detail fsodetail = (CRM_FragmentSO_Detail) page;
                                            fsodetail.getOrderDetail();
                                        }
                                        dialog.dismiss();
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "tidak dapat menambahkan barang dengan tanggal kirim yang berbeda", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Barang dan tanggal kirim sudah diinput", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    void getBarang(final View view, String custcode) {
        if (internetCon.checkConnection(getApplicationContext())) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getBarang");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getApplicationContext()).getUserCode());
                    data.put("custcode", custcode);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-crmsalesorder.php", json, getApplicationContext());
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
                                    String hasil = rslt.getString("hasil");
                                    ArrayList<CRM_SO_Barang> brg = JSON.parseObject(hasil, new TypeReference<ArrayList<CRM_SO_Barang>>() {
                                    });

                                    custBarangList.clear();
                                    custBarangList.addAll(brg);

                                    List<String> spinnerArray = new ArrayList<>();

                                    for (int i = 0; i < brg.size(); i++) {
                                        CRM_SO_Barang item = brg.get(i);
                                        spinnerArray.add(item.getNama());
                                    }

                                    spnbrg = view.findViewById(R.id.spn_so_barang);

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CRM_SalesOrder.this, R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spnbrg.setAdapter(adapter);
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
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void getMaxMinDay() {
        if (internetCon.checkConnection(getApplicationContext())) {
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getMaxMinDay");
                    JSONArray arr2 = new JSONArray();
                    data.put("nowdate", strndate);
                    data.put("newdate", strgdate);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-crmsalesorder.php", json, getApplicationContext());
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
                                    JSONArray hasil = rslt.getJSONArray("hasil");

                                    JSONObject result = hasil.getJSONObject(0);
                                    maxday = result.getInt("nMaxdelvday");
                                    minday = result.getInt("nMindelvday");
                                    diffday =  result.getInt("nDay");

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

    void insertData() {
        Fragment page = pagerAdapter.getRegisteredFragment(0);
        final CRM_FragmentSO_Master fsomaster = (CRM_FragmentSO_Master) page;

        List<CRM_SO_OrderDetail> itemList = helper.getDataAll();
        List<CRM_SO_OrderDetail> itemtgl = helper.getGroupTglKirim();

        if (internetCon.checkConnection(getApplicationContext())) {
            RequestPost RP;
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                JSONObject json = new JSONObject();
                json.put("menutype", menutype);

                JSONObject datamst = new JSONObject();
                JSONArray arrmst = new JSONArray();
                datamst.put("docno", fsomaster.txtNomor.getText());
                datamst.put("customer", fsomaster.txtcust.getText());
                datamst.put("custkirim", fsomaster.custkrm);
                datamst.put("keterangan", fsomaster.txtketerangan.getText());
                datamst.put("status", "R");
                arrmst.put(datamst);
                json.put("datamst", arrmst);

                JSONArray arrdtl = new JSONArray();
                for (int i = 0; i < itemList.size(); i++) {
                    JSONObject datadtl = new JSONObject();
                    CRM_SO_OrderDetail items = itemList.get(i);
                    datadtl.put("docno", fsomaster.txtNomor.getText());
                    datadtl.put("brgcode", items.getKode());
                    datadtl.put("qty", items.getQty());
                    datadtl.put("satcode", items.getSatuan());
                    datadtl.put("tglkirim", items.getTglkirim());
                    arrdtl.put(datadtl);
                }
                json.put("datadtl", arrdtl);

                JSONArray arrtgl = new JSONArray();
                for (int i = 0; i < itemtgl.size(); i++) {
                    JSONObject datatgl = new JSONObject();
                    CRM_SO_OrderDetail itemtglkirim = itemtgl.get(i);
                    datatgl.put("tglkirim", itemtglkirim.getTglkirim());
                    arrtgl.put(datatgl);
                }
                json.put("datatgl", arrtgl);

                RP = new RequestPost("classes/crm_insert_so.php", json, getApplicationContext());
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (pDialog != null) pDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (pDialog != null) pDialog.dismiss();
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        if (response.isSuccessful()) {
                            final String jsonData = response.body().string();
                            try {
                                JSONObject obj = new JSONObject(jsonData);
                                JSONObject result = obj.getJSONObject("result");
                                final int success = result.getInt("success");
                                final String docno = result.getString("docno");
                                final String msg = result.getString("message");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (menutype.equals("Input")) {
                                            fsomaster.txtNomor.setText(docno);
                                            textSubTitle.setText(docno);
                                        }

                                        AlertDialog.Builder builder = new AlertDialog.Builder(CRM_SalesOrder.this);
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
                                    }
                                });

                            } catch (Exception e) {
                                if (pDialog != null) pDialog.dismiss();
                                e.printStackTrace();
                            }

                        }
                    }
                });

            } catch (Exception e) {
                if (pDialog != null) pDialog.dismiss();
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void cancelData() {
        Fragment page = pagerAdapter.getRegisteredFragment(0);
        final CRM_FragmentSO_Master fsomaster = (CRM_FragmentSO_Master) page;

        List<CRM_SO_OrderDetail> itemList = helper.getDataAll();

        if (internetCon.checkConnection(getApplicationContext())) {
            RequestPost RP;
            if (!isFinishing()) {
                pDialog.show();
            }
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();
            try {
                json = new JSONObject();
                json.put("action", "CRM_SalesOrder");
                json.put("method", "cancelPreSO");
                JSONArray arr2 = new JSONArray();
                data.put("psono", fsomaster.txtNomor.getText());
                arr2.put(data);
                json.put("data", arr2);
                Log.i("JSON nya", json.toString());
                RP = new RequestPost("router-crmsalesorder.php", json, getApplicationContext());
                RP.execPostCall(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (pDialog != null) pDialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (pDialog != null) pDialog.dismiss();
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);
                        if (response.isSuccessful()) {
                            final String jsonData = response.body().string();
                            try {
                                JSONObject obj = new JSONObject(jsonData);
                                JSONObject result = obj.getJSONObject("result");
                                final int success = result.getInt("success");
                                final String msg = result.getString("message");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CRM_SalesOrder.this);
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
                                    }
                                });

                            } catch (Exception e) {
                                if (pDialog != null) pDialog.dismiss();
                                e.printStackTrace();
                            }

                        }
                    }
                });

            } catch (Exception e) {
                if (pDialog != null) pDialog.dismiss();
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

