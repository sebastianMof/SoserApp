package com.acruxingenieria.soserapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.acruxingenieria.soserapp.Consulta.ConsultaActivity;
import com.acruxingenieria.soserapp.Marcaje.MarcajeActivity;

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

        configureButtonLogOut();

        receiveDataFromIntent();

        configureTitle();

        configureMenuItems();

        configureMenuListView();

    }

    @Override
    public void onBackPressed(){
        finish();
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
        menuAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menuItems){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(getResources().getColor(R.color.color3));
                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(getResources().getColor(R.color.color5));
                }

                return view;
            }
        };

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

    private void configureButtonLogOut() {
        Button btn_logout = (Button) findViewById(R.id.btnMenuLogout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
