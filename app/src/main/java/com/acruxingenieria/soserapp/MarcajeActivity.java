package com.acruxingenieria.soserapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MarcajeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_marcaje_material:
                    fragment = new MarcajeMaterialFragment();
                    break;
                case R.id.navigation_marcaje_bin:
                    fragment = new MarcajeBinFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje);

        //navBar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationMarcaje);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //default Fragment
        loadFragment(new MarcajeMaterialFragment());
    }

    //REPLACE FRAGMENT METHOD
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.marcaje_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
