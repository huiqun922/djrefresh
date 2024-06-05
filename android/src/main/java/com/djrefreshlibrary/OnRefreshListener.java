package com.djrefreshlibrary;


enum RefreshState {
    /**正常状态**/
    Normal,
    /**下拉中**/
    Pulling,
    /**正在刷新**/
    Refreshing,
    /**刷新完成**/
    Finish
}

public interface OnRefreshListener {
    void onRefresh(RefreshState state);
}
