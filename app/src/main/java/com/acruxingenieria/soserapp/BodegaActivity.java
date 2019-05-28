package com.acruxingenieria.soserapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BodegaActivity extends AppCompatActivity {

    private BodegaTask bodegaTask = null;

    private ArrayList<String> positionsList;
    private ArrayList<String> bodegasList;

    private String positionSelected;
    private String bodegaSelected;

    private Sesion session;

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    private View mProgressView;
    private View mLayoutView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        mProgressView = findViewById(R.id.bodega_progress);
        mLayoutView = findViewById(R.id.ll_bodega);

        configureTitle();

        configurePositionList();
        configureSpinnerPositions();

        configureBodegaList();
        configureSpinnerBodegas();

        configureButtonSubmit();

        configureButtonLogOut();

    }

    private void configureTitle() {
        TextView tv_user = (TextView) findViewById(R.id.tvBodegaUsuario);
        tv_user.setText(session.getUser().toUpperCase());
    }

    private void configurePositionList() {
        positionsList = new ArrayList<>();
        positionsList.add("Administrador");
    }

    private void configureBodegaList() {
        bodegasList = new ArrayList<>();
        getWarehouses();

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

    public void getWarehouses(){
        if (bodegaTask != null) {
            return;
        }

        showProgress(true);
        bodegaTask = new BodegaActivity.BodegaTask();
        bodegaTask.execute((Void) null);

    }

    public class BodegaTask extends AsyncTask<Void, Void, Boolean> {

        BodegaTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/warehouses")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (response.body() != null) {

                    String jsonResponse = response.body().string();

                    try {

                        JSONObject obj = new JSONObject(jsonResponse);
                        JSONArray rows = obj.getJSONArray("rows");
                        for (int i = 0; i < rows.length();i++){
                            JSONObject aux = new JSONObject(rows.getString(i));
                            String[] parts = aux.getString("id").split(":");
                            bodegasList.add(parts[1]);
                        }


                    } catch (Throwable tx) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                    }

                }

                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            bodegaTask = null;
            showProgress(false);

            if (!success) {
                Toast.makeText(BodegaActivity.this, "No hay conexión al servicio.", Toast.LENGTH_LONG).show();
                if (bodegasList.size()==0){
                    bodegasList.add("No hay bodegas disponibles");
                }
            }
            configureSpinnerBodegas();
        }

        @Override
        protected void onCancelled() {
            bodegaTask = null;
            showProgress(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLayoutView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
