package com.example.HomeBalance;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
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
    LinearLayout linearLayout;
    private TextView name;
    private Button wakeup, morningWork, afternoonWork, eveningWork, lunch, naping, freetime, dinner, sleep, hilfe;
    private int hour, minuten;
    private String zeit, testNull;

    String a[];
    String c[] = new String[]{"Aufstehzeit:", "Arbeitsbeginn:", "Pause:", "Arbeitsfortsetzung:", "Powernap:", "Freizeit", "Abendessen", "Arbeitsfortsetzung am Abend:", "Schlafenszeit:"};

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



        /**
         * Auslesen der Eingabe-Datenbank für den User-Namen
         */
        Cursor dataEingabe = datenbankEingabe.getData();
        dataEingabe.moveToLast();
        name.setText("Hallo " + dataEingabe.getString(1) + "!");

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);


        /**
         * Auslesen der Optimierungs-Datenbank für den Tagesablaufplan + dynamische Auffüllung der View
         */
        final Cursor dataOptimierung = datenbankOptimierung.getData();
        dataOptimierung.moveToLast();

        int b = 1;

        for (int i = 0; i<9; i++){
            testNull = dataOptimierung.getString(b);
            if (testNull == null) {
            }else {
                TextView textView2 = new TextView(getActivity());
                textView2.setTextSize(20);
                textView2.setTextAlignment(view.TEXT_ALIGNMENT_CENTER);
                textView2.setTextColor(Color.BLACK);
                textView2.setText(c[i]);
                linearLayout.addView(textView2);

                Button button2 = new Button(getActivity());
                button2.setBackgroundResource(R.drawable.buttonshape);
                button2.setId(i+1);
                button2.setTextSize(15);
                button2.setText(testNull);
                button2.setTextColor(Color.WHITE);
                linearLayout.addView(button2);

                final int index = b;
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zeit = dataOptimierung.getString(index);
                        weckerStellen(zeit);
                    }
                });
            }
            b++;
        }
        return view;
    }

    /**
     * Alarm für mitgegebne Zeit wird in Uhren-App gesetzt
     */
    private void weckerStellen(String zeit) {
        if (zeit!=null) {
            hour = Integer.parseInt(zeit.substring(0, 2));
            minuten = Integer.parseInt(zeit.substring(3));
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

            intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
            intent.putExtra(AlarmClock.EXTRA_MINUTES, minuten);
            startActivity(intent);
        }
    }
}
