// 'use strict';
import React, {Component} from 'react';
import {StyleSheet, View, Button, Text, FlatList} from 'react-native';
import {searchPoi, suggestion} from './js/Search';

export default class Page extends Component {
  static navigationOptions = {
    title: '百度搜索Api_Demo',
  };

  state = {
    data: [],
  };

  /**
   *POI城市内关键字检索
   * @memberof Page
   */
  onTapSearchInCity = async () => {
    const res = await searchPoi.searchInCity('杭州', '美食', 10);
    this.setState({
      data: res.poiList,
    });
  };

  /**
   *周边检索
   * @memberof Page
   */
  onTapSearchNearby = async () => {
    const res = await searchPoi.searchNearby({
      latitude: 30.328573,
      longitude: 120.185871,
      keyword: '美食',
      pageNum: 20,
      radius: 1000,
    });
    this.setState({
      data: res.poiList,
    });
  };

  /**
   *地点检索-输入提示检索
   * @memberof Page
   */
  onTapRequestSuggestion = async () => {
    const res = await suggestion.requestSuggestion('杭州', '博览');
    this.setState({
      data: res.poiList,
    });
  };

  renderItem = ({item, index}) => {
    return (
      <View key={`${item.uid}_${index}`} style={styles.item}>
        <Text>{item.name}</Text>
      </View>
    );
  };

  render() {
    return (
      <View style={styles.con}>
        <View style={styles.buttonCon}>
          <Button
            style={styles.btn}
            title={'城市内关键字检索'}
            onPress={this.onTapSearchInCity}
          />
          <Button
            style={styles.btn}
            title={'周边检索'}
            onPress={this.onTapSearchNearby}
          />
          <Button
            style={styles.btn}
            title={'输入提示检索'}
            onPress={this.onTapRequestSuggestion}
          />
        </View>
        <FlatList
          style={styles.list}
          data={this.state.data}
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
    height: 50,
    borderBottomColor: '#eee',
    borderBottomWidth: 1,
    justifyContent: 'center',
    marginHorizontal: 15,
  },
});
