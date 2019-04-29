package com.lcbenjamin.calculadora;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivityLog extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_log);

        Intent intent = getIntent();

        if(intent != null){

            Bundle params = intent.getExtras();

            if(params != null ){

                ArrayList expres = params.getStringArrayList("expressoes");

                ListView listview = (ListView) findViewById(R.id.listview);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,expres);

                listview.setAdapter(adapter);
            }
        }
    }




}
