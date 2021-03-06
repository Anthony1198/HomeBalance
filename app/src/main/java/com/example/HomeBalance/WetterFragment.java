package com.example.HomeBalance;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * Fragment für den Abruf und die Anzeige der Wetterdaten
 */
public class WetterFragment extends Fragment implements Caller{

    Button anzeigen;
    private LocationManager locationManager;
    TextView wetterDay, wetterCurrent;
    String longitude, latitude, urlMitName, startTime, endTime, timeStep, timeZone, nextDay, temperatur, niederschlag, wind, tempDay, rainDay, windDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wetter, container, false);

        anzeigen = (Button) view.findViewById(R.id.anzeigen);
        wetterDay = (TextView) view.findViewById(R.id.wetterDay);
        wetterCurrent = (TextView) view.findViewById(R.id.wetterCurrent);
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        gpsHolen();
        final HTTPHandler handlerDay = new HTTPHandler(this);
        final HTTPHandler handlerHour = new HTTPHandler(this);
        anzeigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnline()) {
                    toastMessage(getString(R.string.internetverbindung));
                } else {
                    CreditsHandler.getInstance(getContext()).addCredits(2);
                    getWetterDatumZeit();
                    getTimeZone();
                    timeStep = "1d";

                    urlMitName = "http://" + getString(R.string.localeIP) + ":8080/api/weather?location=" + latitude + "," + longitude + "&startTime=" + startTime + "&endTime=" + endTime + "&timesteps=" + timeStep + "&timezone=" + timeZone;
                    toastMessage(getString(R.string.datenverarbeitung));

                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    URL url = null;
                    try{
                        url = new URL(urlMitName);
                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    }
                    handlerHour.setName("DayThread");
                    handlerDay.setUrl(url);
                    handlerDay.setIdentifier("weatherPerDay");
                    handlerDay.start();
                    try {
                        handlerDay.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timeStep = "1h";
                    urlMitName = "http://" + getString(R.string.localeIP) + ":8080/api/weather?location=" + latitude + "," + longitude + "&startTime=" + startTime + "&endTime=" + endTime + "&timesteps=" + timeStep + "&timezone=" + timeZone;
                    url = null;
                    try{
                        url = new URL(urlMitName);
                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    }
                    handlerHour.setName("HourThread");
                    handlerHour.setUrl(url);
                    handlerHour.setIdentifier("weatherPerHour");
                    handlerHour.start();
                }
            }
        });

        /*
         * Auslesen der Optimierungs-Datenbank für den Tagesablaufplan
         */
        Cursor dataWetter = DatenbankHelferWetter.getInstance(this.getContext()).getData();
        if( dataWetter != null && dataWetter.moveToFirst() ) {
            dataWetter.moveToLast();
            wetterDay.setText(dataWetter.getString(1) + "\n" + dataWetter.getString(2) + "\n" + dataWetter.getString(3));
            wetterCurrent.setText(dataWetter.getString(4) + "\n" + dataWetter.getString(5) + "\n" + dataWetter.getString(6));
        }
        return view;
    }

    private void gpsHolen(){
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void toastMessage(String message, FragmentActivity activity) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Internetverbindung wird getestet
     *
     * @return boolean, ob Internetverbindung vorhanden
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public synchronized void handleAnswer(BufferedReader bufferedReader, String identifier, String message) {
        String jsonDocument;
        if(bufferedReader!=null){
            try{
                jsonDocument = bufferedReader.readLine();
                parseJSON(jsonDocument);
            }catch (Exception e){
                toastMessageOnUiThread(getString(R.string.schiefgelaufen));
            }
            if(identifier.equals("weatherPerDay")){
                tempDay = temperatur;
                rainDay = niederschlag;
                windDay = wind;
            }
        }else {
            toastMessageOnUiThread(message);
        }
        if(identifier.equals("weatherPerHour")) {
            AddDataWetter(tempDay, rainDay, windDay, temperatur, niederschlag, wind);
            Fragment mFragment = new WetterFragment();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
        }
    }

    private void toastMessageOnUiThread(String message){
        final String m = message;
        final FragmentActivity activity = requireActivity();
        activity.runOnUiThread(new Runnable() {
            public void run() {
                toastMessage(m,activity);
            }
        });
    }

    /**
     * String mit JSON-Inhalt wird auf Inhalte ausgelesen (Parsing) und in der dazugehörigen Datenbank abgespeichert
     */
    private void parseJSON(String jsonString) throws Exception {


        if (jsonString == null || jsonString.trim().length() == 0) {
            return;
        }

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray timelines = data.getJSONArray("timelines");
        JSONObject intervals1 = timelines.getJSONObject(0);
        JSONArray intervals2 = intervals1.getJSONArray("intervals");
        JSONObject values = intervals2.getJSONObject(0);

        temperatur = "Temperatur: " + values.getJSONObject("values").getString("temperature") + "°C";
        niederschlag = "Niederschlagintensität: " + values.getJSONObject("values").getString("precipitationIntensity");
        wind = "Windgeschwindigkeit: " + values.getJSONObject("values").getString("windSpeed");
        String re = temperatur + " " + niederschlag + " " + wind;

    }

    private void getWetterDatumZeit() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");

        String date = df.format(Calendar.getInstance().getTime());
        String time = df2.format(Calendar.getInstance().getTime());
        startTime = date + "T" + time + "Z";

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        nextDay = df.format((c.getTime()));
        endTime = nextDay + "T" + time + "Z";
    }

    private void getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        timeZone = tz.getID();
    }

    /**
     * Daten werden für Speicherung an die Wetter-Datenbank weitergeleitet
     */
    private void AddDataWetter(String newEntry, String newEntry2, String newEntry3, String newEntry4, String newEntry5, String newEntry6) {
        boolean insertData = DatenbankHelferWetter.getInstance(this.getContext()).addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6);
    }
}

