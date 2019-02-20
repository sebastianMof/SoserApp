package com.acruxingenieria.soserapp.Marcaje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.acruxingenieria.soserapp.MenuActivity;
import com.acruxingenieria.soserapp.R;

public class MarcajeActivity extends AppCompatActivity {

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
                    EditText et_marcaje_material_bin = (EditText) findViewById(R.id.etMarcajeMaterialBin);
                    EditText et_marcaje_material_fechavenc = (EditText) findViewById(R.id.etMarcajeMaterialFechaVenc);
                    EditText et_marcaje_material_cantidad = (EditText) findViewById(R.id.etMarcajeMaterialCantidad);

                    Intent intent =new Intent(MarcajeActivity.this,MarcajeGrabarTagActivity.class);

                    intent.putExtra("tipoMarcaje", "material");
                    intent.putExtra("marcajeMaterialNombre", et_marcaje_material_nombre.getText().toString());
                    intent.putExtra("marcajeMaterialStockcode", et_marcaje_material_stockcode.getText().toString());
                    intent.putExtra("marcajeMaterialBin", et_marcaje_material_bin.getText().toString());
                    intent.putExtra("marcajeMaterialFechavenc", et_marcaje_material_fechavenc.getText().toString());
                    intent.putExtra("marcajeMaterialCantidad", et_marcaje_material_cantidad.getText().toString());

                    startActivity(intent);

                } else if (binFragmentSelected){
                    EditText et_marcaje_bin_bin = (EditText) findViewById(R.id.etMarcajeBinBin);

                    Intent intent =new Intent(MarcajeActivity.this,MarcajeGrabarTagActivity.class);
                    intent.putExtra("tipoMarcaje", "bin");
                    intent.putExtra("marcajeBinBin", et_marcaje_bin_bin.getText().toString());
                    startActivity(intent);
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
                    EditText et_marcaje_material_nombre = (EditText) findViewById(R.id.etMarcajeMaterialNombre);
                    EditText et_marcaje_material_stockcode = (EditText) findViewById(R.id.etMarcajeMaterialStock);
                    EditText et_marcaje_material_bin = (EditText) findViewById(R.id.etMarcajeMaterialBin);
                    EditText et_marcaje_material_fechavenc = (EditText) findViewById(R.id.etMarcajeMaterialFechaVenc);
                    EditText et_marcaje_material_cantidad = (EditText) findViewById(R.id.etMarcajeMaterialCantidad);

                    Intent intent =new Intent(MarcajeActivity.this,MarcajeBorrarTagActivity.class);

                    intent.putExtra("tipoMarcaje", "material");
                    intent.putExtra("marcajeMaterialNombre", et_marcaje_material_nombre.getText().toString());
                    intent.putExtra("marcajeMaterialStockcode", et_marcaje_material_stockcode.getText().toString());
                    intent.putExtra("marcajeMaterialBin", et_marcaje_material_bin.getText().toString());
                    intent.putExtra("marcajeMaterialFechavenc", et_marcaje_material_fechavenc.getText().toString());
                    intent.putExtra("marcajeMaterialCantidad", et_marcaje_material_cantidad.getText().toString());

                    startActivity(intent);

                } else if (binFragmentSelected){
                    EditText et_marcaje_bin_bin = (EditText) findViewById(R.id.etMarcajeBinBin);

                    Intent intent =new Intent(MarcajeActivity.this,MarcajeBorrarTagActivity.class);
                    intent.putExtra("tipoMarcaje", "bin");
                    intent.putExtra("marcajeBinBin", et_marcaje_bin_bin.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}