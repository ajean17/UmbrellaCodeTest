package com.foo.umbrella;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class UmbrellaApp extends Application {

  private static UmbrellaApp thisApp = null;

  public static UmbrellaApp getApplication() {
    return thisApp;
  }

  @Override public void onCreate() {
    super.onCreate();
    thisApp = this;
    AndroidThreeTen.init(thisApp);
  }
}
