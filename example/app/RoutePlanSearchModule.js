import {
  NativeModules,
  // DeviceEventEmitter,
  // NativeEventEmitter,
  // Platform,
} from 'react-native';
const routePlanSearchModule = NativeModules.RoutePlanSearchModule;

/**
 * 步行
 * @param {*} option: option
 * startCity: 起点城市
 * startAddres: 起点位置
 * endCity: 终点城市
 * endAddres: 终点位置
 * city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city
 * startLocation: 起点坐标位置 {latitude, longitude}
 * endLocation: 终点坐标位置 {latitude, longitude}
 */
export const walkingRouteSearch = option => {
  return routePlanSearchModule.walkingRouteSearch(option);
};

/**
 * 驾车
 * @param {*} option
 * startCity: 起点城市
 * startAddres: 起点位置
 * endCity: 终点城市
 * endAddres: 终点位置
 * city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city
 * startLocation: 起点坐标位置 {latitude, longitude}
 * endLocation: 终点坐标位置 {latitude, longitude}
 * trafficPolicyType: 是否开起路况
 * drivingPolicyType: 驾车策略, 默认时间优先
 */
export const drivingRouteSearch = option => {
  // if (
  //   !option.startCity ||
  //   !option.startAddres ||
  //   !option.endCity ||
  //   !option.endAddres
  // ) {
  //   return Promise.reject('drivingRouteSearch 参数不能为空');
  // }
  return routePlanSearchModule.drivingRouteSearch(option);
};

export const trafficPolicyType = {
  ROUTE_PATH: 0, //不开启路况
  ROUTE_PATH_AND_TRAFFIC: 1, //开启路况
};

export const drivingPolicyType = {
  ECAR_TIME_FIRST: 0, // 时间优先策略
  ECAR_AVOID_JAM: 1, // 躲避拥堵策略
  ECAR_DIS_FIRST: 2, //最短距离策略
  ECAR_FEE_FIRST: 3, // 费用较少策略
};

/**
 * 骑行
 * @param {*} option
 * startCity: 起点城市
 * startAddres: 起点位置
 * endCity: 终点城市
 * endAddres: 终点位置
 * city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city
 * startLocation: 起点坐标位置 {latitude, longitude}
 * endLocation: 终点坐标位置 {latitude, longitude}
 * ridingType:  骑行类型（0：普通骑行模式，1：电动车模式）默认是普通模式
 */
export const bikingRouteSearch = option => {
  return routePlanSearchModule.bikingRouteSearch(option);
};

/**
 * 市内公交路线规划
 * @param {*} option
 * city: string 起点城市
 * startAddres: string 起点位置
 * endAddres: string 终点位置
 * city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city
 * startLocation: 起点坐标位置 {latitude, longitude}
 * endLocation: 终点坐标位置 {latitude, longitude}
 * policyType:  换乘策略, 默认时间优先
 */
export const transitRoutePlan = option => {
  return routePlanSearchModule.transitRoutePlan(option);
};

export const policyType = {
  EBUS_TIME_FIRST: 0, // 时间优先策略
  EBUS_TRANSFER_FIRST: 1, // 最少换乘
  EBUS_WALK_FIRST: 2, //最少步行距离
  EBUS_NO_SUBWAY: 3, // 不含地铁
};
