import React, { useCallback, useRef, type PropsWithChildren } from 'react';
import {
  StyleSheet,
  requireNativeComponent,
  type NativeSyntheticEvent,
} from 'react-native';
import type { DJRefreshNativeProps, IRefreshProps } from './IRefreshProps';
import DJRefreshState from './DJRefreshState';

const DJNativeRefreshHeader =
  requireNativeComponent<DJRefreshNativeProps>('DJRefreshHeader');

type RefreshState = {
  state: number;
};

const RefreshHeader = (props: PropsWithChildren<IRefreshProps>) => {
  const {
    refreshHeader,
    refreshing,
    onPullingRefresh,
    onRefresh,
    onEndRefresh,
    onIdleRefresh,
  } = props;

  const currentState = useRef(1);

  const onChangeState = useCallback(
    (event: NativeSyntheticEvent<RefreshState>) => {
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

  return (
    <DJNativeRefreshHeader
      style={styles.positionStyle}
      refreshing={refreshing}
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
