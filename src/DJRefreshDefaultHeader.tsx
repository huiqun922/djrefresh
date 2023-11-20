/* eslint-disable react-native/no-inline-styles */
import { Text, View } from 'react-native';
import type { IRefreshProps } from './IRefreshProps';
import React from 'react';

const DJRefreshDefaultHeader: React.FC<IRefreshProps> = () => {
  return (
    <View style={{ alignSelf: 'flex-start' }}>
      <Text style={{ color: 'red' }}>
        React Native Refresh does not support this platform.
      </Text>
    </View>
  );
};

export default DJRefreshDefaultHeader;
