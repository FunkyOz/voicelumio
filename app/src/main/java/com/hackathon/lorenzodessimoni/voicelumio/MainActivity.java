package com.hackathon.lorenzodessimoni.voicelumio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private static final String IP = "http://10.17.2.9";
    private static final String TAG = "MainActivity";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(IP);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String search = i.getStringExtra("search");
        if(search != null) {
            makeRequest(search);
        }
    }

    private void makeRequest(String searchString) {
        mSocket.on("pushBrowseLibrary", search);
        mSocket.on("pushState", addPlay);
        mSocket.connect();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{\"value\":\"" + searchString + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("search", jsonObject);
    }

    private Emitter.Listener search = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject response = (JSONObject) args[0];
                    try {
                        JSONObject navigation = (JSONObject) response.get("navigation");
                        JSONArray lists = (JSONArray) navigation.get("lists");
                        int length = lists.length();
                        String uri = "";
                        JSONObject item = (JSONObject) lists.get(0);
                        String title = (String) item.get("title");
                        JSONArray items = (JSONArray) item.get("items");
                        JSONObject test = (JSONObject) items.get(0);
                        uri = (String) test.get("uri");
                        /*for (int i = 0; i < length; i++) {
                            JSONObject item = (JSONObject) lists.get(i);
                            String title = (String) item.get("title");
                            if(title.contains("Track")) {
                                JSONArray items = (JSONArray) item.get("items");
                                JSONObject test = (JSONObject) items.get(0);
                                uri = (String) test.get("uri");
                                i = length;
                            }
                        }*/
                        JSONObject uriObject = new JSONObject("{\"uri\":\"" + uri + "\"}");
                        mSocket.emit("addPlay", uriObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener addPlay = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject response = (JSONObject) args[0];
                }
            });
        }
    };
}