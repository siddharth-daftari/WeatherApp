package com.apoorv.android.weatherapp.helper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.toolbox.DiskBasedCache;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by siddharthdaftari on 11/1/17.
 */

public class CallApi {

    public static JSONObject callApi(String urlString) throws JSONException {

        String https_url = urlString;
        URL url;
        JSONObject jsonObject = null;

        try {

            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            String timeApiResponse = print_content(con);
            jsonObject = new JSONObject(timeApiResponse);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    public static String print_content(HttpsURLConnection con){
        String temp ="";
        if(con!=null){

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    temp = temp + "\n" + input;
                }
                br.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }
}
