package com.djrefreshlibrary;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class DJRefreshDefaultHeader extends DJSwipeRefreshHeader {

  protected ImageView mArrowView;
  protected ImageView mProgressView;

  protected ArrowDrawable mArrowDrawable;
  protected ProgressDrawable mProgressDrawable;

  private boolean hasRotate = false;

  public DJRefreshDefaultHeader(Context context) {
    super(context);
    final View thisView = inflate(context, R.layout.default_header_layout, this);
    mArrowView = thisView.findViewById(R.id.header_arrow);
    mProgressView = thisView.findViewById(R.id.header_progress);

    mArrowDrawable = new ArrowDrawable();
    mArrowDrawable.setColor(0xff666666);
    mArrowView.setImageDrawable(mArrowDrawable);

    mProgressDrawable = new ProgressDrawable();
    mProgressDrawable.setColor(0xff666666);
    mProgressView.setImageDrawable(mProgressDrawable);
  }

  @Override
  public void onRefreshing() {
    super.onRefreshing();

    mArrowView.setRotation(0);
    hasRotate = false;

    mArrowView.setVisibility(View.INVISIBLE);
    mProgressView.setVisibility(View.VISIBLE);
    mProgressDrawable.start();
  }

  @Override
  public void onFinished() {
    super.onFinished();

    mArrowView.setRotation(0);
    hasRotate = false;

    mArrowView.setVisibility(View.VISIBLE);
    mProgressView.setVisibility(View.INVISIBLE);
    mProgressDrawable.stop();
  }

  @Override
  public void moveSpinner(float overscrollTop, float totalDragDistance) {
    super.moveSpinner(overscrollTop, totalDragDistance);
    if(overscrollTop > totalDragDistance){
      if(!hasRotate){
        hasRotate = true;
        startRotateAnimation(0,180);
      }
    }else {
      if(hasRotate){
        hasRotate = false;
        startRotateAnimation(180,0);
      }
    }
  }

  @Override
  public void finishSpinner(float overscrollTop, float totalDragDistance) {
    super.finishSpinner(overscrollTop, totalDragDistance);
    mArrowView.setRotation(0);
    hasRotate = false;
    if (overscrollTop > totalDragDistance) {
      mArrowView.setVisibility(View.INVISIBLE);
      mProgressView.setVisibility(View.VISIBLE);
      mProgressDrawable.start();
    }else{
      mArrowView.setVisibility(View.VISIBLE);
      mProgressView.setVisibility(View.INVISIBLE);
      mProgressDrawable.stop();
    }
  }

  public int px2dp(Context context, float pxValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);// + 0.5f是为了让结果四舍五入
  }
  public int dp2px(Context context, float dpValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  private void startRotateAnimation(final int start, final int end) {
    Animation rotate = new Animation() {
      @Override
      public void applyTransformation(float interpolatedTime, Transformation t) {
        mArrowView.setRotation(
          (int) (start + ((end - start) * interpolatedTime)));
      }
    };
    rotate.setDuration(500);
    mArrowView.clearAnimation();
    mArrowView.startAnimation(rotate);
  }
}
