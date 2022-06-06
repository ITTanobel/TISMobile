package com.tanobel.it_yoga.tis_mobile.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Db_CRM_SO_His {
    myDbHelper myhelper;

    public Db_CRM_SO_His(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String psono, String custkirim, String alamat, String tglkirim, String status, String sono, String tglso, String ketso, String statusso, String dono, String tgldo, String statusdo, String sjno, String tglsj, String statussj, String billno, String tglbill, String statusbill) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.PSONO, psono);
        contentValues.put(myDbHelper.CUSTKIRIM, custkirim);
        contentValues.put(myDbHelper.ALAMAT, alamat);
        contentValues.put(myDbHelper.TGLKIRIM, tglkirim);
        contentValues.put(myDbHelper.STATUS, status);
        contentValues.put(myDbHelper.SONO, sono);
        contentValues.put(myDbHelper.TGLSO, tglso);
        contentValues.put(myDbHelper.KETSO, ketso);
        contentValues.put(myDbHelper.STATUSSO, statusso);
        contentValues.put(myDbHelper.DONO, dono);
        contentValues.put(myDbHelper.TGLDO, tgldo);
        contentValues.put(myDbHelper.STATUSDO, statusdo);
        contentValues.put(myDbHelper.SJNO, sjno);
        contentValues.put(myDbHelper.TGLSJ, tglsj);
        contentValues.put(myDbHelper.STATUSSJ, statussj);
        contentValues.put(myDbHelper.BILLNO, billno);
        contentValues.put(myDbHelper.TGLBILL, tglbill);
        contentValues.put(myDbHelper.STATUSBILL, statusbill);
        long insert = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return insert;
    }

    public List<CRM_SO_HisTVList> getDataPreSO() {
        List<CRM_SO_HisTVList> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.PSONO, myDbHelper.CUSTKIRIM, myDbHelper.ALAMAT,  myDbHelper.TGLKIRIM, myDbHelper.STATUS};
        Cursor cursor = db.query(true, myDbHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            CRM_SO_HisTVList operatorTable = new CRM_SO_HisTVList();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setPsono(cursor.getString(cursor.getColumnIndex(myDbHelper.PSONO)));
            operatorTable.setCustkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.CUSTKIRIM)));
            operatorTable.setAlamat(cursor.getString(cursor.getColumnIndex(myDbHelper.ALAMAT)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUS)));

            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<CRM_SO_HisTVList> getDataSO(String Psono) {
        List<CRM_SO_HisTVList> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.SONO, myDbHelper.TGLSO, myDbHelper.KETSO, myDbHelper.STATUSSO};
        String whereClause = myDbHelper.PSONO + " = ?";
        String[] whereArgs = new String[]{Psono};
        Cursor cursor = db.query(true, myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            if (!cursor.getString(cursor.getColumnIndex(myDbHelper.SONO)).trim().equals("")) {
                CRM_SO_HisTVList operatorTable = new CRM_SO_HisTVList();
                //here get all data from cursor and set it into setter method like below
                operatorTable.setSono(cursor.getString(cursor.getColumnIndex(myDbHelper.SONO)));
                operatorTable.setTglso(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLSO)));
                operatorTable.setKetso(cursor.getString(cursor.getColumnIndex(myDbHelper.KETSO)));
                operatorTable.setStatusso(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSSO)));

                listAll.add(operatorTable);
            }
        }

        return listAll;
    }

    public List<CRM_SO_HisTVList> getDataDO(String Sono) {
        List<CRM_SO_HisTVList> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.DONO, myDbHelper.TGLDO, myDbHelper.STATUSDO};
        String whereClause = myDbHelper.SONO + " = ?";
        String[] whereArgs = new String[]{Sono};
        Cursor cursor = db.query(true, myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            if (!cursor.getString(cursor.getColumnIndex(myDbHelper.DONO)).trim().equals("")) {
                CRM_SO_HisTVList operatorTable = new CRM_SO_HisTVList();
                //here get all data from cursor and set it into setter method like below
                operatorTable.setDono(cursor.getString(cursor.getColumnIndex(myDbHelper.DONO)));
                operatorTable.setTgldo(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLDO)));
                operatorTable.setStatusdo(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSDO)));

                listAll.add(operatorTable);
            }
        }

        return listAll;
    }


    public List<CRM_SO_HisTVList> getDataSJ(String Dono) {
        List<CRM_SO_HisTVList> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.SJNO, myDbHelper.TGLSJ, myDbHelper.STATUSSJ};
        String whereClause = myDbHelper.DONO + " = ?";
        String[] whereArgs = new String[]{Dono};
        Cursor cursor = db.query(true, myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            if (!cursor.getString(cursor.getColumnIndex(myDbHelper.SJNO)).trim().equals("")) {
                CRM_SO_HisTVList operatorTable = new CRM_SO_HisTVList();
                //here get all data from cursor and set it into setter method like below
                operatorTable.setSjno(cursor.getString(cursor.getColumnIndex(myDbHelper.SJNO)));
                operatorTable.setTglsj(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLSJ)));
                operatorTable.setStatussj(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSSJ)));

                listAll.add(operatorTable);
            }
        }

        return listAll;
    }

    public List<CRM_SO_HisTVList> getDataBill(String Sjno) {
        List<CRM_SO_HisTVList> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.BILLNO, myDbHelper.TGLBILL, myDbHelper.STATUSBILL};
        String whereClause = myDbHelper.SJNO + " = ?";
        String[] whereArgs = new String[]{Sjno};
        Cursor cursor = db.query(true, myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            if (!cursor.getString(cursor.getColumnIndex(myDbHelper.BILLNO)).trim().equals("")) {
                CRM_SO_HisTVList operatorTable = new CRM_SO_HisTVList();
                //here get all data from cursor and set it into setter method like below
                operatorTable.setBillno(cursor.getString(cursor.getColumnIndex(myDbHelper.BILLNO)));
                operatorTable.setTglbill(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLBILL)));
                operatorTable.setStatusbill(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSBILL)));

                listAll.add(operatorTable);
            }
        }

        return listAll;
    }

    public int delete()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();

        int count =db.delete(myDbHelper.TABLE_NAME, null, null);
        return  count;
    }

    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "DbCRM";    // Database Name
        private static final String TABLE_NAME = "CRM_SO";   // Table Name
        private static final int DATABASE_Version = 1;  // Database Version
        private static final String ID = "id";     // Column I (Primary Key)
        private static final String PSONO = "psono";
        private static final String CUSTKIRIM = "custkirim";
        private static final String ALAMAT = "alamat";
        private static final String TGLKIRIM = "tglkirim";
        private static final String STATUS = "status";
        private static final String SONO = "sono";
        private static final String TGLSO = "tglso";
        private static final String KETSO = "ketso";
        private static final String STATUSSO = "statusso";
        private static final String DONO = "dono";
        private static final String TGLDO = "tgldo";
        private static final String STATUSDO = "statusdo";
        private static final String SJNO = "sjno";
        private static final String TGLSJ = "tglsj";
        private static final String STATUSSJ = "statussj";
        private static final String BILLNO = "billno";
        private static final String TGLBILL = "tglbill";
        private static final String STATUSBILL = "statusbill";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PSONO + " CHAR(12), " + CUSTKIRIM + " VARCHAR(225), " + ALAMAT + " VARCHAR(225)," + TGLKIRIM + " CHAR(12)," +
                "" + STATUS + " CHAR(20)," + SONO + " CHAR(12)," + TGLSO + " CHAR(10)," + KETSO + " VARCHAR(225)," + STATUSSO + " CHAR(20)," + DONO + " CHAR(12)," + TGLDO + " CHAR(10)," +
                "" + STATUSDO + " CHAR(20)," + SJNO + " CHAR(12)," + TGLSJ + " CHAR(10)," + STATUSSJ + " CHAR(20)," + BILLNO + " CHAR(12)," + TGLBILL + " CHAR(10)," + STATUSBILL + " CHAR(20));";
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