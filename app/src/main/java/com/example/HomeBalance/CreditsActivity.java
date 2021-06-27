package com.example.HomeBalance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CreditsActivity extends AppCompatActivity {

    DatenbankHelferCredits datenbankCredits;
    private int credits = 0;
    HomeFragment home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datenbankCredits = new DatenbankHelferCredits(this);
        home = new HomeFragment();
    }

    public void getNewCredits(int anzahl){
        credits = credits + anzahl;
        //AddDataCredits(credits);
        home.setCredits(credits);
    }





    private void AddDataCredits(int newEntry) {
        boolean insertData = datenbankCredits.addData(newEntry);

        if (insertData) {
            //toastMessage("Daten wurden erfolgreich gespeichert!");
        } else {
            toastMessage("Etwas ist schief gelaufen :(");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
