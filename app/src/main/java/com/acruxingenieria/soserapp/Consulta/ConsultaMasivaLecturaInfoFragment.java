package com.acruxingenieria.soserapp.Consulta;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

import java.util.Objects;

public class ConsultaMasivaLecturaInfoFragment extends Fragment {

    private View savedView;

    private String idLecturaMasiva;
    private String stockcode;
    private String nombre;
    private String info;

    public ConsultaMasivaLecturaInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        savedView = inflater.inflate(R.layout.fragment_consulta_masiva_lectura_info, container, false);

        setSavedView(savedView);

        setID(((ConsultaActivity)Objects.requireNonNull(getActivity())).getIdLecturaMasivaSelected());

        queryByID(idLecturaMasiva);

        configureStockcodeTextView();
        configureNombreTextView();
        configureTagInfoTextView();

        return savedView;
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    public View getSavedView(){
        return savedView;
    }

    private void queryByID(String id) {
        //Acá hay que hacer la query para el ID dado, lo siquiente es temporal
        if (id!=null){
            stockcode = "stockcode_"+id;
            nombre = "nombre_"+id;
            info = "info_"+id;
        }

    }

    private void configureStockcodeTextView() {
        TextView tv_stockcode= (TextView) getSavedView().findViewById(R.id.tvConsultaMasivaLecturaInfoStockcode);
        tv_stockcode.setText(stockcode);
    }

    private void configureNombreTextView() {
        TextView tv_nombre= (TextView) getSavedView().findViewById(R.id.tvConsultaMasivaLecturaInfoNombre);
        tv_nombre.setText(nombre);
    }

    private void configureTagInfoTextView() {
        TextView tv_tag_info= (TextView) getSavedView().findViewById(R.id.tvConsultaMasivaLecturaInfo);
        tv_tag_info.setText(info);
    }

    public void setID(String idLecturaMasiva) {
        this.idLecturaMasiva = idLecturaMasiva;
    }

}
