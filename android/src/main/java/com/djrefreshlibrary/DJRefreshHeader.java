package com.djrefreshlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.views.view.ReactViewGroup;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

public class DJRefreshHeader extends ReactViewGroup implements RefreshHeader {

  public DJRefreshHeader(Context context) {
    super(context);
  }

  @NonNull
  @Override
  public View getView() {
    return this; // 真实的视图就是自己，不能返回null
  }

  @NonNull
  @Override
  public SpinnerStyle getSpinnerStyle() {
    return SpinnerStyle.Translate; // 指定为平移，不能null
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void setPrimaryColors(int... colors) {

  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

  }

  @SuppressLint("RestrictedApi")
  @Override
  public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
    return 0;
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

  }

  @Override
  public boolean isSupportHorizontalDrag() {
    return false;
  }

  @Override
  public boolean autoOpen(int duration, float dragRate, boolean animationOnly) {
    return false;
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

  }
}
