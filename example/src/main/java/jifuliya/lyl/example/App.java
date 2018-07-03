package jifuliya.lyl.example;

import android.app.Application;

import com.lyl.router.Portal;

import jifuliya.lyl.portalrouter.PortalRoutor;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Portal.initialize();
    }
}
