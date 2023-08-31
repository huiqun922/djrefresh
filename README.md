# react-native-djrefresh-library

pull to refresh with flatlist
iOS with MJRefresh,Android with SmartRefreshLayout

## Installation

```sh
npm install react-native-djrefresh-library
```

## Usage
### ios
```sh
cd ios & pod install
```
```js
import {
  DJRefreshDefaultHeader,
  DJRefreshHeader,
} from 'react-native-djrefresh-library';

// ...

  <FlatList
          contentInsetAdjustmentBehavior="never"
          automaticallyAdjustContentInsets={false}
          style={styles.list}
          data={DATA}
          renderItem={({ item }) => <Item title={item.title} />}
          keyExtractor={(item) => item.id}
          refreshControl={
            // default header
            // <DJRefreshDefaultHeader
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

            //custom header
            <DJRefreshHeader
              refreshHeader={
                <View style={{ height: 55 }}>
                  <LottieView
                    style={{ width: '100%', height: 55 }}
                    source={require('./assets/animation_llq8e2yb.json')}
                    autoPlay
                    loop
                  />
                </View>
              }
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
          }
        />
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
