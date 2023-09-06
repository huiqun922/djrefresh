import { StyleSheet, requireNativeComponent } from 'react-native';
import type { DJRefreshNativeProps, IRefreshProps } from './IRefreshProps';
import { useCallback, useRef, type PropsWithChildren } from 'react';
import React from 'react';
import DJRefreshState from './DJRefreshState';

const DJNativeRefreshDefaultHeader =
  requireNativeComponent<DJRefreshNativeProps>('DJRefreshDefaultHeader');

const DJRefreshDefaultHeader = (props: PropsWithChildren<IRefreshProps>) => {
  const {
    refreshing,
    onPullingRefresh,
    onRefresh,
    onEndRefresh,
    onIdleRefresh,
    onChangeOffset,
  } = props;

  const currentState = useRef(1);
  const offsetRef = useRef(0);

  const onChangeState = useCallback(
    (event) => {
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

  return (
    <DJNativeRefreshDefaultHeader
      refreshing={refreshing}
      onChangeOffset={offsetCallback}
      onChangeState={onChangeState}
      style={styles.positionStyle}
    />
  );
};

const styles = StyleSheet.create({
  positionStyle: {
    position: 'absolute',
    left: 0,
    right: 0,
    height: 50,
  },
});

export default React.memo(DJRefreshDefaultHeader);
