package com.volleynet.application;

import android.app.Application;
import android.content.Intent;

public class BaseApplication extends Application {
    //Used to get the application object of the application
    private static BaseApplication baseApplication;

    /**
     * Gets the application object
     *
     * @return application object or nil if application is not initialized
     */
    public static BaseApplication getApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //set the baseApplication
        baseApplication = this;
        //configure the application
        configureApplication(isRelease());
    }

    /**
     * Override this method to change the config based on release/debug
     *
     * @param isRelease tells if it's release or debug
     */
    public void configureApplication(boolean isRelease) {
        //nothing to do here
    }

    /**
     * Override this method in your application module (not in lib module) to tell base activity that
     * use config for debug or release. By default it assumes that config is for debug
     *
     * @return true/false
     */
    public boolean isRelease() {
        return false;
    }

    /**
     * Override this method if you want to forceUpdate the app
     *
     * @return true if you have handled the flow and don't want the ApiCall to continue with false case flow i.e. throw callback for ApiCall responseFailed.
     */
    public boolean handleForceUpdate(Intent infoIntent) {
        return false;
    }
}
