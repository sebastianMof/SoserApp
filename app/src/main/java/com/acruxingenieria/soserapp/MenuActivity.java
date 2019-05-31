package com.acruxingenieria.soserapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acruxingenieria.soserapp.Consulta.ConsultaActivity;
import com.acruxingenieria.soserapp.Consulta.ConsultaMasivaFiltro;
import com.acruxingenieria.soserapp.Marcaje.MarcajeActivity;
import com.acruxingenieria.soserapp.Marcaje.MarcajeGrabarTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {

    private ArrayList<String> menuItems;
    private ArrayAdapter<String> menuAdapter;
    private ListView lv_menu;

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    private HandheldTask handheldTask = null;
    private View mProgressView;
    private View mLayoutView;

    private Sesion session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        attempHandheld();

        configureButtonLogOut();

        configureTitle();

        configureMenuItems();

        configureMenuListView();

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private void configureTitle() {
        TextView tv_user = (TextView) findViewById(R.id.tvMenuNombre);
        tv_user.setText(session.getUser());

        TextView tv_bodega = (TextView) findViewById(R.id.tvMenuBodega);
        tv_bodega.setText(session.getBodegaSelected());


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
                        intent.putExtra("session",session);
                        startActivity(intent);
                    }

                        break;
                    case ("Consulta"):{
                        //ConsultaActivity en vez de ConsultaMasivaFiltro
                        Intent intent =new Intent(MenuActivity.this,ConsultaActivity.class);
                        intent.putExtra("session",session);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public class HandheldTask extends AsyncTask<Void, Void, Boolean> {

        HandheldTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            JSONObject postdata = new JSONObject();

            try {
                postdata.put("device_code",session.getDeviceId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
            Log.e("BODY",postdata.toString());

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/handhelds")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            handheldTask = null;
            //showProgress(false);

            if (success) {
                Log.e("TEST","success handheld");
            }

        }

        @Override
        protected void onCancelled() {
            handheldTask = null;
            //showProgress(false);
        }
    }

    private void attempHandheld(){
        if (handheldTask != null) {
            return;
        }

        //showProgress(true);
        handheldTask = new MenuActivity.HandheldTask();
        handheldTask.execute((Void) null);
    }
/*
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
*/

}
