package com.example.HomeBalance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Fragment für die Abfrage von Rezeptdaten
 */
public class RezepteSucheFragment extends Fragment implements Caller{

    Button anzeigenNeue, anzeigenLetzte;
    String urlMitName;
    Switch frueh, mittag, abend, nachtisch, vegan, vegetarisch, glutenfrei, gesund;
    Boolean fruehBoolean, mittagBoolean, abendBoolean, nachtischBoolean, veganBoolean, vegetarischBoolean, glutenfreiBoolean, gesundBoolean;
    String[] rezeptNr1, rezeptNr2, rezeptNr3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rezeptesuchen, container, false);

        anzeigenNeue = (Button) view.findViewById(R.id.anzeigenNeue);
        anzeigenLetzte = (Button) view.findViewById(R.id.anzeigenLetzte);
        frueh = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        mittag = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        abend = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        nachtisch = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        vegan = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        vegetarisch = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        glutenfrei = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        gesund = (Switch) view.findViewById(R.id.fruehstucksRezepte);

        rezeptNr1 = new String[3];
        rezeptNr2 = new String[3];
        rezeptNr3 = new String[3];



        final HTTPHandler handler = new HTTPHandler(this);
        anzeigenNeue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnline()) {
                    toastMessage(getString(R.string.internetverbindung));
                } else {
                    urlZusammenstellung();

                    toastMessage(getString(R.string.datenverarbeitung));
                    URL url = null;
                    try{
                        url = new URL(urlMitName);
                    } catch (MalformedURLException e){
                        e.printStackTrace();
                    }

                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    handler.setUrl(url);
                    handler.start();
                }
            }
        });

        anzeigenLetzte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new RezepteAnzeigenFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
            }
        });

        return view;
    }

    @Override
    public void handleAnswer(BufferedReader bufferedReader, String identifier, String message) {
        if(bufferedReader!=null){
            String jsonDocument;
            try{
                jsonDocument = bufferedReader.readLine();
                parseJSON(jsonDocument);
            }catch(Exception e){
                toastMessageOnUiThread(getString(R.string.schiefgelaufen));
            }
            AddDataRezepte(rezeptNr1[0], rezeptNr1[1], rezeptNr1[2], rezeptNr2[0], rezeptNr2[1], rezeptNr2[2], rezeptNr3[0], rezeptNr3[1], rezeptNr3[2]);
            CreditsHandler.getInstance(getContext()).addCredits(1);
        }else{
            toastMessageOnUiThread(message);
        }
        Fragment mFragment = new RezepteAnzeigenFragment();
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
            return;
        }

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray recipes = jsonObject.getJSONArray("recipes");
        JSONObject rezeptData1 = recipes.getJSONObject(0);
        JSONObject rezeptData2 = recipes.getJSONObject(1);
        JSONObject rezeptData3 = recipes.getJSONObject(2);


        rezeptNr1[0] = rezeptData1.getString("title");
        rezeptNr1[1] = rezeptData1.getString("image");
        rezeptNr1[2] = rezeptData1.getString("sourceUrl");

        rezeptNr2[0] = rezeptData2.getString("title");
        rezeptNr2[1] = rezeptData2.getString("image");
        rezeptNr2[2] = rezeptData2.getString("sourceUrl");

        rezeptNr3[0] = rezeptData3.getString("title");
        rezeptNr3[1] = rezeptData3.getString("image");
        rezeptNr3[2] = rezeptData3.getString("sourceUrl");
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



    private void urlZusammenstellung(){
        urlMitName = "http://" + getString(R.string.localeIP) + ":8080/api/recipes?number=3&tags=";

        fruehBoolean = frueh.isChecked();
        mittagBoolean = mittag.isChecked();
        abendBoolean = abend.isChecked();
        nachtischBoolean = nachtisch.isChecked();
        veganBoolean = vegan.isChecked();
        vegetarischBoolean = vegetarisch.isChecked();
        glutenfreiBoolean = glutenfrei.isChecked();
        gesundBoolean = gesund.isChecked();

        if(fruehBoolean){
            urlMitName= urlMitName;
        }
        if(mittagBoolean){
            urlMitName= urlMitName + "lunch";
        }
        if(abendBoolean){
            urlMitName= urlMitName + "dinner";
        }
        if(nachtischBoolean){
            urlMitName= urlMitName + "dessert";
        }
        if(veganBoolean){
            urlMitName= urlMitName + "vegan";
        }
        if(vegetarischBoolean){
            urlMitName= urlMitName + "vegetarian";
        }
        if(glutenfreiBoolean){
            urlMitName= urlMitName + "glutenFree";
        }
        if(gesundBoolean){
            urlMitName= urlMitName + "veryHealthy";
            CreditsHandler.getInstance(getContext()).addCredits(5);
        }
    }

    /**
     * Daten werden für Speicherung an die Wetter-Datenbank weitergeleitet
     */
    private void AddDataRezepte(String newEntry, String newEntry2, String newEntry3, String newEntry4, String newEntry5, String newEntry6, String newEntry7, String newEntry8, String newEntry9) {
        boolean insertData = DatenbankHelferRezepte.getInstance(this.getContext()).addData(newEntry, newEntry2, newEntry3, newEntry4, newEntry5, newEntry6, newEntry7, newEntry8, newEntry9);

        if (insertData) {
        } else {
            toastMessageOnUiThread(getString(R.string.schiefgelaufen));
        }
    }

}
