package android.app.printerapp;

import android.app.printerapp.viewer.ViewerMainFragment;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.app.Activity;
import android.app.printerapp.library.LibraryController;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import org.apache.commons.io.*;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import android.webkit.JavascriptInterface;

/**
 * Splash screen activity that shows the logo of the app during a time interval
 * of 3 seconds. Then, the main activity is charged and showed.
 *
 * @author sara-perez
 */
public class SplashScreenActivity extends Activity {

    private static final String TAG = "SplashScreenActivity";

    //Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    public static SplashScreenActivity mSplashScreenActivity;

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSplashScreenActivity = this;

        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        //Initialize db and lists
        LibraryController.initializeHistoryList();

        //Initialize default settings
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);



        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.clearCache(true);
        myWebView.getSettings().setJavaScriptEnabled(true);


        myWebView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void performClick(String strl)
            {
                String stringVariable = strl;
                //
                //FileUtils.copyURLToFile("https://drive.google.com/uc?export=download&id=1UR-ldLTkg4B5ZW5vRY2saZ5M3yEj97F6", new File());
                Log.d("STATE", strl);

                ViewerMainFragment.downloadableFilePath = strl;

                Log.d(TAG, "[START PRINTERAPP]");
                Intent mainIntent = new Intent().setClass(
                        SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);



            }
        }, "webView");

        //IP to connect to
        myWebView.loadUrl("http://10.0.2.2:8888/");

        if (isTaskRoot()){

            //Simulate a long loading process on application startup
            Timer timer = new Timer();
            //timer.schedule(splashDelay, SPLASH_SCREEN_DELAY);
            // TODO EXTRACT Set splash sc
            //timer.schedule(splashDelay, 10000);

        }else finish();

    }

    TimerTask splashDelay = new TimerTask() {
        @Override
        public void run() {

            Log.d(TAG, "[START PRINTERAPP]");

            Intent mainIntent = new Intent().setClass(
                    SplashScreenActivity.this, MainActivity.class);
            startActivity(mainIntent);

            //Close the activity so the user won't able to go back this
            //activity pressing Back button
            finish();
        }
    };

}
