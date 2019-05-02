package com.acruxingenieria.soserapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.acruxingenieria.soserapp.QR.QrBuiltInActivity;

import java.util.ArrayList;

public class BodegaActivity extends AppCompatActivity {

    private ArrayList<String> positionsList;
    private ArrayList<String> bodegasList;

    private String positionSelected;
    private String bodegaSelected;
    private Sesion session;

    //Qr
    //public QrBuiltInActivity qrBuiltInActivity = new QrBuiltInActivity();
    //public boolean hasQrBuiltIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        configureTitle();

        configurePositionList();
        configureSpinnerPositions();

        configureBodegaList();
        configureSpinnerBodegas();

        configureButtonSubmit();

        configureButtonLogOut();

        //QrTask qrTask = new QrTask(qrBuiltInActivity);
        //qrTask.execute();

    }

    private void configureTitle() {
        TextView tv_user = (TextView) findViewById(R.id.tvBodegaUsuario);
        tv_user.setText(session.getUser().toUpperCase());
    }

    private void configurePositionList() {
        positionsList = new ArrayList<>();
        //positionsList.add("POSICION_EJEMPLO");
        positionsList.add(session.getPosicion());
    }

    private void configureBodegaList() {
        bodegasList = new ArrayList<>();
        //bodegasList.add("BODEGA_EJEMPLO");
        bodegasList.add(session.getBodega());
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
                session.setPositionSelected(positionSelected);

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
                session.setBodegaSelected(bodegaSelected);

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
                intent.putExtra("session", session);
                startActivity(intent);
            }
        });

    }

    private void configureButtonLogOut() {
        Button btn_logout = (Button) findViewById(R.id.btnBodegaSalir);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BodegaActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent =new Intent(BodegaActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
/*
    private class QrTask extends AsyncTask<Boolean, Boolean, Boolean> {

        public QrBuiltInActivity qrBuiltInActivity;

        QrTask(QrBuiltInActivity qrBuiltInActivity){
            this.qrBuiltInActivity = qrBuiltInActivity;
        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
             return qrBuiltInActivity.hasQRLector();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            hasQrBuiltIn = result;
            Log.e("TEST","hasQrBuiltIn: "+Boolean.toString(hasQrBuiltIn));
        }

    }

    public boolean getHasQrBuiltIn(){
        return hasQrBuiltIn;
    }

*/
}
