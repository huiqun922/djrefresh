/* eslint-disable react/no-unstable-nested-components */
import {
  View,
  Animated,
  StyleSheet,
  Text,
  Alert,
  FlatList,
} from 'react-native';
import React, { useCallback, useEffect, useState } from 'react';
import { RectButton } from 'react-native-gesture-handler';
import Swipeable from 'react-native-gesture-handler/Swipeable';
import { DJRefreshDefaultHeader } from 'react-native-djrefresh-library';

const SwipeableList = () => {
  const DATAS = [
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
    ...DATA,
  ];

  const [refreshing, setRefreshing] = useState(false);

  const refreshAction = useCallback(() => {
    console.log('开始刷新11');
    setRefreshing(true);
    setTimeout(() => {
      console.log('结束刷新11');
      setRefreshing(false);
    }, 3000);
  }, []);

  useEffect(() => {
    console.log('开始刷新 useEffect');
    // setTimeout(() => {
    //   setRefreshing(true);
    // }, 3000);
  }, []);

  return (
    <FlatList
      data={DATAS}
      ItemSeparatorComponent={() => <View style={styles.separator} />}
      renderItem={({ item }) => (
        <AppleStyleSwipeableRow
          item={item}
          close={(pointerInside: boolean) => {
            console.log('pointerInside', pointerInside);
          }}
        />
      )}
      keyExtractor={(_item, index) => `message ${index}`}
      refreshControl={
        // default header
        <DJRefreshDefaultHeader
          refreshing={refreshing}
          locale={''}
          onRefresh={refreshAction}
        />
      }
    />
  );
};

const AppleStyleSwipeableRow = (props: { item: any; close: any }) => {
  const { item, close } = props;
  const renderLeftActions = (_progress: any, dragX: any) => {
    const trans = dragX.interpolate({
      inputRange: [0, 50, 100, 101],
      outputRange: [-20, 0, 0, 1],
    });
    return (
      <RectButton style={styles.leftAction} onPress={close}>
        <Animated.Text
          style={[
            styles.actionText,
            {
              transform: [{ translateX: trans }],
            },
          ]}
        >
          Archive
        </Animated.Text>
      </RectButton>
    );
  };

  return (
    <Swipeable renderRightActions={renderLeftActions}>
      <RectButton
        style={styles.rectButton}
        onPress={() => Alert.alert(item.from)}
      >
        <Text style={styles.fromText}>{item.from}</Text>
        <Text numberOfLines={2} style={styles.messageText}>
          {item.message}
        </Text>
        <Text style={styles.dateText}>
          {item.when} {'❭'}
        </Text>
      </RectButton>
    </Swipeable>
  );
};

const styles = StyleSheet.create({
  rectButton: {
    flex: 1,
    height: 80,
    paddingVertical: 10,
    paddingHorizontal: 20,
    justifyContent: 'space-between',
    flexDirection: 'column',
    backgroundColor: 'white',
  },
  separator: {
    backgroundColor: 'rgb(200, 199, 204)',
    height: StyleSheet.hairlineWidth,
  },
  fromText: {
    fontWeight: 'bold',
    backgroundColor: 'transparent',
  },
  messageText: {
    color: '#999',
    backgroundColor: 'transparent',
  },
  dateText: {
    backgroundColor: 'transparent',
    position: 'absolute',
    right: 20,
    top: 10,
    color: '#999',
    fontWeight: 'bold',
  },
  leftAction: {
    flex: 1,
    backgroundColor: '#497AFC',
    justifyContent: 'center',
  },
  actionText: {
    color: 'white',
    fontSize: 16,
    backgroundColor: 'transparent',
    padding: 10,
  },
});

const DATA = [
  {
    from: "D'Artagnan",
    when: '3:11 PM',
    message:
      'Unus pro omnibus, omnes pro uno. Nunc scelerisque, massa non lacinia porta, quam odio dapibus enim, nec tincidunt dolor leo non neque',
  },
  {
    from: 'Aramis',
    when: '11:46 AM',
    message:
      'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vivamus hendrerit ligula dignissim maximus aliquet. Integer tincidunt, tortor at finibus molestie, ex tellus laoreet libero, lobortis consectetur nisl diam viverra justo.',
  },
  {
    from: 'Athos',
    when: '6:06 AM',
    message:
      'Sed non arcu ullamcorper, eleifend velit eu, tristique metus. Duis id sapien eu orci varius malesuada et ac ipsum. Ut a magna vel urna tristique sagittis et dapibus augue. Vivamus non mauris a turpis auctor sagittis vitae vel ex. Curabitur accumsan quis mauris quis venenatis.',
  },
  {
    from: 'Porthos',
    when: 'Yesterday',
    message:
      'Vivamus id condimentum lorem. Duis semper euismod luctus. Morbi maximus urna ut mi tempus fermentum. Nam eget dui sed ligula rutrum venenatis.',
  },
  {
    from: 'Domestos',
    when: '2 days ago',
    message:
      'Aliquam imperdiet dolor eget aliquet feugiat. Fusce tincidunt mi diam. Pellentesque cursus semper sem. Aliquam ut ullamcorper massa, sed tincidunt eros.',
  },
  {
    from: 'Cardinal Richelieu',
    when: '2 days ago',
    message:
      'Pellentesque id quam ac tortor pellentesque tempor tristique ut nunc. Pellentesque posuere ut massa eget imperdiet. Ut at nisi magna. Ut volutpat tellus ut est viverra, eu egestas ex tincidunt. Cras tellus tellus, fringilla eget massa in, ultricies maximus eros.',
  },
  {
    from: "D'Artagnan",
    when: 'Week ago',
    message:
      'Aliquam non aliquet mi. Proin feugiat nisl maximus arcu imperdiet euismod nec at purus. Vestibulum sed dui eget mauris consequat dignissim.',
  },
  {
    from: 'Cardinal Richelieu',
    when: '2 weeks ago',
    message:
      'Vestibulum ac nisi non augue viverra ullamcorper quis vitae mi. Donec vitae risus aliquam, posuere urna fermentum, fermentum risus. ',
  },
];

export default SwipeableList;
