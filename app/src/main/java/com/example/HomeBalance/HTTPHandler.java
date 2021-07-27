package com.example.HomeBalance;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Thread zum laden der API-Daten im Hintergrund, auf paralell Thread.
 * App-Stillstand/Absturz wird vermieden
 */
public class HTTPHandler extends Thread{
    private final Caller caller;
    private URL url;
    private String identifier;

    public HTTPHandler(Caller caller){
        this.caller = caller;
    }
    @Override
    public void run() {
        if(url!=null){
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(2000);
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    caller.handleAnswer(null, identifier, "Connection not Ok");
                } else {
                    InputStream is = conn.getInputStream();
                    InputStreamReader ris = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(ris);
                    caller.handleAnswer(reader, identifier, "Connection Ok");
                }
            } catch (Exception ex) {
                caller.handleAnswer(null, identifier, "No connection to backend possible");
            }
        }else {
            caller.handleAnswer(null, identifier, "URl malformed");
        }
    }
    public void setUrl(URL url) {
        this.url = url;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
