package com.tanobel.it_yoga.tis_mobile.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Db_PR {
    myDbHelper myhelper;

    public Db_PR(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String branch, String docno, String no, String docref, String brgcode, String brgname, String brgdesc, String satcode, String qty, String sisastok, String iocode, String status, String statusdesc, String noref, String tglkirim, String approval, String userapproval, String statusold) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.BRANCH, branch);
        contentValues.put(myDbHelper.DOCNO, docno);
        contentValues.put(myDbHelper.NO, no);
        contentValues.put(myDbHelper.DOCREF, docref);
        contentValues.put(myDbHelper.BRGCODE, brgcode);
        contentValues.put(myDbHelper.BRGNAME, brgname);
        contentValues.put(myDbHelper.BRGDESC, brgdesc);
        contentValues.put(myDbHelper.SATCODE, satcode);
        contentValues.put(myDbHelper.QTY, qty);
        contentValues.put(myDbHelper.SISASTOK, sisastok);
        contentValues.put(myDbHelper.IOCODE, iocode);
        contentValues.put(myDbHelper.STATUS, status);
        contentValues.put(myDbHelper.STATUSDESC, statusdesc);
        contentValues.put(myDbHelper.NOREF, noref);
        contentValues.put(myDbHelper.TGLKIRIM, tglkirim);
        contentValues.put(myDbHelper.APPROVAL, approval);
        contentValues.put(myDbHelper.USERAPPROVAL, userapproval);
        contentValues.put(myDbHelper.STATUSOLD, statusold);
        long insert = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return insert;
    }

    public List<PR_Dtl> getDataAll() {
        List<PR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID, myDbHelper.BRANCH, myDbHelper.DOCNO, myDbHelper.NO, myDbHelper.DOCREF, myDbHelper.BRGCODE, myDbHelper.BRGNAME, myDbHelper.BRGDESC, myDbHelper.SATCODE, myDbHelper.QTY, myDbHelper.SISASTOK, myDbHelper.IOCODE, myDbHelper.STATUS, myDbHelper.STATUSDESC, myDbHelper.NOREF, myDbHelper.TGLKIRIM, myDbHelper.APPROVAL, myDbHelper.USERAPPROVAL, myDbHelper.STATUSOLD};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            PR_Dtl operatorTable = new PR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCNO)));
            operatorTable.setNo(cursor.getString(cursor.getColumnIndex(myDbHelper.NO)));
            operatorTable.setDocref(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCREF)));
            operatorTable.setBrgcode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setBrgname(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setBrgdesc(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGDESC)));
            operatorTable.setSatcode(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setQty(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY)));
            operatorTable.setSisastok(cursor.getString(cursor.getColumnIndex(myDbHelper.SISASTOK)));
            operatorTable.setIocode(cursor.getString(cursor.getColumnIndex(myDbHelper.IOCODE)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUS)));
            operatorTable.setStatusdesc(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSDESC)));
            operatorTable.setNoref(cursor.getString(cursor.getColumnIndex(myDbHelper.NOREF)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));
            operatorTable.setApproval(cursor.getString(cursor.getColumnIndex(myDbHelper.APPROVAL)));
            operatorTable.setUserapproval(cursor.getString(cursor.getColumnIndex(myDbHelper.USERAPPROVAL)));
            operatorTable.setStatusold(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUSOLD)));

            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PR_Dtl> getDataByStatusOld() {
        List<PR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID, myDbHelper.BRANCH, myDbHelper.DOCNO, myDbHelper.NO};
        String whereClause = myDbHelper.STATUS+" = "+myDbHelper.STATUSOLD;
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, whereClause, null, null, null, null);

        while (cursor.moveToNext()) {
            PR_Dtl operatorTable = new PR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCNO)));
            operatorTable.setNo(cursor.getString(cursor.getColumnIndex(myDbHelper.NO)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PR_Dtl> getDataGroupByStatus() {
        List<PR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.BRANCH, myDbHelper.DOCNO, myDbHelper.APPROVAL, myDbHelper.USERAPPROVAL, myDbHelper.STATUS};
        String whereClause = myDbHelper.STATUS+" <> "+myDbHelper.STATUSOLD+" and "+myDbHelper.STATUS+" <> ?";
        String[] whereArgs = new String[] {"C"};
        String columnsgroup = myDbHelper.BRANCH+","+ myDbHelper.DOCNO+","+ myDbHelper.STATUS;
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            PR_Dtl operatorTable = new PR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCNO)));
            operatorTable.setApproval(cursor.getString(cursor.getColumnIndex(myDbHelper.APPROVAL)));
            operatorTable.setUserapproval(cursor.getString(cursor.getColumnIndex(myDbHelper.USERAPPROVAL)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUS)));
            listAll.add(operatorTable);
        }

        return listAll;
    }

    public List<PR_Dtl> getDataGroupByStatusCancel() {
        List<PR_Dtl> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.BRANCH, myDbHelper.DOCNO, myDbHelper.APPROVAL, myDbHelper.USERAPPROVAL, myDbHelper.STATUS};
        String whereClause = myDbHelper.STATUS+" <> "+myDbHelper.STATUSOLD+" and "+myDbHelper.STATUS+" = ?";
        String[] whereArgs = new String[] {"C"};
        String columnsgroup = myDbHelper.BRANCH+","+ myDbHelper.DOCNO+","+ myDbHelper.STATUS;
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, whereClause, whereArgs, columnsgroup, null, null);

        while (cursor.moveToNext()) {
            PR_Dtl operatorTable = new PR_Dtl();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setBranch(cursor.getString(cursor.getColumnIndex(myDbHelper.BRANCH)));
            operatorTable.setDocno(cursor.getString(cursor.getColumnIndex(myDbHelper.DOCNO)));
            operatorTable.setApproval(cursor.getString(cursor.getColumnIndex(myDbHelper.APPROVAL)));
            operatorTable.setUserapproval(cursor.getString(cursor.getColumnIndex(myDbHelper.USERAPPROVAL)));
            operatorTable.setStatus(cursor.getString(cursor.getColumnIndex(myDbHelper.STATUS)));
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
        private static final String DATABASE_NAME = "DbPR";    // Database Name
        private static final String TABLE_NAME = "PR";   // Table Name
        private static final int DATABASE_Version = 1;  // Database Version
        private static final String ID = "id";
        private static final String BRANCH = "branch";
        private static final String DOCNO = "docno";
        private static final String NO = "no";
        private static final String DOCREF = "docref";
        private static final String BRGCODE = "brgcode";
        private static final String BRGNAME = "brgname";
        private static final String BRGDESC = "brgdesc";
        private static final String SATCODE = "satcode";
        private static final String QTY = "qty";
        private static final String SISASTOK = "sisastok";
        private static final String IOCODE = "iocode";
        private static final String STATUS = "status";
        private static final String STATUSDESC = "statusdesc";
        private static final String NOREF = "noref";
        private static final String TGLKIRIM = "tglkirim";
        private static final String APPROVAL = "approval";
        private static final String USERAPPROVAL = "userapproval";
        private static final String STATUSOLD = "statusold";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BRANCH + " CHAR(4)," + DOCNO + " CHAR(12), " + NO + " CHAR(2), " + DOCREF + " CHAR(12), " + BRGCODE + " VARCHAR(255), " + BRGNAME + " VARCHAR(225)," +
                "" + BRGDESC + " TEXT, " + SATCODE + " CHAR(10), " + QTY + " CHAR(20), " + SISASTOK + " CHAR(20), " + IOCODE + " VARCHAR(100), " + STATUS + " CHAR(1), " + STATUSDESC + " CHAR(100), " + NOREF + " CHAR(2), " +
                "" + TGLKIRIM + " CHAR(10), " + APPROVAL + " CHAR(1), " + USERAPPROVAL + " CHAR(1), " + STATUSOLD + " CHAR(1));";
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