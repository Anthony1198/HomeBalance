package com.example.HomeBalance;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

/**
 * Controller für die Anzeige des TimePickers in der DatenEingabeActivity
 */
public class TimePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c =  Calendar.getInstance();
        int stunden = c.get(Calendar.HOUR_OF_DAY);
        int minuten = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), stunden, minuten, true);
    }
}
