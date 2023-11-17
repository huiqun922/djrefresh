package com.djrefreshlibrary;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class DJRefreshDefaultHeaderManager extends SimpleViewManager<View> {
  public static final String REACT_CLASS = "DJRefreshDefaultHeader";

  @Override
  @NonNull
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  @NonNull
  public View createViewInstance(ThemedReactContext reactContext) {
    return new DJRefreshDefaultHeader(reactContext);
  }

  @ReactProp(name = "locale")
  public void setLocale(DJRefreshDefaultHeader view, String locale) {
    view.setLocale(locale);
  }

}
