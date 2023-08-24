import * as React from 'react';

import { FlatList, RefreshControl, StyleSheet, Text, View } from 'react-native';
import {
  DJRefreshDefaultHeader,
  DJRefreshLayout,
  DJRefreshView,
} from 'react-native-djrefresh-library';
import { useState } from 'react';
//import RefreshNormalHeader from './RefreshNormalHeader';
import RefreshDefaultHeader from './RefreshDefaultHeader';

const DATA = [
  {
    id: 'bd7acbea-c1b1-46c2-aed5-3ad53abb28ba',
    title: 'First Item',
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
    <View style={styles.container}>
      <DJRefreshView color="#320000" style={styles.box} />
      <View style={styles.list}>
        <FlatList
          style={styles.list}
          data={DATA}
          renderItem={({ item }) => <Item title={item.title} />}
          keyExtractor={(item) => item.id}
          refreshControl={
            // <RefreshDefaultHeader
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
            <DJRefreshLayout
              enable={true}
              refreshing={refreshing}
              onRefresh={() => {
                console.log('开始刷新');
                setRefreshing(true);
                setTimeout(() => {
                  console.log('结束刷新');
                  setRefreshing(false);
                }, 3000);
              }}
              refreshHeader={<DJRefreshDefaultHeader />}
            />
          }
        />
      </View>
    </View>
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
    width: '100%',
    height: 300,
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
