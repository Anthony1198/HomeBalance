package com.example.HomeBalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Datenbankklasse für die Rezeptdaten der Rezept-API
 */
public class DatenbankHelferRezepte extends SQLiteOpenHelper {

    /**
     * Variablen Deklaration
     */
    private static DatenbankHelferRezepte rezepteDatenbankHelfer;

    public static synchronized DatenbankHelferRezepte getInstance(Context context){
        if(rezepteDatenbankHelfer == null){
            rezepteDatenbankHelfer = new DatenbankHelferRezepte(context.getApplicationContext());
        }
        return rezepteDatenbankHelfer;
    }

    private static final String TABLE_NAME = "Rezeptdaten";
    private static final String COL1 = "ID";
    private static final String COL2 = "rezept1tilte";
    private static final String COL3 = "rezept1bild";
    private static final String COL4 = "rezept1url";
    private static final String COL5 = "rezept2title";
    private static final String COL6 = "rezept2bild";
    private static final String COL7 = "rezept2url";
    private static final String COL8 = "rezept3title";
    private static final String COL9 = "rezept3bild";
    private static final String COL10 = "rezept3url";

    private static final String TAG = "DatenbankEingabe";


    public DatenbankHelferRezepte(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " TEXT," + COL6 + " TEXT,"+ COL7 + " TEXT,"+ COL8 + " TEXT,"+ COL9 + " TEXT," + COL10 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion!=oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Fügt die mitgegeben Daten in die Datenbank hinzu
     * @return Boolean ob Daten erfolgreich gespeichert wurden
     */

    public boolean addData(String item, String item2, String item3, String item4, String item5, String item6, String item7, String item8, String item9) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, item2);
        contentValues.put(COL4, item3);
        contentValues.put(COL5, item4);
        contentValues.put(COL6, item5);
        contentValues.put(COL7, item6);
        contentValues.put(COL8, item7);
        contentValues.put(COL9, item8);
        contentValues.put(COL10, item9);


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