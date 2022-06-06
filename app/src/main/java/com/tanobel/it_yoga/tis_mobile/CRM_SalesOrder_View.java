package com.tanobel.it_yoga.tis_mobile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_MasterLVList;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_UserPlant;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_MasterLVAdapter;
import com.tanobel.it_yoga.tis_mobile.util.DateInputMask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by IT_Yoga on 23/11/2018.
 */
public class CRM_SalesOrder_View extends AppCompatActivity {

    ArrayList<CRM_SO_UserPlant> userplantList = new ArrayList<>();
    ArrayList<CRM_SO_MasterLVList> dataList = new ArrayList<>();
    ListView listView;
    private static CRM_SO_MasterLVAdapter listAdapter;
    private RequestPost RP;
    ProgressDialog pDialog;
    SwipeRefreshLayout pullToRefresh;
    InternetConnection internetCon = new InternetConnection();
    String menuname, menutype, menustatus, tipesearch, docno1, docno2, tgl1, tgl2;
    int plant;

    EditText txtdocno1, txtdocno2, txttgl1, txttgl2;
    TextView txtplant, txtsd1, txtsd2;
    Spinner spnusrplant;
    ImageButton btnsearch, btntgl1, btntgl2;
    RadioButton rdodoc, rdotgl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_so_view);

        Toolbar toolbar = findViewById(R.id.toolbar_so_view);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            getSupportActionBar().setTitle((String) bd.get("menu_name"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            menuname = (String) bd.get("menu_name");
            menutype = (String) bd.get("menu_type");
        }

        pDialog = new ProgressDialog(CRM_SalesOrder_View.this);
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);

        txtplant = findViewById(R.id.txt_plant_crm_so);
        btnsearch = findViewById(R.id.btn_so_add);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        listAdapter = new CRM_SO_MasterLVAdapter(dataList, getApplicationContext());

        listView = findViewById(R.id.list_so_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CRM_SO_MasterLVList CRM_SO_Master_DataList = dataList.get(position);

                if (menutype.equals("Edit") || menutype.equals("Cancel")) {
                    getPSOByRef(CRM_SO_Master_DataList.getDocno(), position);
                } else {
                    Intent i = new Intent(CRM_SalesOrder_View.this, CRM_SalesOrder.class);
                    i.putExtra("menu_name", menuname);
                    i.putExtra("menu_type", menutype);
                    i.putExtra("docno", CRM_SO_Master_DataList.getDocno());
                    i.putExtra("custid", CRM_SO_Master_DataList.getCustid());
                    i.putExtra("tglinput", CRM_SO_Master_DataList.getTglinput());
                    i.putExtra("status", CRM_SO_Master_DataList.getStatus());
                    i.putExtra("custkrm", CRM_SO_Master_DataList.getCustkirim());
                    i.putExtra("ket", CRM_SO_Master_DataList.getKet());
                    startActivity(i);
                }
            }
        });

        pullToRefresh = findViewById(R.id.pullToRefresh_so_view);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(tipesearch, plant, docno1, docno2, tgl1, tgl2, menustatus);
                pullToRefresh.setRefreshing(false);
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

    @Override
    public void onResume() {
        super.onResume();
        if (tipesearch != null) {
            getData(tipesearch, plant, docno1, docno2, tgl1, tgl2, menustatus);
        }
    }


    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CRM_SalesOrder_View.this);
        View searchdialog = layoutInflater.inflate(R.layout.search_crm_so_view, null);

        spnusrplant = searchdialog.findViewById(R.id.spnplant_so_view);
        txtdocno1 = searchdialog.findViewById(R.id.txtdocno1_so_view);
        txtdocno2 = searchdialog.findViewById(R.id.txtdocno2_so_view);
        txttgl1 = searchdialog.findViewById(R.id.txttgl1_so_view);
        txttgl2 = searchdialog.findViewById(R.id.txttgl2_so_view);
        txtsd1 = searchdialog.findViewById(R.id.txtsd1_so_view);
        txtsd2 = searchdialog.findViewById(R.id.txtsd2_so_view);
        btntgl1 = searchdialog.findViewById(R.id.btntgl1_so_view);
        btntgl2 = searchdialog.findViewById(R.id.btntgl2_so_view);
        rdodoc = searchdialog.findViewById(R.id.rdodocno_so_view);
        rdotgl = searchdialog.findViewById(R.id.rdotgl_so_view);

        getUserPlant();

        rdodoc.setChecked(true);
        txttgl1.setVisibility(EditText.GONE);
        txtsd2.setVisibility(TextView.GONE);
        txttgl2.setVisibility(EditText.GONE);
        btntgl1.setVisibility(ImageButton.GONE);
        btntgl2.setVisibility(ImageButton.GONE);

        rdodoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdotgl.setChecked(false);
                txttgl1.setVisibility(EditText.GONE);
                txtsd2.setVisibility(TextView.GONE);
                txttgl2.setVisibility(EditText.GONE);
                btntgl1.setVisibility(ImageButton.GONE);
                btntgl2.setVisibility(ImageButton.GONE);

                txtdocno1.setVisibility(EditText.VISIBLE);
                txtsd1.setVisibility(TextView.VISIBLE);
                txtdocno2.setVisibility(EditText.VISIBLE);
            }
        });

        rdotgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdodoc.setChecked(false);
                txtdocno1.setVisibility(EditText.GONE);
                txtsd1.setVisibility(TextView.GONE);
                txtdocno2.setVisibility(EditText.GONE);

                txttgl1.setVisibility(EditText.VISIBLE);
                txtsd2.setVisibility(TextView.VISIBLE);
                txttgl2.setVisibility(EditText.VISIBLE);
                btntgl1.setVisibility(ImageButton.VISIBLE);
                btntgl2.setVisibility(ImageButton.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
                txttgl1.setText(mdformat.format(calendar.getTime()));
                txttgl2.setText(mdformat.format(calendar.getTime()));
            }
        });

        txtdocno2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                txtdocno2.setText(txtdocno1.getText());
            }
        });

        new DateInputMask(txttgl1);
        new DateInputMask(txttgl2);

        btntgl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_SalesOrder_View.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        txttgl1.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        btntgl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_SalesOrder_View.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        txttgl2.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(CRM_SalesOrder_View.this)
                .setView(searchdialog)
                .setPositiveButton("Search", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((androidx.appcompat.app.AlertDialog) dialog).getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        plant = spnusrplant.getSelectedItemPosition();
                        if (rdodoc.isChecked()) {
                            tipesearch = "Doc";
                            docno1 = txtdocno1.getText().toString();
                            docno2 = txtdocno2.getText().toString();
                            tgl1 = "";
                            tgl2 = "";
                        } else {
                            tipesearch = "Tgl";
                            docno1 = "";
                            docno2 = "";
                            tgl1 = txttgl1.getText().toString();
                            tgl2 = txttgl2.getText().toString();
                        }

                        if (menutype.equals("Edit") || menutype.equals("Cancel")) {
                            menustatus = "'R','L'";
                        } else {
                            menustatus = "'R', 'L', 'C'";
                        }

                        getData(tipesearch, plant, docno1, docno2, tgl1, tgl2, menustatus);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    void getUserPlant() {
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
                    json.put("method", "getOrderMaster");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getApplicationContext()).getUserCode());
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
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
                                    String userplant = rslt.getString("userplant");

                                    ArrayList<CRM_SO_UserPlant> usrplant = JSON.parseObject(userplant, new TypeReference<ArrayList<CRM_SO_UserPlant>>() {
                                    });

                                    userplantList.clear();
                                    userplantList.addAll(usrplant);

                                    List<String> spinnerArray = new ArrayList<>();

                                    for (int i = 0; i < usrplant.size(); i++) {
                                        CRM_SO_UserPlant itemusrplant = usrplant.get(i);
                                        spinnerArray.add(itemusrplant.getDesc());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CRM_SalesOrder_View.this, R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spnusrplant.setAdapter(adapter);

                                    spnusrplant.setSelection(0);
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

    void getData(String tipe, final int plantpos, String docno1, String docno2, String tgl1, String tgl2, String status) {
        if (internetCon.checkConnection(getApplicationContext())) {
            if (!isFinishing()) {
                pDialog.show();
            }
            try {
                final CRM_SO_UserPlant item = userplantList.get(plantpos);

                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getPreSO");
                    JSONArray arr2 = new JSONArray();
                    data.put("menutype", menutype);
                    data.put("tipe", tipe);
                    data.put("user", ((MainModule) getApplicationContext()).getUserCode());
                    data.put("plant", item.getBranch());
                    data.put("docno1", docno1);
                    data.put("docno2", docno2);
                    data.put("tgl1", tgl1);
                    data.put("tgl2", tgl2);
                    data.put("status", status);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    if (pDialog != null) pDialog.dismiss();
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
                                    String hasil = rslt.getString("hasil");

                                    txtplant.setText(item.getDesc());

                                    ArrayList<CRM_SO_MasterLVList> LVList = JSON.parseObject(hasil, new TypeReference<ArrayList<CRM_SO_MasterLVList>>() {
                                    });
                                    dataList.clear();
                                    dataList.addAll(LVList);
                                    listAdapter.notifyDataSetChanged();
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

    void getPSOByRef(String docno, final int position) {
        if (internetCon.checkConnection(getApplicationContext())) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getPreSOMstByRef");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) getApplicationContext()).getUserCode());
                    data.put("docno", docno);
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
                                    JSONArray hasil = rslt.getJSONArray("hasil");

                                    final String status[] = new String[1];

                                    for (int i = 0; i < hasil.length(); i++) {
                                        JSONObject psomst = hasil.getJSONObject(i);
                                        status[0] = psomst.getString("status");
                                    }

                                    if (status[0].equals("F") || status[0].equals("P")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Pre SO sudah dibuatkan sales order", Toast.LENGTH_SHORT);
                                        toast.show();
                                        getData(tipesearch, plant, docno1, docno2, tgl1, tgl2, menustatus);
                                    } else if (status[0].equals("C")) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Status Pre SO sudah dicancel", Toast.LENGTH_SHORT);
                                        toast.show();
                                        getData(tipesearch, plant, docno1, docno2, tgl1, tgl2, menustatus);
                                    } else {
                                        CRM_SO_MasterLVList CRM_SO_Master_DataList = dataList.get(position);

                                        Intent i = new Intent(CRM_SalesOrder_View.this, CRM_SalesOrder.class);
                                        i.putExtra("menu_name", menuname);
                                        i.putExtra("menu_type", menutype);
                                        i.putExtra("docno", CRM_SO_Master_DataList.getDocno());
                                        i.putExtra("custid", CRM_SO_Master_DataList.getCustid());
                                        i.putExtra("tglinput", CRM_SO_Master_DataList.getTglinput());
                                        i.putExtra("status", CRM_SO_Master_DataList.getStatus());
                                        i.putExtra("custkrm", CRM_SO_Master_DataList.getCustkirim());
                                        i.putExtra("ket", CRM_SO_Master_DataList.getKet());
                                        startActivity(i);
                                    }

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
}
