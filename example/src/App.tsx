/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';

import {
  FlatList,
  SafeAreaView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import {
  DJRefreshHeader,
  DJRefreshDefaultHeader
} from 'react-native-djrefresh-library';
import { useState } from 'react';

const DATA = [
  {
    id: 'bd7acbea-c1b1-46c2-aed5-3ad53abb28ba',
    title: 'First Item1',
  },
  {
    id: '3ac68afc-c605-48d3-a4f8-fbd91aa97f63',
    title: 'Second Item',
  },
  {
    id: '58694a0f-3da1-471f-bd96-145571e29d72',
    title: 'Third Item',
  },
];

type ItemProps = { title: string };

const Item = ({ title }: ItemProps) => (
  <View style={styles.item}>
    <Text style={styles.title}>{title}</Text>
  </View>
);

export default function App() {
  const [refreshing, setRefreshing] = useState(false);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.list}>
        <FlatList
          contentInsetAdjustmentBehavior="never"
          automaticallyAdjustContentInsets={false}
          style={styles.list}
          data={DATA}
          renderItem={({ item }) => <Item title={item.title} />}
          keyExtractor={(item) => item.id}
          refreshControl={
           // default header
            <DJRefreshDefaultHeader
              refreshing={refreshing}
              onRefresh={() => {
                console.log('开始刷新');
                setRefreshing(true);
                setTimeout(() => {
                  console.log('结束刷新');
                  setRefreshing(false);
                }, 3000);
              }}
            />

            //custom header
            // <DJRefreshHeader
            //   refreshHeader={
            //     <View style={{ height: 55 }}>
            //       <LottieView
            //         style={{ width: '100%', height: 55 }}
            //         source={require('./assets/animation_llq8e2yb.json')}
            //         autoPlay
            //         loop
            //       />
            //     </View>
            //   }
            //   refreshing={refreshing}
            //   onRefresh={() => {
            //     console.log('开始刷新');
            //     setRefreshing(true);
            //     setTimeout(() => {
            //       console.log('结束刷新');
            //       setRefreshing(false);
            //     }, 3000);
            //   }}
            // />
          }
        />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  list: {
    flex: 1,
    width: 200,
    backgroundColor: '#f0f0f0',
  },
  item: {
    backgroundColor: '#f9c2ff',
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16,
  },
  title: {
    fontSize: 32,
  },
});
