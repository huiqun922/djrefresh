import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
} from 'react-native';

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

export const DjrefreshLibraryView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<DjrefreshLibraryProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
