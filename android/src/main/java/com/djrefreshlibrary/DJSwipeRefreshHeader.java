package com.djrefreshlibrary;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class DJSwipeRefreshHeader extends RelativeLayout {
  public DJSwipeRefreshHeader(Context context) {
    super(context);
  }

  private Animation.AnimationListener mListener;

  public void setAnimationListener(Animation.AnimationListener listener) {
    mListener = listener;
  }

  @Override
  public void onAnimationStart() {
    super.onAnimationStart();
    if (mListener != null) {
      mListener.onAnimationStart(getAnimation());
    }
  }

  @Override
  public void onAnimationEnd() {
    super.onAnimationEnd();
    if (mListener != null) {
      mListener.onAnimationEnd(getAnimation());
    }
  }

  public void moveSpinner(float overscrollTop,float totalDragDistance) {

  }

  public void finishSpinner(float overscrollTop, float totalDragDistance) {

  }

  public void onPulling(){

  }
  public void onRefreshing(){

  }
  public void onFinished(){

  }
}
