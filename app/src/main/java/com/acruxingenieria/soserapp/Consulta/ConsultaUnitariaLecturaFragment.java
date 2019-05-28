package com.acruxingenieria.soserapp.Consulta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.Sesion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConsultaUnitariaLecturaFragment extends Fragment {

    private View savedView;
    private String idLecturaUnitaria;
    private String stockcode;
    private String nombre;
    private String info;

    private View mProgressView;
    private View mFormView1;
    private View mFormView2;

    private GetEquipmentTask getEquipmentTask = null;
    private GetEquipmentByIdTask getEquipmentByIdTask = null;


    public ConsultaUnitariaLecturaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        savedView = inflater.inflate(R.layout.fragment_consulta_unitaria_lectura, container, false);
        setSavedView(savedView);

        mFormView1 = getSavedView().findViewById(R.id.consulta_unitaria_lectura_linear_1);
        mFormView2 = getSavedView().findViewById(R.id.consulta_unitaria_lectura_linear_2);
        mProgressView = getSavedView().findViewById(R.id.consulta_unitaria_lectura_progress);

        setID(((ConsultaActivity)Objects.requireNonNull(getActivity())).getIdLecturaUnitaria());

        queryByID(idLecturaUnitaria);

        configureStockcodeTextView();
        configureNombreTextView();
        configureTagInfoTextView();

        return savedView;
    }

    private void queryByID(String id) {
        //Acá hay que hacer la query para el ID dado, lo siquiente es temporal
        if (id!=null){
            showProgress(true);
            ArrayList<String> aux = new ArrayList<>();
            aux.add(id);
            attemp(aux);

        }

    }

    private void configureStockcodeTextView() {
        TextView tv_stockcode= (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaStockcode);
        tv_stockcode.setText(stockcode);
    }

    private void configureNombreTextView() {
        TextView tv_nombre= (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaNombre);
        tv_nombre.setText(nombre);
    }

    private void configureTagInfoTextView() {
        TextView tv_tag_info= (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaInfo);
        tv_tag_info.setText(info);
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    private View getSavedView(){
        return savedView;
    }

    public void setID(String idLecturaUnitaria) {
        this.idLecturaUnitaria = idLecturaUnitaria;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView1.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView2.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView1.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView1.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mFormView2.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView2.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mFormView1.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView2.setVisibility(show ? View.GONE : View.VISIBLE);

        }
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
                    .addHeader("Authorization",((ConsultaActivity)Objects.requireNonNull(getActivity())).getSession().getToken())
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
            showProgress(false);

            if (success){
                Log.e("TEST","serialCode: "+serialCode);
                attempById(serialCode);
            }

            if (!success) {
                TextView error = getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaInfo);
                error.setText("Error en la solicitud, revisar tag leído.");
            }

        }

        @Override
        protected void onCancelled() {
            getEquipmentTask = null;
            showProgress(false);
        }
    }

    public class GetEquipmentByIdTask extends AsyncTask<Void, Void, Boolean> {

        String serialCode;

        GetEquipmentByIdTask(String serialCode) {
            this.serialCode=serialCode;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            //https://node-red-soser-api.mybluemix.net/equipment/:serial_code
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("node-red-soser-api.mybluemix.net")
                    .addPathSegment("equipment")
                    .addPathSegment(serialCode)
                    .build();

            final Request request = new Request.Builder()
                    .url(httpUrl)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",((ConsultaActivity)Objects.requireNonNull(getActivity())).getSession().getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (response.body() != null) {

                    String jsonResponse = response.body().string();
                    try {

                        JSONObject obj = new JSONObject(jsonResponse);

                        stockcode = "Código de stock: "+obj.getString("stock_code");
                        nombre = "Nombre: "+obj.getString("name");

                        info = "";
                        info = info + "Cantidad: " + obj.getString("quantity") + obj.getString("measure_unit")+"\n";
                        info = info + "Tipo: " + obj.getString("type")+"\n";
                        info = info + "código de serie: " + obj.getString("serial_code")+"\n";
                        info = info + "código de bin: " + obj.getString("bin_code")+"\n";
                        info = info + "Fecha de vencimiento: " + obj.getString("expire_date")+"\n";

                        info = info + "Etiquetas: "+"\n";
                        JSONArray tags = new JSONArray();
                        tags = obj.getJSONArray("tags_id");
                        for (int i = 0 ; i < tags.length() ; i++){
                            info = info + tags.getString(i)+"\n";
                        }

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
            getEquipmentByIdTask = null;
            showProgress(false);

            if (success){
                Log.e("TEST","success");
                configureStockcodeTextView();
                configureNombreTextView();
                configureTagInfoTextView();

            }

            if (!success) {
                TextView error = getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaInfo);
                error.setText("Error en la solicitud, revisar tag leído.");
            }

        }

        @Override
        protected void onCancelled() {
            getEquipmentByIdTask = null;
            showProgress(false);
        }
    }


    private void attemp(ArrayList<String> id){
        if (getEquipmentTask != null) {
            return;
        }

        showProgress(true);
        getEquipmentTask = new GetEquipmentTask(id);
        getEquipmentTask.execute((Void) null);
    }

    private void attempById(String serialCode){
        if (getEquipmentByIdTask != null) {
            return;
        }

        showProgress(true);
        getEquipmentByIdTask = new GetEquipmentByIdTask(serialCode);
        getEquipmentByIdTask.execute((Void) null);
    }

}