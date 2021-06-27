package com.example.HomeBalance;


import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CreditsActivity extends AppCompatActivity {


    DatenbankHelferCredits datenbankCredits = new DatenbankHelferCredits(this);;
    private int credits = 0;
    private static CreditsActivity creditAct = new CreditsActivity();

    public void CreditsActivity() {


    }

    public void setNewCredits(int anzahl, Button creditButton){
        credits += anzahl;
        creditButton.setText("CREDITS: "+credits);
    }

    public static CreditsActivity getInstance(){
        return creditAct;
    }
}
