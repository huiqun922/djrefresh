/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.djrefreshlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ListViewCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

/**
 * The DJSwipeRefreshLayout should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that
 * instantiates this view should add an OnRefreshListener to be notified
 * whenever the swipe to refresh gesture is completed. The DJSwipeRefreshLayout
 * will notify the listener each and every time the gesture is completed again;
 * the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should
 * not be a refresh, it must call setRefreshing(false) to cancel any visual
 * indication of a refresh. If an activity wishes to show just the progress
 * animation, it should call setRefreshing(true). To disable the gesture and
 * progress animation, call setEnabled(false) on the view.
 * <p>
 * This layout should be made the parent of the view that will be refreshed as a
 * result of the gesture and can only support one direct child. This view will
 * also be made the target of the gesture and will be forced to match both the
 * width and the height supplied in this layout. The DJSwipeRefreshLayout does not
 * provide accessibility events; instead, a menu item must be provided to allow
 * refresh of the content wherever this gesture is used.
 * </p>
 */
public class DJSwipeRefreshLayout extends ViewGroup implements NestedScrollingParent3,
        NestedScrollingParent2, NestedScrollingChild3, NestedScrollingChild2, NestedScrollingParent,
        NestedScrollingChild {
  private static final String LOG_TAG = DJSwipeRefreshLayout.class.getSimpleName();

  private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
  private static final int INVALID_POINTER = -1;
  private static final float DRAG_RATE = .5f;


  private static final int FINISH_DURATION = 150;

  private static final int ANIMATE_TO_TRIGGER_DURATION = 200;

  private static final int ANIMATE_TO_START_DURATION = 200;

  // Default offset in dips from the top of the view to where the progress spinner should stop
  private static final int DEFAULT_PULL_TARGET = 64;

  private View mTarget; // the target of the gesture
  OnRefreshListener mListener;
  private boolean mRefreshing = false;
  private int mTouchSlop;
  private float mTotalDragDistance = -1;

  protected int mFrom;

  // If nested scrolling is enabled, the total amount that needed to be
  // consumed by this as the nested scrolling parent is used in place of the
  // overscroll determined by MOVE events in the onTouch handler
  private float mTotalUnconsumed;
  private final NestedScrollingParentHelper mNestedScrollingParentHelper;
  private final NestedScrollingChildHelper mNestedScrollingChildHelper;
  private final int[] mParentScrollConsumed = new int[2];
  private final int[] mParentOffsetInWindow = new int[2];

  // Used for calls from old versions of onNestedScroll to v3 version of onNestedScroll. This only
  // exists to prevent GC costs that are present before API 21.
  private final int[] mNestedScrollingV2ConsumedCompat = new int[2];
  private boolean mNestedScrollInProgress;

  private int mMediumAnimationDuration;
  int mCurrentTargetOffsetTop;

  private float mInitialMotionY;
  private float mInitialDownY;
  private boolean mIsBeingDragged;
  private int mActivePointerId = INVALID_POINTER;

  // Target is returning to its start offset because it was cancelled or a
  // refresh was triggered.
  private boolean mReturningToStart;
  private final DecelerateInterpolator mDecelerateInterpolator;
  private static final int[] LAYOUT_ATTRS = new int[] {
          android.R.attr.enabled
  };
  DJSwipeRefreshHeader mHeader;
  private int mCircleViewIndex = -1;
  protected int mOriginalOffsetTop;

  private Animation mPullAnimation;

  private Animation mFinishAnimation;


  private OnChildScrollUpCallback mChildScrollUpCallback;

  /** @see #setLegacyRequestDisallowInterceptTouchEventEnabled */
  private boolean mEnableLegacyRequestDisallowInterceptTouch;

  private Animation.AnimationListener mPullListener = new Animation.AnimationListener() {
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    /**下拉动画结束，进入刷新状态**/
    @Override
    public void onAnimationEnd(Animation animation) {
      if (mRefreshing) {
        if (mListener != null) {
            mListener.onRefresh(RefreshState.Refreshing);
        }
        mHeader.onRefreshing();
        mCurrentTargetOffsetTop = mHeader.getBottom();
      } else {
        //reset();
        startFinishAnimation(null);
      }
    }
  };

  private Animation.AnimationListener mFinishListener = new Animation.AnimationListener() {
    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    /**下拉动画结束，进入刷新状态**/
    @Override
    public void onAnimationEnd(Animation animation) {

      reset();
      mHeader.onFinished();
      if (mListener != null) {
        mListener.onRefresh(RefreshState.Finish);
      }
    }
  };


  void reset() {
    mHeader.clearAnimation();
    // Return the circle to its start position
    setTargetOffsetTopAndBottom(mOriginalOffsetTop - mCurrentTargetOffsetTop);
    mCurrentTargetOffsetTop = mHeader.getBottom();
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    if (!enabled) {
      reset();
    }
  }

  static class SavedState extends View.BaseSavedState {
    final boolean mRefreshing;

    /**
     * Constructor called from {@link DJSwipeRefreshLayout#onSaveInstanceState()}
     */
    SavedState(Parcelable superState, boolean refreshing) {
      super(superState);
      this.mRefreshing = refreshing;
    }

    /**
     * Constructor called from {@link #CREATOR}
     */
    SavedState(Parcel in) {
      super(in);
      mRefreshing = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      super.writeToParcel(out, flags);
      out.writeByte(mRefreshing ? (byte) 1 : (byte) 0);
    }

    public static final Parcelable.Creator<SavedState> CREATOR =
            new Parcelable.Creator<SavedState>() {
              @Override
              public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
              }

              @Override
              public SavedState[] newArray(int size) {
                return new SavedState[size];
              }
            };
  }

  @Override
  protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    return new SavedState(superState, mRefreshing);
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    SavedState savedState = (SavedState) state;
    super.onRestoreInstanceState(savedState.getSuperState());
    setRefreshing(savedState.mRefreshing);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    reset();
  }

  /**
   * Simple constructor to use when creating a DJSwipeRefreshLayout from code.
   *
   * @param context
   */
  public DJSwipeRefreshLayout(@NonNull Context context) {
    this(context, null);
  }

  /**
   * Constructor that is called when inflating DJSwipeRefreshLayout from XML.
   *
   * @param context
   * @param attrs
   */
  public DJSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    mMediumAnimationDuration = getResources().getInteger(
            android.R.integer.config_mediumAnimTime);

    setWillNotDraw(false);
    mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

    final DisplayMetrics metrics = getResources().getDisplayMetrics();
    setChildrenDrawingOrderEnabled(true);

    //触发下拉刷新的距离
    mTotalDragDistance = (int) (DEFAULT_PULL_TARGET * metrics.density);
    mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

    mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
    setNestedScrollingEnabled(true);

    mOriginalOffsetTop = mCurrentTargetOffsetTop;
    moveToStart(1.0f);

    final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
    setEnabled(a.getBoolean(0, true));
    a.recycle();
  }

  @Override
  protected int getChildDrawingOrder(int childCount, int i) {
    if (mCircleViewIndex < 0) {
      return i;
    } else if (i == childCount - 1) {
      // Draw the selected child last
      return mCircleViewIndex;
    } else if (i >= mCircleViewIndex) {
      // Move the children after the selected child earlier one
      return i + 1;
    } else {
      // Keep the children before the selected child the same
      return i;
    }
  }

  private void createHeaderView() {
    mHeader = new DJRefreshDefaultHeader(getContext());
    addView(mHeader);
  }

  /**
   * Set the listener to be notified when a refresh is triggered via the swipe
   * gesture.
   */
  public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
    mListener = listener;
  }

  /**
   * Notify the widget that refresh state has changed. Do not call this when
   * refresh is triggered by a swipe gesture.
   *
   * @param refreshing Whether or not the view should show refresh progress.
   */
  public void setRefreshing(boolean refreshing) {
    if (refreshing && !mRefreshing) {
      mRefreshing = true;
      startPullDownAnimation(mPullListener);
    } else {
      setRefreshing(refreshing, false /* notify */);
    }
  }

  /**进入下拉的动画**/
  private void startPullDownAnimation(AnimationListener listener) {
    final int endOffset = (int) (mTotalDragDistance + mOriginalOffsetTop);
    final float[] offset = {0};
    mPullAnimation = new Animation() {
      @Override
      public void applyTransformation(float interpolatedTime, Transformation t) {
        //setAnimationProgress(interpolatedTime);
        offset[0] = interpolatedTime * endOffset;
        setTargetOffsetTopAndBottom((int) offset[0] - mCurrentTargetOffsetTop);

      }
    };
    mPullAnimation.setDuration(mMediumAnimationDuration);
    if (listener != null) {
      mHeader.setAnimationListener(listener);
    }
    mHeader.clearAnimation();
    mHeader.startAnimation(mPullAnimation);

    /**进入下拉状态**/
      if (mListener != null) {
        mListener.onRefresh(RefreshState.Pulling);
      }

    mHeader.onPulling();


  }

  private void setRefreshing(boolean refreshing, final boolean notify) {
    if (mRefreshing != refreshing) {
      ensureTarget();
      mRefreshing = refreshing;
      if (mRefreshing) {
        animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mPullListener);
      } else {
        startFinishAnimation(mFinishListener);
      }
    }
  }

  void startFinishAnimation(Animation.AnimationListener listener) {
    mFinishAnimation = new Animation() {
      @Override
      public void applyTransformation(float interpolatedTime, Transformation t) {
        //setAnimationProgress(1 - interpolatedTime);
        moveToStart(interpolatedTime);
      }
    };
    mFinishAnimation.setDuration(FINISH_DURATION);
    mHeader.setAnimationListener(listener);
    mHeader.clearAnimation();
    mHeader.startAnimation(mFinishAnimation);
  }

  /**
   * @return Whether the SwipeRefreshWidget is actively showing refresh
   *         progress.
   */
  public boolean isRefreshing() {
    return mRefreshing;
  }

  private void ensureTarget() {
    // Don't bother getting the parent height if the parent hasn't been laid
    // out yet.
    if (mTarget == null) {
      for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        if (child instanceof DJSwipeRefreshHeader) {
          mHeader = (DJSwipeRefreshHeader) child;
          continue;
        }else{
          mTarget = child;
          continue;
        }
      }
    }

    if(mHeader == null){
       createHeaderView();
    }
  }

  /**
   * Set the distance to trigger a sync in dips
   *
   * @param distance
   */
  public void setDistanceToTriggerSync(int distance) {
    mTotalDragDistance = distance;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    final int width = getMeasuredWidth();
    final int height = getMeasuredHeight();
    if (getChildCount() == 0) {
      return;
    }
    if (mTarget == null) {
      ensureTarget();
    }
    if (mTarget == null) {
      return;
    }
    final View child = mTarget;
    final int childLeft = getPaddingLeft();
    final int childTop = getPaddingTop();
    final int childWidth = width - getPaddingLeft() - getPaddingRight();
    final int childHeight = height - getPaddingTop() - getPaddingBottom();
    child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

    int headerTop = (int) (mCurrentTargetOffsetTop - mTotalDragDistance);
    mHeader.layout(childLeft, headerTop,
            childLeft + childWidth, (int) (headerTop + mTotalDragDistance));
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (mTarget == null) {
      ensureTarget();
    }
    if (mTarget == null) {
      return;
    }
    mTarget.measure(MeasureSpec.makeMeasureSpec(
            getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
            MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
            getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    mHeader.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec((int) mTotalDragDistance, MeasureSpec.EXACTLY));
  }

  /**
   * @return Whether it is possible for the child view of this layout to
   *         scroll up. Override this if the child view is a custom view.
   */
  public boolean canChildScrollUp() {
    if (mChildScrollUpCallback != null) {
      return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
    }
    if (mTarget instanceof ListView) {
      return ListViewCompat.canScrollList((ListView) mTarget, -1);
    }
    return mTarget.canScrollVertically(-1);
  }

  /**
   * Set a callback to override {@link DJSwipeRefreshLayout#canChildScrollUp()} method. Non-null
   * callback will return the value provided by the callback and ignore all internal logic.
   * @param callback Callback that should be called when canChildScrollUp() is called.
   */
  public void setOnChildScrollUpCallback(@Nullable OnChildScrollUpCallback callback) {
    mChildScrollUpCallback = callback;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    ensureTarget();

    final int action = ev.getActionMasked();
    int pointerIndex;

    if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
      mReturningToStart = false;
    }

    if (!isEnabled() || mReturningToStart || canChildScrollUp()
            || mRefreshing || mNestedScrollInProgress) {
      // Fail fast if we're not in a state where a swipe is possible
      return false;
    }

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        setTargetOffsetTopAndBottom(mOriginalOffsetTop);
        mActivePointerId = ev.getPointerId(0);
        mIsBeingDragged = false;

        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          return false;
        }
        mInitialDownY = ev.getY(pointerIndex);
        break;

      case MotionEvent.ACTION_MOVE:
        if (mActivePointerId == INVALID_POINTER) {
          Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
          return false;
        }

        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          return false;
        }
        final float y = ev.getY(pointerIndex);
        startDragging(y);
        break;

      case MotionEvent.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        mIsBeingDragged = false;
        mActivePointerId = INVALID_POINTER;
        break;
    }

    return mIsBeingDragged;
  }

  /**
   * Enables the legacy behavior of {@link #requestDisallowInterceptTouchEvent} from before
   * 1.1.0-alpha03, where the request is not propagated up to its parents in either of the
   * following two cases:
   * <ul>
   *     <li>The child as an {@link AbsListView} and the runtime is API < 21</li>
   *     <li>The child has nested scrolling disabled</li>
   * </ul>
   * Use this method <em>only</em> if your application:
   * <ul>
   *     <li>is upgrading DJSwipeRefreshLayout from &lt; 1.1.0-alpha03 to &gt;= 1.1.0-alpha03</li>
   *     <li>relies on a parent of DJSwipeRefreshLayout to intercept touch events and that
   *     parent no longer responds to touch events</li>
   *     <li>setting this method to {@code true} fixes that issue</li>
   * </ul>
   *
   * @param enabled {@code true} to enable the legacy behavior, {@code false} for default behavior
   * @deprecated Only use this method if the changes introduced in
   *             {@link #requestDisallowInterceptTouchEvent} in version 1.1.0-alpha03 are breaking
   *             your application.
   */
  @Deprecated
  public void setLegacyRequestDisallowInterceptTouchEventEnabled(boolean enabled) {
    mEnableLegacyRequestDisallowInterceptTouch = enabled;
  }

  @Override
  public void requestDisallowInterceptTouchEvent(boolean b) {
    // if this is a List < L or another view that doesn't support nested
    // scrolling, ignore this request so that the vertical scroll event
    // isn't stolen
    if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
            || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))) {
      if (mEnableLegacyRequestDisallowInterceptTouch) {
        // Nope.
      } else {
        // Ignore here, but pass it up to our parent
        ViewParent parent = getParent();
        if (parent != null) {
          parent.requestDisallowInterceptTouchEvent(b);
        }
      }
    } else {
      super.requestDisallowInterceptTouchEvent(b);
    }
  }

  // NestedScrollingParent 3

  @Override
  public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                             int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type,
                             @NonNull int[] consumed) {
    if (type != ViewCompat.TYPE_TOUCH) {
      return;
    }

    // This is a bit of a hack. onNestedScroll is typically called up the hierarchy of nested
    // scrolling parents/children, where each consumes distances before passing the remainder
    // to parents.  In our case, we want to try to run after children, and after parents, so we
    // first pass scroll distances to parents and consume after everything else has.
    int consumedBeforeParents = consumed[1];
    dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            mParentOffsetInWindow, type, consumed);
    int consumedByParents = consumed[1] - consumedBeforeParents;
    int unconsumedAfterParents = dyUnconsumed - consumedByParents;

    // There are two reasons why scroll distance may be totally consumed.  1) All of the nested
    // scrolling parents up the hierarchy implement NestedScrolling3 and consumed all of the
    // distance or 2) at least 1 nested scrolling parent doesn't implement NestedScrolling3 and
    // for comparability reasons, we are supposed to act like they have.
    //
    // We must assume 2) is the case because we have no way of determining that it isn't, and
    // therefore must fallback to a previous hack that was done before nested scrolling 3
    // existed.
    int remainingDistanceToScroll;
    if (unconsumedAfterParents == 0) {
      // The previously implemented hack is to see how far we were offset and assume that that
      // distance is equal to how much all of our parents consumed.
      remainingDistanceToScroll = dyUnconsumed + mParentOffsetInWindow[1];
    } else {
      remainingDistanceToScroll = unconsumedAfterParents;
    }

    // Not sure why we have to make sure the child can't scroll up... but seems dangerous to
    // remove.
    if (remainingDistanceToScroll < 0 && !canChildScrollUp()) {
      mTotalUnconsumed += Math.abs(remainingDistanceToScroll);
      moveSpinner(mTotalUnconsumed);

      // If we've gotten here, we need to consume whatever is left to consume, which at this
      // point is either equal to 0, or remainingDistanceToScroll.
      consumed[1] += unconsumedAfterParents;
    }
  }

  // NestedScrollingParent 2

  @Override
  public boolean onStartNestedScroll(View child, View target, int axes, int type) {
    if (type == ViewCompat.TYPE_TOUCH) {
      return onStartNestedScroll(child, target, axes);
    } else {
      return false;
    }
  }

  @Override
  public void onNestedScrollAccepted(View child, View target, int axes, int type) {
    // Should always be true because onStartNestedScroll returns false for all type !=
    // ViewCompat.TYPE_TOUCH, but check just in case.
    if (type == ViewCompat.TYPE_TOUCH) {
      onNestedScrollAccepted(child, target, axes);
    }
  }

  @Override
  public void onStopNestedScroll(View target, int type) {
    // Should always be true because onStartNestedScroll returns false for all type !=
    // ViewCompat.TYPE_TOUCH, but check just in case.
    if (type == ViewCompat.TYPE_TOUCH) {
      onStopNestedScroll(target);
    }
  }

  @Override
  public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                             int dyUnconsumed, int type) {
    onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type,
            mNestedScrollingV2ConsumedCompat);
  }

  @Override
  public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
    // Should always be true because onStartNestedScroll returns false for all type !=
    // ViewCompat.TYPE_TOUCH, but check just in case.
    if (type == ViewCompat.TYPE_TOUCH) {
      onNestedPreScroll(target, dx, dy, consumed);
    }
  }

  // NestedScrollingParent 1

  @Override
  public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
    return isEnabled() && !mReturningToStart && !mRefreshing
            && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
  }

  @Override
  public void onNestedScrollAccepted(View child, View target, int axes) {
    // Reset the counter of how much leftover scroll needs to be consumed.
    mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    // Dispatch up to the nested parent
    startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
    mTotalUnconsumed = 0;
    mNestedScrollInProgress = true;
  }

  @Override
  public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
    // before allowing the list to scroll
    if (dy > 0 && mTotalUnconsumed > 0) {
      if (dy > mTotalUnconsumed) {
        consumed[1] = (int) mTotalUnconsumed;
        mTotalUnconsumed = 0;
      } else {
        mTotalUnconsumed -= dy;
        consumed[1] = dy;
      }
      moveSpinner(mTotalUnconsumed);
    }

    // Now let our nested parent consume the leftovers
    final int[] parentConsumed = mParentScrollConsumed;
    if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
      consumed[0] += parentConsumed[0];
      consumed[1] += parentConsumed[1];
    }
  }

  @Override
  public int getNestedScrollAxes() {
    return mNestedScrollingParentHelper.getNestedScrollAxes();
  }

  @Override
  public void onStopNestedScroll(View target) {
    mNestedScrollingParentHelper.onStopNestedScroll(target);
    mNestedScrollInProgress = false;
    // Finish the spinner for nested scrolling if we ever consumed any
    // unconsumed nested scroll
    if (mTotalUnconsumed > 0) {
      finishSpinner(mTotalUnconsumed);
      mTotalUnconsumed = 0;
    }
    // Dispatch up our nested parent
    stopNestedScroll();
  }

  @Override
  public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                             final int dxUnconsumed, final int dyUnconsumed) {
    onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            ViewCompat.TYPE_TOUCH, mNestedScrollingV2ConsumedCompat);
  }

  @Override
  public boolean onNestedPreFling(View target, float velocityX,
                                  float velocityY) {
    return dispatchNestedPreFling(velocityX, velocityY);
  }

  @Override
  public boolean onNestedFling(View target, float velocityX, float velocityY,
                               boolean consumed) {
    return dispatchNestedFling(velocityX, velocityY, consumed);
  }

  // NestedScrollingChild 3

  @Override
  public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                   int dyUnconsumed, @Nullable int[] offsetInWindow, @ViewCompat.NestedScrollType int type,
                                   @NonNull int[] consumed) {
    if (type == ViewCompat.TYPE_TOUCH) {
      mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
              dyUnconsumed, offsetInWindow, type, consumed);
    }
  }

  // NestedScrollingChild 2

  @Override
  public boolean startNestedScroll(int axes, int type) {
    return type == ViewCompat.TYPE_TOUCH && startNestedScroll(axes);
  }

  @Override
  public void stopNestedScroll(int type) {
    if (type == ViewCompat.TYPE_TOUCH) {
      stopNestedScroll();
    }
  }

  @Override
  public boolean hasNestedScrollingParent(int type) {
    return type == ViewCompat.TYPE_TOUCH && hasNestedScrollingParent();
  }

  @Override
  public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                      int dyUnconsumed, int[] offsetInWindow, int type) {
    return type == ViewCompat.TYPE_TOUCH && mNestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
  }

  @Override
  public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow,
                                         int type) {
    return type == ViewCompat.TYPE_TOUCH && dispatchNestedPreScroll(dx, dy, consumed,
            offsetInWindow);
  }

  // NestedScrollingChild 1

  @Override
  public void setNestedScrollingEnabled(boolean enabled) {
    mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
  }

  @Override
  public boolean isNestedScrollingEnabled() {
    return mNestedScrollingChildHelper.isNestedScrollingEnabled();
  }

  @Override
  public boolean startNestedScroll(int axes) {
    return mNestedScrollingChildHelper.startNestedScroll(axes);
  }

  @Override
  public void stopNestedScroll() {
    mNestedScrollingChildHelper.stopNestedScroll();
  }

  @Override
  public boolean hasNestedScrollingParent() {
    return mNestedScrollingChildHelper.hasNestedScrollingParent();
  }

  @Override
  public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                      int dyUnconsumed, int[] offsetInWindow) {
    return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
    return mNestedScrollingChildHelper.dispatchNestedPreScroll(
            dx, dy, consumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
    return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
  }

  @Override
  public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
    return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
  }

  private boolean isAnimationRunning(Animation animation) {
    return animation != null && animation.hasStarted() && !animation.hasEnded();
  }

  private void moveSpinner(float overscrollTop) {
    float originalDragPercent = overscrollTop / mTotalDragDistance;

    float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
//    float adjustedPercent = (float) Math.max(dragPercent - .4, 0) * 5 / 3;
    float extraOS = Math.abs(overscrollTop) - mTotalDragDistance;
    float slingshotDist = mTotalDragDistance;
    float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2)
            / slingshotDist);
    float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
            (tensionSlingshotPercent / 4), 2)) * 2f;
    float extraMove = slingshotDist * tensionPercent * 2;

    int targetY = mOriginalOffsetTop + (int) ((slingshotDist * dragPercent) + extraMove);

    mHeader.moveSpinner(overscrollTop,mTotalDragDistance);

    setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop);
  }

/**松手**/
  private void finishSpinner(float overscrollTop) {
    if (overscrollTop > mTotalDragDistance) {
      setRefreshing(true, true /* notify */);
    } else {
      // 没有滑动到足够距离，取消刷新
      mRefreshing = false;
      Animation.AnimationListener listener = new Animation.AnimationListener() {

          @Override
          public void onAnimationStart(Animation animation) {
          }

          @Override
          public void onAnimationEnd(Animation animation) {

            if( mListener!=null){
              mListener.onRefresh(RefreshState.Normal);
            }
          }
          @Override
          public void onAnimationRepeat(Animation animation) {
          }

        };
      animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
    }
    mHeader.finishSpinner(overscrollTop,mTotalDragDistance);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    final int action = ev.getActionMasked();
    int pointerIndex = -1;

    if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
      mReturningToStart = false;
    }

    if (!isEnabled() || mReturningToStart || canChildScrollUp()
            || mRefreshing || mNestedScrollInProgress) {
      // Fail fast if we're not in a state where a swipe is possible
      return false;
    }

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mActivePointerId = ev.getPointerId(0);
        mIsBeingDragged = false;
        break;

      case MotionEvent.ACTION_MOVE: {
        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
          return false;
        }

        final float y = ev.getY(pointerIndex);
        startDragging(y);

        if (mIsBeingDragged) {
          final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
          if (overscrollTop > 0) {
            // While the spinner is being dragged down, our parent shouldn't try
            // to intercept touch events. It will stop the drag gesture abruptly.
            getParent().requestDisallowInterceptTouchEvent(true);
            moveSpinner(overscrollTop);
          } else {
            return false;
          }
        }
        break;
      }
      case MotionEvent.ACTION_POINTER_DOWN: {
        pointerIndex = ev.getActionIndex();
        if (pointerIndex < 0) {
          Log.e(LOG_TAG,
                  "Got ACTION_POINTER_DOWN event but have an invalid action index.");
          return false;
        }
        mActivePointerId = ev.getPointerId(pointerIndex);
        break;
      }

      case MotionEvent.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;

      case MotionEvent.ACTION_UP: {
        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
          return false;
        }

        if (mIsBeingDragged) {
          final float y = ev.getY(pointerIndex);
          final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
          mIsBeingDragged = false;
          finishSpinner(overscrollTop);
        }
        mActivePointerId = INVALID_POINTER;
        return false;
      }
      case MotionEvent.ACTION_CANCEL:
        return false;
    }

    return true;
  }

  private void startDragging(float y) {
    final float yDiff = y - mInitialDownY;
    if (yDiff > mTouchSlop && !mIsBeingDragged) {
      mInitialMotionY = mInitialDownY + mTouchSlop;
      mIsBeingDragged = true;
    }
  }

  private void animateOffsetToCorrectPosition(int from, AnimationListener listener) {
    mFrom = from;
    mAnimateToCorrectPosition.reset();
    mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
    mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
    if (listener != null) {
      mHeader.setAnimationListener(listener);
    }
    mHeader.clearAnimation();
    mHeader.startAnimation(mAnimateToCorrectPosition);
  }

  private void animateOffsetToStartPosition(int from, AnimationListener listener) {
    mFrom = from;
    mAnimateToStartPosition.reset();
    mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
    mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
    if (listener != null) {
      mHeader.setAnimationListener(listener);
    }
    mHeader.clearAnimation();
    mHeader.startAnimation(mAnimateToStartPosition);

  }

  private final Animation mAnimateToCorrectPosition = new Animation() {
    @Override
    public void applyTransformation(float interpolatedTime, Transformation t) {
      int endTarget = (int) (mTotalDragDistance - Math.abs(mOriginalOffsetTop));

      int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
      int offset = targetTop - mHeader.getBottom();
      setTargetOffsetTopAndBottom(offset);
    }
  };

  void moveToStart(float interpolatedTime) {
    if (mHeader == null || mTarget == null) {
      return;
    }
    int targetTop = (mCurrentTargetOffsetTop + (int) ((mOriginalOffsetTop - mCurrentTargetOffsetTop) * interpolatedTime));
    int offset = targetTop - mHeader.getBottom();
    setTargetOffsetTopAndBottom(offset);
  }

  private final Animation mAnimateToStartPosition = new Animation() {
    @Override
    public void applyTransformation(float interpolatedTime, Transformation t) {
      moveToStart(interpolatedTime);
    }
  };

  void setTargetOffsetTopAndBottom(int offset) {
    if(mHeader !=null) {
      ViewCompat.offsetTopAndBottom(mHeader, offset);
    }
    if(mTarget!=null) {
      ViewCompat.offsetTopAndBottom(mTarget, offset);
    }
    mCurrentTargetOffsetTop = mHeader.getBottom();
  }

  private void onSecondaryPointerUp(MotionEvent ev) {
    final int pointerIndex = ev.getActionIndex();
    final int pointerId = ev.getPointerId(pointerIndex);
    if (pointerId == mActivePointerId) {
      // This was our active pointer going up. Choose a new
      // active pointer and adjust accordingly.
      final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
      mActivePointerId = ev.getPointerId(newPointerIndex);
    }
  }

  /**
   * Classes that wish to override {@link DJSwipeRefreshLayout#canChildScrollUp()} method
   * behavior should implement this interface.
   */
  public interface OnChildScrollUpCallback {
    /**
     * Callback that will be called when {@link DJSwipeRefreshLayout#canChildScrollUp()} method
     * is called to allow the implementer to override its behavior.
     *
     * @param parent DJSwipeRefreshLayout that this callback is overriding.
     * @param child The child view of DJSwipeRefreshLayout.
     *
     * @return Whether it is possible for the child view of parent layout to scroll up.
     */
    boolean canChildScrollUp(@NonNull DJSwipeRefreshLayout parent, @Nullable View child);
  }
}
