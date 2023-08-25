import React, { useCallback, useRef } from 'react';
import { StyleSheet, requireNativeComponent } from 'react-native';
import type { DJRefreshNativeProps, IRefreshProps } from './IRefreshProps';
import DJRefreshState from './DJRefreshState';

const DJNativeRefreshHeader =
  requireNativeComponent<DJRefreshNativeProps>('DJRefreshHeader');

const RefreshHeader: React.FC<IRefreshProps> = (props) => {
  const {
    refreshHeader,
    refreshing,
    onPullingRefresh,
    onRefresh,
    onEndRefresh,
    onIdleRefresh,
    onChangeOffset,
    style,
  } = props;

  const currentState = useRef(1);
  const offsetRef = useRef(0);

  const onChangeState = useCallback(
    (event) => {
      console.log('ios 刷新状态发生改变: ', event.nativeEvent, onRefresh);
      const { state } = event.nativeEvent;
      if (currentState.current !== state) {
        currentState.current = state;
        if (state === 1) {
          onIdleRefresh && onIdleRefresh(DJRefreshState.Idle);
        } else if (state === 2) {
          onPullingRefresh && onPullingRefresh(DJRefreshState.Pulling);
        } else if (state === 3) {
          onRefresh && onRefresh(DJRefreshState.Refreshing);
        } else if (state === 4) {
          onEndRefresh && onEndRefresh(DJRefreshState.End);
        }
      }
    },
    [onEndRefresh, onIdleRefresh, onPullingRefresh, onRefresh]
  );

  const offsetCallback = useCallback(
    (event) => {
      const { offset } = event.nativeEvent;
      offsetRef.current = offset;
      onChangeOffset && onChangeOffset(event);
    },
    [onChangeOffset]
  );

  console.log('refreshing ios', style?.height);

  return (
    <DJNativeRefreshHeader
      style={styles.positionStyle}
      refreshing={refreshing}
      onChangeOffset={offsetCallback}
      onChangeState={onChangeState}
    >
      {refreshHeader}
    </DJNativeRefreshHeader>
  );
};

const styles = StyleSheet.create({
  positionStyle: {
    position: 'absolute',
    left: 0,
    right: 0,
  },
});

const DJRefreshHeader = React.memo<IRefreshProps>(RefreshHeader);

DJRefreshHeader.displayName = 'DJRefreshHeader';

export default DJRefreshHeader;
