package com.example.melody.dementia;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by meghabettadpur on 2017-03-04.
 */


public class findImage  extends Activity {
        /** Called when the activity is first created. */
        private Button login;
        private EditText username, password;



        protected String getImage(String keyWord)
        {
            HttpURLConnection connection;
            OutputStreamWriter request = null;
            Random rand = new Random();
            Integer rndNum = rand.nextInt(1000);
            String response = null;
            JSONObject document = new JSONObject();

            try {
                //document.put("language", "en");
                //document.put("id", rndNum);
                //document.put("text", speechStr);


                //JSONArray jsonArray = new JSONArray();
                //jsonArray.put(document);
                //JSONObject documentObj = new JSONObject();
                //documentObj.put("documents", jsonArray);
                //String jsonStr = documentObj.toString();





                 URL url = null;

                 String URLstringBegin = "https://api.cognitive.microsoft.com/bing/v5.0/images/search?";
                 String word="q=" + keyWord;
                 String URLStringEnd ="&count=1&mkt=en-us&safeSearch=Strict";

                  String FinalURL = URLstringBegin + word + URLStringEnd;


                url = new URL(FinalURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
               // connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Ocp-Apim-Subscription-Key","ab8f465afdcd4280a1c8475f27d6fa5d");
                connection.setRequestMethod("GET");
                request = new OutputStreamWriter(connection.getOutputStream());

                // Toast.makeText(this,"Returned Text from Speech: \n"+ jsonStr, Toast.LENGTH_LONG).show();


               // request.write(jsonStr);
                request.flush();
                request.close();
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
