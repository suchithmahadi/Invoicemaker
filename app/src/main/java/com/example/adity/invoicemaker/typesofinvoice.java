package com.example.adity.invoicemaker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class typesofinvoice extends AppCompatActivity {

    ArrayList<HashMap<String,String>> INTERESTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typesofinvoice);




        INTERESTS=new ArrayList<>();

        HashMap<String, String> data1 = new HashMap<>();
        HashMap<String, String> data2 = new HashMap<>();
        HashMap<String, String> data3 = new HashMap<>();
        HashMap<String, String> data4= new HashMap<>();
        HashMap<String, String> data5 = new HashMap<>();
        HashMap<String, String> data6 = new HashMap<>();
        HashMap<String, String> data7 = new HashMap<>();
        HashMap<String, String> data8 = new HashMap<>();
        HashMap<String, String> data9 = new HashMap<>();
        HashMap<String, String> data10 = new HashMap<>();

        data1.put("type","For Intra state");
        data1.put("description","(goods traveling within a state)");
        INTERESTS.add(data1);

        data2.put("type","For Inter state");
        data2.put("description","(goods traveling Between states)");
        INTERESTS.add(data2);

        data3.put("type","For Export");
        data3.put("description","()");
        INTERESTS.add(data3);

        data4.put("type","For Exempt Supply");
        data4.put("description","(goods or services or both which attracts nil rate of tax)");
        INTERESTS.add(data4);

        data5.put("type","Sales Returns");
        data5.put("description","()");
        INTERESTS.add(data5);

        data6.put("type","For Charging Extra");
        data6.put("description","()");
        INTERESTS.add(data6);

        data7.put("type","Receipt Voucher");
        data7.put("description","()");
        INTERESTS.add(data7);


        data8.put("type","Refund Voucher");
        data8.put("description","()");
        INTERESTS.add(data8);

        data9.put("type","Payment Voucher");
        data9.put("description","()");
        INTERESTS.add(data9);

        data10.put("type","Invoice for Rcm");
        data10.put("description","()");
        INTERESTS.add(data10);
        ListView lv=(ListView)findViewById(R.id.listv);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListAdapter adapter=new SimpleAdapter(typesofinvoice.this,INTERESTS,R.layout.listitem,new String[]{"type","description"},new int[]{R.id.type,R.id.description});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(typesofinvoice.this,InvoiceGenerate.class));
            }
        });

    }
}
