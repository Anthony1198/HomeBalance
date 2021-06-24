package com.example.HomeBalance;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

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

public class RezepteFragment extends Fragment {

    Button anzeigen;
    TextView rezept1View, rezept2View, rezept3View;
    ImageButton rezept1Bild, rezept2Bild, rezept3Bild;
    String urlMitName;
    Switch frueh, mittag, abend, nachtisch, vegan, vegetarisch, glutenfrei, gesund;
    Boolean fruehBoolean, mittagBoolean, abendBoolean, nachtischBoolean, veganBoolean, vegetarischBoolean, glutenfreiBoolean, gesundBoolean;
    String[] rezeptNr1, rezeptNr2, rezeptNr3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rezepte, container, false);

        anzeigen = (Button) view.findViewById(R.id.anzeigen);
        rezept1View = (TextView) view.findViewById(R.id.rezept1);
        rezept2View = (TextView) view.findViewById(R.id.rezept2);
        rezept3View = (TextView) view.findViewById(R.id.rezept3);
        rezept1Bild = (ImageButton) view.findViewById(R.id.rezeptButton);
        rezept2Bild = (ImageButton) view.findViewById(R.id.rezeptButton2);
        rezept3Bild = (ImageButton) view.findViewById(R.id.rezeptButton3);
        frueh = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        mittag = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        abend = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        nachtisch = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        vegan = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        vegetarisch = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        glutenfrei = (Switch) view.findViewById(R.id.fruehstucksRezepte);
        gesund = (Switch) view.findViewById(R.id.fruehstucksRezepte);


        fruehBoolean = frueh.isChecked();
        mittagBoolean = mittag.isChecked();
        abendBoolean = abend.isChecked();
        nachtischBoolean = nachtisch.isChecked();
        veganBoolean = vegan.isChecked();
        vegetarischBoolean = vegetarisch.isChecked();
        glutenfreiBoolean = glutenfrei.isChecked();
        gesundBoolean = gesund.isChecked();

        rezeptNr1 = new String[2];
        rezeptNr2 = new String[2];
        rezeptNr3 = new String[2];




        anzeigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {
                    urlZusammenstellung();

                    toastMessage("Daten werden verarbeitet!");


                    // Ausführung des Hintergrund-Thread mit HTTP-Request
                    RezepteFragment.MeinHintergrundThread mht = new RezepteFragment.MeinHintergrundThread();
                    mht.start();
                    try {
                        mht.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    setzteTEXT();


                }
            }
        });


        rezept1Bild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline() == false) {
                    toastMessage("Keine Internetverbindung!");
                } else {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rezeptNr1[2]));
                    startActivity(browserIntent);
                }
            }
        });


        return view;
    }

    private class MeinHintergrundThread extends Thread {

        @Override
        public void run() {

            try {
                String jsonDocument = holeRezeptDaten();
                parseJSON(jsonDocument);
                rezept1View.setText(rezeptNr1[0]);



            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }
        }
    }

    private String holeRezeptDaten() throws Exception {

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

        /**
         * String mit JSON-Inhalt wird auf Inhalte ausgelesen (Parsing) und in der dazugehörigen Datenbank abgespeichert
         */
        private void parseJSON(String jsonString) throws Exception {


            if (jsonString == null || jsonString.trim().length() == 0) {

                //Bei erhalt eines leeren Strings wird eine Fehlermeldung zurückgeliefert
                toastMessage("JSON ist leer!");
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray recipes = jsonObject.getJSONArray("recipes");
            JSONObject rezeptData1 = recipes.getJSONObject(0);
            JSONObject rezeptData2 = recipes.getJSONObject(1);
            //JSONObject rezeptData3 = recipes.getJSONObject(2);


            rezeptNr1[0] = rezeptData1.getString("title");
            rezeptNr1[1] = rezeptData1.getString("image");
            rezeptNr1[2] = rezeptData1.getString("sourceUrl");

            /*rezeptNr2[0] = rezeptData2.getString("title");
            rezeptNr2[1] = rezeptData2.getString("image");
            rezeptNr2[2] = rezeptData2.getString("sourceUrl");*/

            /*rezeptNr3[0] = rezeptData3.getString("title");
            rezeptNr3[1] = rezeptData3.getString("image");
            rezeptNr3[2] = rezeptData3.getString("sourceUrl");*/
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

    private void rezeptBildLaden() {

        Picasso.get().load(rezeptNr1[1]).into(rezept1Bild);
    }

    public void setzteTEXT() {
        rezept1View.setText(rezeptNr1[0]);
        //rezept2View.setText(rezeptNr2[0]);
        rezeptBildLaden();
    }


    private void urlZusammenstellung(){
        urlMitName = "http://192.168.178.62:8080/api/recipes?number=1&tags=";

        if(fruehBoolean == true){
            urlMitName= urlMitName;
        }
        if(mittagBoolean == true){
            urlMitName= urlMitName + "lunch";
        }
        if(abendBoolean == true){
            urlMitName= urlMitName + "dinner";
        }
        if(nachtischBoolean == true){
            urlMitName= urlMitName + "dessert";
        }
        if(veganBoolean == true){
            urlMitName= urlMitName + "vegan";
        }
        if(vegetarischBoolean == true){
            urlMitName= urlMitName + "vegetarian";
        }
        if(glutenfreiBoolean == true){
            urlMitName= urlMitName + "glutenFree";
        }
        if(gesundBoolean == true){
            urlMitName= urlMitName + "veryHealthy";
        }


    }


}
