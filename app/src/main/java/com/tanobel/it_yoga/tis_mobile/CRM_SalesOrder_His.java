package com.tanobel.it_yoga.tis_mobile;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tanobel.it_yoga.tis_mobile.holder.SOHolder;
import com.tanobel.it_yoga.tis_mobile.holder.IconTreeItemHolder;
import com.tanobel.it_yoga.tis_mobile.holder.PreSOHolder;
import com.tanobel.it_yoga.tis_mobile.holder.DOHolder;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_HisDtlLVAdapter;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_HisDtlLVList;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_HisTVList;
import com.tanobel.it_yoga.tis_mobile.model.CRM_SO_UserPlant;
import com.tanobel.it_yoga.tis_mobile.model.Db_CRM_SO_His;
import com.tanobel.it_yoga.tis_mobile.util.DateInputMask;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

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
 * Created by IT_Yoga on 17/01/2019.
 */

public class CRM_SalesOrder_His extends AppCompatActivity implements TreeNode.TreeNodeClickListener {
    ArrayList<CRM_SO_UserPlant> userplantList = new ArrayList<>();
    ArrayList<CRM_SO_HisTVList> dataList = new ArrayList<>();
    ArrayList<CRM_SO_HisDtlLVList> dataListDtl = new ArrayList<>();
    ListView listView;
    private static CRM_SO_HisDtlLVAdapter listAdapter;
    private AndroidTreeView tView;
    String menuname, menutype, menustatus, tipesearch, docno1, docno2, tgl1, tgl2;
    ;
    int plant;
    EditText txtdocno1, txtdocno2, txttgl1, txttgl2;
    TextView txtplant, txttipedoc, txtsd1, txtsd2, txtdocno;
    Spinner spnusrplant, spntipedoc;
    ImageButton btnsearch, btntgl1, btntgl2;
    RadioButton rdodoc, rdotgl;
    Bundle savebundle;
    RelativeLayout containerView;

    private RequestPost RP;
    ProgressDialog pDialog;
    InternetConnection internetCon = new InternetConnection();

    Db_CRM_SO_His helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_so_history);

        Toolbar toolbar = findViewById(R.id.toolbar_so_his);
        setSupportActionBar(toolbar);

        savebundle = savedInstanceState;

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            getSupportActionBar().setTitle((String) bd.get("menu_name"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            menuname = (String) bd.get("menu_name");
            menutype = (String) bd.get("menu_type");
        }

        helper = new Db_CRM_SO_His(this);
        this.deleteDatabase("DbCRM");

        pDialog = new ProgressDialog(CRM_SalesOrder_His.this);
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

        containerView = findViewById(R.id.container);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CRM_SalesOrder_His.this);
        View searchdialog = layoutInflater.inflate(R.layout.search_crm_so_view, null);

        spnusrplant = searchdialog.findViewById(R.id.spnplant_so_view);
        txttipedoc = searchdialog.findViewById(R.id.txttipe_so_view);
        spntipedoc = searchdialog.findViewById(R.id.spntipe_so_view);
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

        txttipedoc.setVisibility(Spinner.VISIBLE);
        spntipedoc.setVisibility(Spinner.VISIBLE);

        rdodoc.setChecked(true);
        txttgl1.setVisibility(EditText.GONE);
        txtsd2.setVisibility(TextView.GONE);
        txttgl2.setVisibility(EditText.GONE);
        btntgl1.setVisibility(ImageButton.GONE);
        btntgl2.setVisibility(ImageButton.GONE);

        List<String> spinnerArrayTipe = new ArrayList<>();
        spinnerArrayTipe.add("Pre SO");
        spinnerArrayTipe.add("PGI / Surat Jalan");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(CRM_SalesOrder_His.this, R.layout.spinner_item_so, spinnerArrayTipe);
        adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
        spntipedoc.setAdapter(adapter);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_SalesOrder_His.this, new DatePickerDialog.OnDateSetListener() {

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_SalesOrder_His.this, new DatePickerDialog.OnDateSetListener() {

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

        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(CRM_SalesOrder_His.this)
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

                        getData(CRM_SalesOrder_His.this, tipesearch, plant, docno1, docno2, tgl1, tgl2);
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

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CRM_SalesOrder_His.this, R.layout.spinner_item_so, spinnerArray);
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

    void getData(final Context context, String tipe, final int plantpos, String docno1, String docno2, String tgl1, String tgl2) {
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
                    json.put("method", "getOrderHis");
                    JSONArray arr2 = new JSONArray();
                    data.put("tipedoc", spntipedoc.getSelectedItem());
                    data.put("tipe", tipe);
                    data.put("user", ((MainModule) getApplicationContext()).getUserCode());
                    data.put("plant", item.getBranch());
                    data.put("docno1", docno1);
                    data.put("docno2", docno2);
                    data.put("tgl1", tgl1);
                    data.put("tgl2", tgl2);
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

                                    ArrayList<CRM_SO_HisTVList> LVList = JSON.parseObject(hasil, new TypeReference<ArrayList<CRM_SO_HisTVList>>() {
                                    });
                                    dataList.clear();
                                    dataList.addAll(LVList);

                                    containerView.removeAllViews();
                                    helper.delete();

                                    for (int i = 0; i < dataList.size(); i++) {
                                        CRM_SO_HisTVList itemtv = dataList.get(i);
                                        long id = helper.insertData(itemtv.getPsono(), itemtv.getCustkirim(), itemtv.getAlamat(), itemtv.getTglkirim(), itemtv.getStatus(), itemtv.getSono(), itemtv.getTglso(), itemtv.getKetso(), itemtv.getStatusso(), itemtv.getDono(), itemtv.getTgldo(), itemtv.getStatusdo(), itemtv.getSjno(), itemtv.getTglsj(), itemtv.getStatussj(), itemtv.getBillno(), itemtv.getTglbill(), itemtv.getStatusbill());
                                        if (id <= 0) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Ada kegagalan tambah item", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }

                                    final TreeNode root = TreeNode.root();

                                    if (spntipedoc.getSelectedItem().equals("Pre SO")) {
                                        List<CRM_SO_HisTVList> itemPSO = helper.getDataPreSO();
                                        for (int i = 0; i < itemPSO.size(); i++) {
                                            CRM_SO_HisTVList itemspso = itemPSO.get(i);
                                            TreeNode PreSO = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, "PSO", itemspso.getPsono(), itemspso.getCustkirim(), itemspso.getAlamat(), itemspso.getTglkirim(), itemspso.getStatus())).setViewHolder(new PreSOHolder(CRM_SalesOrder_His.this));

                                            List<CRM_SO_HisTVList> itemSO = helper.getDataSO(itemspso.getPsono());
                                            for (int j = 0; j < itemSO.size(); j++) {
                                                CRM_SO_HisTVList itemsso = itemSO.get(j);
                                                TreeNode SO = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_shopping_basket, "SO", itemsso.getSono(), "", itemsso.getKetso(), itemsso.getTglso(), itemsso.getStatusso())).setViewHolder(new SOHolder(CRM_SalesOrder_His.this));

                                                List<CRM_SO_HisTVList> itemDO = helper.getDataDO(itemsso.getSono());
                                                for (int k = 0; k < itemDO.size(); k++) {
                                                    CRM_SO_HisTVList itemsdo = itemDO.get(k);
                                                    TreeNode DO = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_schedule, "DO", itemsdo.getDono(), "", "", itemsdo.getTgldo(), itemsdo.getStatusdo())).setViewHolder(new DOHolder(CRM_SalesOrder_His.this));

                                                    List<CRM_SO_HisTVList> itemSJ = helper.getDataSJ(itemsdo.getDono());
                                                    for (int l = 0; l < itemSJ.size(); l++) {
                                                        CRM_SO_HisTVList itemssj = itemSJ.get(l);
                                                        TreeNode SJ = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_send, "SJ", itemssj.getSjno(), "", "", itemssj.getTglsj(), itemssj.getStatussj())).setViewHolder(new DOHolder(CRM_SalesOrder_His.this));

                                                        List<CRM_SO_HisTVList> itemBill = helper.getDataBill(itemssj.getSjno());
                                                        for (int m = 0; m < itemBill.size(); m++) {
                                                            CRM_SO_HisTVList itemsbill = itemBill.get(m);
                                                            TreeNode BILL = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_credit_card, "BILL", itemsbill.getBillno(), "", "", itemsbill.getTglbill(), itemsbill.getStatusbill())).setViewHolder(new DOHolder(CRM_SalesOrder_His.this));
                                                            SJ.addChildren(BILL);
                                                        }
                                                        DO.addChildren(SJ);
                                                    }
                                                    SO.addChildren(DO);
                                                }
                                                PreSO.addChildren(SO);
                                            }
                                            root.addChildren(PreSO);
                                        }
                                    } else {
                                        List<CRM_SO_HisTVList> itemPSO = helper.getDataPreSO();
                                        for (int i = 0; i < itemPSO.size(); i++) {
                                            CRM_SO_HisTVList itemspso = itemPSO.get(i);
                                            TreeNode PreSO = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_send, "SJ", itemspso.getPsono(), itemspso.getCustkirim(), itemspso.getAlamat(), itemspso.getTglkirim(), itemspso.getStatus())).setViewHolder(new PreSOHolder(CRM_SalesOrder_His.this));

                                            List<CRM_SO_HisTVList> itemSO = helper.getDataSO(itemspso.getPsono());
                                            for (int j = 0; j < itemSO.size(); j++) {
                                                CRM_SO_HisTVList itemsso = itemSO.get(j);
                                                TreeNode SO = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_done, "SJ", itemsso.getSono(), "", itemsso.getKetso(), itemsso.getTglso(), itemsso.getStatusso())).setViewHolder(new SOHolder(CRM_SalesOrder_His.this));

                                                List<CRM_SO_HisTVList> itemDO = helper.getDataDO(itemsso.getSono());
                                                for (int k = 0; k < itemDO.size(); k++) {
                                                    CRM_SO_HisTVList itemsdo = itemDO.get(k);
                                                    TreeNode DO = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_keyboard_return, "SJ", itemsdo.getDono(), "", "", itemsdo.getTgldo(), itemsdo.getStatusdo())).setViewHolder(new DOHolder(CRM_SalesOrder_His.this));

                                                    SO.addChildren(DO);
                                                }
                                                PreSO.addChildren(SO);
                                            }
                                            root.addChildren(PreSO);
                                        }
                                    }

                                    tView = new AndroidTreeView(CRM_SalesOrder_His.this, root);
                                    tView.setDefaultAnimation(true);
                                    tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                                    tView.setUseAutoToggle(false);
                                    tView.setDefaultNodeClickListener(CRM_SalesOrder_His.this);
                                    containerView.addView(tView.getView());

                                    if (savebundle != null) {
                                        String state = savebundle.getString("tState");
                                        if (!TextUtils.isEmpty(state)) {
                                            tView.restoreState(state);
                                        }
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
            Toast toast = Toast.makeText(getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(TreeNode node, Object value) {
        CRM_SO_UserPlant item = userplantList.get(plant);
        showViewDialog(((IconTreeItemHolder.IconTreeItem)value).tipe, item.getBranch(), ((IconTreeItemHolder.IconTreeItem)value).text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.treenode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                if (dataList.size() > 0) {
                    tView.expandAll();
                }
                break;

            case R.id.collapseAll:
                if (dataList.size() > 0) {
                    tView.collapseAll();
                }
                break;

            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showViewDialog(String tipe, String plant, String docno) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CRM_SalesOrder_His.this);
        View viewdialog = layoutInflater.inflate(R.layout.layout_crmhis_dtl, null);

        txtdocno = viewdialog.findViewById(R.id.headerdocno);
        txtdocno.setText(docno);

        listAdapter = new CRM_SO_HisDtlLVAdapter(dataListDtl, getApplicationContext(), tipe);

        listView = viewdialog.findViewById(R.id.list_crmhis_dtl);
        listView.setAdapter(listAdapter);

        dataListDtl.clear();
        getDataDtl(tipe, plant, docno);

        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(CRM_SalesOrder_His.this)
                .setView(viewdialog)
                .setNegativeButton("Close", null)
                .create();

        dialog.show();
    }

    void getDataDtl(String tipe, String plant, String docno) {
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
                    json.put("method", "getOrderHisDtl");
                    JSONArray arr2 = new JSONArray();
                    data.put("tipe", tipe);
                    data.put("plant", plant);
                    data.put("docno", docno);
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

                                    ArrayList<CRM_SO_HisDtlLVList> LVList = JSON.parseObject(hasil, new TypeReference<ArrayList<CRM_SO_HisDtlLVList>>() {
                                    });
                                    dataListDtl.clear();
                                    dataListDtl.addAll(LVList);
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
}
