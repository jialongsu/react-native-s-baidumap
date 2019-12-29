import React, {Component} from 'react';
import {
  requireNativeComponent,
  View,
  findNodeHandle,
  NativeModules,
  Platform,
} from 'react-native';
import PropTypes from 'prop-types';

const isIos = Platform.OS === 'ios';

export default class MapView extends Component {
  static propsTypes = {
    ...View.propTypes,
    /**
     *显示放大缩小按钮
     */
    zoomControlsVisible: PropTypes.bool,
    /**
     *显示交通路线
     */
    trafficEnabled: PropTypes.bool,
    /**
     *显示百度热力图层
     */
    baiduHeatMapEnabled: PropTypes.bool,
    /**
     *地图类型 1: 标准地图 2: 卫星地图 3:空白地图
     */
    baiduMapType: PropTypes.number,
    /**
     *显示当前定位位置
     */
    locationEnabled: PropTypes.bool,
    /**
     *地图缩放级别[4,21]
     */
    zoom: PropTypes.number,
    /**
     *地图最大缩放级别，最大21
     */
    zoomMaxLevel: PropTypes.number,
    /**
     *地图最小缩放级别，最小4
     */
    zoomMinLevel: PropTypes.number,
    /**
     *地图中心点位置
     */
    centerLatLng: PropTypes.shape({
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
    }),
    /**
     *根据marker点经纬度缩缩放地图，使所有Overlay都在合适的视野内
     */
    // zoomToSpanMarkers: PropTypes.arrayOf(
    //   PropTypes.shape({
    //     latitude: PropTypes.number.isRequired,
    //     longitude: PropTypes.number.isRequired,
    //   }),
    // ),
    /**
     *点击地图
     */
    onMapClick: PropTypes.func,
    /**
     *点击地图地点
     */
    onMapPoiClick: PropTypes.func,
    /**
     *长按地图
     */
    onMapLongClick: PropTypes.func,
    /**
     *双击地图
     */
    onMapDoubleClick: PropTypes.func,
    /**
     *地图状态变化
     */
    onMapStatusChangeStart: PropTypes.func,
    onMapStatusChange: PropTypes.func,
    onMapStatusChangeFinish: PropTypes.func,
    onMapLoaded: PropTypes.func,
    /**
     *点击marker
     */
    onMarkerClick: PropTypes.func,
    /**
     *拖拽marker
     */
    onMarkerDragStart: PropTypes.func,
    onMarkerDrag: PropTypes.func,
    onMarkerDragEnd: PropTypes.func,
  };

  static defaultProps = {
    zoomControlsVisible: true,
    trafficEnabled: false,
    baiduHeatMapEnabled: false,
    locationEnabled: false,
    baiduMapType: 1, // 0,1,2
    zoom: 18,
    // zoomMinLevel: 10,
    // zoomMaxLevel: 16,
    // centerLatLng: {
    //   latitude: 39.913607,
    //   longitude: 116.404844,
    // },
    // zoomToSpanMarkers: [],
    onMapClick: () => {},
    onMapPoiClick: () => {},
    onMapLongClick: () => {},
    onMapDoubleClick: () => {},
    onMapStatusChangeStart: () => {},
    onMapStatusChange: () => {},
    onMapStatusChangeFinish: () => {},
    onMapLoaded: () => {},
    onMarkerClick: () => {},
    onMarkerDragStart: () => {},
    onMarkerDrag: () => {},
    onMarkerDragEnd: () => {},
  };

  /**
   *设置地图中心
   * @memberof MapView
   */
  setCenter = center => {
    if (isIos) {
      this.map.setNativeProps({
        centerLatLng: center,
      });
    } else {
      this._runCommand('setMapCenter', [center]);
    }
  };

  /**
   *设置地图缩放
   * @memberof MapView
   */
  setZoom = zoom => {
    if (isIos) {
      this.map.setNativeProps({
        zoom,
      });
    } else {
      this._runCommand('setZoom', [zoom]);
    }
  };

  /**
   *显示地图中所有的marker点
   * @memberof MapView
   */
  setZoomToSpanMarkers = zoomToSpanMarkers => {
    this.map.setNativeProps({
      zoomToSpanMarkers,
    });
  };

  _runCommand = (name, args) => {
    NativeModules.UIManager.dispatchViewManagerCommand(
      findNodeHandle(this.map),
      this._uiManagerCommand(name),
      args,
    );
  };

  _uiManagerCommand(name) {
    const uiManager = NativeModules.UIManager;
    const componentName = 'BaiduMapView';

    if (!uiManager.getViewManagerConfig) {
      // RN < 0.58
      return uiManager[componentName].Commands[name];
    }
    // RN >= 0.58
    return uiManager.getViewManagerConfig(componentName).Commands[name];
  }

  getBaiduMapView = view => {
    this.map = view;
  };

  render() {
    return <BaiduMapView ref={this.getBaiduMapView} {...this.props} />;
  }
}

const BaiduMapView = requireNativeComponent('BaiduMapView', MapView, {
  nativeOnly: {onChange: true},
});
