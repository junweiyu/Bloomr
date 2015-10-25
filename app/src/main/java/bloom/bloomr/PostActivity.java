package bloom.bloomr;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class PostActivity extends FormActivity
{
    public static final int OPTION_SAVE = 0;
    public static final int OPTION_POPULATE = 1;
    public static final int OPTION_CANCEL = 2;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        generateForm(FormActivity.parseFileToString(this, "schemas.json"));

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        menu.add(0, OPTION_SAVE, 0, "Save");
        menu.add(0, OPTION_POPULATE, 0, "Populate");
        menu.add(0, OPTION_CANCEL, 0, "Cancel");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch( item.getItemId() )
        {
            case OPTION_SAVE:
                save();
                break;

            case OPTION_POPULATE:
                populate( FormActivity.parseFileToString( this, "data.json" ) );
                break;

            case OPTION_CANCEL:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public JSONObject save() {
        JSONObject event = super.save();
        store(event);
        return event;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public void store(JSONObject event){
        final JSONObject events = event;
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                Log.d("JSON ", events.toString());
                Date createDate = new Date();
                String response = "";
                try {

                    String currentUrl = "http://10.0.3.2:5000/store";
                    Log.d("String", "The url is: " + currentUrl);
                    url = new URL(currentUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));

                    writer.write(events.toString());

                    writer.flush();
                    writer.close();
                    os.close();
                    Log.d("RESPOND", conn.getResponseMessage());
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                    } else {
                        response = "";

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return;

    }

    public void getEvents(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    Date createDate = new Date();

                    String currentUrl = "http://10.0.3.2:5000/get";
                    Log.d("String", "The url is: " + currentUrl);
                    url = new URL(currentUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return;
                }
                String mResponse = "";
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(in);
                    //Scanners scan line by line: if your response is longer than 1 line, you need a loop
//                            mTimeResponse = scanner.nextLine(); //parses the GET request into a string
                    while (scanner.hasNext()) {
                        mResponse = mResponse + scanner.nextLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                if (!mResponse.equals("")) {
                    Log.d("Response", mResponse);
                }
            }
        }).start();
    }
}