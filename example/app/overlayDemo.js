/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, {Component} from 'react';
import {StyleSheet, Platform} from 'react-native';
import {MapView, Arc, Circle, Polygon} from 'react-native-s-baidumap';

const isIos = Platform.OS === 'ios';

export default class App extends Component {
  render() {
    return (
      <MapView
        style={styles.map}
        zoom={10}
        trafficEnabled={false}
        zoomControlsVisible={true}
        baiduHeatMapEnabled={false}
        centerLatLng={{
          latitude: 39.90923,
          longitude: 116.447428,
        }}>
        <Arc
          width={6}
          color={'#F6000F'}
          points={[
            {
              latitude: 40.065,
              longitude: 116.124,
            },
            {
              latitude: 40.125,
              longitude: 116.304,
            },
            {
              latitude: 40.065,
              longitude: 116.404,
            },
          ]}
        />
        <Polygon
          width={6}
          color={'#F6000F'}
          fillColor={isIos ? '#F6000F' : '#80F6000F'}
          points={[
            {
              latitude: 40.065,
              longitude: 117.124,
            },
            {
              latitude: 40.125,
              longitude: 117.304,
            },
            {
              latitude: 40.065,
              longitude: 117.404,
            },
          ]}
        />
        <Circle
          circleCenter={{
            latitude: 39.90923,
            longitude: 116.447428,
          }}
          radius={10000}
          fillColor={isIos ? '#F6000F' : '#80F6000F'}
        />
      </MapView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  buttonCon: {
    flexDirection: 'row',
    alignItems: 'center',
    flexWrap: 'wrap',
  },
  btn: {
    marginHorizontal: 10,
  },
  map: {
    flex: 1,
  },
});
