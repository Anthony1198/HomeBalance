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
    private Caller caller;
    private URL url;

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
                    caller.handleAnswer(null, "Connection not Ok");
                } else {
                    InputStream is = conn.getInputStream();
                    InputStreamReader ris = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(ris);
                    caller.handleAnswer(reader, "Connection Ok");
                }
            } catch (Exception ex) {
                caller.handleAnswer(null, "Unknown Exception");
            }
        }else {
            caller.handleAnswer(null, "URl is null");
        }
    }
    public void setUrl(URL url) {
        this.url = url;
    }
}
