package com.tanobel.it_yoga.tis_mobile.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Db_CRM_SO {
    myDbHelper myhelper;
    public Db_CRM_SO(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String docno, String brgcode, String brgname, int qty, String satcode, String tglkirim)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.DOCNO, docno);
        contentValues.put(myDbHelper.BRGCODE, brgcode);
        contentValues.put(myDbHelper.BRGNAME, brgname);
        contentValues.put(myDbHelper.QTY, qty);
        contentValues.put(myDbHelper.SATCODE, satcode);
        contentValues.put(myDbHelper.TGLKIRIM, tglkirim);
        long insert = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return insert;
    }

    public String getData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.DOCNO,myDbHelper.BRGCODE,myDbHelper.BRGNAME,myDbHelper.QTY,myDbHelper.SATCODE,myDbHelper.TGLKIRIM};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            String kode =cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM));
            String nama =cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME));
            buffer.append(kode+ "   " + nama + " \n");
        }
        return buffer.toString();
    }

    public List<CRM_SO_OrderDetail> getDataAll()
    {
        List<CRM_SO_OrderDetail> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.DOCNO,myDbHelper.BRGCODE,myDbHelper.BRGNAME,myDbHelper.QTY,myDbHelper.SATCODE,myDbHelper.TGLKIRIM};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            CRM_SO_OrderDetail operatorTable = new CRM_SO_OrderDetail();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setKode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setNama(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setQty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY))));
            operatorTable.setSatuan(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));

            listAll.add(operatorTable);
        }

        return listAll ;
    }

    public List<CRM_SO_OrderDetail> getDataByIdBrgCode(String id, String brgcode)
    {
        List<CRM_SO_OrderDetail> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.DOCNO,myDbHelper.BRGCODE,myDbHelper.BRGNAME,myDbHelper.QTY,myDbHelper.SATCODE,myDbHelper.TGLKIRIM};
        String whereClause = myDbHelper.ID+" <> ? and "+myDbHelper.BRGCODE+" = ?";
        String[] whereArgs = new String[] {id, brgcode};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);

        while (cursor.moveToNext())
        {
            CRM_SO_OrderDetail operatorTable = new CRM_SO_OrderDetail();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setKode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setNama(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setQty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY))));
            operatorTable.setSatuan(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));

            listAll.add(operatorTable);
        }

        return listAll ;
    }

    public List<CRM_SO_OrderDetail> getDataByBrgCodeTglKrm(String brgcode, String tglkirim)
    {
        List<CRM_SO_OrderDetail> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.DOCNO,myDbHelper.BRGCODE,myDbHelper.BRGNAME,myDbHelper.QTY,myDbHelper.SATCODE,myDbHelper.TGLKIRIM};
        String whereClause = myDbHelper.BRGCODE+" = ? AND "+myDbHelper.TGLKIRIM+" = ?";
        String[] whereArgs = new String[] {brgcode, tglkirim};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);

        while (cursor.moveToNext())
        {
            CRM_SO_OrderDetail operatorTable = new CRM_SO_OrderDetail();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setKode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setNama(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setQty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY))));
            operatorTable.setSatuan(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));

            listAll.add(operatorTable);
        }

        return listAll ;
    }

    public List<CRM_SO_OrderDetail> getDataByIdBrgCodeTglKrm(String id, String brgcode, String tglkirim)
    {
        List<CRM_SO_OrderDetail> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.DOCNO,myDbHelper.BRGCODE,myDbHelper.BRGNAME,myDbHelper.QTY,myDbHelper.SATCODE,myDbHelper.TGLKIRIM};
        String whereClause = myDbHelper.ID+" <> ? and " +  myDbHelper.BRGCODE+" = ? and " +  myDbHelper.TGLKIRIM+" = ?";
        String[] whereArgs = new String[] {id, brgcode, tglkirim};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);

        while (cursor.moveToNext())
        {
            CRM_SO_OrderDetail operatorTable = new CRM_SO_OrderDetail();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setKode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setNama(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setQty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY))));
            operatorTable.setSatuan(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));

            listAll.add(operatorTable);
        }

        return listAll ;
    }

    public List<CRM_SO_OrderDetail> getDataByTglKrm(String tglkirim)
        {
        List<CRM_SO_OrderDetail> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.ID,myDbHelper.DOCNO,myDbHelper.BRGCODE,myDbHelper.BRGNAME,myDbHelper.QTY,myDbHelper.SATCODE,myDbHelper.TGLKIRIM};
        String whereClause = myDbHelper.TGLKIRIM+" = ?";
        String[] whereArgs = new String[] {tglkirim};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,whereClause,whereArgs,null,null,null);

        while (cursor.moveToNext())
        {
            CRM_SO_OrderDetail operatorTable = new CRM_SO_OrderDetail();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.ID))));
            operatorTable.setKode(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGCODE)));
            operatorTable.setNama(cursor.getString(cursor.getColumnIndex(myDbHelper.BRGNAME)));
            operatorTable.setQty(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myDbHelper.QTY))));
            operatorTable.setSatuan(cursor.getString(cursor.getColumnIndex(myDbHelper.SATCODE)));
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));

            listAll.add(operatorTable);
        }

        return listAll ;
    }

    public List<CRM_SO_OrderDetail> getGroupTglKirim()
    {
        List<CRM_SO_OrderDetail> listAll = new ArrayList<>();

        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.TGLKIRIM};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,myDbHelper.TGLKIRIM,null,null);

        while (cursor.moveToNext())
        {
            CRM_SO_OrderDetail operatorTable = new CRM_SO_OrderDetail();
            //here get all data from cursor and set it into setter method like below
            operatorTable.setId(0);
            operatorTable.setKode("");
            operatorTable.setNama("");
            operatorTable.setQty(0);
            operatorTable.setSatuan("");
            operatorTable.setTglkirim(cursor.getString(cursor.getColumnIndex(myDbHelper.TGLKIRIM)));

            listAll.add(operatorTable);
        }

        return listAll ;
    }

    public int delete(String id)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={id};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.ID+" = ?",whereArgs);
        return  count;
    }

    public int update(String id , String newBrgcode, String newBrgname, int newQty, String newSatcode, String newTglkirim)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.BRGCODE,newBrgcode);
        contentValues.put(myDbHelper.BRGNAME,newBrgname);
        contentValues.put(myDbHelper.QTY,newQty);
        contentValues.put(myDbHelper.SATCODE,newSatcode);
        contentValues.put(myDbHelper.TGLKIRIM,newTglkirim);
        String[] whereArgs= {id};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.ID+" = ?",whereArgs );
        return count;
    }

    public int updateTglKrm(String newTglkirim)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TGLKIRIM,newTglkirim);
        int count =db.update(myDbHelper.TABLE_NAME,contentValues,null,null);
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "DbCRM";    // Database Name
        private static final String TABLE_NAME = "CRM_SO";   // Table Name
        private static final int DATABASE_Version = 1;  // Database Version
        private static final String ID="id";     // Column I (Primary Key)
        private static final String DOCNO = "docno";    //Column II
        private static final String BRGCODE= "brgcode";    // Column III
        private static final String BRGNAME= "brgname";
        private static final String QTY= "qty";
        private static final String SATCODE= "satcode";
        private static final String TGLKIRIM= "tglkirim";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DOCNO+" CHAR(12), "+BRGCODE+" VARCHAR(255), "+ BRGNAME+" VARCHAR(225),"+ QTY+" INTEGER," +
                ""+ SATCODE+" CHAR(10),"+ TGLKIRIM+" CHAR(10));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.i("Info", ""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Log.i("Info", ""+e);
            }
        }
    }
}