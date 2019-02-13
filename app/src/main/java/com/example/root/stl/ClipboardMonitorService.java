package com.example.root.stl;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClipboardMonitorService extends Service {
    private static final String TAG = "ClipboardManager";
    public static String CLIPBORD_MONITOR_SERVICE_ACTION = ClipboardMonitorService.class.getName() + "clipboardservice_broadcast";
    public static String CIP_URL = "extra_mlresponse";
    private ClipboardManager mClipboardManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(
                mOnPrimaryClipChangedListener);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //start the clipboard monito service
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = mClipboardManager.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        broadcast_url(item.getText().toString());
        mClipboardManager.addPrimaryClipChangedListener(
                mOnPrimaryClipChangedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClipboardManager != null) {

     }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ClipboardManager.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener =
            new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    Log.d(TAG, "onPrimaryClipChanged");
           if (!(mClipboardManager.hasPrimaryClip())) {
                        ClipData clip = mClipboardManager.getPrimaryClip();
                        ClipData.Item item = clip.getItemAt(0);
                        String clip_text_url = item.getText().toString();
               broadcast_url("https://see-the-light.herokuapp.com/news/get?link="+clip_text_url);
                        if (isValid(clip_text_url)) {
                            String endpoint = "https://see-the-light.herokuapp.com/news/get?link=";

                        } else {
                            Toast.makeText(getApplicationContext(), "copy a valid news url", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            };

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
    //loadUrl("https://see-the-light.herokuapp.com/news/get?link=" + etName.getText().toString()
    public void  broadcast_url(String url){
        Log.d(TAG, "sending CLIPBOAED INFO...");
        Intent intent = new Intent(CLIPBORD_MONITOR_SERVICE_ACTION);
        intent.putExtra(CIP_URL, url);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
