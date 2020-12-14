package com.example.copwatch.application;

import android.app.Application;
import android.content.Context;

public class CopWatchApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    private static Context appContext;
    public static Context getContext() {
        return appContext;
    }
}
