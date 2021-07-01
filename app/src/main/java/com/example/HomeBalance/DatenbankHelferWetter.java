package com.example.HomeBalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Datenbankklasse für die Daten der Frau
 */

public class DatenbankHelferWetter extends SQLiteOpenHelper {

    /**
     * Variablen Deklaration
     */

    private static final String TABLE_NAME = "Wetterdaten";
    private static final String COL1 = "ID";
    private static final String COL2 = "tempDay";
    private static final String COL3 = "rainDay";
    private static final String COL4 = "windDay";
    private static final String COL5 = "tempCurrent";
    private static final String COL6 = "rainCurrent";
    private static final String COL7 = "windCurrent";

    private static final String TAG = "DatenbankEingabe";


    public DatenbankHelferWetter(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " TEXT," + COL6 + " TEXT," + COL7 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Fügt die mitgegeben Daten in die Datenbank hinzu
     * @return Boolean ob Daten erfolgreich gespeichert wurden
     */

    public boolean addData(String item, String item2, String item3, String item4, String item5, String item6) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, item2);
        contentValues.put(COL4, item3);
        contentValues.put(COL5, item4);
        contentValues.put(COL6, item5);
        contentValues.put(COL7, item6);


        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gibt alle Daten zurück von der Datenbank
     * @return Cursor-Objekt mit Datenbank-Daten
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}