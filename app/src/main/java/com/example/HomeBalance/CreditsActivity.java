package com.example.HomeBalance;

import android.content.Context;
import android.database.Cursor;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class CreditsActivity extends AppCompatActivity {
    private int credits;
    private static CreditsActivity creditAct;
    Context context;

    public CreditsActivity(Context context){
        this.context = context;
        credits = getSavedCredits(DatenbankHelferCredits.getInstance(context).getData());
    }

    private int getSavedCredits(Cursor data) {
        int actualCredits = 0;
        if( data != null) {
            data.moveToLast();
            try{
                actualCredits = Integer.parseInt(data.getString(1));
            } catch (Exception e){
                //Keine Credits vorhanden, z. B. beim Start
                return actualCredits;
            }
        }
        return actualCredits;
    }

    public int getCredits(){
        return credits;
    }

    public void addCredits(int anzahl){
        credits += anzahl;
        DatenbankHelferCredits.getInstance(context).addData(credits);
    }

    public static synchronized CreditsActivity getInstance(Context context){
        if(creditAct == null){
            creditAct = new CreditsActivity(context);
        }
        return creditAct;
    }
}
