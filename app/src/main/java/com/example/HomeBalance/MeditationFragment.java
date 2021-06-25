package com.example.HomeBalance;

import android.content.Context;
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
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MeditationFragment extends Fragment {

    Button anzeigenNeue2, anzeigenLetzte2;
    String urlMitName;
    TextView test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        test = (TextView) view.findViewById(R.id.test);
        anzeigenLetzte2 = (Button) view.findViewById(R.id.anzeigenLetzte2);
        anzeigenNeue2 = (Button) view.findViewById(R.id.anzeigenNeue2);

        anzeigenNeue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    urlMitName = "http://192.168.178.62:8080/api/meditation?search=Schlaf%20Meditation&language=German";

                    toastMessage("Daten werden verarbeitet!");

                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    MeditationFragment.MeinHintergrundThread mht = new MeditationFragment.MeinHintergrundThread();
                    mht.start();
                    try {
                        mht.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Fragment mFragment = new RezepteAnzeigenFragment();
                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
                }
            }
        });

        anzeigenLetzte2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new RezepteAnzeigenFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
            }
        });



        return view;
    }

    private class MeinHintergrundThread extends Thread {

        @Override
        public void run() {
            try {
                String jsonDocument = holeMeditationsDaten();
                parseJSON(jsonDocument);
                test.setText(jsonDocument);


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

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject result1 = results.getJSONObject(0);

        String a = result1.getString("link");

        test.setText(a);
    }


    private String holeMeditationsDaten() throws Exception {

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
        test.setText(httpErgebnisDokument);
        return httpErgebnisDokument;

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

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
