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

public class DatenbankActivity2 extends SQLiteOpenHelper {

    /**
     * Variablen Deklaration
     */

    private static final String TABLE_NAME = "Optimierungswerte";
    private static final String COL1 = "ID";
    private static final String COL2 = "wakeup";
    private static final String COL3 = "morningWork";
    private static final String COL4 = "afternoonWork";
    private static final String COL5 = "eveningWork";
    private static final String COL6 = "lunch";
    private static final String COL7 = "nap";
    private static final String COL8 = "freetime";
    private static final String COL9 = "dinner";
    private static final String COL10 = "sleep";



    private static final String TAG = "Datenbank";


    public DatenbankActivity2(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " TEXT," + COL6 + " TEXT,"  + COL7 + " TEXT,"+ COL8 + " TEXT,"+ COL9 + " TEXT,"+ COL10 + " TEXT)";
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
        contentValues.put(COL9, item7);
        contentValues.put(COL10, item7);

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

    /**
     * Zählt Anzahl der Tabellen-Zeilen
     * @return Anzahl Zeilen
     */
    public int getRowsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * Sucht maximalen Wert in der kfa Spalte
     * @return maximaler kfa-Wert
     */
    public int getMAXkfa() {
        int max =0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT MAX(kfa) FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        if (data != null && data.moveToFirst()) {
            max = data.getInt(0);
            data.close();
        }
        return max;
    }

    /**
     * Datenbank wird gelöscht
     */
    public void löscheDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}