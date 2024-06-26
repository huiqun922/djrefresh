package com.djrefreshlibrary;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ColorPropConverter;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.AndroidSwipeRefreshLayoutManagerInterface;
import com.facebook.react.views.swiperefresh.ReactSwipeRefreshLayout;

import java.util.Map;

public class DJRefreshLayoutManager extends ViewGroupManager<DJRefreshLayout>  {
  public static final String REACT_CLASS = "DJRefreshLayout";
  private ThemedReactContext mReactContext;

  @Override
  @NonNull
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  @NonNull
  public DJRefreshLayout createViewInstance(ThemedReactContext reactContext) {
    Log.e("DJRefresh","DJRefreshLayoutManager createViewInstance");
    mReactContext = reactContext;
    return new DJRefreshLayout(reactContext);
  }

  @ReactProp(name = "headerHeight")
  public void setHeaderHeight(DJRefreshLayout view, float headerHeight) {
    if (headerHeight != 0.0f) {
      //view.setHeaderHeight(headerHeight);
    }
  }

  @ReactProp(
    name = "enabled",
    defaultBoolean = true
  )
  public void setEnabled(DJRefreshLayout djRefreshLayout, boolean b) {
    djRefreshLayout.setEnabled(b);
  }

  @ReactProp(name = "refreshing")
  public void setRefreshing(DJRefreshLayout djRefreshLayout, boolean refreshing) {
    djRefreshLayout.setRefreshing(refreshing);
  }

  @Override
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    String onChangeStateEvent = DJRefreshLayout.onChangeStateEvent;
    String onChangeOffsetEvent = DJRefreshLayout.onChangeOffsetEvent;
    return MapBuilder.<String, Object>builder()
      .put(onChangeStateEvent, MapBuilder.of("registrationName", onChangeStateEvent))
      .put(onChangeOffsetEvent, MapBuilder.of("registrationName", onChangeOffsetEvent)).build();
  }

}
