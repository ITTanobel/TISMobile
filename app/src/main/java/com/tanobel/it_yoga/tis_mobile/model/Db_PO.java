package com.tanobel.it_yoga.tis_mobile.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Db_PO {
    myDbHelper myhelper;

    public Db_PO(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String branch, String docno, String no, String docref, String branchto, String brgcode, String brgname, String brgdesc, String satcode, String qty, String iocode, String status, String statusdesc, String statusold, String harga1, String disc1, String brutto, String mdisc1, String dpp, String ppn, String netto) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.BRANCH, branch);
        contentValues.put(myDbHelper.DOCNO, docno);
        contentValues.put(myDbHelper.NO, no);
        contentValues.put(myDbHelper.DOCREF, docref);
        contentValues.put(myDbHelper.BRANCHTO, branchto);
        contentValues.put(myDbHelper.BRGCODE, brgcode);
        contentValues.put(myDbHelper.BRGNAME, brgname);
        contentValues.put(myDbHelper.BRGDESC, brgdesc);
        contentValues.put(myDbHelper.SATCODE, satcode);
        contentValues.put(myDbHelper.QTY, qty);
        contentValues.put(myDbHelper.IOCODE, iocode);
        contentValues.put(myDbHelper.STATUS, status);
        contentValues.put(myDbHelper.STATUSDESC, statusdesc);
        contentValues.put(myDbHelper.STATUSOLD, statusold);
        contentValues.put(myDbHelper.HARGA1, harga1);
        contentValues.put(myDbHelper.DISC1, disc1);
        contentValues.put(myDbHelper.BRUTTO, brutto);
        contentValues.put(myDbHelper.MDISC1, mdisc1);
        contentValues.put(myDbHelper.DPP, dpp);
        contentValues.put(myDbHelper.PPN, ppn);
        contentValues.put(myDbHelper.NETTO, netto);
        long insert = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return insert;
    }

    public List<PO_Dtl> getDataAll() {
        List<PO_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID, myDbHelper.BRANCH, myDbHelper.DOCNO, myDbHelper.NO, myDbHelper.DOCREF, myDbHelper.BRANCHTO, myDbHelper.BRGCODE, myDbHelper.BRGNAME, myDbHelper.BRGDESC, myDbHelper.SATCODE, myDbHelper.QTY, myDbHelper.IOCODE, myDbHelper.STATUS, myDbHelper.STATUSDESC, myDbHelper.STATUSOLD, myDbHelper.HARGA1, myDbHelper.DISC1, myDbHelper.BRUTTO, myDbHelper.MDISC1, myDbHelper.DPP, myDbHelper.PPN, myDbHelper.NETTO};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            PO_Dtl operatorTable = new PO_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCNO)));
            operatorTable.setNo(cursor.getString(cursor.getColumnIndex(myDbHelper.NO)));
            operatorTable.setDocref(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCREF)));
            operatorTable.setBranchto(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCHTO)));
            operatorTable.setBrgcode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setBrgname(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setBrgdesc(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGDESC)));
            operatorTable.setSatcode(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setQty(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY)));
            operatorTable.setIocode(cursor.getString(cursor.getColumnIndex(myDbHelper.IOCODE)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUS)));
            operatorTable.setStatusdesc(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSDESC)));
            operatorTable.setStatusold(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSOLD)));
            operatorTable.setHarga1(cursor.getString(cursor.getColumnIndex(myDbHelper.HARGA1)));
            operatorTable.setDisc1(cursor.getString(cursor.getColumnIndex(myDbHelper.DISC1)));
            operatorTable.setBrutto(cursor.getString(cursor.getColumnIndex(myDbHelper.BRUTTO)));
            operatorTable.setMdisc1(cursor.getString(cursor.getColumnIndex(myDbHelper.MDISC1)));
            operatorTable.setDpp(cursor.getString(cursor.getColumnIndex(myDbHelper.DPP)));
            operatorTable.setPpn(cursor.getString(cursor.getColumnIndex(myDbHelper.PPN)));
            operatorTable.setNetto(cursor.getString(cursor.getColumnIndex(myDbHelper.NETTO)));

            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PO_Dtl> getDataGroupByStatus() {
        List<PO_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_PO.myDbHelper.BRANCH, Db_PO.myDbHelper.DOCNO, Db_PO.myDbHelper.STATUS};
        String whereClause = Db_PO.myDbHelper.STATUS+" <> "+ Db_PO.myDbHelper.STATUSOLD+" and "+ Db_PO.myDbHelper.STATUS+" <> ?";
        String[] whereArgs = new String[] {"C"};
        String columnsgroup = Db_PO.myDbHelper.BRANCH+","+ Db_PO.myDbHelper.DOCNO+","+ Db_PO.myDbHelper.STATUS;
        Cursor cursor = db.query(Db_PO.myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            PO_Dtl operatorTable = new PO_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.DOCNO)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PO_Dtl> getDataGroupByStatusCancel() {
        List<PO_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_PO.myDbHelper.BRANCH, Db_PO.myDbHelper.DOCNO, Db_PO.myDbHelper.STATUS};
        String whereClause = Db_PO.myDbHelper.STATUS+" <> "+ Db_PO.myDbHelper.STATUSOLD+" and "+ Db_PO.myDbHelper.STATUS+" = ?";
        String[] whereArgs = new String[] {"C"};
        String columnsgroup = Db_PO.myDbHelper.BRANCH+","+ Db_PO.myDbHelper.DOCNO+","+ Db_PO.myDbHelper.STATUS;
        Cursor cursor = db.query(Db_PO.myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            PO_Dtl operatorTable = new PO_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.DOCNO)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PO_Dtl> getDataGroupByStatusRelease() {
        List<PO_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_PO.myDbHelper.BRANCH, Db_PO.myDbHelper.DOCNO, Db_PO.myDbHelper.STATUS};
        String whereClause = Db_PO.myDbHelper.STATUS+" = ?";
        String[] whereArgs = new String[] {"R"};
        String columnsgroup = Db_PO.myDbHelper.BRANCH+","+ Db_PO.myDbHelper.DOCNO+","+ Db_PO.myDbHelper.STATUS;
        Cursor cursor = db.query(Db_PO.myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            PO_Dtl operatorTable = new PO_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.DOCNO)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(Db_PO.myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PO_Dtl> getDataByStatusOld() {
        List<PO_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID, myDbHelper.BRANCH, myDbHelper.DOCNO, myDbHelper.NO};
        String whereClause = myDbHelper.STATUS+" = "+myDbHelper.STATUSOLD;
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, whereClause, null, null, null, null);

        while (cursor.moveToNext()) {
            PO_Dtl operatorTable = new PO_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCNO)));
            operatorTable.setNo(cursor.getString(cursor.getColumnIndex(myDbHelper.NO)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public int update(String id, String newStatus, String newStatusdesc) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.STATUS, newStatus);
        contentValues.put(myDbHelper.STATUSDESC, newStatusdesc);
        String[] whereArgs = {id};
        int count = db.update(myDbHelper.TABLE_NAME, contentValues, myDbHelper.ID + " = ?", whereArgs);
        return count;
    }

    public int delete()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();

        int count =db.delete(myDbHelper.TABLE_NAME, null, null);
        return  count;
    }

    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "DbPO";    // Database Name
        private static final String TABLE_NAME = "PO";   // Table Name
        private static final int DATABASE_Version = 1;  // Database Version
        private static final String ID = "id";
        private static final String BRANCH = "branch";
        private static final String DOCNO = "docno";
        private static final String NO = "no";
        private static final String DOCREF = "docref";
        private static final String BRANCHTO = "branchto";
        private static final String BRGCODE = "brgcode";
        private static final String BRGNAME = "brgname";
        private static final String BRGDESC = "brgdesc";
        private static final String SATCODE = "satcode";
        private static final String QTY = "qty";
        private static final String IOCODE = "iocode";
        private static final String STATUS = "status";
        private static final String STATUSDESC = "statusdesc";
        private static final String STATUSOLD = "statusold";
        private static final String HARGA1 = "harga1";
        private static final String DISC1 = "disc1";
        private static final String BRUTTO  = "brutto";
        private static final String MDISC1  = "mdisc1";
        private static final String DPP  = "dpp";
        private static final String PPN  = "ppn";
        private static final String NETTO  = "netto";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BRANCH + " CHAR(4)," + DOCNO + " CHAR(12), " + NO + " CHAR(2), " + DOCREF + " CHAR(12), " + BRANCHTO + " CHAR(12), " + BRGCODE + " VARCHAR(255), " + BRGNAME + " VARCHAR(225)," +
                "" + BRGDESC + " TEXT, " + SATCODE + " CHAR(10), " + QTY + " CHAR(20), " + IOCODE + " VARCHAR(100), " + STATUS + " CHAR(1), " + STATUSDESC + " CHAR(100), " + STATUSOLD + " CHAR(1), " + HARGA1 + " VARCHAR(25)," +
                "" + DISC1 + " VARCHAR(25), " + BRUTTO + " VARCHAR(25), " + MDISC1 + " VARCHAR(25), " + DPP + " VARCHAR(25), " + PPN + " VARCHAR(25), " + NETTO + " VARCHAR(25));";
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