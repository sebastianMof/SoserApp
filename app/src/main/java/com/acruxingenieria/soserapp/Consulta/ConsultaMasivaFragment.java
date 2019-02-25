package com.acruxingenieria.soserapp.Consulta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;


public class ConsultaMasivaFragment extends Fragment {

    private View savedView;

    private ArrayList<String> idLecturaMasiva = new ArrayList<>();

    public ConsultaMasivaFragment() {
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
        savedView = inflater.inflate(R.layout.fragment_consulta_masiva, container, false);
        setSavedView(savedView);

        TextView tv_msg = (TextView) getSavedView().findViewById(R.id.tvConsultaMasivaError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        return savedView;
    }

    public View getSavedView() {
        return savedView;
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    public boolean read() {
        idLecturaMasiva = new ArrayList<>();
        //LECTURA RFID -> idLecturaMasiva con los IDs
        try {
            idLecturaMasiva.add("id_lectura_1");
            idLecturaMasiva.add("id_lectura_2");
            idLecturaMasiva.add("id_lectura_3");
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }

    public ArrayList<String> getIdLecturaMasiva() {
        return idLecturaMasiva;
    }
}
