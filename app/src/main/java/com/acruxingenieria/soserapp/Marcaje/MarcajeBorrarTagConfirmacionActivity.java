package com.acruxingenieria.soserapp.Marcaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

public class MarcajeBorrarTagConfirmacionActivity extends AppCompatActivity {

    private String codeID;
    private String mUser;
    private String positionSelected;
    private String bodegaSelected;
    private String DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_borrar_tag_confirmacion);

        TextView tv_msg_error = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionError);
        tv_msg_error.setMovementMethod(new ScrollingMovementMethod());
        TextView tv_msg_info = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
        tv_msg_info.setMovementMethod(new ScrollingMovementMethod());

        receiveDataFromIntent();
        getEraseData();
        showEraseData();

        configureButtonAtras();
        configureButtonConfirmar();
    }

    private void getEraseData() {
        //METHOD TO GET THE DATA TO ERASE
        /*
        codeID, mUser, positionSelected, bodegaSelected --> DATA;
        */
        DATA = "Información del TAG";
    }

    private void showEraseData() {
        //DISPLAY THE DATA ON SCREEN
        TextView tv_msg_info = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
        tv_msg_info.setText("");
        tv_msg_info.append(DATA);
    }



    private void configureButtonConfirmar() {

        Button btn_borrar_tag = (Button) findViewById(R.id.btnMarcajeBorrarTagConfirmacion);
        btn_borrar_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DELETE METHOD

                String result = "readedTAG";
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnMarcajeBorrarTagConfirmacionAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void receiveDataFromIntent() {

        codeID = getIntent().getStringExtra("code");

        mUser = getIntent().getStringExtra("mUser");
        positionSelected = getIntent().getStringExtra("positionSelected");
        bodegaSelected = getIntent().getStringExtra("bodegaSelected");

    }

}
