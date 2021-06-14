package com.example.HomeBalance;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class DatenEingabeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private String urlMitName;
    private String urlMitNameOptimierung;

    String vornameInhalt;
    String aufstehzeitInhalt;
    String routineInhalt;
    String arbneitszeitInhalt;
    int alterInhalt;
    Boolean napInhalt;
    Boolean fruehstueckInhalt;

    TextView ausgewaehlt;


    DatenbankActivity1 datenbank;
    private Button abschicken, aufstehzeitButton, routineButton, arbeitszeitButton;
    private TextView vorname, alter, aufstehzeit, routine, arbeitszeit, textView;
    private Switch nap, fruehstueck;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dateneingabe);

        vorname = (EditText) findViewById(R.id.vorname);
        alter = (EditText) findViewById(R.id.alter);
        aufstehzeit = (TextView) findViewById(R.id.aufstehzeit);
        routine = (TextView) findViewById(R.id.routine);
        arbeitszeit = (TextView) findViewById(R.id.arbeitszeit);
        abschicken = (Button) findViewById(R.id.abschicken);
        aufstehzeitButton = (Button) findViewById(R.id.aufstehzeitButton);
        routineButton = (Button) findViewById(R.id.routineButton);
        arbeitszeitButton = (Button) findViewById(R.id.arbeitszeitButton);
        nap = (Switch) findViewById(R.id.nap);
        fruehstueck = (Switch) findViewById(R.id.fruehstueck);
        datenbank = new DatenbankActivity1(this);

        textView = (TextView) findViewById(R.id.testtest);



       aufstehzeitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ausgewaehlt = aufstehzeit;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "timePickerAustehzeit");
            }
        });

        routineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ausgewaehlt = routine;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "timePickerRoutine");
            }
        });

        arbeitszeitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ausgewaehlt = arbeitszeit;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "timePickerArbeitszeit");
            }
        });




        abschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vorname.getText().length() != 0 && alter.getText().length() != 0 &&  aufstehzeit.getText().length() != 0 && routine.getText().length() != 0 && arbeitszeit.getText().length() != 0) {
                    vornameInhalt = vorname.getText().toString();
                    alterInhalt = Integer.parseInt(alter.getText().toString());
                    aufstehzeitInhalt = aufstehzeit.getText().toString();
                    routineInhalt = routine.getText().toString();
                    arbneitszeitInhalt = arbeitszeit.getText().toString();

                    napInhalt = nap.isChecked();
                    fruehstueckInhalt = fruehstueck.isChecked();

                    AddData(vornameInhalt, alterInhalt, aufstehzeitInhalt, routineInhalt, arbneitszeitInhalt, napInhalt, fruehstueckInhalt);

                    if (isOnline() == false) {
                        toastMessage("Keine Internetverbindung!");
                    } else {
                        urlMitName = "http://localhost:8080/api/schedule?" + "nap=" + napInhalt + "&age=" + alterInhalt + "&breakfast=" + fruehstueckInhalt + "&wakeUpTime=" + aufstehzeitInhalt + "&getReadyDuration=" + routineInhalt + "&workingHours=" + arbneitszeitInhalt;

                        // Button deaktivieren während ein HTTP-Request läuft
                        abschicken.setEnabled(false);

                        toastMessage("Daten werden verarbeitet!");

                        String JSON = null;
                        try {
                            JSON = holeOptimierungsDaten();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        textView.setText(JSON);



                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //startActivity(intent);
                    }

                } else {
                    toastMessage("Daten nicht vollständig!");
                }
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int stunden, int minuten) {
        ausgewaehlt.setText(stunden + " : " + minuten);
    }

    /**
     * Daten werden für Speicherung an die Datenbank weitergeleitet
     */
    private void AddData(String newEntry, int newEntry2, String newEntry3, String newEntry4, String newEntry5, Boolean newEntry6, Boolean newEntry7) {
        boolean insertData = datenbank.addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6, newEntry7);

            if (insertData) {
                //toastMessage("Daten wurden erfolgreich gespeichert!");
            } else {
                toastMessage("Etwas ist schief gelaufen :(");
            }
    }


    private String holeOptimierungsDaten() throws Exception {

        URL url = null;
        HttpURLConnection conn = null;
        String httpErgebnisDokument = "";

        url = new URL(urlMitName);
        conn = (HttpURLConnection) url.openConnection();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

            throw new Exception(getString(R.string.HTTPS));

        } else {

            InputStream is = conn.getInputStream();
            InputStreamReader ris = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(ris);

            // JSON-Dokument wird zeilenweise eingelesen
            String zeile = "";
            while ((zeile = reader.readLine()) != null) {

                httpErgebnisDokument += zeile;
            }
        }

        return httpErgebnisDokument;

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Internetverbindung wird getestet
     *
     * @return boolean, ob Internetverbindung vorhanden
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
