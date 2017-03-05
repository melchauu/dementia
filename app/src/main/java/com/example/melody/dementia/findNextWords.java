package com.example.melody.dementia;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by meghabettadpur on 2017-03-05.
 */

public class findNextWords extends Activity {

    // // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)



        protected String theNextWordIs(String speechStr) {

            HttpURLConnection connection;
            OutputStreamWriter request = null;

            String response = null;
            JSONObject document = new JSONObject();


            try {


                String urlBegin = "https://westus.api.cognitive.microsoft.com/text/weblm/v1.0/generateNextWords?model=query&words=";
                String urlEnd="&order=5&maxNumOfCandidatesReturned=1";
                String completeUrl = urlBegin + "horses" + urlEnd;

                URL url = null;



                url = new URL(completeUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Ocp-Apim-Subscription-Key","efb6846f61c1460c8d8eef308d9c0189");
                connection.setRequestMethod("POST");
                request = new OutputStreamWriter(connection.getOutputStream());

                // Toast.makeText(this,"Returned Text from Speech: \n"+ jsonStr, Toast.LENGTH_LONG).show();

                //request.write("RandomText");

               // request.flush();
                //request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                // Response from server after login process will be stored in response variable.
                response = sb.toString();
                // You can perform UI operations here
                //Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
                isr.close();
                reader.close();



            }
            // TODO Auto-generated catch block

            catch(IOException e)
            {
                // Error
                e.printStackTrace();
            }
            catch(Exception e)
            {
                // Error
                e.printStackTrace();
            }
            return response.toString();
        }


}


