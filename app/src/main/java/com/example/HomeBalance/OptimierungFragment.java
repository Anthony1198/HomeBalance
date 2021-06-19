package com.example.HomeBalance;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OptimierungFragment extends Fragment {
    private TextView wakeup, morningWork, afternoonWork, eveningWork, lunch, naping, freetime, dinner, sleep;
    String aufwachen, arbeitsbeginn, pause, arbeitsfortsetzung, napen, freizeit,  abendessen, schlafenszeit, arbeitsfortsetzungabend;

    DatenbankOptimierungActivity datenbank;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optimierung, container, false);

        datenbank = new DatenbankOptimierungActivity(getActivity());
        wakeup = (TextView) view.findViewById(R.id.aufstehzeit);
        morningWork = (TextView) view.findViewById(R.id.arbeitsbeginn);
        afternoonWork = (TextView) view.findViewById(R.id.arbeitfortsetzung);
        eveningWork = (TextView) view.findViewById(R.id.arbeitfortsetzung2);
        lunch = (TextView) view.findViewById(R.id.pause);
        naping = (TextView) view.findViewById(R.id.naping);
        freetime = (TextView) view.findViewById(R.id.freizeit);
        dinner = (TextView) view.findViewById(R.id.abendessen);
        sleep = (TextView) view.findViewById(R.id.schlafen);


        Cursor data = datenbank.getData();
        data.moveToLast();
        aufwachen = "Aufstehzeit: " + data.getString(1);
        arbeitsbeginn = "Arbeitsbeginn: " + data.getString(2);
        arbeitsfortsetzung = "Arbeitsfortsetzung: " + data.getString(3);
        arbeitsfortsetzungabend = "Arbeitsfortsetzun am Abend: " + data.getString(4);
        pause = "Pausenzeit: " + data.getString(5);
        napen = "Nap: " + data.getString(6);
        freizeit = "Freizeit: " + data.getString(7);
        abendessen = "Abendessen: " + data.getString(8);
        schlafenszeit = "Schlafenszeit: " + data.getString(9);



        wakeup.setText(aufwachen);
        morningWork.setText(arbeitsbeginn);
        afternoonWork.setText(arbeitsfortsetzung);
        eveningWork.setText(arbeitsfortsetzungabend);
        lunch.setText(pause);
        naping.setText(napen);
        freetime.setText(freizeit);
        dinner.setText(abendessen);
        sleep.setText(schlafenszeit);




        return view;
    }
}
