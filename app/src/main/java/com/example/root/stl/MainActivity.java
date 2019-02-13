package com.example.root.stl;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Dialog webViewDialog;
    private WebView webView;
    private Button btClose;
    ClipboardManager mClipboardManager;
    TextView txttext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



//        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        ClipData clipData = mClipboardManager.getPrimaryClip();
//        ClipData.Item item = clipData.getItemAt(0);
//        Toast.makeText(this, " sthe urrrrrrrrl is"+item.getText().toString(), Toast.LENGTH_LONG).show();
//
//        mClipboardManager.addPrimaryClipChangedListener(
//                mOnPrimaryClipChangedListener);


        Intent intent = new Intent(this, ClipboardMonitorService.class);
        startService(intent);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String url_val = intent.getStringExtra(ClipboardMonitorService.CIP_URL);
                Toast.makeText(context, " Receeeved"+url_val, Toast.LENGTH_LONG).show();
            }
        }, new IntentFilter(ClipboardMonitorService.CLIPBORD_MONITOR_SERVICE_ACTION));


        //Create a new dialog
        webViewDialog = new Dialog(this);
        webViewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        webViewDialog.setContentView(R.layout.layout);
        webViewDialog.setCancelable(true);
        btClose = (Button) webViewDialog.findViewById(R.id.bt_close);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dismiss the dialog
                webViewDialog.dismiss();
            }//            mClipboardManager.removePrimaryClipChangedListener(
//                    mOnPrimaryClipChangedListener);


        });
        webView = (WebView) webViewDialog.findViewById(R.id.wb_webview);
        webView.setScrollbarFadingEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("AndroidWebView");
        webView.clearCache(true);
        webView.loadUrl("https://see-the-light.herokuapp.com/news/get?link=https://www.the-star.co.ke/news/2019/02/13/women-are-f" +
                "orced-to-have-sex-in-return-for-ebola-jabs-in-drc_c1893937");
        Handler handler=new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
//ToDo your function
//hide your popup here
                webViewDialog.show();
            }
        },2000);

    }
    public ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    if (!(mClipboardManager.hasPrimaryClip())) {
                        ClipData clip = mClipboardManager.getPrimaryClip();
                        ClipData.Item  item = clip.getItemAt(0);
                        String clip_text_url = item.getText().toString();
                        Toast.makeText(getApplicationContext(), clip_text_url, Toast.LENGTH_LONG).show();
                        if (isValid(clip_text_url)) {
                            String endpoint = "https://see-the-light.herokuapp.com/news/get?link=";

                        } else {
                            Toast.makeText(getApplicationContext(), "copy a valid news url", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /* Returns true if url is valid */
    public static boolean isValid(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
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



