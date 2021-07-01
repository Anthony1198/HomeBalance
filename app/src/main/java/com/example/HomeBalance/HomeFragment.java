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
    Button creditsButton;
    private TextView name;
    private int hour, minuten;
    private String zeit, testNull;


    String c[] = new String[]{"Aufstehzeit:", "Arbeitsbeginn:", "Pause:", "Arbeitsfortsetzung:", "Powernap:", "Freizeit", "Abendessen", "Arbeitsfortsetzung am Abend:", "Schlafenszeit:"};

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
         * Initialisierung der View-Komponenten
         */
        name = (TextView) view.findViewById(R.id.name);
        creditsButton = (Button) view.findViewById(R.id.credits);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        CreditsActivity.getInstance().showCreditsOnButton(creditsButton);


        /**
         * Auslesen der Eingabe-Datenbank für den User-Namen, wenn nicht vorhanden, dann steht die Dateneingabe noch aus
         */
        Cursor dataEingabe = DatenbankHelferEingabe.getInstance(this.getContext()).getData();
        if( dataEingabe != null && dataEingabe.moveToFirst() ) {
            dataEingabe.moveToLast();
            name.setText("Hallo " + dataEingabe.getString(1) + "!");
        } else {
            TextView anzeige = new TextView(getActivity());
            anzeige.setTextSize(20);
            anzeige.setTextAlignment(view.TEXT_ALIGNMENT_CENTER);
            anzeige.setTextColor(Color.BLACK);
            anzeige.setText("Bitte die Dateneingabe auf dem + durchführen!");
            linearLayout.addView(anzeige);
        }



        /**
         * Auslesen der Optimierungs-Datenbank für den Tagesablaufplan + dynamische Auffüllung der View
         */
        final Cursor dataOptimierung = DatenbankHelferOptimierung.getInstance(this.getContext()).getData();

        if( dataOptimierung != null && dataEingabe.moveToFirst() ) {
            dataOptimierung.moveToLast();

            int b = 1;

            for (int i = 0; i < 9; i++) {
                testNull = dataOptimierung.getString(b);
                if (testNull == null) {
                } else {
                    TextView anzeige = new TextView(getActivity());
                    anzeige.setTextSize(20);
                    anzeige.setTextAlignment(view.TEXT_ALIGNMENT_CENTER);
                    anzeige.setTextColor(Color.BLACK);
                    anzeige.setText(c[i]);
                    linearLayout.addView(anzeige);

                    Button btn = new Button(getActivity());
                    btn.setBackgroundResource(R.drawable.buttonshape);
                    btn.setId(i + 1);
                    btn.setTextSize(15);
                    btn.setText(testNull);
                    btn.setTextColor(Color.WHITE);
                    linearLayout.addView(btn);

                    final int index = b;
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CreditsActivity.getInstance().addCreditsAndShowOnButton(1, creditsButton);
                            zeit = dataOptimierung.getString(index);
                            weckerStellen(zeit);
                        }
                    });
                }
                b++;
            }
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
