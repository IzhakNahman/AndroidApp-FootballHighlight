package com.nahman.footballhighlights.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class IO {
    private IO() {
    }

    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }


    public static String readURL(String http) throws IOException{

        URL url = new URL(http);
        //HTTP + S
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        //HttpsUrlConnection extends HttpUtlConnection extends URLConnection extends Object

        InputStream in = con.getInputStream();

        return read(in);

    }

    public static String read(InputStream in) throws IOException {

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(System.lineSeparator());
                }

                return sb.toString();

            }

    }


    public static String read(AssetManager am, String fileName) throws IOException {
        //InputStream in = am.open(fileName);
        return read(am.open(fileName));
    }

//    public static boolean isInternetAvailable() {
//        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com");
//            //You can replace it with your name
//            return !ipAddr.equals("");
//
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public static boolean haveNetworkConnection(Context con) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
