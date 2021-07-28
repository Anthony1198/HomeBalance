package com.example.HomeBalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Datenbankklasse für die eingegebenen Daten des Users
 */
public class DatenbankHelferEingabe extends SQLiteOpenHelper {

    private static DatenbankHelferEingabe eingabeDatenbankHelfer;

    public static synchronized DatenbankHelferEingabe getInstance(Context context){
        if(eingabeDatenbankHelfer == null){
            eingabeDatenbankHelfer = new DatenbankHelferEingabe(context.getApplicationContext());
        }
        return eingabeDatenbankHelfer;
    }
    /**
     * Variablen Deklaration
     */
    private static final String TABLE_NAME = "Nutzerwerte";
    private static final String COL1 = "ID";
    private static final String COL2 = "vorname";
    private static final String COL3 = "altern";
    private static final String COL4 = "aufstehzeit";
    private static final String COL5 = "routine";
    private static final String COL6 = "arbeitszeit";
    private static final String COL7 = "nap";
    private static final String COL8 = "fruehstueck";

    private static final String TAG = "DatenbankEingabe";


    public DatenbankHelferEingabe(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " INTEGER," + COL4 + " TEXT," + COL5 + " TEXT," + COL6 + " TEXT," + COL7 + " BOOLEAN," + COL8 + " BOOLEAN)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion != oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Fügt die mitgegeben Daten in die Datenbank hinzu
     * @return Boolean ob Daten erfolgreich gespeichert wurden
     */
    public boolean addData(String item, int item2, String item3, String item4, String item5, boolean item6, boolean item7) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, item2);
        contentValues.put(COL4, item3);
        contentValues.put(COL5, item4);
        contentValues.put(COL6, item5);
        contentValues.put(COL7, item6);
        contentValues.put(COL8, item7);

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