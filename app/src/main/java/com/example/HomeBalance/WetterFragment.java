package com.example.HomeBalance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.getSystemServiceName;

public class WetterFragment extends Fragment {

    Button anzeigen;
    private LocationManager locationManager;
    TextView wetterdaten;
    String longitude, latitude, urlMitName, startTime, endTime, timeSteps, timeZone, wetterJSON;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wetter, container, false);

        anzeigen = (Button) view.findViewById(R.id.anzeigen);
        wetterdaten = (TextView) view.findViewById(R.id.textView2);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        gpsHolen();

        anzeigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    getZeitDatum();
                    getTimeZone();
                    endTime = "2021-06-22T15:00:00Z";

                    urlMitName = "http://192.168.178.62:8080/api/weather?location=" + latitude + "," + longitude + "&startTime=" + startTime + "&endTime=" + endTime + "&timesteps=" + "1h" + "&timezone=" + timeZone;
                    toastMessage("Daten werden verarbeitet!");


                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    WetterFragment.MeinHintergrundThread mht = new MeinHintergrundThread();
                    mht.start();


                }
            }
        });

        return view;
    }




    private void gpsHolen(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                //wetterdaten.setText(latitude + " " + longitude);
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Internetverbindung wird getestet
     *
     * @return boolean, ob Internetverbindung vorhanden
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    private class MeinHintergrundThread extends Thread {

        @Override
        public void run() {

            try {
                String jsonDocument = holeWetterDaten();
                //String jsonDocument =  getResources().getString(R.string.wetterBeispiel);
                parseJSON(jsonDocument);
                //AddDataEOptimierung(wakeup, morningWork, afternoonWork, eveningWork, lunch, naping, freetime, dinner, sleep);



                //wetterdaten.setText(jsonDocument);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }
        }

    }










    /**
     * String mit JSON-Inhalt wird auf Inhalte ausgelesen (Parsing) und in der dazugehörigen Datenbank abgespeichert
     */
    private void parseJSON(String jsonString) throws Exception {


        if (jsonString == null || jsonString.trim().length() == 0) {

            //Bei erhalt eines leeren Strings wird eine Fehlermeldung zurückgeliefert
            toastMessage("JSON ist leer!");
        }
        //wetterdaten.setText(jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray timelines = data.getJSONArray("timelines");
        JSONObject intervals1 = timelines.getJSONObject(0);
        JSONArray intervals2 = intervals1.getJSONArray("intervals");
        JSONObject values = intervals2.getJSONObject(0);

        String test = "Temperatur: " + values.getJSONObject("values").getString("temperature") + "°C";

        //JSONArray timelines = data.getJSONArray("timelines");

        //JSONArray intervals = timelines.getJSONArray(1);
        //JSONObject values1 = intervals.getJSONObject(1);

        //String temeratur = values1.getString("temperature");

        wetterdaten.setText(test);
    }









    private String holeWetterDaten() throws Exception {

        URL url = null;
        HttpURLConnection conn = null;
        String httpErgebnisDokument = "";

        url = new URL(urlMitName);
        conn = (HttpURLConnection) url.openConnection();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {

            throw new Exception(getString(R.string.HTTPS));

        } else {

            InputStream is = conn.getInputStream();
            InputStreamReader ris = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(ris);

            httpErgebnisDokument = reader.readLine();
        }

        return httpErgebnisDokument;

    }

    private void getZeitDatum(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-d");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        String time = df2.format(Calendar.getInstance().getTime());
        startTime = date + "T" + time + "Z";

    }

    private void getTimeZone(){
        TimeZone tz = TimeZone.getDefault();
        timeZone= tz.getID();
    }

}

