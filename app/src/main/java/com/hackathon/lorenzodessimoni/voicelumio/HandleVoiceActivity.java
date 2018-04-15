package com.hackathon.lorenzodessimoni.voicelumio;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class HandleVoiceActivity extends Activity {
    private static final String IP = "http://10.17.2.9";
    private static String TAG = HandleVoiceActivity.class.getSimpleName();
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(IP);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.on("pushBrowseLibrary", search);
        mSocket.on("pushState", addPlay);
        mSocket.connect();
        Log.d(TAG, "Creo socket");
        String search = getIntent().getStringExtra(SearchManager.QUERY);
        makeRequest(search);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("search");
        mSocket.off("addPlay");
        mSocket.disconnect();
    }

    //-------------METHODS

    private void makeRequest(String query) {
        JSONObject jsonObject = null;
        try {
            mSocket.emit("clearQueue", new JSONObject("{\"value\": \"spop\"}"));
            jsonObject = new JSONObject("{\"value\":\"" + query + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Creo richiesta: " + query);
        mSocket.emit("search", jsonObject);
    }

    private void parseSearchResponse(JSONObject response) {
        try {
            JSONObject navigation = (JSONObject) response.get("navigation");
            //Lists of plugins
            JSONArray lists = (JSONArray) navigation.get("lists");
            int howManyPlugins = lists.length();
            if(howManyPlugins <= 0) {
                Toast.makeText(this, "No audio found!", Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject localPlugin = null;
            JSONObject youtubePlugin = null;
            for (int i = 0; i < howManyPlugins; i++) {
                JSONObject plugin = (JSONObject) lists.get(i);
                if (plugin.getString("title").toLowerCase().contains("found"))
                    localPlugin = plugin;
                else if (plugin.getString("title").toLowerCase().contains("youtube"))
                    youtubePlugin = plugin;
            }
            JSONArray audioList;
            if (localPlugin != null) {               // Se ci sono risultati locali
                audioList = (JSONArray) localPlugin.get("items");
            } else if (youtubePlugin != null ){      // Seconda scelta ci sono youtube
                audioList = (JSONArray) youtubePlugin.get("items");
            }else {                                  // Altrimenti prendo il primo plugin a caso
                audioList = (JSONArray) ((JSONObject)lists.get(0)).get("items");
            }
            //Prendo il primo audio nella lista
            JSONObject audioSelected = (JSONObject) audioList.get(0);
            AudioItem audioItem = AudioItem.parse(audioSelected);
            //TODO HERE REFRESH LAYOUT ON APP
            JSONObject uriObject = new JSONObject("{\"uri\":\"" + audioItem.uri + "\", \"service\":\"" + audioItem.service + "\"}");
            mSocket.emit("addPlay", uriObject);
            mSocket.off("search");
            mSocket.off("addPlay");
            mSocket.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //-------------SOCKET LISTENERS

    private Emitter.Listener search = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Ricevo risposta evento search");
                    parseSearchResponse((JSONObject) args[0]);
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
                    Log.d(TAG, "Ricevo risposta evento addPlay");
                    JSONObject response = (JSONObject) args[0];
                }
            });
        }
    };

}
