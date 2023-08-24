export interface IRefreshProps {
  refreshing?: boolean;
  enable?: boolean;
  onRefresh?: Function; // 刷新中
  onPullingRefresh?: Function; // 松开就可以进行刷新
  onEndRefresh?: Function; // 刷新结束, 但是动画还未结束
  onIdleRefresh?: Function; // 闲置状态或者刷新完全结束
  onChangeOffset?: Function;
  refreshHeader?: React.ReactElement;
  forwardedRef?: any;
}
