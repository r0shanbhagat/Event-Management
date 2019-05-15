package com.dev.eventmanagement;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
