package com.acruxingenieria.soserapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BodegaActivity extends AppCompatActivity {

    private ArrayList<String> positionsList;
    private ArrayList<String> bodegasList;

    private String positionSelected;
    private String bodegaSelected;
    private String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);

        configureTitle();

        configurePositionList();
        configureSpinnerPositions();

        configureBodegaList();
        configureSpinnerBodegas();

        configureButtonSubmit();

        configureButtonLogOut();


    }

    private void configureTitle() {
        mUser= getIntent().getStringExtra("mUser");
        TextView tv_user = (TextView) findViewById(R.id.tvBodegaUsuario);
        tv_user.setText(mUser.toUpperCase());
    }

    private void configurePositionList() {
        positionsList = new ArrayList<>();
        positionsList.add("POSICION_EJEMPLO");
    }

    private void configureBodegaList() {
        bodegasList = new ArrayList<>();
        bodegasList.add("BODEGA_EJEMPLO");
    }

    private void configureSpinnerPositions() {
        Spinner spn_pos = (Spinner) findViewById(R.id.spinnerBodegaPosicion);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(BodegaActivity.this, R.layout.spinner_item, positionsList){

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.color1));

                return view;
            }
        };
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_pos.setAdapter(spnAdapter);

        spn_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                positionSelected =  (String) adapterView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void configureSpinnerBodegas() {
        Spinner spn_bodega = (Spinner) findViewById(R.id.spinnerBodegaNombre);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(BodegaActivity.this, R.layout.spinner_item, bodegasList){

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.color1));

                return view;
            }
        };
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_bodega.setAdapter(spnAdapter);

        spn_bodega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bodegaSelected =  (String) adapterView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void configureButtonSubmit() {
        Button btn_login = (Button) findViewById(R.id.btnBodegaIngresar);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BodegaActivity.this,MenuActivity.class);
                intent.putExtra("mUser", mUser);
                intent.putExtra("positionSelected", positionSelected);
                intent.putExtra("bodegaSelected", bodegaSelected);
                startActivity(intent);
                finish();
            }
        });

    }

    private void configureButtonLogOut() {
        Button btn_logout = (Button) findViewById(R.id.btnBodegaSalir);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BodegaActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
