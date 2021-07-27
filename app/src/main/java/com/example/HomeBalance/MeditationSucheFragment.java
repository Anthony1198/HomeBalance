package com.example.HomeBalance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
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
import java.net.MalformedURLException;
import java.net.URL;

public class MeditationSucheFragment extends Fragment implements Caller{

    Button anzeigenNeue2, anzeigenLetzte2;
    String urlMitName;
    RadioButton radioDeutsch, radioEnglisch, radioFranz;
    String [] audio1, audio2, audio3;
    Boolean deutschBoolean, englischBoolean, franzBoolean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditationsuche, container, false);

        audio1 = new String[3];
        audio2 = new String[3];
        audio3 = new String[3];

        radioDeutsch = (RadioButton) view.findViewById(R.id.radioDeutsch);
        radioEnglisch = (RadioButton) view.findViewById(R.id.radioEnglisch);
        radioFranz = (RadioButton) view.findViewById(R.id.radioFranz);
        anzeigenLetzte2 = (Button) view.findViewById(R.id.anzeigenLetzte2);
        anzeigenNeue2 = (Button) view.findViewById(R.id.anzeigenNeue2);


        final HTTPHandler handler = new HTTPHandler(this);
        anzeigenNeue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnline()) {
                    toastMessage("Keine Internetverbindung!");
                } else {

                    urlZusammenstellung();

                    toastMessage("Daten werden verarbeitet!");

                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    URL url = null;
                    try{
                        url = new URL(urlMitName);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    handler.setUrl(url);
                    handler.start();
                }
            }
        });


        anzeigenLetzte2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new MeditationAnzeigenFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
            }
        });



        return view;
    }

    private void urlZusammenstellung() {
        urlMitName = "http://" + getString(R.string.localeIP) + ":8080/api/meditation?search=Meditation&language=";

        deutschBoolean = radioDeutsch.isChecked();
        englischBoolean = radioEnglisch.isChecked();
        franzBoolean = radioFranz.isChecked();


        if (deutschBoolean) {
            urlMitName = urlMitName + "German";
        }
        if (englischBoolean) {
            urlMitName = urlMitName + "English";
        }
        if (franzBoolean) {
            urlMitName = urlMitName + "French";
        }
    }

    @Override
    public void handleAnswer(BufferedReader bufferedReader, String identifier, String message) {
        if (bufferedReader!=null){
            String jsonDocument = "";
            try {
                String output;
                while ((output = bufferedReader.readLine()) != null){
                    jsonDocument += output;
                }
                parseJSON(jsonDocument);
            } catch (Exception ex) {
                toastMessageOnUiThread("Answer not readable");
            }
            AddDataMeditation(audio1[0], audio1[1], audio1[2], audio2[0], audio2[1], audio2[2], audio3[0], audio3[1], audio3[2]);
            CreditsHandler.getInstance(getContext()).addCredits(1);
        } else {
            toastMessageOnUiThread(message);
        }
        Fragment mFragment = new MeditationAnzeigenFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
    }

    private void toastMessageOnUiThread(String message){
        final String m = message;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                toastMessage(m);
            }
        });
    }


    /**
     * String mit JSON-Inhalt wird auf Inhalte ausgelesen (Parsing) und in der dazugehörigen Datenbank abgespeichert
     */
    private void parseJSON(String jsonString) throws Exception {


        if (jsonString == null || jsonString.trim().length() == 0) {

            //Bei erhalt eines leeren Strings wird eine Fehlermeldung zurückgeliefert
            toastMessage("JSON ist leer!");
            return;
        }

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject result1 = results.getJSONObject(0);
        JSONObject result2 = results.getJSONObject(1);
        JSONObject result3 = results.getJSONObject(2);


        audio1[0] = result1.getString("title_original");
        audio1[1] = result1.getString("image");
        audio1[2] = result1.getString("audio");

        audio2[0] = result2.getString("title_original");
        audio2[1] = result2.getString("image");
        audio2[2] = result2.getString("audio");

        audio3[0] = result3.getString("title_original");
        audio3[1] = result3.getString("image");
        audio3[2] = result3.getString("audio");
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

    /**
     * Daten werden für Speicherung an die Meditation-Datenbank weitergeleitet
     */
    private void AddDataMeditation(String newEntry, String newEntry2, String newEntry3, String newEntry4, String newEntry5, String newEntry6, String newEntry7, String newEntry8, String newEntry9) {
        boolean insertData = DatenbankHelferMeditation.getInstance(this.getContext()).addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6, newEntry7, newEntry8, newEntry9);

        if (insertData) {
            toastMessage("Daten wurden erfolgreich gespeichert!");
        } else {
            toastMessage("Etwas ist schief gelaufen :(");
        }
    }
}
