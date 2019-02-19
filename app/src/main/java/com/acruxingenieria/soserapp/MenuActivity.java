package com.acruxingenieria.soserapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private String mUser;
    private String positionSelected;
    private String bodegaSelected;

    private ArrayList<String> menuItems;
    private ArrayAdapter<String> menuAdapter;
    private ListView lv_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        receiveDataFromIntent();

        configureTitle();

        configureMenuItems();

        configureMenuListView();

    }

    private void receiveDataFromIntent() {
        mUser= getIntent().getStringExtra("mUser");
        positionSelected= getIntent().getStringExtra("positionSelected");
        bodegaSelected= getIntent().getStringExtra("bodegaSelected");

    }

    private void configureTitle() {
        TextView tv_user = (TextView) findViewById(R.id.tvMenuNombre);
        tv_user.setText(mUser);

        TextView tv_bodega = (TextView) findViewById(R.id.tvMenuBodega);
        tv_bodega.setText(bodegaSelected);


    }

    private void configureMenuItems() {
        menuItems=new ArrayList<String>();
        //method to get de items according to data received and replace this
        menuItems.add("Marcaje");
        menuItems.add("Consulta");
    }

    private void configureMenuListView() {
        menuAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuItems);

        lv_menu=(ListView)findViewById(R.id.lvMenu);
        lv_menu.setAdapter(menuAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (adapterView.getItemAtPosition(i).toString()) {

                    case ("Marcaje"): {
                        Intent intent =new Intent(MenuActivity.this,MarcajeActivity.class);
                        intent.putExtra("mUser", mUser);
                        intent.putExtra("positionSelected", positionSelected);
                        intent.putExtra("bodegaSelected", bodegaSelected);
                        startActivity(intent);
                    }

                        break;
                    case ("Consulta"):{
                        Intent intent =new Intent(MenuActivity.this,ConsultaActivity.class);
                        intent.putExtra("mUser", mUser);
                        intent.putExtra("positionSelected", positionSelected);
                        intent.putExtra("bodegaSelected", bodegaSelected);
                        startActivity(intent);

                    }
                        break;

                }
            }
        });
    }

}
