package com.hackathon.lorenzodessimoni.voicelumio;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class HandleVoiceActivity extends Activity {
    private static String TAG = HandleVoiceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String search = getIntent().getStringExtra(SearchManager.QUERY);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("search", search);
        startActivity(intent);
    }


}
