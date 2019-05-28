package com.acruxingenieria.soserapp.Marcaje;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.Sesion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarcajeBorrarTagConfirmacionActivity extends AppCompatActivity {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    private GetBinByCodeTask getBinTask = null;
    private GetEquipmentByCodeTask getEquipmentTask = null;

    private String codeID;
    private String tipoMarcaje;
    private String DATA="";
    private String lectorSelected;

    private Sesion session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_borrar_tag_confirmacion);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        TextView tv_msg_error = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionError);
        tv_msg_error.setMovementMethod(new ScrollingMovementMethod());
        TextView tv_msg_info = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
        tv_msg_info.setMovementMethod(new ScrollingMovementMethod());

        receiveDataFromIntent();

        getEraseData();

        configureButtonAtras();
        configureButtonConfirmar();
    }

    private void getEraseData() {
        //METHOD TO GET THE DATA TO ERASE
        /*
        codeID, mUser, positionSelected, bodegaSelected --> DATA;
        */
        if (tipoMarcaje.equals("bin")){
            attempBin(codeID);
        } else if (tipoMarcaje.equals("material")){
            attempEquipment(codeID);
        }

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
                String result = codeID;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                returnIntent.putExtra("lectorSelected",lectorSelected);


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
        tipoMarcaje = getIntent().getStringExtra("tipoMarcaje");
        lectorSelected = getIntent().getStringExtra("lectorSelected");
    }

    public class GetBinByCodeTask extends AsyncTask<Void, Void, Boolean> {

        String ID;

        GetBinByCodeTask(String id) {
         ID = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/bins/"+ID)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String jsonResponse = response.body().string();
                Log.e("TEST", jsonResponse);

                try {
                    JSONObject obj = new JSONObject(jsonResponse);

                    DATA = "tipo: " + obj.getString("type");
                    DATA = DATA + "\n";
                    DATA = DATA +"codigo de bin: " + obj.getString("bin_code");
                    DATA = DATA + "\n";
                    DATA = DATA +"codigo de bodega: " + obj.getString("warehouse_code");
                    DATA = DATA + "\n";
                    DATA = DATA + "etiquetas: ";
                    DATA = DATA + "\n";
                    JSONArray aux = obj.getJSONArray("tags_id");
                    for (int i =0; i< aux.length();i++){
                        DATA = DATA + aux.get(i);
                        DATA = DATA + "\n";
                    }
                    DATA = DATA + "creado: " + obj.getString("created_at");
                    DATA = DATA + "\n";
                    DATA = DATA + "modificado: " + obj.getString("updated_at");

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                }


                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getBinTask = null;
            Log.e("TEST","progress false");

            showEraseData();

            if (!success) {
                TextView tv_msg_info = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
                tv_msg_info.setText("Error en la lectura de la etiqueta.");

            }

        }

        @Override
        protected void onCancelled() {
            getBinTask = null;
            Log.e("TEST","progress false");
        }
    }

    public class GetEquipmentByCodeTask extends AsyncTask<Void, Void, Boolean> {

        String ID;

        GetEquipmentByCodeTask(String id) {
            ID = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/equipment/"+ID)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String jsonResponse = response.body().string();

                try {
                    JSONObject obj = new JSONObject(jsonResponse);

                    DATA = "Nombre: " + obj.getString("name");
                    DATA = DATA + "\n";
                    DATA = DATA + "Stockcode: " + obj.getString("stock_code");
                    DATA = DATA + "\n";
                    DATA = DATA +"Código de bin: " + obj.getString("bin_code");
                    DATA = DATA + "\n";
                    DATA = DATA +"Vencimiento: " + obj.getString("expire_date");
                    DATA = DATA + "\n";
                    DATA = DATA +"Cantidad: " + obj.getString("quantity") + " " +obj.getString("measure_unit");
                    DATA = DATA + "\n";
                    DATA = DATA + "etiquetas: ";
                    DATA = DATA + "\n";
                    JSONArray aux = obj.getJSONArray("tags_id");
                    for (int i =0; i< aux.length();i++){
                        DATA = DATA + aux.get(i);
                        DATA = DATA + "\n";
                    }
                    DATA = DATA + "tipo: " + obj.getString("type");
                    DATA = DATA + "creado: " + obj.getString("created_at");
                    DATA = DATA + "\n";
                    DATA = DATA + "modificado: " + obj.getString("updated_at");

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                }




                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getBinTask = null;
            Log.e("TEST","progress false");

            showEraseData();

            if (!success) {
                TextView tv_msg_info = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
                tv_msg_info.setText("Error en la lectura de la etiqueta.");

            }

        }

        @Override
        protected void onCancelled() {
            getBinTask = null;
            Log.e("TEST","progress false");
        }
    }

    private void attempBin(String id){
        if (getBinTask != null) {
            return;
        }

        Log.e("TEST","progress true");

        getBinTask = new MarcajeBorrarTagConfirmacionActivity.GetBinByCodeTask(id);
        getBinTask.execute((Void) null);
    }
    private void attempEquipment(String id){
        if (getEquipmentTask != null) {
            return;
        }

        Log.e("TEST","progress true");

        getEquipmentTask = new MarcajeBorrarTagConfirmacionActivity.GetEquipmentByCodeTask(id);
        getEquipmentTask.execute((Void) null);
    }

}
