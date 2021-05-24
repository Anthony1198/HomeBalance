package com.example.HomeBalance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //Floating Button zum Hinzufügen von neuen Daten
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Neue Daten bitte eintragen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Übersicht als initiale Auswahl in der Navigation
        bottomNav.setSelectedItemId(R.id.nav_home);

        //Übersicht als initiales Fragment im Container
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
    }

    //Anzeige des ausgewählten Fragments
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_meditation:
                            selectedFragment = new MeditationFragment();
                            break;
                        case R.id.nav_wetter:
                            selectedFragment = new WetterFragment();
                            break;
                        case R.id.nav_optimierung:
                            selectedFragment = new OptimierungFragment();
                            break;
                        case R.id.nav_rezepte:
                            selectedFragment = new RezepteFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}