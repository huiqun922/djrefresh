import React, { useCallback, useMemo } from 'react';
import {
  StyleSheet,
  requireNativeComponent,
  type ViewStyle,
} from 'react-native';
import DJRefreshLayout from './DJRefreshLayout';
import type { IRefreshProps } from './IRefreshProps';

type DJRefresHeaderProps = {
  style: ViewStyle;
  children: React.ReactNode;
};

const RefreshHeader: React.FC<IRefreshProps> = (props) => {
  const { refreshing, onRefresh, children, style, refreshHeader } = props;

  const onPullingRefreshCallBack = useCallback(() => {}, []);

  const onEndRefreshCallBack = useCallback(() => {}, []);

  const onIdleRefreshCallBack = useCallback(() => {}, []);

  const buildStyles = useMemo(() => {
    const flattenStyle = StyleSheet.flatten(style ? style : {});
    if (!flattenStyle.height) {
      console.warn('style中必须设置固定高度');
    }
    return {
      style: flattenStyle,
    };
  }, [style]);

  return (
    <DJRefreshLayout
      enable={true}
      refreshing={refreshing}
      onPullingRefresh={onPullingRefreshCallBack}
      onRefresh={onRefresh}
      onEndRefresh={onEndRefreshCallBack}
      onIdleRefresh={onIdleRefreshCallBack}
      refreshHeader={
        <DJNativeRefreshHeader style={buildStyles.style}>
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
