package com.acruxingenieria.soserapp.Consulta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

import java.util.Objects;

public class ConsultaUnitariaLecturaFragment extends Fragment {

    private View savedView;
    private String idLecturaUnitaria;
    private String stockcode;
    private String nombre;
    private String info;

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

        setID(((ConsultaActivity)Objects.requireNonNull(getActivity())).getIdLecturaUnitaria());

        queryByID(idLecturaUnitaria);

        configureStockcodeTextView();
        configureNombreTextView();
        configureTagInfoTextView();

        return savedView;
    }

    private void queryByID(String id) {
        //Acá hay que hacer la query para el ID dado, lo siquiente es temporal
        stockcode = "stockcode_"+id;
        nombre = "nombre_"+id;
        info = "info_"+id;
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
}