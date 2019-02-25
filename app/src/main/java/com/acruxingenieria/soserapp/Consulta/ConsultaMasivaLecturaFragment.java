package com.acruxingenieria.soserapp.Consulta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;
import java.util.Objects;

public class ConsultaMasivaLecturaFragment extends Fragment {

    private View savedView;

    private ArrayList<String> lecturaMasivaItems;
    private ArrayAdapter<String> lecturaMasivaAdapter;
    private ListView lv_lecturaMasiva;

    public ConsultaMasivaLecturaFragment() {
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
        savedView = inflater.inflate(R.layout.fragment_consulta_masiva_lectura, container, false);
        setSavedView(savedView);

        configureLecturaMasivaItems();
        configureCantidadTextView();
        configureLecturaMasivaListView();

        return savedView;
    }

    private void configureCantidadTextView() {
        TextView tv_consulta_masiva_lectura_cantidad = (TextView) getSavedView().findViewById(R.id.tvConsultaMasivaLecturaCantidad);
        int cantidad = lecturaMasivaItems.size();
        tv_consulta_masiva_lectura_cantidad.setText(String.valueOf(cantidad));

    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    private View getSavedView() {
        return savedView;
    }

    private void configureLecturaMasivaItems() {
        lecturaMasivaItems=((ConsultaActivity)Objects.requireNonNull(getActivity())).getIdLecturaMasiva();
    }

    private void configureLecturaMasivaListView() {
        lecturaMasivaAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,lecturaMasivaItems){
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

        lv_lecturaMasiva=(ListView)getSavedView().findViewById(R.id.lvConsultaMasivaLectura);
        lv_lecturaMasiva.setAdapter(lecturaMasivaAdapter);

        lv_lecturaMasiva.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getActivity(),adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_LONG).show();

                //Desplegar info para id = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

}