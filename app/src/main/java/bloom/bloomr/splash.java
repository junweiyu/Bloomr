package bloom.bloomr;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;


public class splash extends Activity {


    public static int Splash_Time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
//        Typeface myFont = Typeface.createFromAsset(getAssets(), "Cutie Patootie Skinny.ttf");
//        TextView myTextView = (TextView) findViewById(R.id.logo1);
//        TextView mySlogan = (TextView) findViewById(R.id.slogan);
//        myTextView.setTypeface(myFont);
//        mySlogan.setTypeface(myFont);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this, bloom.bloomr.LoginActivity.class);
                startActivity(i);
                finish();
            }

        }, Splash_Time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
