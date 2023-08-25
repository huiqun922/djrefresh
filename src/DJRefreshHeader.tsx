/* eslint-disable react-native/no-inline-styles */
import React from 'react';
import { View, Text } from 'react-native';
import type { IRefreshProps } from './IRefreshProps';

const RefreshHeader: React.FC = () => {
  return (
    <View style={{ alignSelf: 'flex-start' }}>
      <Text style={{ color: 'red' }}>
        React Native Refresh does not support this platform.
      </Text>
    </View>
  );
};

const MemoRefreshHeader = React.memo<IRefreshProps>(RefreshHeader);
export default MemoRefreshHeader;
