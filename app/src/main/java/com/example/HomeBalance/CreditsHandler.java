package com.example.HomeBalance;

import android.content.Context;
import android.database.Cursor;


public class CreditsHandler {
    private int credits;
    private static CreditsHandler creditAct;
    Context context;

    public CreditsHandler(Context context){
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

    public static synchronized CreditsHandler getInstance(Context context){
        if(creditAct == null){
            creditAct = new CreditsHandler(context);
        }
        return creditAct;
    }
}
