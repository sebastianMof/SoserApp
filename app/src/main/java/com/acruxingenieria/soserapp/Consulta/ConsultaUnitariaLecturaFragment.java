package com.acruxingenieria.soserapp.Consulta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

public class ConsultaUnitariaLecturaFragment extends Fragment {

    private View savedView;

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

        configureStockcodeTextView();
        configureNombreTextView();
        configureTagInfoTextView();

        return savedView;
    }

    private void configureStockcodeTextView() {
        TextView tv_stockcode= (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaStockcode);
    }

    private void configureNombreTextView() {
        TextView tv_nombre= (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaNombre);
    }

    private void configureTagInfoTextView() {
        TextView tv_tag_info= (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaLecturaInfo);
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    private View getSavedView(){
        return savedView;
    }

}