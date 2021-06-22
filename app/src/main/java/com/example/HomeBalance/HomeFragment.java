package com.example.HomeBalance;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Controller für die Anzeige der Tagesstruktur, welche durch die eingegebenen Daten mit der API brechnet wurde
 */
public class HomeFragment extends Fragment {

    /**
     * Deklaration nötiger Varibalen
     */
    private TextView name;
    private Button wakeup, morningWork, afternoonWork, eveningWork, lunch, naping, freetime, dinner, sleep, hilfe;
    private int hour, minuten;
    private String zeit, testNull;

    /**
     * Deklaration der SQLite-Datenbanken
     */
    DatenbankHelferOptimierung datenbankOptimierung;
    DatenbankHelferEingabe datenbankEingabe;

    /**
     * onCreate-Methode wird bei erstmaligem Aufruf der Activity ausgeführt
     *
     * @return Gibt die fertige view an fragment_home zurück
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /**
         * Initialisierung der Datenbanken
         */
        datenbankEingabe = new DatenbankHelferEingabe(getActivity());
        datenbankOptimierung = new DatenbankHelferOptimierung(getActivity());


        /**
         * Initialisierung der View-Komponenten
         */
        name = (TextView) view.findViewById(R.id.name);
        wakeup = (Button) view.findViewById(R.id.aufstehzeitButton);
        morningWork = (Button) view.findViewById(R.id.arbeitsbeginnButton);
        afternoonWork = (Button) view.findViewById(R.id.arbeitfortsetzungButton);
        eveningWork = (Button) view.findViewById(R.id.arbeitfortsetzung2Button);
        lunch = (Button) view.findViewById(R.id.pauseButton);
        naping = (Button) view.findViewById(R.id.napingButton);
        freetime = (Button) view.findViewById(R.id.freizeitButton);
        dinner = (Button) view.findViewById(R.id.abendessenButton);
        sleep = (Button) view.findViewById(R.id.schlafenButton);
        hilfe = (Button) view.findViewById(R.id.hilfeButton);


        /**
         * Auslesen der Eingabe-Datenbank für den User-Namen
         */
        Cursor dataEingabe = datenbankEingabe.getData();
        dataEingabe.moveToLast();
        name.setText("Hallo " + dataEingabe.getString(1) + "!");


        /**
         * Auslesen der Optimierungs-Datenbank für den Tagesablaufplan
         */
        final Cursor dataOptimierung = datenbankOptimierung.getData();
        dataOptimierung.moveToLast();

        wakeup.setText(dataOptimierung.getString(1));
        morningWork.setText(dataOptimierung.getString(2));
        lunch.setText(dataOptimierung.getString(5));
        afternoonWork.setText(dataOptimierung.getString(3));
        dinner.setText(dataOptimierung.getString(8));
        sleep.setText(dataOptimierung.getString(9));

        /**
         * IF-Bedingungen bei nicht vorhandenen Werten
         */
       testNull = dataOptimierung.getString(4);
        if (testNull == null) {
            eveningWork.setText("Nicht notwendig");
        } else {
            eveningWork.setText(testNull);
        }

        testNull = dataOptimierung.getString(6);
        if (testNull == null) {
            naping.setText("Nicht gewünscht");
        } else {
            naping.setText(testNull);
        }

        testNull = dataOptimierung.getString(7);
        if (testNull == null) {
            freetime.setText("Keine übrige Zeit");
        } else {
            freetime.setText(testNull);
        }


        /**
         * Bei Click auf einen Zeit-Button wird eine Methode zum setzen eines Alarms in der Uhren-App durchgeführt
         */
        wakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(1));
                weckerStellen(zeit);
            }
        });

        morningWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(2));
                weckerStellen(zeit);
            }
        });

        afternoonWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(3));
                weckerStellen(zeit);
            }
        });

        eveningWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(4));
                if (zeit == null){
                    toastMessage("Nicht möglich!");
                } else {
                    weckerStellen(zeit);
                }
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(5));
                weckerStellen(zeit);
            }
        });
        naping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(6));
                if (zeit == null){
                    toastMessage("Nicht möglich!");
                } else {
                    weckerStellen(zeit);
                }
            }
        });
        freetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(7));
                if (zeit == null){
                    toastMessage("Nicht möglich!");
                } else {
                    weckerStellen(zeit);
                }
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(8));
                weckerStellen(zeit);
            }
        });
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zeit = (dataOptimierung.getString(9));
                weckerStellen(zeit);
            }
        });

        hilfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new HilfeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
            }
        });


        return view;
    }

    /**
     * Alarm für mitgegebne Zeit wird in Uhren-App gesetzt
     */
    private void weckerStellen(String zeit) {
        hour = Integer.parseInt(zeit.substring(0, 2));
        minuten = Integer.parseInt(zeit.substring(3));
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minuten);
        startActivity(intent);
    }

    /**
     * Methode zum Ausführen von Toast Messages
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
