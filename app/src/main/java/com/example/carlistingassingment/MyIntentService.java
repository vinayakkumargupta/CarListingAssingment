package com.example.carlistingassingment;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;


public class MyIntentService extends IntentService {
    public static final String SERVICE_PAYLOAD = "SERVICE_PAYLOAD";
    public static final String SERVICE_MESSAGE = "SERVICE_MESSAGE";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        String data;
        try {
            data = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            data = e.getMessage();
        }
        sendMessageToUi(data);


    }

    private void sendMessageToUi(String data) {
        Intent intent = new Intent(SERVICE_MESSAGE);
        intent.putExtra(SERVICE_PAYLOAD,"dummy data");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}