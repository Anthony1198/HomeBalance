package com.example.HomeBalance;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DatenEingabeActivity extends AppCompatActivity {

    DatenbankActivity1 datenbank;
    private Button abschicken;
    private EditText vorname, alter, aufstehzeit, routine, arbeitszeit;
    private Switch nap, fruehstueck;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dateneingabe);

        vorname = (EditText) findViewById(R.id.vorname);
        alter = (EditText) findViewById(R.id.alter);
        aufstehzeit = (EditText) findViewById(R.id.aufstehzeit);
        routine = (EditText) findViewById(R.id.routine);
        arbeitszeit = (EditText) findViewById(R.id.arbeitszeit);
        abschicken = (Button) findViewById(R.id.abschicken);
        nap = (Switch) findViewById(R.id.nap);
        fruehstueck = (Switch) findViewById(R.id.fruehstueck);

        datenbank = new DatenbankActivity1(this);


        abschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vorname.getText().length() != 0 && alter.getText().length() != 0 &&  aufstehzeit.getText().length() != 0 && routine.getText().length() != 0 && arbeitszeit.getText().length() != 0)  {
                    String vornameInhalt = vorname.getText().toString();
                    int alterInhalt = Integer.parseInt(alter.getText().toString());
                    String aufstehzeitInhalt = aufstehzeit.getText().toString();
                    String routineInhalt = routine.getText().toString();
                    String arbneitszeitInhalt = arbeitszeit.getText().toString();

                    Boolean napInhalt = nap.isChecked();
                    Boolean fruehstueckInhalt = fruehstueck.isChecked();

                    AddData(vornameInhalt, alterInhalt, aufstehzeitInhalt, routineInhalt, arbneitszeitInhalt, napInhalt, fruehstueckInhalt);

                    
                    




                } else {
                    toastMessage("Felder dürfen nicht leer sein!");
                }

            }

        });
    }

    /**
     * Daten werden für Speicherung an die Datenbank weitergeleitet
     */
    public void AddData(String newEntry, int newEntry2, String newEntry3, String newEntry4, String newEntry5, Boolean newEntry6, Boolean newEntry7) {
        boolean insertData = datenbank.addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6, newEntry7);

            if (insertData) {
                toastMessage("Daten wurden erfolgreich gespeichert!");
            } else {
                toastMessage("Etwas ist schief gelaufen :(");
            }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
