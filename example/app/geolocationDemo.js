/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, {Component} from 'react';
import {StyleSheet, View, Button, Text} from 'react-native';
import {MapView, Marker, Geolocation} from 'react-native-s-baidumap';
import riderIcon from './images/riderIcon.png';

const {geolocation, geocode, reverseGeoCode} = Geolocation;

export default class App extends Component {
  state = {
    location: {
      latitude: 39.913607,
      longitude: 116.404844,
    },
    myLocationData: {},
    isShow: false,
    markerAry: [],
  };

  static navigationOptions = {
    title: '百度地图定位Demo',
  };

  constructor(props) {
    super(props);
    //使用定位api之前，需要调用此方法初始化定位sdk, android需要手动开启定位权限
    geolocation.initSDK('lTKp2cIoXGnui3TGpxCKTUhnGcaYfm5U');
  }

  componentDidMount() {
    //需要在开始定位之前使用setOptions方法设置定位参数
    geolocation.setOptions({
      scanSpan: 8000, //android 间隔返回位置信息
      distanceFilter: 8, //ios 最小更新距离
    });
    setTimeout(() => {
      this.setState({
        markerAry: [
          {
            latitude: 38.714607,
            longitude: 116.706844,
          },
          {
            latitude: 39.894607,
            longitude: 116.796844,
          },
          {
            latitude: 37.984607,
            longitude: 116.916844,
          },
        ],
      });
    }, 2000);
    //定位回调
    this.geolocationListener = geolocation.addListener(res => {
      console.log('location.addListener', res);
      const {latitude, longitude, direction, radius} = res;
      this.setState({
        myLocationData: {
          direction,
          radius,
          latitude,
          longitude,
          locationMode: 2,
          fillColor: '#FF6A07',
          strokeColor: '#000000',
        },
        isShow: false,
      });
    });
  }

  componentWillUnmount() {
    this.geolocationListener.remove();
    this.stopGeolocation();
  }

  /**
   *地理编码
   * @memberof App
   */
  geocode = async () => {
    const res = await geocode('利一家园', '杭州');
    console.log('addListener', res);
  };

  /**
   *反地理编码
   * @memberof App
   */
  reverseGeoCode = async () => {
    const res = await reverseGeoCode(30.23954372829861, 120.26289523654513);
    console.log('reverseGeoCode', res);
  };

  /**
   *定位
   * @memberof App
   */
  geolocation = () => {
    geolocation.start();
  };

  /**
   *停止定位
   * @memberof App
   */
  stopGeolocation = () => {
    geolocation.stop();
  };

  setMapView = view => {
    this.mapView = view;
  };

  render() {
    const {location, isShow, myLocationData} = this.state;

    return (
      <View style={styles.container}>
        <View style={styles.buttonCon}>
          <Button
            style={styles.btn}
            title={'定位'}
            onPress={this.geolocation}
          />
          <Button
            style={styles.btn}
            title={'停止定位'}
            onPress={this.stopGeolocation}
          />
          <Button
            style={styles.btn}
            title={'地理编码'}
            onPress={this.geocode}
          />
          <Button
            style={styles.btn}
            title={'反地理编码'}
            onPress={this.reverseGeoCode}
          />
        </View>
        <MapView
          ref={this.setMapView}
          style={styles.map}
          zoom={10}
          zoomMaxLevel={18}
          locationEnabled
          myLocationData={myLocationData}
          centerLatLng={location}
          onMarkerClick={ev => {
            console.log('onMarkerClick:', ev);
          }}>
          {this.state.markerAry &&
            this.state.markerAry.map((item, i) => {
              return (
                <Marker
                  title={'Marker' + i}
                  active={true}
                  icon={require('./images/riderIcon.png')}
                  location={item}
                />
              );
            })}
          <Marker
            title={'Marker'}
            active={true}
            icon={riderIcon}
            location={location}
            // infoWindowYOffset={-90}
          />
          <Marker
            title={isShow ? 'Marker2' : 'Marker2-isShow'}
            active={true}
            icon={
              'https://dlm-images.oss-cn-hangzhou.aliyuncs.com/6e39c0b3a8b447c588be6c22bf13b908'
            }
            location={{
              latitude: 38.914607,
              longitude: 116.406844,
            }}
            // infoWindowYOffset={-80}
          />
        </MapView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  map: {
    width: '100%',
    flex: 1,
  },
  buttonCon: {
    flexDirection: 'row',
    alignItems: 'center',
    flexWrap: 'wrap',
  },
  btn: {
    marginHorizontal: 10,
  },
});
