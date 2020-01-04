import {
  NativeModules,
  DeviceEventEmitter,
  NativeEventEmitter,
  Platform,
} from 'react-native';

const isIos = Platform.OS === 'ios';
const BaiduGeolocationModule = NativeModules.BaiduGeolocationModule;
const BaiduGeolocationEmitter = isIos
  ? new NativeEventEmitter(BaiduGeolocationModule)
  : DeviceEventEmitter;

export const coorTypes = {
  //默认GCJ02
  //GCJ02：国测局坐标；
  //BD09ll：百度经纬度坐标；
  //BD09：百度墨卡托坐标；
  //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
  BMK09LL: BaiduGeolocationModule.BMK09LL,
  BMK09MC: BaiduGeolocationModule.BMK09MC,
  GCJ02: BaiduGeolocationModule.GCJ02,
  WGS84: BaiduGeolocationModule.WGS84,
};

export const geolocation = {
  initSDK: key => {
    //key: 百度地图申请的key
    if (!key) {
      return console.warn('initSDK key参数不能为空');
    }
    isIos && BaiduGeolocationModule.initSDK(key);
  },
  start: BaiduGeolocationModule.start,
  stop: BaiduGeolocationModule.stop,
  setOptions: options => {
    const defaultOptions = {
      coorType: coorTypes.BMK09LL, //返回经纬度坐标类型
      scanSpan: 8000, //设置发起定位请求的间隔，int类型，单位ms，仅限android
      openGps: true, //设置是否使用gps，仅限android
      needDeviceDirect: true, //在网络定位时，是否需要设备方向，仅限android
      backgroundLocationUpdates: false, //启动后台定位，仅限ios
      distanceFilter: 8, //设定定位的最小更新距离，仅限ios
      ...options,
    };
    BaiduGeolocationModule.setOptions(defaultOptions);
  },
  addListener: listener => {
    return BaiduGeolocationEmitter.addListener('baiduMapLocation', res => {
      listener && listener(res);
    });
  },
};

/**
 * 地理编码
 */
export const geocode = (address, city = '') => {
  if (!address) {
    return Promise.reject('geocode address参数不能为空');
  }
  return BaiduGeolocationModule.geocode(address, city);
};

/**
 * 逆地理编码
 */
export const reverseGeoCode = (lat, lng) => {
  if (!lat || !lng) {
    return Promise.reject('reverseGeoCode lat, lng参数不能为空');
  }
  return BaiduGeolocationModule.reverseGeoCode(lat, lng);
};
