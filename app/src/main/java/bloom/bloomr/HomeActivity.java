package bloom.bloomr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class HomeActivity extends AppCompatActivity {

    public int current_id = 0;
    public ArrayList<ImageView> eventImages = new ArrayList<ImageView>();
    public ArrayList<Button> eventNames = new ArrayList<Button>();
    public static User currentUser;
    public static ArrayList<Event> events = new ArrayList<Event>();
    public static Event eventForPage;
    public Integer[] imageIds = {R.drawable.internship, R.drawable.beach, R.drawable.mentorship};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton postButton = (FloatingActionButton) findViewById(R.id.post_button);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, PostActivity.class);
                startActivity(i);
            }
        });

        Context context = getBaseContext();
        View coordinatorLayout = findViewById(R.id.coordinator);
        View scrollLayout = coordinatorLayout.findViewById(R.id.scroll);
        LinearLayout linearLayout = (LinearLayout)scrollLayout.findViewById(R.id.postings);
        Log.d("SHOWING ", linearLayout.getLayoutParams().toString());
        loadImages(linearLayout);
    }

    public void loadImages(LinearLayout linearLayout) {
        final LinearLayout ll = linearLayout;
        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(30, 20, 30, 100);
                for (int i = 0; i < eventImages.size(); i++) {

                    Log.d("SIZE OF LIST ", String.valueOf(eventNames.get(i).getText()));
                    ll.addView(eventImages.get(i));
                    ll.addView(eventNames.get(i), layoutParams);
                }

            }
        };
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
                    try {
                        JSONObject jObj = new JSONObject(mResponse);
                        JSONArray events = jObj.getJSONArray("events");
                        if (events.length() != 0) {
                            for (int i = events.length() - 1; i >= Math.max(events.length() - 10, 0); i--) {
                                JSONObject event = events.getJSONObject(i);
                                String owner = event.getString("Name");
                                String organization = event.getString("Organization");
                                String title = event.getString("Title");
                                String description = event.getString("Description");
                                String dates = event.getString("Date(s) of Activity");
                                int privacy = Integer.parseInt(event.getString("Private (Seen only by Friends)"));
                                String website = event.getString("Website");
                                String capacity = event.getString("Capacity");
                                Log.d("Owner ", owner);
                                Log.d("Title ", title);
                                Log.d("Organization ", organization);
                                Log.d("Dates ", dates);
                                Log.d("Privacy ", privacy + "");
                                Log.d("Website ", website);
                                if (capacity.equals("custom")) {
                                    capacity = event.getString("Custom");
                                }
                                Log.d("Capacity ", capacity + "");
                                TextView valueTV = new TextView(HomeActivity.this);
                                valueTV.setId(current_id);
                                dates = dates.replaceAll("/", "");

                                valueTV.setText("Event " + title + '\n' + "Organizer " + owner + '\n' + "When: " + title + '\n' + "Website " + website);
                                final String values = title + "," + owner + "," + organization + "," + dates + "," + privacy + "," + website + "," + capacity + "," + description;
                                Button eventBtn = new Button(HomeActivity.this);
                                eventBtn.setText(title + '\n' + dates);
                                final Event e = new Event(HomeActivity.currentUser, title, privacy, description, website, dates, capacity);
                                eventBtn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent i = new Intent(HomeActivity.this, EventActivity.class);
                                        i.putExtra("Values", values);
                                        HomeActivity.eventForPage = e;
                                        Log.d("WORKING ", e.getTitle());
                                        startActivity(i);
                                    }
                                });

                                ImageView eventImage = new ImageView(HomeActivity.this);
                                eventImage.setImageResource(imageIds[i%3]);
                                eventImages.add(eventImage);
                                eventNames.add(eventBtn);

//                                ll.addView(eventImage);
//                                ll.addView(eventBtn);
                            }
                            Looper.prepare();
                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", "hello");
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

//    public void generateEvents(LinearLayout lll) {
//        final LinearLayout ll = lll;
//        new Handler()(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("SIZE OF LIST ", eventImages.size() + "");
//                for (int i = 0; i < eventImages.size(); i++) {
//                    Log.d("SIZE OF LIST ", String.valueOf(eventNames.get(i).getText()));
//                    ll.addView(eventImages.get(i));
//                    ll.addView(eventNames.get(i));
//                }
//            }
//        }, 0);
//    }

//    public void loadEvent(LinearLayout lll, Button eventBtnn, ImageView eventImagee) {
//        final LinearLayout ll = lll;
//        final Button eventBtn = eventBtnn;
//        final ImageView eventImage = eventImagee;
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ll.addView(eventImage);
//                ll.addView((eventBtn));
//            }
//        }, 1000);
//    }

//    public void post(View v) {
//        Intent i = new Intent(HomeActivity.this, PostActivity.class);
//        startActivity(i);
//    }
}
