package com.djrefreshlibrary;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.NativeGestureUtil;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/*
  WritableMap map = new WritableNativeMap();
      if (newState == RefreshState.None) {
        map.putInt("state", 1);
        eventEmitter.receiveEvent(getTargetId(), onChangeStateEvent, map);
      } else if (newState == RefreshState.PullDownToRefresh) {
        map.putInt("state", 1);
        eventEmitter.receiveEvent(getTargetId(), onChangeStateEvent, map);
      } else if (newState == RefreshState.ReleaseToRefresh) {
        map.putInt("state", 2);
        eventEmitter.receiveEvent(getTargetId(), onChangeStateEvent, map);
      } else if (newState == RefreshState.RefreshReleased) {
        map.putInt("state", 3);
        eventEmitter.receiveEvent(getTargetId(), onChangeStateEvent, map);
      } else if (newState == RefreshState.RefreshFinish) {
        map.putInt("state", 4);
        eventEmitter.receiveEvent(getTargetId(), onChangeStateEvent, map);
      }
 */

public class DJRefreshLayout extends DJSwipeRefreshLayout {

  public static final String onChangeOffsetEvent = "onChangeOffset";
  public static final String onChangeStateEvent = "onChangeState";
  private ReactContext mReactContext;
  private RCTEventEmitter eventEmitter;

  private boolean mDidLayout = false;
  private boolean mRefresh = false;


  public DJRefreshLayout(ReactContext reactContext) {
    super(reactContext);
    mReactContext = reactContext;
    eventEmitter = reactContext.getJSModule(RCTEventEmitter.class);
    this.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(RefreshState state) {
        Log.e("DJRefresh","onRefresh");
        WritableMap map = new WritableNativeMap();
        map.putInt("state", state.ordinal());
        eventEmitter.receiveEvent(getTargetId(), onChangeStateEvent, map);
      }
    });
  }

  private int getTargetId() {
    return this.getId();
  }

  public void setRefreshing(boolean refreshing) {
    this.mRefresh = refreshing;
    if (this.mDidLayout) {
      super.setRefreshing(refreshing);
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (!this.mDidLayout) {
      this.mDidLayout = true;
      this.setRefreshing(this.mRefresh);
    }
  }
}
