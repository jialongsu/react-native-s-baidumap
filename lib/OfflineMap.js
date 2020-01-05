import {NativeModules} from 'react-native';

const OfflineMapModule = NativeModules.OfflineMapModule;

/**
 * 开始下载离线地图
 * @param {*} cityId
 */
export const start = cityId => {
  if (!cityId) {
    return console.warn('下载离线地图cityId不能为空');
  }
  OfflineMapModule.start(cityId);
};

/**
 * 暂停下载离线地图
 * @param {*} cityId
 */
export const stop = cityId => {
  if (!cityId) {
    return console.warn('暂停下载离线地图cityId不能为空');
  }
  OfflineMapModule.stop(cityId);
};

/**
 * 删除已下载的离线地图
 * @param {*} cityId
 */
export const remove = cityId => {
  if (!cityId) {
    return console.warn('删除已下载的离线地图cityId不能为空');
  }
  OfflineMapModule.remove(cityId);
};

/**
 * 更新已下载的离线地图
 * @param {*} cityId
 */
export const update = cityId => {
  if (!cityId) {
    return console.warn('更新已下载的离线地图cityId不能为空');
  }
  OfflineMapModule.update(cityId);
};

/**
 * 获取热门城市
 * Promise
 */
export const getHotCityList = OfflineMapModule.getHotCityList;

/**
 * 搜索城市
 * @param {*} cityName
 * Promise
 */
export const searchCity = cityName => {
  if (!cityName) {
    return Promise.reject('搜索支持下载的离线地图的城市cityName不能为空');
  }
  return OfflineMapModule.searchCity(cityName);
};

/**
 * 获取所有支持离线地图的城市
 * Promise
 */
export const getOfflineAllCityList = OfflineMapModule.getOfflineAllCityList;

/**
 * 获取已下载过的离线地图
 * Promise
 */
export const getDownloadedCityList = OfflineMapModule.getDownloadedCityList;
