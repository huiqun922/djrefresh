import React, { useCallback } from 'react';
import {
  DJRefreshDefaultHeader,
  DJRefreshLayout,
  type IRefreshProps,
} from 'react-native-djrefresh-library';

const DefaultRefreshHeader: React.FC<IRefreshProps> = (props) => {
  const { refreshing, onRefresh } = props;

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
      refreshHeader={<DJRefreshDefaultHeader />}
    />
  );
};

export default React.memo(DefaultRefreshHeader);
