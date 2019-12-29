import {NativeModules} from 'react-native';

const BaiduMapSearch = NativeModules.BaiduMapSearch;
// const baiduMapSearchEmitter = isIos
//   ? new NativeEventEmitter(BaiduMapSearch)
//   : DeviceEventEmitter;

//POI检索
export const searchPoi = {
  //POI城市内检索（关键字检索）
  searchInCity: (city, keyword, pageNum = 20) => {
    if (!city || !keyword) {
      const error = 'searchInCity city / keyword参数不能为空';
      return Promise.reject(error);
    }
    return BaiduMapSearch.searchInCity(city, keyword, pageNum);
  },
  /**
   * 周边检索
   * options = {
   * latitude,
   * longitude,
   * keyword,
   * pageNum,
   * radius
   * }
   */
  searchNearby: options => {
    if (!options.latitude || !options.longitude) {
      const error = 'searchNearby latitude / longitude参数不能为空';
      return Promise.reject(error);
    }
    return BaiduMapSearch.searchNearby(options);
  },
};

//地点检索-输入提示检索
export const suggestion = {
  requestSuggestion: (city = '', keyword = '') => {
    if (!keyword) {
      const error = 'requestSuggestion keyword参数不能为空';
      return Promise.reject(error);
    }
    return BaiduMapSearch.requestSuggestion(city, keyword);
  },
};
