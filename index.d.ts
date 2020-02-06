import {Component} from 'react';
import {ViewStyle} from 'react-native';

/**
 * 百度地图组件
 */
export class MapView extends Component<MapViewProps> {
  /**
   *设置地图中心
   * @memberof MapView
   */
  setCenter: (center: LatLang) => void;
  /**
   *设置地图缩放
   * @memberof MapView
   */
  setZoom: (zoom: number) => void;
  /**
   *显示地图中所有的marker点
   * @memberof MapView
   */
  setZoomToSpanMarkers: (zoomToSpanMarkers: LatLang[]) => void;
  /**
   *获取地图实例
   * @memberof MapView
   */
  getBaiduMapView: () => MapView;
}

export interface LatLang {
  latitude: number;
  longitude: number;
}

export interface IStatus extends LatLang {
  zoomLevel: number;
}

export interface IMyLocationData {
  latitude: number;
  longitude: number;
  radius: number;
  direction: number;
  locationMode?: number; //定位图层显示方式：0:默认模式 1：跟随模式 2：罗盘模式
  fillColor?: string; //精度圈填充颜色
  strokeColor?: string; //精度圈边框颜色
}

export interface MapViewProps extends ViewStyle {
  /**
   *显示放大缩小按钮
   */
  zoomControlsVisible?: boolean;
  /**
   *显示交通路线
   */
  trafficEnabled?: boolean;
  /**
   *显示百度热力图层
   */
  baiduHeatMapEnabled?: boolean;
  /**
   *地图类型 1: 标准地图 2: 卫星地图 3:空白地图
   */
  baiduMapType?: 1 | 2 | 3;
  /**
   *显示当前定位位置
   */
  locationEnabled?: boolean;
  /**
   *显示当前定位位置
   *使用该属性前locationEnabled必须设置为true
   */
  myLocationData?: IMyLocationData;
  /**
   *地图缩放级别[4,21]
   */
  zoom?: number;
  /**
   *地图最大缩放级别，最大21
   */
  zoomMaxLevel?: number;
  /**
   *地图最小缩放级别，最小4
   */
  zoomMinLevel?: number;
  /**
   *地图中心点位置
   */
  centerLatLng?: LatLang;
  /**
   *设置是否允许缩放手势
   */
  zoomGesturesEnabled?: boolean;
  /**
   *是否允许拖拽手势
   */
  scrollGesturesEnabled?: boolean;
  /**
   *是否允许俯视手势
   */
  overlookingGesturesEnabled?: boolean;
  /**
   *是否允许旋转手势
   */
  rotateGesturesEnabled?: boolean;
  /**
   *用于设置个性化地图的样式文件
   */
  mapCustomStyleFileName?: string;
  /**
   *点击地图
   */
  onMapClick?: () => void;
  /**
   *点击地图地点
   */
  onMapPoiClick?: (res: {name: string; id: string}) => void;
  /**
   *长按地图
   */
  onMapLongClick?: (res: LatLang) => void;
  /**
   *双击地图
   */
  onMapDoubleClick?: (res: LatLang) => void;
  /**
   *地图状态变化
   */
  onMapStatusChangeStart?: (res: IStatus) => void;
  onMapStatusChange?: (res: IStatus) => void;
  onMapStatusChangeFinish?: (res: IStatus) => void;
  onMapLoaded?: () => void;
  /**
   *点击marker
   */
  onMarkerClick?: () => void;
  /**
   *拖拽marker
   */
  onMarkerDragStart?: (res: LatLang) => void;
  onMarkerDrag?: (res: LatLang) => void;
  onMarkerDragEnd?: (res: LatLang) => void;
}

/**
 *标记点组件
 */
export class Marker extends Component<MarkerProps> {}

export interface MarkerProps extends ViewStyle {
  /**
   *标题
   */
  title?: string;
  /**
   *坐标
   */
  location: LatLang;
  /**
   *图片
   */
  icon: string | number;
  /**
   * 是否可拖拽
   */
  draggable?: boolean;
  /**
   * 是否显示infowind,
   */
  active?: boolean;
  /**
   * 设置marker层级
   */
  zIndex?: number;
  /**
   * infowind y轴偏移
   */
  infoWindowYOffset?: number;
  /**
   * 是否开启近大远小效果, 仅android
   */
  perspective?: boolean;
  /**
   * 透明度, 仅android
   */
  alpha?: number;
  /**
   * 旋转角度, 仅android
   */
  rotate?: number;
  /**
   * 是否平贴地图, 仅android
   */
  flat?: boolean;
  /**
   * infoWindow 最小高度, 仅android
   */
  infoWindowMinHeight?: number;
  /**
   * infoWindow 最小宽度, 仅android
   */
  infoWindowMinWidth?: number;
  /**
   * infoWindow 字体, 仅android
   */
  infoWindowTextSize?: number;
}

/**
 * Polyline view
 */
export class Polyline extends Component<PolylineProps> {}

export interface PolylineProps extends ViewStyle {
  /**
   *折线坐标点列表
   */
  points: LatLang[];
  /**
   *颜色
   */
  color?: string;
  /**
   *宽度
   */
  width?: number;
}

/**
 * Arc view
 */
export class Arc extends Component<ArcProps> {}

export interface ArcProps extends ViewStyle {
  /**
   *弧线坐标点列表
   */
  points: LatLang[];
  /**
   *颜色
   */
  color?: string;
  /**
   *宽度
   */
  width?: number;
}

/**
 * Circle view
 */
export class Circle extends Component<CircleProps> {}

export interface CircleProps extends ViewStyle {
  /**
   *圆形中点
   */
  circleCenter: LatLang;
  /**
   *颜色
   */
  color?: string;
  /**
   *边框宽度
   */
  width?: number;
  /**
   *圆形半径
   */
  radius?: number;
  /**
  *圆形背景颜色
  */
  fillColor?: string,
  /**
   *圆形背景颜色透明度，仅ios
  */
  fillColorAlpha?: number,
}

/**
 * Polygon view
 */
export class Polygon extends Component<PolygonProps> {}

export interface PolygonProps extends ViewStyle {
  /**
   *多边形坐标点列表
   */
  points: LatLang[];
  /**
   *颜色
   */
  color?: string;
  /**
   *边框宽度
   */
  width?: number;
  /**
   *圆形背景颜色
   */
  fillColor?: string;
  /**
   *圆形背景颜色透明度，仅ios
  */
  fillColorAlpha?: number,
}

/**
 * 搜索 Api
 */
export const Search: {
  searchPoi: ISearchPoi;
  suggestion: ISuggestion;
};

export interface ISearchPoi {
  //POI城市内检索（关键字检索）
  searchInCity: (
    city: string,
    keyword: string,
    pageNum?: number,
  ) => Promise<IPoiSearchResult>;
  //周边检索
  searchNearby: (options: {
    latitude: number;
    longitude: number;
    keyword: string;
    pageNum?: number;
    radius?: number;
  }) => Promise<IPoiSearchResult>;
}

export interface ISuggestion {
  //地点检索-输入提示检索
  requestSuggestion: (
    city: string,
    keyword: string,
  ) => Promise<IPoiSearchResult>;
}

export interface IPoiSearchResult {
  code: number;
  poiList: IPoiSearchItem[];
}

export interface IPoiSearchItem {
  name: string;
  address: string;
  city: string;
  province: string;
  uid: string;
  latitude: number;
  longitude: number;
}

/**
 * 定位 API
 */
export const Geolocation: {
  geolocation: IGeolocation; //定位
  geocode: (address: string, city?: string) => Promise<IGeocodeResult | string>; //地理编码
  reverseGeoCode: (
    lat: number,
    lng: number,
  ) => Promise<IReverseGeoCodeResult | string>; //逆地理编码
  coorTypes: {[key: string]: coorTypes}; //坐标类型
};

interface IGeocodeResult {
  level: number; //地址类型，包含：UNKNOWN、国家、省、商圈、生活服务等等
  latitude: number;
  longitude: number;
}

interface IReverseGeoCodeResult {
  latitude: number;
  longitude: number;
  address: string; //地址名称
  locationDescribe: string; //结合当前位置POI的语义化结果描述
  adCode: string; //行政区域编码
  city: string; //城市名称
  cityCode: string; //城市编码
  country: string; //国家
  province: string; //省份名称
  district: string; //区县名称
  street: string; //街道名称
  countryCode: string; //国家代码
  town: string; //乡镇
}

interface IGeolocation {
  initSDK: (key: string) => void; //初始化定位sdk
  start: () => void; //开始定位
  stop: () => void; //停止定位
  setOptions: (options: IOptions) => void; //停止定位
  addListener: (listener: (location: ILocation) => void) => void; //定位监听
}

interface IOptions {
  coorType: coorTypes; //返回经纬度坐标类型
  scanSpan: number; //设置发起定位请求的间隔，int类型，单位ms，仅限android
  openGps: boolean; //设置是否使用gps，仅限android
  needDeviceDirect: boolean; //在网络定位时，是否需要设备方向，仅限android
  backgroundLocationUpdates: boolean; //启动后台定位，仅限ios
  distanceFilter: number; //设定定位的最小更新距离，仅限ios
}

interface ILocation {
  locationDescribe: string; //位置语义化结果的定位点在什么地方周围的描述信息
  adCode: string; //行政区划编码属性
  city: string; //城市名字属性
  cityCode: string; //城市编码属性
  country: string; //国家名字属性
  countryCode: string; //国家编码属性
  Province: string; //省份名字属性
  district: string; //区名字属性
  street: string; //街道名字属性
  streetNumber: string; //街道号码属性
  latitude: number; //当前位置纬度
  longitude: number; //当前位置经度
  buildingId: string; //室内定位成功时返回的百度建筑物ID
  buildingName: string; //室内定位成功时返回的百度建筑物名称
}

/**
 *默认BD09ll
 *GCJ02：国测局坐标；
 *BD09ll：百度经纬度坐标；
 *BD09：百度墨卡托坐标；
 *海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
 */
export type coorTypes = 'BMK09LL' | 'BMK09MC' | 'GCJ02' | 'WGS84';

/**
 * 路径规划api
 */
export const RoutePlanSearch: {
  walkingRouteSearch: (options: IRouterOptions) => Promise<any>; //步行路线规划
  drivingRouteSearch: (options: IDrivingRouteOptions) => Promise<any>; //驾车路线规划
  bikingRouteSearch: (options: IBikingRouteSearch) => Promise<any>; //骑行路线规划
  transitRoutePlan: (options: ITransitRoutePlan) => Promise<any>; //市内公交路线规划
  drivingPolicyType: {[key: string]: drivingPolicyType};
  trafficPolicyType: {[key: string]: trafficPolicyType};
  policyType: {[key: string]: policyType};
};

interface IRouterOptions {
  startCity?: string; //起点城市
  startAddres: string; //起点位置
  endCity?: string; //终点城市
  endAddres: string; //终点位置
  city?: string; //起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city
  startLocation: LatLang; //起点坐标位置 {latitude, longitude}
  endLocation: LatLang; //终点坐标位置 {latitude, longitude}
}

interface IDrivingRouteOptions extends IRouterOptions {
  trafficPolicyType?: trafficPolicyType; //是否开起路况
  drivingPolicyType?: drivingPolicyType; //驾车策略, 默认时间优先
}

/**
 * ECAR_TIME_FIRST: 时间优先策略
 * ECAR_AVOID_JAM: 躲避拥堵策略
 * ECAR_DIS_FIRST: 最短距离策略
 * ECAR_FEE_FIRST: 费用较少策略
 */
export type drivingPolicyType =
  | 'ECAR_TIME_FIRST'
  | 'ECAR_AVOID_JAM'
  | 'ECAR_DIS_FIRST'
  | 'ECAR_FEE_FIRST';

/**
 * ROUTE_PATH: 不开启路况
 * ROUTE_PATH_AND_TRAFFIC: 开启路况
 */
export type trafficPolicyType = 'ROUTE_PATH' | 'ROUTE_PATH_AND_TRAFFIC';

interface IBikingRouteSearch extends IRouterOptions {
  /**
   * 骑行类型（0：普通骑行模式，1：电动车模式）默认是普通模式
   */
  ridingType?: 0 | 1;
}

interface ITransitRoutePlan extends IRouterOptions {
  /**
   * 骑行类型（0：普通骑行模式，1：电动车模式）默认是普通模式
   */
  policyType?: policyType;
}

/**
 * EBUS_TIME_FIRST: 时间优先策略
 * EBUS_TRANSFER_FIRST: 最少换乘
 * EBUS_WALK_FIRST: 最少步行距离
 * EBUS_NO_SUBWAY: 不含地铁
 */
export type policyType =
  | 'EBUS_TIME_FIRST'
  | 'EBUS_TRANSFER_FIRST'
  | 'EBUS_WALK_FIRST'
  | 'EBUS_NO_SUBWAY';

/**
 * 离线地图 api
 */
export const OfflineMap: {
  start: (cityId: number) => void; //开始下载离线地图
  stop: (cityId: number) => void; // 暂停下载离线地图
  remove: (cityId: number) => void; //删除已下载的离线地图
  update: (cityId: number) => void; //更新已下载的离线地图
  getHotCityList: () => Promise<any>; //获取热门城市
  searchCity: (cityName: string) => Promise<any>; //搜索城市
  getOfflineAllCityList: () => Promise<any>; //获取所有支持离线地图的城市
  getDownloadedCityList: () => Promise<any>; //获取已下载过的离线地图
};
