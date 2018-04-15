package com.hackathon.lorenzodessimoni.voicelumio;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AudioItem {

    public String service;
    public String title;
    public String type;
    public String artist;
    public String album;
    public String uri;
    public String albumart;

    public AudioItem(){}

    public static AudioItem parse(JSONObject json){
        AudioItem audio = new AudioItem();
        try {
            Log.d("AudioItem", json.getString("album"));
            audio.service = json.getString("service");
            audio.type = json.getString("type");
            audio.artist = json.getString("artist");
            audio.title = json.getString("title");
            audio.album = json.getString("album");
            audio.uri = json.getString("uri");
            audio.albumart = json.getString("albumart");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return audio;
    }

}
