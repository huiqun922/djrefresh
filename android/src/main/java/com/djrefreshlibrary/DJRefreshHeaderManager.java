package com.djrefreshlibrary;

import android.graphics.Color;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;

import java.util.Map;

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
