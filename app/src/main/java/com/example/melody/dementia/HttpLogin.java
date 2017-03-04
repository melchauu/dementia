package com.example.melody.dementia;

/**
 * Created by melody on 2017-03-04.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpLogin extends Activity {
    /** Called when the activity is first created. */
    private Button login;
    private EditText username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /*login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String   mUsername = username.getText().toString();
                String  mPassword = password.getText().toString();

                tryLogin(mUsername, mPassword);
            }
        });*/
    }

    protected void sendReceiveRequest(String speechStr, String mPassword)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;
        JSONObject document = new JSONObject();
        try {
            document.put("language", "en");
            document.put("id", rand);
            document.put("year", "3rd");
            document.put("curriculum", "Arts");
            document.put("birthday", "5/5/1993");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        URL url = null;
        String response = null;
        String key = "9bc644535e2e4cf6bd097ae545737ff1";
        String parameters = "username="+speechStr+"&password="+mPassword;

        try
        {
            url = new URL("https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/languages?numberOfLanguagesToDetect=1");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
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
            Toast.makeText(this,"Message from Server: \n"+ response, 0).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
    }
}