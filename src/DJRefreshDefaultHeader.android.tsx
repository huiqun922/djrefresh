import { requireNativeComponent } from 'react-native';
import { useCallback, type PropsWithChildren } from 'react';
import React from 'react';
import DJRefreshLayout from './DJRefreshLayout';
import type { IRefreshProps } from './IRefreshProps';

const DJNativeRefreshDefaultHeader = requireNativeComponent<any>(
  'DJRefreshDefaultHeader'
);

const DJRefreshDefaultHeader = (props: PropsWithChildren<IRefreshProps>) => {
  const { refreshing, onRefresh, children } = props;

  const onPullingRefreshCallBack = useCallback(() => {}, []);

  const onEndRefreshCallBack = useCallback(() => {}, []);

  const onIdleRefreshCallBack = useCallback(() => {}, []);

  return (
    <DJRefreshLayout
      enable={true}
      refreshing={refreshing}
      onPullingRefresh={onPullingRefreshCallBack}
      onRefresh={onRefresh}
      onEndRefresh={onEndRefreshCallBack}
      onIdleRefresh={onIdleRefreshCallBack}
      refreshHeader={<DJNativeRefreshDefaultHeader />}
    >
      {children}
    </DJRefreshLayout>
  );
};

export default React.memo(DJRefreshDefaultHeader);
