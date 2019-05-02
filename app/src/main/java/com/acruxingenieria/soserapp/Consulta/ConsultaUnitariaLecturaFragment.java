package com.acruxingenieria.soserapp.Consulta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

import java.util.Objects;

public class ConsultaUnitariaLecturaFragment extends Fragment {

    private View savedView;
    private String idLecturaUnitaria;
    private String stockcode;
    private String nombre;
    private String info;

    private View mProgressView;
    private View mFormView1;
    private View mFormView2;

    private Sesion session;

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
            //session=;
            Log.e("TEST",session.getToken());

            stockcode = "stockcode_"+id;
            nombre = "nombre_"+id;
            info = "info_"+id;
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
}