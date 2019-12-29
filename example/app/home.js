'use strict';
import React, {Component} from 'react';
import {StyleSheet, Text, TouchableOpacity, ScrollView} from 'react-native';

export default class Page extends Component {
  onTapItem = ({pageName}) => {
    this.props.navigation.navigate(pageName);
  };

  render() {
    const pageAry = [
      {text: '百度地图Demo', pageName: 'MapDemo'},
      {text: '百度地图定位Demo', pageName: 'GeolocationDem'},
      {text: '百度搜索Api_Demo', pageName: 'SearchDemo'},
      {text: '百度搜索路线规划', pageName: 'RoutePlanSearchDemo'},
    ];
    return (
      <ScrollView style={styles.con}>
        {pageAry.map((item, i) => {
          return (
            <TouchableOpacity
              key={i}
              style={styles.item}
              activeOpacity={1}
              onPress={this.onTapItem.bind(this, item)}>
              <Text style={styles.itemText}>{item.text}</Text>
            </TouchableOpacity>
          );
        })}
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  con: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  item: {
    height: 100,
    borderRadius: 5,
    borderWidth: 1,
    borderColor: '#333',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
    marginHorizontal: 30,
  },
  itemText: {
    fontSize: 16,
  },
});
