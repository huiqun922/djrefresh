import React from 'react';
import { View } from 'react-native';
import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
} from 'react-native';
import DJRefreshState from './DJRefreshState';
import type { IRefreshProps } from './IRefreshProps';
import DJRefreshHeader from './DJRefreshHeader';
import DJRefreshDefaultHeader from './DJRefreshDefaultHeader';
import DJRefreshLayout from './DJRefreshLayout';

const LINKING_ERROR =
  `The package 'react-native-djrefresh-library' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

type DjrefreshLibraryProps = {
  color: string;
  style: ViewStyle;
};

const ComponentName = 'DjrefreshLibraryView';

const DjrefreshLibraryView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<DjrefreshLibraryProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

export const DJRefreshView = (props: DjrefreshLibraryProps) => {
  return (
    <View>
      <DjrefreshLibraryView {...props} />
    </View>
  );
};

export type { IRefreshProps, DJRefreshState };
export { DJRefreshLayout, DJRefreshDefaultHeader, DJRefreshHeader };
