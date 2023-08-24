import React, { useMemo } from 'react';
import {
  StyleSheet,
  requireNativeComponent,
  type ViewStyle,
} from 'react-native';

type DJRefresHeaderProps = {
  style: ViewStyle;
  children: React.ReactNode;
};

const RefreshHeader: React.FC = (props: any) => {
  const { children, style } = props;

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
    <DJRefreshHeader style={buildStyles.style}>{children}</DJRefreshHeader>
  );
};

const DJRefreshHeader =
  requireNativeComponent<DJRefresHeaderProps>('DJRefreshHeader');

const MemoRefreshHeader = React.memo<any>(RefreshHeader);

MemoRefreshHeader.displayName = 'DJRefreshHeader';

export default MemoRefreshHeader;
