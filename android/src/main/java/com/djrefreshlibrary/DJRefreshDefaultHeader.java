package com.djrefreshlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
      mTextFinish = "Release Complete";//"刷新完成";
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
  }

}
