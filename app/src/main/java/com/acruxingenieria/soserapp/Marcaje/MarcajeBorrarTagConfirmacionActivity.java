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
import android.widget.Toast;

import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.Sesion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarcajeBorrarTagConfirmacionActivity extends AppCompatActivity {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");


    private GetEquipmentTask getEquipmentTask = null;
    private GetEquipmentByIdTask getEquipmentByIdTask=null;

    private GetBinTask getBinTask = null;
    private GetBinByIdTask getBinByIdTask=null;

    private DeleteBinByIdTask deleteBinByIdTask=null;
    private DeleteEquipmentByIdTask deleteEquipmentByIdTask=null;

    private String codeID;
    private String binCodeActivity;
    private String equipmentCodeActivity;
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
                if (tipoMarcaje.equals("bin")){
                    attempDeleteBin(binCodeActivity);
                } else {
                    attempDeleteEquipment(equipmentCodeActivity);
                }


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

    private void attempEquipment(String id){
        if (getEquipmentTask != null) {
            return;
        }

        Log.e("TEST","progress true");
        ArrayList<String> aux = new ArrayList<>();
        aux.add(id);
        getEquipmentTask = new MarcajeBorrarTagConfirmacionActivity.GetEquipmentTask(aux);
        getEquipmentTask.execute((Void) null);
    }

    private void attempEquipmentById(String id){
        if (getEquipmentByIdTask != null) {
            return;
        }

        Log.e("TEST","progress true");

        getEquipmentByIdTask = new MarcajeBorrarTagConfirmacionActivity.GetEquipmentByIdTask(id);
        getEquipmentByIdTask.execute((Void) null);
    }

    public class GetEquipmentTask extends AsyncTask<Void, Void, Boolean> {

        String IDs;
        String serialCode;

        GetEquipmentTask(ArrayList<String> id) {
            IDs = "";
            for (int i=0;i<id.size();i++){
                IDs = IDs + id.get(i);
                if (i>1){
                    IDs = IDs + ",";
                }
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("node-red-soser-api.mybluemix.net")
                    .addPathSegment("equipment")
                    .addQueryParameter("tags_id", IDs)
                    .build();

            final Request request = new Request.Builder()
                    .url(httpUrl)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (response.body() != null) {

                    String jsonResponse = response.body().string();
                    try {

                        JSONObject obj = new JSONObject(jsonResponse);
                        JSONArray rows = obj.getJSONArray("rows");
                        JSONObject lastObj = rows.getJSONObject(rows.length()-1);
                        String id = lastObj.getString("id");
                        String[] serial_code = id.split(":");

                        serialCode=serial_code[1];
                        equipmentCodeActivity = serialCode;

                        return true;

                    } catch (Throwable tx) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                        return false;
                    }

                }

                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getEquipmentTask = null;

            if (success){
                Log.e("TEST","serialCode: "+serialCode);
                attempEquipmentById(serialCode);
            }

            if (!success) {
                TextView error = findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
                error.setText("Error en la solicitud, la etiqueta leída no está registrada.");
            }

        }

        @Override
        protected void onCancelled() {
            getEquipmentTask = null;
        }
    }

    public class GetEquipmentByIdTask extends AsyncTask<Void, Void, Boolean> {

        String ID;

        GetEquipmentByIdTask(String id) {
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
                    DATA = DATA +"Fecha de vencimiento: " + obj.getString("expire_date");
                    DATA = DATA + "\n";
                    DATA = DATA +"Cantidad: " + obj.getString("quantity") + " " +obj.getString("measure_unit");
                    DATA = DATA + "\n";
                    DATA = DATA + "Etiquetas: ";
                    DATA = DATA + "\n";
                    JSONArray aux = obj.getJSONArray("tags_id");
                    for (int i =0; i< aux.length();i++){
                        DATA = DATA + aux.get(i);
                        DATA = DATA + "\n";
                    }
                    DATA = DATA + "Tipo: " + obj.getString("type");
                    DATA = DATA + "\n";
                    DATA = DATA + "Creado: " + obj.getString("created_at");
                    DATA = DATA + "\n";
                    DATA = DATA + "Modificado: " + obj.getString("updated_at");

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                    return false;
                }

                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getEquipmentByIdTask = null;
            Log.e("TEST","progress false");

            if (success){
                showEraseData();

            } else {
                TextView tv_msg_info = (TextView) findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
                tv_msg_info.setText("Error en la lectura de la etiqueta.");
            }

        }

        @Override
        protected void onCancelled() {
            getEquipmentByIdTask = null;
            Log.e("TEST","progress false");
        }
    }

    private void attempBin(String id){
        if (getBinTask != null) {
            return;
        }

        ArrayList<String> aux = new ArrayList<>();
        aux.add(id);
        getBinTask = new MarcajeBorrarTagConfirmacionActivity.GetBinTask(aux);
        getBinTask.execute((Void) null);
    }

    private void attempBinById(String id){
        if (getBinByIdTask != null) {
            return;
        }

        getBinByIdTask = new MarcajeBorrarTagConfirmacionActivity.GetBinByIdTask(id);
        getBinByIdTask.execute((Void) null);
    }

    public class GetBinTask extends AsyncTask<Void, Void, Boolean> {

        String IDs;
        String binCode;

        GetBinTask(ArrayList<String> id) {
            IDs = "";
            for (int i=0;i<id.size();i++){
                IDs = IDs + id.get(i);
                if (i>1){
                    IDs = IDs + ",";
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("node-red-soser-api.mybluemix.net")
                    .addPathSegment("bins")
                    .addQueryParameter("tags_id", IDs)
                    .build();

            final Request request = new Request.Builder()
                    .url(httpUrl)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (response.body() != null) {

                    String jsonResponse = response.body().string();
                    try {

                        JSONObject obj = new JSONObject(jsonResponse);
                        JSONArray rows = obj.getJSONArray("rows");
                        JSONObject lastObj = rows.getJSONObject(rows.length()-1);
                        String id = lastObj.getString("id");
                        String[] bin_code = id.split(":");

                        binCode=bin_code[1];
                        binCodeActivity=binCode;
                        return true;

                    } catch (Throwable tx) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                        return false;
                    }

                }

                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getBinTask = null;
            //showProgress(false);

            if (success){
                Log.e("TEST","binCode: "+binCode);
                attempBinById(binCode);
            }

            if (!success) {
                TextView error = findViewById(R.id.tvMarcajeBorrarTagConfirmacionInfo);
                error.setText("Error en la solicitud, la etiqueta leída no está registrada.");
            }

        }

        @Override
        protected void onCancelled() {
            getEquipmentTask = null;
            //showProgress(false);
        }
    }

    public class GetBinByIdTask extends AsyncTask<Void, Void, Boolean> {

        String ID;

        GetBinByIdTask(String id) {
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

                try {
                    JSONObject obj = new JSONObject(jsonResponse);

                    DATA = "";
                    DATA = DATA + "Código de bin: " + obj.getString("bin_code")+ "\n";
                    DATA = DATA + "Código de bodega: " + obj.getString("warehouse_code")+ "\n";
                    DATA = DATA + "Tipo: " + obj.getString("type")+ "\n";
                    DATA = DATA + "Etiquetas: " + "\n";

                    JSONArray tags = new JSONArray();
                    tags = obj.getJSONArray("tags_id");
                    for (int i =0;i<tags.length();i++){
                        DATA = DATA + "   " + tags.getString(i) +"\n";
                    }

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                    return false;
                }

                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getBinByIdTask = null;

            if (success){
                showEraseData();

            } else {
                DATA = "Problema con la lectura, revisar etiqueta.";
                showEraseData();
            }

        }

        @Override
        protected void onCancelled() {
            getBinByIdTask = null;
        }
    }

    private void attempDeleteBin(String id){
        if (deleteBinByIdTask != null) {
            return;
        }

        deleteBinByIdTask = new MarcajeBorrarTagConfirmacionActivity.DeleteBinByIdTask(id);
        deleteBinByIdTask.execute((Void) null);
    }

    public class DeleteBinByIdTask extends AsyncTask<Void, Void, Boolean> {

        String ID;

        DeleteBinByIdTask(String id) {
            ID = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Log.e("TEST","background");
            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/bins/"+ID)
                    .method("DELETE",null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String jsonResponse = response.body().string();

                try {
                    Log.e("TEST","jsonResponse: "+jsonResponse);

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                    return false;
                }

                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            deleteBinByIdTask = null;
            Log.e("TEST","progress false");

            if (success){
                Log.e("TEST","success()");

            } else {
                DATA = "Problema con la lectura, revisar etiqueta.";
                showEraseData();
            }

        }

        @Override
        protected void onCancelled() {
            deleteBinByIdTask = null;
            Log.e("TEST","progress false");
        }
    }

    private void attempDeleteEquipment(String id){
        if (deleteEquipmentByIdTask != null) {
            return;
        }

        deleteEquipmentByIdTask = new MarcajeBorrarTagConfirmacionActivity.DeleteEquipmentByIdTask(id);
        deleteEquipmentByIdTask.execute((Void) null);
    }

    public class DeleteEquipmentByIdTask extends AsyncTask<Void, Void, Boolean> {

        String ID;

        DeleteEquipmentByIdTask(String id) {
            ID = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Log.e("TEST","background");
            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/equipment/"+ID)
                    .method("DELETE",null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String jsonResponse = response.body().string();

                try {
                    Log.e("TEST","jsonResponse: "+jsonResponse);

                } catch (Throwable tx) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                    return false;
                }

                return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            deleteEquipmentByIdTask = null;

            if (success){
                Log.e("TEST","success()");

            } else {
                DATA = "Problema con la lectura, revisar etiqueta.";
                showEraseData();
            }

        }

        @Override
        protected void onCancelled() {
            deleteEquipmentByIdTask = null;
            Log.e("TEST","progress false");
        }
    }

}