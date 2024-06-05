import React, { useCallback, useRef, type PropsWithChildren } from 'react';
import {
  PanResponder,
  requireNativeComponent,
  View,
  StyleSheet,
} from 'react-native';
import DJRefreshState from './DJRefreshState';
import type { DJRefreshNativeProps, IRefreshProps } from './IRefreshProps';

const DJNativeRefreshLayout =
  requireNativeComponent<DJRefreshNativeProps>('DJRefreshLayout');

const RefreshLayout = (props: PropsWithChildren<IRefreshProps>) => {
  const {
    children,
    refreshHeader,
    refreshing,
    enable,
    onPullingRefresh,
    onRefresh,
    onEndRefresh,
    onIdleRefresh,
    onChangeOffset,
    forwardedRef,
  } = props;

  const currentState = useRef(1);
  const offsetRef = useRef(0);
  const panResponderRef = useRef(
    PanResponder.create({
      onMoveShouldSetPanResponderCapture: () => {
        if (offsetRef.current >= 2) {
          //满足条件捕获事件
          return true;
        }
        return false;
      },
    })
  );

  const onChangeState = useCallback(
    (event: any) => {
      // console.log('刷新状态发生改变: ', event.nativeEvent, onRefresh);
      const { state } = event.nativeEvent;
      if (currentState.current !== state) {
        currentState.current = state;
        if (state === 0) {
          onIdleRefresh && onIdleRefresh(DJRefreshState.Idle);
        } else if (state === 1) {
          onPullingRefresh && onPullingRefresh(DJRefreshState.Pulling);
        } else if (state === 2) {
          onRefresh && onRefresh(DJRefreshState.Refreshing);
        } else if (state === 3) {
          onEndRefresh && onEndRefresh(DJRefreshState.End);
        }
      }
    },
    [onEndRefresh, onIdleRefresh, onPullingRefresh, onRefresh]
  );

  const offsetCallback = useCallback(
    (event: any) => {
      const { offset } = event.nativeEvent;
      offsetRef.current = offset;
      onChangeOffset && onChangeOffset(event);
    },
    [onChangeOffset]
  );
  const headerHeight =
    refreshHeader?.props?.children?.props?.style?.height ?? 0;
  return (
    <View style={styles.layoutStyle}>
      <DJNativeRefreshLayout
        {...panResponderRef.current.panHandlers}
        ref={forwardedRef}
        style={styles.layoutStyle}
        enable={enable}
        refreshing={refreshing}
        onChangeOffset={offsetCallback}
        onChangeState={onChangeState}
        headerHeight={headerHeight}
      >
        {refreshHeader}
        {children}
      </DJNativeRefreshLayout>
    </View>
  );
};

const styles = StyleSheet.create({
  layoutStyle: {
    flex: 1,
    overflow: 'hidden',
  },
});

const DJRefreshLayout = React.forwardRef((props: any, ref) => (
  <RefreshLayout forwardedRef={ref} {...props} />
));

DJRefreshLayout.displayName = 'DJRefreshLayout';

export default DJRefreshLayout;
