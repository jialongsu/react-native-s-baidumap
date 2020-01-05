'use strict';
import React, {Component} from 'react';
import {
  StyleSheet,
  View,
  Button,
  Text,
  FlatList,
  TouchableOpacity,
} from 'react-native';
import {OfflineMap} from 'react-native-s-baidumap';

export default class Page extends Component {
  static navigationOptions = {
    title: '百度离线地图',
  };

  state = {
    list: [],
    type: 1,
  };

  componentDidMount() {
    this.onGetHotCity();
  }

  componentWillUnmount() {
    this.timer && clearInterval(this.timer);
  }

  onGetHotCity = () => {
    this.timer && clearInterval(this.timer);
    OfflineMap.getHotCityList().then(res => {
      if (res.code === 0) {
        this.setState({
          list: res.list,
          type: 1,
        });
      }
      console.log('onGetHotCity', res);
    });
  };

  getOfflineAllCityList = () => {
    this.timer && clearInterval(this.timer);
    OfflineMap.getOfflineAllCityList().then(res => {
      if (res.code === 0) {
        this.setState({
          list: res.list,
          type: 2,
        });
      }
      console.log('getOfflineAllCityList', res);
    });
  };

  onSearchCity = () => {
    this.timer && clearInterval(this.timer);
    OfflineMap.searchCity('杭州').then(res => {
      if (res.code === 0) {
        this.setState({
          list: res.list,
          type: 3,
        });
      }
      console.log('onSearchCity', res);
    });
  };

  getDownloadedCityList = () => {
    OfflineMap.getDownloadedCityList().then(res => {
      if (res.code === 0) {
        this.setState({
          list: res.list,
          type: 4,
        });
      }
      console.log('getDownloadedCityList', res);
    });
    this.updatList();
  };

  updatList = () => {
    this.timer && clearInterval(this.timer);
    this.timer = setInterval(() => {
      this.getDownloadedCityList();
    }, 1000);
  };

  startDownload = cityId => {
    OfflineMap.start(cityId);
  };

  stopDownload = cityId => {
    OfflineMap.stop(cityId);
  };

  remove = cityId => {
    OfflineMap.remove(cityId);
    this.getDownloadedCityList();
  };

  renderItem = ({item, index}) => {
    const {type} = this.state;
    return (
      <TouchableOpacity
        key={`${item.cityID}_${index}`}
        style={styles.item}
        onPress={this.startDownload.bind(this, item.cityID)}>
        <Text>{item.cityName}</Text>
        <View>
          <Text>
            数据包大小：{item.dataSize || item.size}
            {type === 4 && `,已下载${item.ratio}%`}
          </Text>
          {type === 4 && (
            <Button
              title={'删除'}
              onPress={this.remove.bind(this, item.cityID)}
            />
          )}
          {item.status === 1 && (
            <Button
              title={'暂停'}
              onPress={this.stopDownload.bind(this, item.cityID)}
            />
          )}
          {item.status === 3 && (
            <Button
              title={'开始'}
              onPress={this.startDownload.bind(this, item.cityID)}
            />
          )}
        </View>
      </TouchableOpacity>
    );
  };

  render() {
    const {list} = this.state;
    return (
      <View style={styles.con}>
        <View style={styles.buttonCon}>
          <Button
            style={styles.btn}
            title={'获取热门城市'}
            onPress={this.onGetHotCity}
          />
          <Button
            style={styles.btn}
            title={'搜索城市'}
            onPress={this.onSearchCity}
          />
          <Button
            style={styles.btn}
            title={'获取所有支持离线地图的城市'}
            onPress={this.getOfflineAllCityList}
          />
          <Button
            style={styles.btn}
            title={'获取已下载过的离线地图'}
            onPress={this.getDownloadedCityList}
          />
        </View>
        <FlatList
          style={styles.list}
          data={list}
          renderItem={this.renderItem}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  con: {
    flex: 1,
  },
  list: {
    flex: 1,
    marginTop: 10,
  },
  buttonCon: {
    flexDirection: 'row',
    alignItems: 'center',
    flexWrap: 'wrap',
  },
  btn: {
    marginHorizontal: 10,
  },
  item: {
    minHeight: 50,
    borderBottomColor: '#eee',
    borderBottomWidth: 1,
    marginHorizontal: 15,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 10,
  },
});
