package com.example.billbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final int OUT = 1;
    private static final int IN = 2;
    private DBManager mgr;
    private EditText etCount,etDescribe;
    private RadioGroup radioGroup;
    private TextView tvInfo;
    private Button btnAdd;
    private int countType = OUT;
    private ListView listView;
    private SimpleAdapter listAdapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<Map<String,Object>> data;
    private static String DB_NAME="splite.db";
    private Cursor cursor;
    private Map<String,Object>item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT){

            setContentView(R.layout.activity_main);
        }
        else{
            setContentView(R.layout.landscapet);
        }
        //初始化 DBManager
        mgr = new DBManager(this);
        etCount = (EditText) findViewById(R.id.etCount);
        etDescribe = (EditText) findViewById(R.id.etDescribe);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        tvInfo = (TextView)findViewById(R.id.tvInfo);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        listView=(ListView) findViewById(R.id.listView);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioOut:
                        countType = OUT;
                        break;
                    case R.id.radioIn:
                        countType = IN;
                        break;
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Count count = new Count();
                long time = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str = format.format(new Date(time));
                count.setDate(str);
                count.setMoney(Double.parseDouble(etCount.getText().toString()));
                count.setDescribe(etDescribe.getText().toString());
                count.setType(countType + "");
                mgr.insert(count); //插入
                resetInfo();
                //mgr.dbFindAll();
                //showList();
            }
        });
        resetInfo();

        Button selBtn=(Button) findViewById(R.id.queall);
        selBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbFindAll();


            }
        });

        listView=(ListView) findViewById(R.id.listView);
        //初始化 DBManager
        data=new ArrayList<Map<String,Object>>();
        dbFindAll();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String,Object>listItem=(Map<String,Object>)
                        listView.getItemAtPosition(position);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object>listItem=(Map<String,Object>)
                        listView.getItemAtPosition(position);
//                et_word.setText((String) listItem.get("word"));
//                et_mean.setText((String) listItem.get("mean"));
//                et_egg.setText((String) listItem.get("egg"));
//                selID=(String) listItem.get("_id");
                //Log.i("mydbDemo","id="+selID);
            }
        });
    }

    public void resetInfo(){
        Double out = mgr.getResult(OUT);
        Double in = mgr.getResult(IN);
        Double all = in - out;
        tvInfo.setText("合计支出："+out+" 合计收入："+in+" \n 支出收入合计：" +all+"。");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();//应用的最后一个 Activity 关闭时应释放 DB
        mgr.closeDB();
    }

    private void showList(){
        listAdapter=new SimpleAdapter(this,data,
                R.layout.listview,
                new String[]{"id","count","type","date","describe"},
                new int[]{R.id.tvID,R.id.tvmoney,R.id.tvtype,R.id.tvdate,R.id.tvdescribe});
        listView.setAdapter(listAdapter);
    }
    public void dbFindAll(){
        data.clear();
        cursor=db.query(com.example.billbook.DBHelper.TABLE_NAME,null,null,null,null,
                null,"id ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String id=cursor.getString(0);
            Double count=cursor.getDouble(1);
            String type=cursor.getString(2);
            String date=cursor.getString(3);
            String describe=cursor.getString(4);
            item=new HashMap<String, Object>();
            item.put("id",id);
            item.put("count",count);
            item.put("type",type);
            item.put("date",date);
            item.put("describe",describe);
            data.add(item);
            cursor.moveToNext();
        }
         showList();
    }




}
