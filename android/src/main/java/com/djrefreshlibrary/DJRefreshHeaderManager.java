package com.djrefreshlibrary;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class DJRefreshHeaderManager extends ViewGroupManager<DJRefreshHeader> {
    public static final String REACT_CLASS = "DJRefreshHeader";

    @Override
    @NonNull
    public String getName() {
      return REACT_CLASS;
    }

    @Override
    @NonNull
    public DJRefreshHeader createViewInstance(ThemedReactContext reactContext) {
      return new DJRefreshHeader(reactContext);
    }

}
