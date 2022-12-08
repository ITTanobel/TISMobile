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

    public long insertData(String branch, String docno, String no, String docref, String branchto, String brgcode, String brgname, String brgdesc, String satcode, String qty, String iocode, String status, String statusdesc, String statusold, String harga1, String disc1, String brutto, String mdisc1, String dpp, String ppn, String netto) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_GR.myDbHelper.BRANCH, branch);
        contentValues.put(Db_GR.myDbHelper.DOCNO, docno);
        contentValues.put(Db_GR.myDbHelper.NO, no);
        contentValues.put(Db_GR.myDbHelper.DOCREF, docref);
        contentValues.put(Db_GR.myDbHelper.BRANCHTO, branchto);
        contentValues.put(Db_GR.myDbHelper.BRGCODE, brgcode);
        contentValues.put(Db_GR.myDbHelper.BRGNAME, brgname);
        contentValues.put(Db_GR.myDbHelper.BRGDESC, brgdesc);
        contentValues.put(Db_GR.myDbHelper.SATCODE, satcode);
        contentValues.put(Db_GR.myDbHelper.QTY, qty);
        contentValues.put(Db_GR.myDbHelper.IOCODE, iocode);
        contentValues.put(Db_GR.myDbHelper.STATUS, status);
        contentValues.put(Db_GR.myDbHelper.STATUSDESC, statusdesc);
        contentValues.put(Db_GR.myDbHelper.STATUSOLD, statusold);
        contentValues.put(Db_GR.myDbHelper.HARGA1, harga1);
        contentValues.put(Db_GR.myDbHelper.DISC1, disc1);
        contentValues.put(Db_GR.myDbHelper.BRUTTO, brutto);
        contentValues.put(Db_GR.myDbHelper.MDISC1, mdisc1);
        contentValues.put(Db_GR.myDbHelper.DPP, dpp);
        contentValues.put(Db_GR.myDbHelper.PPN, ppn);
        contentValues.put(Db_GR.myDbHelper.NETTO, netto);
        long insert = dbb.insert(Db_GR.myDbHelper.TABLE_NAME, null, contentValues);
        return insert;
    }

    public List<GR_Dtl> getDataAll() {
        List<GR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_GR.myDbHelper.ID, Db_GR.myDbHelper.BRANCH, Db_GR.myDbHelper.DOCNO, Db_GR.myDbHelper.NO, Db_GR.myDbHelper.DOCREF, Db_GR.myDbHelper.BRANCHTO, Db_GR.myDbHelper.BRGCODE, Db_GR.myDbHelper.BRGNAME, Db_GR.myDbHelper.BRGDESC, Db_GR.myDbHelper.SATCODE, Db_GR.myDbHelper.QTY, Db_GR.myDbHelper.IOCODE, Db_GR.myDbHelper.STATUS, Db_GR.myDbHelper.STATUSDESC, Db_GR.myDbHelper.STATUSOLD, Db_GR.myDbHelper.HARGA1, Db_GR.myDbHelper.DISC1, Db_GR.myDbHelper.BRUTTO, Db_GR.myDbHelper.MDISC1, Db_GR.myDbHelper.DPP, Db_GR.myDbHelper.PPN, Db_GR.myDbHelper.NETTO};
        Cursor cursor = db.query(Db_GR.myDbHelper.TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            GR_Dtl operatorTable = new GR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.ID))));
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCNO)));
            operatorTable.setNo(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.NO)));
            operatorTable.setDocref(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCREF)));
            operatorTable.setBranchto(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCHTO)));
            operatorTable.setBrgcode(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRGCODE)));
            operatorTable.setBrgname(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRGNAME)));
            operatorTable.setBrgdesc(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRGDESC)));
            operatorTable.setSatcode(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.SATCODE)));
            operatorTable.setQty(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.QTY)));
            operatorTable.setIocode(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.IOCODE)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.STATUS)));
            operatorTable.setStatusdesc(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.STATUSDESC)));
            operatorTable.setStatusold(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.STATUSOLD)));
            operatorTable.setHarga1(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.HARGA1)));
            operatorTable.setDisc1(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DISC1)));
            operatorTable.setBrutto(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRUTTO)));
            operatorTable.setMdisc1(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.MDISC1)));
            operatorTable.setDpp(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DPP)));
            operatorTable.setPpn(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.PPN)));
            operatorTable.setNetto(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.NETTO)));

            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<GR_Dtl> getDataGroupByStatus() {
        List<GR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_GR.myDbHelper.BRANCH, Db_GR.myDbHelper.DOCNO, Db_GR.myDbHelper.STATUS};
        String whereClause = Db_GR.myDbHelper.STATUS+" <> "+ Db_GR.myDbHelper.STATUSOLD+" and "+ Db_GR.myDbHelper.STATUS+" <> ?";
        String[] whereArgs = new String[] {"C"};
        String columnsgroup = Db_GR.myDbHelper.BRANCH+","+ Db_GR.myDbHelper.DOCNO+","+ Db_GR.myDbHelper.STATUS;
        Cursor cursor = db.query(Db_GR.myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            GR_Dtl operatorTable = new GR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCNO)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<GR_Dtl> getDataGroupByStatusCancel() {
        List<GR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_GR.myDbHelper.BRANCH, Db_GR.myDbHelper.DOCNO, Db_GR.myDbHelper.STATUS};
        String whereClause = Db_GR.myDbHelper.STATUS+" <> "+ Db_GR.myDbHelper.STATUSOLD+" and "+ Db_GR.myDbHelper.STATUS+" = ?";
        String[] whereArgs = new String[] {"C"};
        String columnsgroup = Db_GR.myDbHelper.BRANCH+","+ Db_GR.myDbHelper.DOCNO+","+ Db_GR.myDbHelper.STATUS;
        Cursor cursor = db.query(Db_GR.myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            GR_Dtl operatorTable = new GR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCNO)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<GR_Dtl> getDataGroupByStatusRelease() {
        List<GR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_GR.myDbHelper.BRANCH, Db_GR.myDbHelper.DOCNO, Db_GR.myDbHelper.STATUS};
        String whereClause = Db_GR.myDbHelper.STATUS+" = ?";
        String[] whereArgs = new String[] {"R"};
        String columnsgroup = Db_GR.myDbHelper.BRANCH+","+ Db_GR.myDbHelper.DOCNO+","+ Db_GR.myDbHelper.STATUS;
        Cursor cursor = db.query(Db_GR.myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            GR_Dtl operatorTable = new GR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCNO)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<GR_Dtl> getDataByStatusOld() {
        List<GR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {Db_GR.myDbHelper.ID, Db_GR.myDbHelper.BRANCH, Db_GR.myDbHelper.DOCNO, Db_GR.myDbHelper.NO};
        String whereClause = Db_GR.myDbHelper.STATUS+" = "+ Db_GR.myDbHelper.STATUSOLD;
        Cursor cursor = db.query(Db_GR.myDbHelper.TABLE_NAME, columns, whereClause, null, null, null, null);

        while (cursor.moveToNext()) {
            GR_Dtl operatorTable = new GR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.ID))));
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.DOCNO)));
            operatorTable.setNo(cursor.getString(cursor.getColumnIndexOrThrow(Db_GR.myDbHelper.NO)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public int update(String id, String newStatus, String newStatusdesc) {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Db_GR.myDbHelper.STATUS, newStatus);
        contentValues.put(Db_GR.myDbHelper.STATUSDESC, newStatusdesc);
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
