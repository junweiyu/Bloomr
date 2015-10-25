package bloom.bloomr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {


    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Bundle extras = getIntent().getExtras();
        String string = extras.getString("Values");
        String[] values = string.split(",");
        String title = values[0];
        TextView tooltext = (TextView)toolbar.findViewById(R.id.toolbar_title);
        tooltext.setText(title);
        tooltext.setTextSize(10);
        setSupportActionBar(toolbar);
        String owner = values[1];
        String organization = values[2];
        String dates = values[3];
        String privacy = values[4];
        String website = values[5];
        String capacity = values[6];
        String description = values[7];
        Event event = HomeActivity.eventForPage;

        View coordinatorLayout = findViewById(R.id.event_coordinator);
        View scrollLayout = coordinatorLayout.findViewById(R.id.event_scroll);
        LinearLayout linearLayout = (LinearLayout)scrollLayout.findViewById(R.id.event_text);
        TextView txt = new TextView(EventActivity.this);
        txt.setText(title + '\n' + "When: " + dates + "\n" + "Hosted by: " + owner + '\n' + "Organization: " + organization + '\n' + "Description: " + description + '\n' + "More information at " + website);
        linearLayout.addView(txt);

        final Button join = (Button)toolbar.findViewById(R.id.event_btn);

        join.setText("Join [" + event.getVolunteers().size() + "]");
        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                join();
                join.setText("Join [" + HomeActivity.eventForPage.getVolunteers().size() + "]");
            }
        });
    }

    public void join() {
        //TODO implement events and users
        if (!HomeActivity.eventForPage.getVolunteers().contains(HomeActivity.currentUser)){
            HomeActivity.eventForPage.addVolunteer(HomeActivity.currentUser);
        }
    }
}
