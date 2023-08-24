import React, {
  useCallback,
  useMemo,
  useRef,
  type PropsWithChildren,
} from 'react';
import {
  PanResponder,
  requireNativeComponent,
  View,
  StyleSheet,
  type ViewStyle,
} from 'react-native';
import DJRefreshState from './DJRefreshState';
import type { IRefreshProps } from './IRefreshProps';

type DJRefreshLayoutProps = {
  style: ViewStyle;
  enable: boolean | undefined;
  refreshing: boolean | undefined;
  onChangeOffset: Function;
  onChangeState: Function;
  headerHeight: number;
  refreshHeader?: React.ReactElement;
  children: React.ReactNode;
};

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
    (event) => {
      console.log('刷新状态发生改变: ', event.nativeEvent, onRefresh);
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

  //获取子组件中的高度
  //在其依赖的变量发生改变时执行
  const build = useMemo(() => {
    let height = 0;
    const newChildren = React.Children.map(children, (element) => {
      if (!React.isValidElement(element) || !enable) {
        return element;
      }
      // const flattenStyle = StyleSheet.flatten(
      //   element.props && element.props.style ? element.props : {}
      // );
      height = element.props?.style?.height;
      return element;
    });
    return {
      children: newChildren,
      headerHeight: height,
    };
  }, [children, enable]);

  console.log('子组件类型6: ', children);
  console.log('子组件类型7: ', refreshHeader);

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
        headerHeight={build.headerHeight}
      >
        {build.children}
        {refreshHeader}
      </DJNativeRefreshLayout>
    </View>
  );
};

const styles = StyleSheet.create({
  layoutStyle: {
    flex: 1,
    //overflow: 'hidden',
  },
});

const DJNativeRefreshLayout =
  requireNativeComponent<DJRefreshLayoutProps>('DJRefreshLayout');

const MemoRefreshLayout = React.memo(RefreshLayout);

const DJRefreshLayout = React.forwardRef((props: any, ref) => (
  <MemoRefreshLayout forwardedRef={ref} {...props} />
));

DJRefreshLayout.displayName = 'DJRefreshLayout';

export default DJRefreshLayout;
