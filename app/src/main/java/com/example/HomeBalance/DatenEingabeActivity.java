package com.example.HomeBalance;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Controller für die Eingabeseite der Nutzerdaten
 */
public class DatenEingabeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    /**
     * Deklaration aller notwendigen Variablen
     */
    private String urlMitName, vornameInhalt, aufstehzeitInhalt, routineInhalt, arbneitszeitInhalt;

    int alterInhalt;

    Boolean napInhalt, fruehstueckInhalt;

    String wakeup = null;
    String morningWork = null;
    String afternoonWork = null;
    String eveningWork = null;
    String lunch = null;
    String naping = null;
    String freetime = null;
    String dinner = null;
    String sleep = null;

    /**
     * Deklaration der SQLite-Datenbanken
     */
    DatenbankHelferEingabe datenbankEingabe;
    DatenbankHelferOptimierung datenbankOptimierung;

    /**
     * Deklaration der View-Komponenten
     */
    private Button abschicken, aufstehzeitButton, routineButton, arbeitszeitButton;
    private TextView vorname, alter, aufstehzeit, routine, arbeitszeit, ausgewaehlt;
    private Switch nap, fruehstueck;


    /**
     * onCreate-Methode wird bei erstmaligem Aufruf der Activity ausgeführt
     *
     * @return Gibt die fertige view an fragment_home zurück
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateneingabe);

        /**
         * Initialisierung der Datenbanken
         */
        datenbankEingabe = new DatenbankHelferEingabe(this);
        datenbankOptimierung = new DatenbankHelferOptimierung(this);

        /**
         * Initialisierung der View-Komponenten
         */
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

        befuellen();


        /**
         * Bei Click auf einen Zeit-Auswahl Button wird der TimePicker aufgerufen und auf der View angezeigt
         */
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


        /**
         * Bei Click auf dem Abschick-Button werden die Datenfelder auf vollständigkeit überprüft => ausgelesen => In der Datenbank gespeichert
         * => Internetverbindung gecheckt => Die URL für den HTTP-Aufruf zusammengestellt => Ein Hintergrund-Thread gestartet => MainActivity gestartet
         */
        abschicken.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (vorname.getText().length() != 0 && alter.getText().length() != 0 && aufstehzeit.getText().length() != 0 && routine.getText().length() != 0 && arbeitszeit.getText().length() != 0) {
                vornameInhalt = vorname.getText().toString();
                alterInhalt = Integer.parseInt(alter.getText().toString());
                aufstehzeitInhalt = aufstehzeit.getText().toString();
                routineInhalt = routine.getText().toString();
                arbneitszeitInhalt = arbeitszeit.getText().toString();

                napInhalt = nap.isChecked();
                fruehstueckInhalt = fruehstueck.isChecked();

                AddDataEingabe(vornameInhalt, alterInhalt, aufstehzeitInhalt, routineInhalt, arbneitszeitInhalt, napInhalt, fruehstueckInhalt);

                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    urlMitName = "http://192.168.178.62:8080/api/schedule?" + "nap=" + napInhalt + "&age=" + alterInhalt + "&breakfast=" + fruehstueckInhalt + "&wakeUpTime=" + aufstehzeitInhalt + "&getReadyDuration=" + routineInhalt + "&workingHours=" + arbneitszeitInhalt;

                    // Button deaktivieren während ein HTTP-Request läuft
                    abschicken.setEnabled(false);

                    toastMessage("Daten werden verarbeitet!");


                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    MeinHintergrundThread mht = new MeinHintergrundThread();
                    mht.start();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            } else {
                toastMessage("Daten nicht vollständig!");
            }
        }
    });
}

    /**
     * Die Methode befüllt schon vorhandene Eingabedaten in die Maske
     */
    private void befuellen(){
        Cursor dataEingabe = datenbankEingabe.getData();
        dataEingabe.moveToLast();
        if (dataEingabe.getString(1) != null){
            vorname.setText(dataEingabe.getString(1));
            alter.setText(dataEingabe.getString(2));
            aufstehzeit.setText(dataEingabe.getString(3));
            routine.setText(dataEingabe.getString(4));
            arbeitszeit.setText(dataEingabe.getString(5));
            if(dataEingabe.getString(6).equals("1")) {
                nap.setChecked(true);
            }
            if(dataEingabe.getString(7).equals("1")) {
                fruehstueck.setChecked(true);
            }
        }
    }

    /**
     * Die Methode übernimmt die im TimePicker gewählte Zeit und formatiert diese in das Format "HH:MM" um
     */
    @Override
    public void onTimeSet(TimePicker view, int stunden, int minuten) {
        String formatiertStunden = String.valueOf(stunden);
        String formatiertMinuten = String.valueOf(minuten);

        if (stunden < 10) {
            formatiertStunden = "0" + formatiertStunden;
        }
        if (minuten < 10) {
            formatiertMinuten = "0" + formatiertMinuten;
        }
        ausgewaehlt.setText(formatiertStunden + ":" + formatiertMinuten);
    }

    /**
     * Daten werden für Speicherung an die Eingabe-Datenbank weitergeleitet
     */
    private void AddDataEingabe(String newEntry, int newEntry2, String newEntry3, String newEntry4, String newEntry5, Boolean newEntry6, Boolean newEntry7) {
        boolean insertData = datenbankEingabe.addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6, newEntry7);

        if (insertData) {
            toastMessage("Daten wurden erfolgreich gespeichert!");
        } else {
            toastMessage("Etwas ist schief gelaufen :(");
        }
    }

    /**
     * Daten werden für Speicherung an die Optimierungs-Datenbank weitergeleitet
     */
    private void AddDataOptimierung(String newEntry, String newEntry2, String newEntry3, String newEntry4, String newEntry5, String newEntry6, String newEntry7, String newEntry8, String newEntry9) {
        boolean insertData = datenbankOptimierung.addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6, newEntry7, newEntry8, newEntry9);

        if (insertData) {
            //toastMessage("Daten wurden erfolgreich gespeichert!");
        } else {
            toastMessage("Etwas ist schief gelaufen :(");
        }
    }

    /**
     * Die Methode schickt eine Anfrage an die REST-API
     *
     * @return String mit JSON-Inhalt der API-Antwort
     */
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

            httpErgebnisDokument = reader.readLine();
        }
        return httpErgebnisDokument;
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    /**
     * Thread zum laden der API-Daten im Hintergrund, auf paralell Thread.
     * App-Stillstand/Absturz wird vermieden
     */
    private class MeinHintergrundThread extends Thread {

        @Override
        public void run() {
            try {
                String jsonDocument = holeOptimierungsDaten();
                parseJSON(jsonDocument);
                AddDataOptimierung(wakeup, morningWork, afternoonWork, eveningWork, lunch, naping, freetime, dinner, sleep);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * String mit JSON-Inhalt wird auf Inhalte ausgelesen (Parsing) und in der dazugehörigen Datenbank abgespeichert
     */
    private void parseJSON(String jsonString) throws Exception {
        if (jsonString == null || jsonString.trim().length() == 0) {
            //Bei erhalt eines leeren Strings wird eine Fehlermeldung zurückgeliefert
            toastMessage("JSON ist leer!");
        }
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray schedule = jsonObject.getJSONArray("schedule");
        String keyName = null;

        for (int i = 0; i < schedule.length(); i++) {
            JSONObject abs = schedule.getJSONObject(i);
            keyName = abs.keys().next();

            switch(keyName){
                case "wakeup":
                    wakeup = abs.getString("wakeup");
                    break;
                case "morningWork":
                    morningWork = abs.getString("morningWork");
                    break;
                case "afternoonWork":
                    afternoonWork = abs.getString("afternoonWork");
                    break;
                case "eveningWork":
                    eveningWork = abs.getString("eveningWork");
                    break;
                case "lunch":
                    lunch = abs.getString("lunch");
                    break;
                case "nap":
                    naping = abs.getString("nap");
                    break;
                case "freetime":
                    freetime = abs.getString("freetime");
                    break;
                case "dinner":
                    dinner = abs.getString("dinner");
                    break;
                case "sleep":
                    sleep = abs.getString("sleep");
                    break;
                default:
            }
        }
    }
}
