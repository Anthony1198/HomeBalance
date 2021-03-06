package com.example.HomeBalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Datenbankklasse für die Anzahl der Credits
 */
public class DatenbankHelferCredits extends SQLiteOpenHelper {

    /**
     * Variablen Deklaration
     */
    private static final String TABLE_NAME = "Creditwerte";
    private static final String COL1 = "credits";


    private static final String TAG = "DatenbankCredits";

    private static DatenbankHelferCredits creditsDatenbankHelfer;

    public static synchronized DatenbankHelferCredits getInstance(Context context){
        if(creditsDatenbankHelfer == null){
            creditsDatenbankHelfer = new DatenbankHelferCredits(context.getApplicationContext());
        }
        return creditsDatenbankHelfer;
    }

    public DatenbankHelferCredits(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Fügt die mitgegeben Daten in die Datenbank hinzu
     * @return Boolean ob Daten erfolgreich gespeichert wurden
     */

    public boolean addData(int item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, item);


        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    /**
     * Gibt alle Daten zurück von der Datenbank
     * @return Cursor-Objekt mit Datenbank-Daten
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

}