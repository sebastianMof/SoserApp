package com.acruxingenieria.soserapp.Marcaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.Sesion;

public class MarcajeActivity extends AppCompatActivity {

    private Sesion session;

    private boolean materialFragmentSelected = false;
    private boolean binFragmentSelected = false;
    private Fragment marcajeMaterialFragment = new MarcajeMaterialFragment();
    private Fragment marcajeBinFragment = new MarcajeBinFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_marcaje_material:
                    fragment = marcajeMaterialFragment;
                    materialFragmentSelected = true;
                    binFragmentSelected = false;
                    break;
                case R.id.navigation_marcaje_bin:
                    fragment = marcajeBinFragment;
                    materialFragmentSelected = false;
                    binFragmentSelected = true;
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        //navBar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationMarcaje);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //default Fragment
        loadFragment(marcajeMaterialFragment);
        materialFragmentSelected = true;
        binFragmentSelected = false;

        //Configs
        configureButtonGrabarTag();
        configureButtonBorrarTag();
        configureButtonAtras();
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    //REPLACE FRAGMENT METHOD
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.marcaje_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnMarcajeAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureButtonGrabarTag(){

        Button btn_grabar_tag = (Button) findViewById(R.id.btnMarcajeGrabarTAG);
        btn_grabar_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialFragmentSelected){
                    EditText et_marcaje_material_nombre = (EditText) findViewById(R.id.etMarcajeMaterialNombre);
                    EditText et_marcaje_material_stockcode = (EditText) findViewById(R.id.etMarcajeMaterialStock);
                    EditText et_marcaje_material_serialcode = (EditText) findViewById(R.id.etMarcajeMaterialSerialcode);
                    EditText et_marcaje_material_bin = (EditText) findViewById(R.id.etMarcajeMaterialBin);
                    EditText et_marcaje_material_fechavenc = (EditText) findViewById(R.id.etMarcajeMaterialFechaVenc);
                    EditText et_marcaje_material_cantidad = (EditText) findViewById(R.id.etMarcajeMaterialCantidad);

                    Intent intent =new Intent(MarcajeActivity.this,MarcajeGrabarTagActivity.class);

                    intent.putExtra("tipoMarcaje", "material");
                    intent.putExtra("marcajeMaterialNombre", et_marcaje_material_nombre.getText().toString());
                    intent.putExtra("marcajeMaterialStockcode", et_marcaje_material_stockcode.getText().toString());
                    intent.putExtra("marcajeMaterialSerialcode", et_marcaje_material_serialcode.getText().toString());
                    intent.putExtra("marcajeMaterialBin", et_marcaje_material_bin.getText().toString());
                    intent.putExtra("marcajeMaterialFechavenc", et_marcaje_material_fechavenc.getText().toString());
                    intent.putExtra("marcajeMaterialCantidad", et_marcaje_material_cantidad.getText().toString());

                    String unit = ((MarcajeMaterialFragment)marcajeMaterialFragment).getUnitSelected();
                    intent.putExtra("marcajeMaterialUnidad", unit);

                    intent.putExtra("session", session);

                    intent.putExtra("mUser", session.getUser());
                    intent.putExtra("positionSelected", session.getPositionSelected());
                    intent.putExtra("bodegaSelected", session.getBodegaSelected());

                    startActivityForResult(intent, 1);

                } else if (binFragmentSelected){
                    EditText et_marcaje_bin_bin = (EditText) findViewById(R.id.etMarcajeBinBin);

                    Intent intent =new Intent(MarcajeActivity.this,MarcajeGrabarTagActivity.class);

                    intent.putExtra("tipoMarcaje", "bin");
                    intent.putExtra("marcajeBinBin", et_marcaje_bin_bin.getText().toString());

                    intent.putExtra("mUser", session.getUser());
                    intent.putExtra("positionSelected", session.getPositionSelected());
                    intent.putExtra("bodegaSelected", session.getBodegaSelected());

                    intent.putExtra("session", session);

                    startActivityForResult(intent, 1);
                }


            }
        });
    }

    private void configureButtonBorrarTag() {
        Button btn_borrar_tag = (Button) findViewById(R.id.btnMarcajeBorrarTAG);
        btn_borrar_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialFragmentSelected){
                    Intent intent =new Intent(MarcajeActivity.this,MarcajeBorrarTagActivity.class);

                    intent.putExtra("mUser", session.getUser());
                    intent.putExtra("positionSelected", session.getPositionSelected());
                    intent.putExtra("bodegaSelected", session.getBodegaSelected());
                    intent.putExtra("tipoMarcaje", "material");

                    intent.putExtra("session", session);

                    startActivityForResult(intent,3);

                } else if (binFragmentSelected){
                    Intent intent =new Intent(MarcajeActivity.this,MarcajeBorrarTagActivity.class);

                    intent.putExtra("mUser", session.getUser());
                    intent.putExtra("positionSelected", session.getPositionSelected());
                    intent.putExtra("bodegaSelected", session.getBodegaSelected());
                    intent.putExtra("tipoMarcaje", "bin");
                    intent.putExtra("session", session);

                    startActivityForResult(intent,3);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {//1 for GRABAR TAG
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(MarcajeActivity.this,"Marcado exitoso",Toast.LENGTH_LONG).show();
                View fragmentView;
                if (data.getStringExtra("tipoMarcaje").equals("material")){
                    fragmentView = ((MarcajeMaterialFragment)marcajeMaterialFragment).getSavedView();

                    EditText et_nombre = fragmentView.findViewById(R.id.etMarcajeMaterialNombre);
                    EditText et_stockcode = fragmentView.findViewById(R.id.etMarcajeMaterialStock);
                    EditText et_serialcode = fragmentView.findViewById(R.id.etMarcajeMaterialSerialcode);
                    EditText et_fechavenc = fragmentView.findViewById(R.id.etMarcajeMaterialFechaVenc);
                    EditText et_cantidad = fragmentView.findViewById(R.id.etMarcajeMaterialCantidad);

                    et_nombre.setText("");
                    et_stockcode.setText("");
                    et_serialcode.setText("");
                    et_fechavenc.setText("");
                    et_cantidad.setText("");

                } else if (data.getStringExtra("tipoMarcaje").equals("bin")){
                    fragmentView = ((MarcajeBinFragment)marcajeBinFragment).getSavedView();
                    EditText et_bin = fragmentView.findViewById(R.id.etMarcajeBinBin);
                    et_bin.setText("");
                }

            }
        } else if (requestCode == 2){//2 for read BIN(from fragment)
            if(resultCode == Activity.RESULT_OK){
                String result= data.getStringExtra("result");
                ((MarcajeMaterialFragment)marcajeMaterialFragment).setReadedBIN(result);

            }
        } else if (requestCode == 3){//3 for DELETE
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(MarcajeActivity.this,"Borrado exitoso",Toast.LENGTH_LONG).show();
            }
        }
    }

}