package com.djrefreshlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.views.view.ReactViewGroup;

public class DJRefreshHeader extends DJSwipeRefreshHeader  {

  private View mChildren;
  public DJRefreshHeader(Context context) {
    super(context);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (getChildCount() == 0) {
      return;
    }
    if (mChildren == null) {
      ensureChildren();
    }
    if (mChildren == null) {
      return;
    }
    final int width = getMeasuredWidth();
    final int height = getMeasuredHeight();

    final int childLeft = getPaddingLeft();
    final int childTop = getPaddingTop();
    final int childWidth = width - getPaddingLeft() - getPaddingRight();
    final int childHeight = height - getPaddingTop() - getPaddingBottom();
    mChildren.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (getChildCount() == 0) {
      return;
    }
    if (mChildren == null) {
      ensureChildren();
    }
    if (mChildren == null) {
      return;
    }
    mChildren.measure(MeasureSpec.makeMeasureSpec(
      getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
      MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
      getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
  }

  @Override
  public void requestLayout() {
    super.requestLayout();
  }

  private void ensureChildren(){
    if (mChildren == null) {
      mChildren = getChildAt(0);
    }
  }
}
