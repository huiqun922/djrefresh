package com.djrefreshlibrary;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

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

}
