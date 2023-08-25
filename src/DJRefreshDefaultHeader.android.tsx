import { requireNativeComponent } from 'react-native';
import { useCallback } from 'react';
import React from 'react';
import DJRefreshLayout from './DJRefreshLayout';
import type { IRefreshProps } from './IRefreshProps';

const DJNativeRefreshDefaultHeader = requireNativeComponent<any>(
  'DJRefreshDefaultHeader'
);

const DJRefreshDefaultHeader: React.FC<IRefreshProps> = (props) => {
  const { refreshing, onRefresh, children } = props;

  const onPullingRefreshCallBack = useCallback(() => {}, []);

  const onEndRefreshCallBack = useCallback(() => {}, []);

  const onIdleRefreshCallBack = useCallback(() => {}, []);

  console.log('refreshing', refreshing);

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
