package com.djrefreshlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DJRefreshDefaultHeader extends ClassicsHeader {

  public DJRefreshDefaultHeader(Context context) {
    super(context);
  }
  @NonNull
  @Override
  public View getView() {
    return this;//真实的视图就是自己，不能返回null
  }

  @NonNull
  @Override
  public SpinnerStyle getSpinnerStyle() {
    return SpinnerStyle.Translate;//指定为平移，不能null
  }

  public void setLocale(String locale) {
    Log.e("DJRefresh",locale);
    if(locale.contains("en")){
      mTextPulling = "Pull to refresh";//"下拉可以刷新";
      mTextRefreshing = "Refreshing";//"正在刷新...";
      mTextLoading = "Loading";//"正在加载...";
      mTextRelease = "Release to refresh";//"释放立即刷新";
      mTextFinish = "Refresh Complete";//"刷新完成";
      mTextFailed = "Failed";//"刷新失败";
      mTextUpdate = "'Last update' M-d HH:mm";//"上次更新 M-d HH:mm";
      mTextSecondary = "Release to refresh";//"释放进入二楼";
    }else {
      mTextPulling = "下拉可以刷新";
      mTextRefreshing = "正在刷新...";
      mTextLoading = "正在加载...";
      mTextRelease = "释放立即刷新";
      mTextFinish = "刷新完成";
      mTextFailed = "刷新失败";
      mTextUpdate = "上次更新 M-d HH:mm";
      mTextSecondary = "释放进入二楼";
    }
    setTimeFormat(new SimpleDateFormat(mTextUpdate, Locale.getDefault()));
//    mTitleText.setBackgroundColor(Color.parseColor("#FF0000"));
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleText.getLayoutParams();
    params.width = dp2px(this.getContext(),160);
    mTitleText.setLayoutParams(params);
    mTitleText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    mTitleText.setGravity(Gravity.CENTER);

    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) mLastUpdateText.getLayoutParams();
    params2.width = dp2px(this.getContext(),160);
    mLastUpdateText.setLayoutParams(params2);
    mLastUpdateText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    mLastUpdateText.setGravity(Gravity.CENTER);
  }
  @Override
  public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    super.onStateChanged(refreshLayout,oldState,newState);

    mTitleText.requestLayout();
  }

  public int px2dp(Context context, float pxValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);// + 0.5f是为了让结果四舍五入
  }
  public int dp2px(Context context, float dpValue) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

}
