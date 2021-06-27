package com.example.HomeBalance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class MeditationAnzeigenFragment extends Fragment {

    TextView meditation1title, meditation2title, meditation3title;
    ImageButton mediation1bild, mediation2bild, mediation3bild;
    String[] meditationNr1, meditationNr2, meditationNr3;

    DatenbankHelferMeditation datenbankMeditation;

    /**
     * onCreate-Methode wird bei erstmaligem Aufruf der Activity ausgeführt
     *
     * @return Gibt die fertige view an fragment_rezepteanzeigen zurück
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_meditationanzeigen, container, false);

        datenbankMeditation = new DatenbankHelferMeditation(getActivity());

        /**
         * Initialisierung der View-Komponenten
         */
        meditation1title = (TextView) view.findViewById(R.id.meditation1);
        meditation2title = (TextView) view.findViewById(R.id.meditation2);
        meditation3title = (TextView) view.findViewById(R.id.meditation3);
        mediation1bild = (ImageButton) view.findViewById(R.id.meditationButton1);
        mediation2bild = (ImageButton) view.findViewById(R.id.meditationButton2);
        mediation3bild = (ImageButton) view.findViewById(R.id.meditationButton3);

        meditationNr1 = new String[3];
        meditationNr2 = new String[3];
        meditationNr3 = new String[3];
        /**
         * Auslesen der Eingabe-Datenbank für den User-Namen
         */
        Cursor dataEingabe = datenbankMeditation.getData();
        dataEingabe.moveToLast();

        meditationNr1[0] = (dataEingabe.getString(1));
        meditationNr1[1] = (dataEingabe.getString(2));
        meditationNr1[2] = (dataEingabe.getString(3));
        meditationNr2[0] = (dataEingabe.getString(4));
        meditationNr2[1] = (dataEingabe.getString(5));
        meditationNr2[2] = (dataEingabe.getString(6));
        meditationNr3[0] = (dataEingabe.getString(7));
        meditationNr3[1] = (dataEingabe.getString(8));
        meditationNr3[2] = (dataEingabe.getString(9));

        meditation1title.setText(meditationNr1[0]);
        meditation2title.setText(meditationNr2[0]);
        meditation3title.setText(meditationNr3[0]);

        Picasso.get().load(meditationNr1[1]).into(mediation1bild);
        Picasso.get().load(meditationNr2[1]).into(mediation2bild);
        Picasso.get().load(meditationNr3[1]).into(mediation3bild);


        mediation1bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meditationNr1[2]));
                    startActivity(browserIntent);
                }
            }
        });

        mediation2bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meditationNr2[2]));
                    startActivity(browserIntent);
                }
            }
        });

        mediation3bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meditationNr3[2]));
                    startActivity(browserIntent);
                }
            }
        });

        return view;
    }


    /**
     * Internetverbindung wird getestet
     *
     * @return boolean, ob Internetverbindung vorhanden
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}

