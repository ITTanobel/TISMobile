package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tanobel.it_yoga.tis_mobile.CRM_FragmentSO_Detail;
import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.MainModule;
import com.tanobel.it_yoga.tis_mobile.util.RequestPost;
import com.tanobel.it_yoga.tis_mobile.CRM_SalesOrder;
import com.tanobel.it_yoga.tis_mobile.R;
import com.tanobel.it_yoga.tis_mobile.util.DateInputMask;

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

public class CRM_SO_RVAdapter extends RecyclerView.Adapter<CRM_SO_RVAdapter.ViewHolder> {
    private RequestPost RP;
    InternetConnection internetCon = new InternetConnection();
    ArrayList<CRM_SO_Barang> custBarangList = new ArrayList<>();

    private List<CRM_SO_OrderDetail> kt = new ArrayList<>();
    private Typeface font;
    private CRM_FragmentSO_Detail mFragment;
    ProgressDialog pDialog;
    EditText editdate, qty;
    ImageButton btntanggal;
    Spinner spnbrg, spnsatuan;
    String menutype, custcode;
    Date nowdate, getdate;
    String strndate, strgdate;
    int maxday, minday, diffday;

    Db_CRM_SO helper;

    public CRM_SO_RVAdapter(CRM_FragmentSO_Detail mFragment, List<CRM_SO_OrderDetail> kt, String menutype) {
        this.kt = kt;
        this.mFragment = mFragment;
        this.menutype = menutype;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        font = Typeface.createFromAsset(mFragment.getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_crmso_detail, viewGroup, false);

        pDialog = new ProgressDialog(mFragment.getActivity());
        pDialog.setMessage("Please wait....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.txtbrg.setText(kt.get(position).getNama());
        viewHolder.txtqty.setText(String.valueOf(kt.get(position).getQty()));
        viewHolder.txtsatuan.setText(kt.get(position).getSatuan());
        viewHolder.txttglkirim.setText(kt.get(position).getTglkirim());
    }

    @Override
    public int getItemCount() {
        return kt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtbrg, txtqty, txtsatuan, txttglkirim;

        public ViewHolder(View view) {
            super(view);

            txtbrg = view.findViewById(R.id.txt_so_brg);
            txtqty = view.findViewById(R.id.txt_so_qty);
            txtsatuan = view.findViewById(R.id.txt_so_satuan);
            txttglkirim = view.findViewById(R.id.txt_so_tgl);

            CardView cardView = view.findViewById(R.id.cv_so_detail);
            cardView.setOnClickListener(this);
            ImageButton edit = view.findViewById(R.id.btn_so_edit);
            ImageButton delete = view.findViewById(R.id.btn_so_delete);
            TextView edittxt = view.findViewById(R.id.txt_btn_so_edit);
            TextView deletetxt = view.findViewById(R.id.txt_btn_so_delete);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);

            if (menutype.equals("View") || menutype.equals("Cancel")) {
                edit.setVisibility(ImageButton.GONE);
                delete.setVisibility(ImageButton.GONE);
                edittxt.setVisibility(TextView.GONE);
                deletetxt.setVisibility(TextView.GONE);
            }

            helper = new Db_CRM_SO(mFragment.getActivity());
        }

        @Override
        public void onClick(View view) {
            final CRM_SO_OrderDetail item = kt.get(getAdapterPosition() - 1);
            if (view.getId() == R.id.btn_so_edit) {
                CRM_SalesOrder crm_salesOrder = (CRM_SalesOrder) mFragment.getActivity();
                custcode = crm_salesOrder.custcode;

                showInputDialog(String.valueOf(item.getId()), item.getKode(), item.getQty(), item.getTglkirim());
            } else if (view.getId() == R.id.btn_so_delete) {
                helper.delete(String.valueOf(item.getId()));
                mFragment.getOrderDetail();
            }
        }
    }

    protected void showInputDialog(final String id_item, String kode_barang, int qty_val, String tglkirim_val) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(mFragment.getActivity());
        View editpassdialog = layoutInflater.inflate(R.layout.add_sales_order, null);

        getBarang(editpassdialog, kode_barang);

        spnbrg = editpassdialog.findViewById(R.id.spn_so_barang);
        spnsatuan = editpassdialog.findViewById(R.id.spn_so_satuan);

        spnbrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CRM_SO_Barang item_alamat = custBarangList.get(position);

                List<String> spinnerArray = new ArrayList<>();
                spinnerArray.add(item_alamat.getSatcode());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(mFragment.getActivity(), R.layout.spinner_item_so, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        qty.setText(String.valueOf(qty_val));
        spnsatuan.setSelection(0);
        editdate.setText(tglkirim_val);

        try {
            String[] datatgl = tglkirim_val.split("/", 3);

            Calendar editcalendar = Calendar.getInstance();
            editcalendar.set(Integer.parseInt(datatgl[2]), Integer.parseInt(datatgl[1]), Integer.parseInt(datatgl[0]));
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
            nowdate = mdformat.parse(mdformat.format(editcalendar.getTime()));
            getdate = mdformat.parse(mdformat.format(editcalendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btntanggal = editpassdialog.findViewById(R.id.btn_so_tglkirim);
        btntanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(mFragment.getActivity(), new DatePickerDialog.OnDateSetListener() {

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

        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(mFragment.getActivity())
                .setView(editpassdialog)
                .setPositiveButton("Edit Item", null) //Set to null. We override the onclick
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
                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Harap pilih barang", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (qty.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Harap isi quantity", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (editdate.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Harap isi tanggal kirim", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (getdate.before(nowdate)) {
                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Tanggal kirim tidak boleh kurang dari tanggal hari ini.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (diffday > maxday) {
                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Tanggal kirim maksimal " + String.valueOf(maxday) + " hari dari tanggal hari ini.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (diffday < minday) {
                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Tanggal kirim minimal " + String.valueOf(minday) + " hari dari tanggal hari ini.", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            CRM_SO_Barang item = custBarangList.get(spnbrg.getSelectedItemPosition());
                            List<CRM_SO_OrderDetail> itemList = helper.getDataByIdBrgCodeTglKrm(id_item, item.getKode(), String.valueOf(editdate.getText()));
                            if (itemList.size() == 0) {
                                if (menutype.equals("Input")) {
                                    int id = helper.update(id_item, item.getKode(), item.getNama(), Integer.parseInt(String.valueOf(qty.getText())), String.valueOf(spnsatuan.getSelectedItem()), String.valueOf(editdate.getText()));
                                    if (id <= 0) {
                                        Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Ada kegagalan edit item", Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        mFragment.getOrderDetail();
                                    }
                                    dialog.dismiss();
                                }

                                if (menutype.equals("Edit")) {
                                    List<CRM_SO_OrderDetail> itembrg = helper.getDataByIdBrgCode(id_item, item.getKode());
                                    if (itembrg.size()==0) {
                                        int id = helper.update(id_item, item.getKode(), item.getNama(), Integer.parseInt(String.valueOf(qty.getText())), String.valueOf(spnsatuan.getSelectedItem()), String.valueOf(editdate.getText()));
                                        if (id <= 0) {
                                            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Ada kegagalan edit item", Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            helper.updateTglKrm(String.valueOf(editdate.getText()));
                                            mFragment.getOrderDetail();
                                        }
                                        dialog.dismiss();
                                    } else {
                                        Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Barang sudah diinput", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            } else {
                                Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Barang dan tanggal sudah diinput", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    void getMaxMinDay() {
        if (internetCon.checkConnection(mFragment.getActivity().getApplicationContext())) {
            if (!mFragment.getActivity().isFinishing()) {
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
                RP = new RequestPost("router-crmsalesorder.php", json, mFragment.getActivity().getApplicationContext());
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
                        mFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(jsonData);
                                    JSONObject rslt = obj.getJSONObject("result");
                                    JSONArray hasil = rslt.getJSONArray("hasil");

                                    JSONObject result = hasil.getJSONObject(0);
                                    maxday = result.getInt("nMaxdelvday");
                                    minday = result.getInt("nMindelvday");
                                    diffday = result.getInt("nDay");

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
            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void getBarang(final View view, final String kode_barang) {
        if (internetCon.checkConnection(mFragment.getActivity().getApplicationContext())) {
            try {
                JSONObject json = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    json = new JSONObject();
                    json.put("action", "CRM_SalesOrder");
                    json.put("method", "getBarang");
                    JSONArray arr2 = new JSONArray();
                    data.put("user", ((MainModule) mFragment.getActivity().getApplicationContext()).getUserCode());
                    data.put("custcode", custcode);
                    arr2.put(data);
                    json.put("data", arr2);
                    Log.i("JSON nya", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RP = new RequestPost("router-crmsalesorder.php", json, mFragment.getActivity().getApplicationContext());
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
                        mFragment.getActivity().runOnUiThread(new Runnable() {
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

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mFragment.getActivity(), R.layout.spinner_item_so, spinnerArray);
                                    adapter.setDropDownViewResource(R.layout.spinner_item_down_so);
                                    spnbrg.setAdapter(adapter);

                                    for (int i = 0; i < custBarangList.size(); i++) {
                                        CRM_SO_Barang item = custBarangList.get(i);
                                        if (item.getKode().equals(kode_barang)) {
                                            spnbrg.setSelection(i);
                                        }
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
            Toast toast = Toast.makeText(mFragment.getActivity().getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

