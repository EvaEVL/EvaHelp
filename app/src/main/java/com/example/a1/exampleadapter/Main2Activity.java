package com.example.a1.exampleadapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ru.yandex.speechkit.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Main2Activity extends AppCompatActivity  {
    BoxAdapter boxAdapter;
    static final String API_KEY_YANDEX = "670655db-edd8-4ee5-b3b7-e9d47ec78ed8";

    int key_for_adapterList = MainActivity.what_i_get;

    ArrayList<Product> arrLib = new ArrayList<>();
    final Context context = this;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (MainActivity.ap.get(key_for_adapterList).arrProducts != null){
            arrLib = MainActivity.ap.get(key_for_adapterList).arrProducts;
        }

        SpeechKit.getInstance().configure(this, API_KEY_YANDEX);

        boxAdapter = new BoxAdapter(this,arrLib);

        ListView lvMain1 = (ListView) findViewById(R.id.lvMein1);
        lvMain1.setAdapter(boxAdapter);

        lvMain1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int ih, long l) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_get_names);
                dialog.show();

                final EditText ed1 = (EditText) dialog.findViewById(R.id.ed1);
                final EditText ed2 = (EditText) dialog.findViewById(R.id.edi2);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                Button ok = (Button) dialog.findViewById(R.id.ok);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrLib.set(ih, new Product(ed1.getText().toString(), ed2.getText().toString()));
                        boxAdapter.notifyDataSetInvalidated();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
    public void addToList1(View view ){
        arrLib.add(new Product("word","word1"));
        Toast.makeText(this,String.valueOf(arrLib.size())+" "+key_for_adapterList,Toast.LENGTH_SHORT).show();
        if (arrLib != null)
            MainActivity.ap.get(key_for_adapterList).arrProducts = arrLib;

        boxAdapter.notifyDataSetChanged();
        MainActivity.saveToFile(MainActivity.ap,getApplicationContext());
    }

    public void go_Back(View v){
        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
        startActivity (intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.ap.get(key_for_adapterList).arrProducts != null){
            arrLib = MainActivity.ap.get(key_for_adapterList).arrProducts;
        }
    }

    public void play(View view) {
        ThreadPlayVoice th = new ThreadPlayVoice(arrLib);
        th.run();
    }
}
