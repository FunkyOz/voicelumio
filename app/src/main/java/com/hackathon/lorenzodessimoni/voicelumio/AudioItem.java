package com.hackathon.lorenzodessimoni.voicelumio;

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
            audio.service = json.getString("service");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audio.type = json.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audio.artist = json.getString("artist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audio.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audio.album = json.getString("album");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audio.uri = json.getString("uri");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audio.albumart = json.getString("albumart");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return audio;
    }

}
