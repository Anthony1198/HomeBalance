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
        credits = getActualCredits(DatenbankHelferCredits.getInstance(context).getData());
    }

    private int getActualCredits(Cursor addData) {
        int actualCredits = 0;
        //TODO: Aus dem cursor die eigentlichen Credits rausholen und zurückgeben
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
