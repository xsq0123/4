package com.example.billbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "count";

    private SimpleAdapter listAdapter;
    //private DBHelper dbHelper;
    //private SQLiteDatabase db;
    private ArrayList<Map<String,Object>> data;
    //private static String DB_NAME="splite.db";
    private Cursor cursor;
    private Map<String,Object>item;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }
    public void insert(Count count) {
        db.beginTransaction(); //事务
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("count",count.getMoney());
            contentValues.put("type",count.getType());
            contentValues.put("date", count.getDate());
            contentValues.put("describe",count.getDescribe());
            db.insert(TABLE_NAME,"id",contentValues);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
    public Double getResult(int type)
    {
        Double result = 0.0;
        Cursor c = db.rawQuery("select id,count,type,date,describe from "+ TABLE_NAME,null);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            if (c.getInt(2) == type)
                result += c.getFloat(1);
        }
        c.close();
        return result;
    }
    public void closeDB(){
        db.close();
    }
    public  void openDB(){

    }

    //showList();
//    dbHelper = new DBHelper(this);
  //  db = dbHelper.getWritableDatabase();
//        db.beginTransaction();

//        dbHelper=new DBHelper(this);
//        db=mgr.d.getWritableDatabase();
 //   data=new ArrayList<Map<String,Object>>();
 //   dbFindAll();




}