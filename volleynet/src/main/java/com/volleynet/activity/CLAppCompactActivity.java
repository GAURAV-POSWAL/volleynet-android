package com.volleynet.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.volleynet.utils.ActivityState;


public class CLAppCompactActivity extends AppCompatActivity {
    private ActivityState state;

    public ActivityState getActivityState() {
        return state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = ActivityState.OnCreate;
    }

    @Override
    protected void onStart() {
        super.onStart();
        state = ActivityState.OnStart;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        state = ActivityState.OnRestart;
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = ActivityState.OnResume;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        state = ActivityState.OnPostResume;
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = ActivityState.OnPause;
    }

    @Override
    protected void onStop() {
        super.onStop();
        state = ActivityState.OnStop;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        state = ActivityState.OnDestroy;
    }

    /**
     * Use this method to start a new activity
     *
     * @param intent Intent of the activity
     */
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * Use this method to start a new activity
     *
     * @param cls Activity Class to be shown
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        super.startActivity(intent);
    }
}