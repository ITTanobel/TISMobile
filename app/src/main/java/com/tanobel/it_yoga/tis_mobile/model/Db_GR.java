package com.tanobel.it_yoga.tis_mobile.model;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
public class Db_GR {
    Db_GR.myDbHelper myhelper;

    public Db_GR(Context context) {
        myhelper = new Db_GR.myDbHelper(context);
    }

    public long insertData(String branch, String docno, String no_po,String kode_barang,String nama_barang,String satuan,String jumlah) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_GR.myDbHelper.BRANCH, branch);
        contentValues.put(Db_GR.myDbHelper.DOCNO, docno);
        contentValues.put(Db_GR.myDbHelper.NO_PO, no_po);
        contentValues.put(Db_GR.myDbHelper.KODE_BARANG, kode_barang);
        contentValues.put(Db_GR.myDbHelper.NAMA_BARANG, nama_barang);
        contentValues.put(Db_GR.myDbHelper.SATUAN, satuan);
        contentValues.put(Db_GR.myDbHelper.JUMLAH, jumlah);
        long insert = dbb.insert(Db_GR.myDbHelper.TABLE_NAME, null, contentValues);
        return insert;
    }

    public List<GR_Dtl> getDataAll() {
        List<GR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_GR.myDbHelper.BRANCH, Db_GR.myDbHelper.DOCNO, Db_GR.myDbHelper.NO_PO, Db_GR.myDbHelper.KODE_BARANG, Db_GR.myDbHelper.NAMA_BARANG, Db_GR.myDbHelper.SATUAN, Db_GR.myDbHelper.JUMLAH};
        Cursor cursor = db.query(Db_GR.myDbHelper.TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            GR_Dtl operatorTable = new GR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCNO)));
            operatorTable.setNo_po(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.NO_PO)));
            operatorTable.setKode_barang(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.KODE_BARANG)));
            operatorTable.setNama_barang(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.NAMA_BARANG)));
            operatorTable.setSatuan(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.SATUAN)));
            operatorTable.setJumlah(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.JUMLAH)));

            listAll.add(operatorTable);
        }

        return listAll;
    }

    public int update(String id, String newStatus, String newStatusdesc) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] whereArgs = {id};
        int count = db.update(Db_GR.myDbHelper.TABLE_NAME, contentValues, Db_GR.myDbHelper.ID + " = ?", whereArgs);
        return count;
    }

    public int delete()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();

        int count =db.delete(Db_GR.myDbHelper.TABLE_NAME, null, null);
        return  count;
    }

    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "DbGR";    // Database Name
        private static final String TABLE_NAME = "GR";   // Table Name
        private static final int DATABASE_Version = 1;  // Database Version
        private static final String ID = "id";
        public static final String NO_PO = "no_po";
        private static final String BRANCH = "branch";
        private static final String DOCNO = "docno";
        private static final String KODE_BARANG = "kode_barang";
        private static final String NAMA_BARANG = "nama_barang";
        private static final String SATUAN = "satuan";
        private static final String JUMLAH = "jumlah";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BRANCH + " CHAR(4)," + DOCNO + " CHAR(12), " + NO_PO + " VARCHAR(300), " + KODE_BARANG + " VARCHAR(200), " + NAMA_BARANG + " VARCHAR(500), " + SATUAN + " VARCHAR(100), " + JUMLAH + " VARCHAR(500));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.i("Info", "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                Log.i("Info", "" + e);
            }
        }
    }
}
