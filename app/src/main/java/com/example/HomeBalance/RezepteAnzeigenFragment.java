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

public class RezepteAnzeigenFragment extends Fragment {

    TextView rezept1title, rezept2title, rezept3title;
    ImageButton rezept1bild, rezept2bild, rezept3bild;
    String[] rezeptNr1, rezeptNr2, rezeptNr3;

    /**
     * onCreate-Methode wird bei erstmaligem Aufruf der Activity ausgeführt
     *
     * @return Gibt die fertige view an fragment_rezepteanzeigen zurück
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_rezepteanzeigen, container, false);

        /**
         * Initialisierung der View-Komponenten
         */
        rezept1title = (TextView) view.findViewById(R.id.rezept1);
        rezept2title = (TextView) view.findViewById(R.id.rezept2);
        rezept3title = (TextView) view.findViewById(R.id.rezept3);
        rezept1bild = (ImageButton) view.findViewById(R.id.rezeptButton1);
        rezept2bild = (ImageButton) view.findViewById(R.id.rezeptButton2);
        rezept3bild = (ImageButton) view.findViewById(R.id.rezeptButton3);

        rezeptNr1 = new String[3];
        rezeptNr2 = new String[3];
        rezeptNr3 = new String[3];



        /**
         * Auslesen der Eingabe-Datenbank für den User-Namen
         */
        Cursor dataRezepte = DatenbankHelferRezepte.getInstance(this.getContext()).getData();
        if( dataRezepte != null && dataRezepte.moveToFirst() ) {
            dataRezepte.moveToLast();

            rezeptNr1[0] = (dataRezepte.getString(1));
            rezeptNr1[1] = (dataRezepte.getString(2));
            rezeptNr1[2] = (dataRezepte.getString(3));
            rezeptNr2[0] = (dataRezepte.getString(4));
            rezeptNr2[1] = (dataRezepte.getString(5));
            rezeptNr2[2] = (dataRezepte.getString(6));
            rezeptNr3[0] = (dataRezepte.getString(7));
            rezeptNr3[1] = (dataRezepte.getString(8));
            rezeptNr3[2] = (dataRezepte.getString(9));

            rezept1title.setText(rezeptNr1[0]);
            rezept2title.setText(rezeptNr2[0]);
            rezept3title.setText(rezeptNr3[0]);

            Picasso.get().load(rezeptNr1[1]).into(rezept1bild);
            Picasso.get().load(rezeptNr2[1]).into(rezept2bild);
            Picasso.get().load(rezeptNr3[1]).into(rezept3bild);
        } else {
            toastMessage("Es wurden noch keine Rezepte gesucht!");
        }

        rezept1bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    CreditsHandler.getInstance(getContext()).addCredits(2);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rezeptNr1[2]));
                    startActivity(browserIntent);
                }
            }
        });

        rezept2bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    CreditsHandler.getInstance(getContext()).addCredits(2);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rezeptNr2[2]));
                    startActivity(browserIntent);
                }
            }
        });

        rezept3bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    CreditsHandler.getInstance(getContext()).addCredits(2);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rezeptNr3[2]));
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

