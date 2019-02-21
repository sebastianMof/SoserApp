package com.acruxingenieria.soserapp.Consulta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;


public class ConsultaMasivaFragment extends Fragment {

    private View savedView;

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

    private View getSavedView() {
        return savedView;
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }


}
