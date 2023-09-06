import React, { useCallback, type PropsWithChildren } from 'react';
import { requireNativeComponent, type ViewStyle } from 'react-native';
import DJRefreshLayout from './DJRefreshLayout';
import type { IRefreshProps } from './IRefreshProps';

type DJRefresHeaderProps = {
  style: ViewStyle;
  children: React.ReactNode;
};

const RefreshHeader = (props: PropsWithChildren<IRefreshProps>) => {
  const { refreshing, onRefresh, children, headerStyle, refreshHeader } = props;

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
      refreshHeader={
        <DJNativeRefreshHeader style={headerStyle ?? {}}>
          {refreshHeader}
        </DJNativeRefreshHeader>
      }
    >
      {children}
    </DJRefreshLayout>
  );
};

const DJNativeRefreshHeader =
  requireNativeComponent<DJRefresHeaderProps>('DJRefreshHeader');

const DJRefreshHeader = React.memo<IRefreshProps>(RefreshHeader);

DJRefreshHeader.displayName = 'DJRefreshHeader';

export default DJRefreshHeader;
