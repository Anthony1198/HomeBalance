package com.example.HomeBalance;

import android.database.Cursor;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class CreditsActivity extends AppCompatActivity {
    private int credits;
    private static CreditsActivity creditAct;

    public CreditsActivity(){
        credits = getActualCredits(DatenbankHelferCredits.getInstance(this).getData());
    }

    private int getActualCredits(Cursor addData) {
        int actualCredits = 0;
        //TODO: Aus dem cursor die eigentlichen Credits rausholen und zurückgeben
        return actualCredits;
    }

    public void showCreditsOnButton(Button creditButton){
        if(creditButton != null) creditButton.setText(getString(R.string.credits)+credits);
    }

    public void addCreditsAndShowOnButton(int anzahl, Button creditButton){
        credits += anzahl;
        if(creditButton != null) creditButton.setText(getString(R.string.credits) + credits);
        DatenbankHelferCredits.getInstance(this).addData(credits);
    }

    public void addCredits(int anzahl){
        credits += anzahl;
        DatenbankHelferCredits.getInstance(this).addData(credits);
    }

    public static synchronized CreditsActivity getInstance(){
        if(creditAct == null){
            creditAct = new CreditsActivity();
        }
        return creditAct;
    }
}
