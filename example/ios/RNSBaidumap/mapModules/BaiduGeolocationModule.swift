//
//  BaiduGeolocationModule.swift
//  project
//
//  Created by Arno on 2019/12/17.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation

@objc(BaiduGeolocationModule)
class BaiduGeolocationModule: RCTEventEmitter, BMKLocationManagerDelegate, BMKLocationAuthDelegate, BMKGeoCodeSearchDelegate {
  
  var locationManager: BMKLocationManager?
  lazy var geoCodeSearch: BMKGeoCodeSearch = {
    //初始化BMKGeoCodeSearch实例
    let geoCodeSearch = BMKGeoCodeSearch()
    //设置地理编码检索的代理
    geoCodeSearch.delegate = self
    return geoCodeSearch
  }()
  var hasListeners: Bool = false
  
//  override init() {
//    super.init();
//    //BMKLocationManager需要使用主线程初始化
//    DispatchQueue.main.async(execute: {
//      self.initManager()
//    })
//  }
  
  @objc override func supportedEvents() -> [String]! {
     return ["baiduMapLocation", "baiduMapGeocode", "baiduMapReverseGeoCode"];
   }
  
  override func startObserving() {
    hasListeners = true
  }
  
  override func stopObserving() {
    hasListeners = false
  }
  
  public func initManager(key: String) {
    // 初始化百度地图定位SDK
//    BMKLocationAuth.sharedInstance()?.checkPermision(withKey: "9htcLFC9dx8eWciK3sH8zQGPLhF5BX3z", authDelegate: self)
    BMKLocationAuth.sharedInstance()?.checkPermision(withKey: key, authDelegate: self)
    //初始化BMKLocationManager的实例
     let manager = BMKLocationManager()
     //设置定位管理类实例的代理
     manager.delegate = self
     locationManager = manager;
  }
  
  /**
   导出常量
   */
  override func constantsToExport() -> [AnyHashable : Any]! {
    return [
      "BMK09LL": 1,
      "BMK09MC": 2,
      "GCJ02": 3,
      "WGS84": 4,
    ]
  }
  
  /**
   *设置定位属性
  */
   @objc func setOptions(_ options: Dictionary<String, Any>) {
    
    let manager = self.locationManager
    var coorType: BMKLocationCoordinateType = BMKLocationCoordinateType.BMK09LL
    switch options["coorType"] as? NSNumber ?? 1 {
      case 1:
        coorType = BMKLocationCoordinateType.BMK09LL
      case 2:
        coorType = BMKLocationCoordinateType.BMK09MC
      case 3:
        coorType = BMKLocationCoordinateType.GCJ02
      default:
        coorType = BMKLocationCoordinateType.WGS84
    }
    //设定定位坐标系类型，默认为 BMKLocationCoordinateTypeGCJ02
    manager?.coordinateType = coorType
    //设定定位的最小更新距离。默认为 kCLDistanceFilterNone。
    manager?.distanceFilter = options["distanceFilter"] as! CLLocationDistance
    //设定定位精度，默认为 kCLLocationAccuracyBest
    manager?.desiredAccuracy = kCLLocationAccuracyBest
    //设定定位类型，默认为 CLActivityTypeAutomotiveNavigation
    manager?.activityType = CLActivityType.automotiveNavigation
    //指定定位是否会被系统自动暂停，默认为NO
    manager?.pausesLocationUpdatesAutomatically = false
    /**
     是否允许后台定位，默认为NO。只在iOS 9.0及之后起作用。
     设置为YES的时候必须保证 Background Modes 中的 Location updates 处于选中状态，否则会抛出异常。
     由于iOS系统限制，需要在定位未开始之前或定位停止之后，修改该属性的值才会有效果。
     */
    manager?.allowsBackgroundLocationUpdates = options["backgroundLocationUpdates"] as! Bool
    /**
     指定单次定位超时时间,默认为10s，最小值是2s。注意单次定位请求前设置。
     注意: 单次定位超时时间从确定了定位权限(非kCLAuthorizationStatusNotDetermined状态)
     后开始计算。
     */
    manager?.locationTimeout = 10
  }
  
  /**
   *初始化定位服务
  */
   @objc func initSDK(_ key: String) {
    //BMKLocationManager需要使用主线程初始化
    if(locationManager == nil) {
      DispatchQueue.main.async(execute: {
        self.initManager(key: key)
      })
    }
  }
  
  /**
   *开启定位服务
  */
   @objc func start() {
    locationManager?.startUpdatingHeading()
    locationManager?.startUpdatingLocation()
  }
  
  /**
   *关闭定位服务
  */
   @objc func stop() {
    locationManager?.stopUpdatingHeading()
    locationManager?.stopUpdatingLocation()
  }
  
  /**
   *地理编码
  */
   @objc func geocode(_ address: String, _ city: String) {
    //初始化请求参数类BMKBMKGeoCodeSearchOption的实例
    let geoCodeOption = BMKGeoCodeSearchOption()
    /**
     待解析的地址。必选。
     可以输入2种样式的值，分别是：
     1、标准的结构化地址信息，如北京市海淀区上地十街十号 【推荐，地址结构越完整，解析精度越高】
     2、支持“*路与*路交叉口”描述方式，如北一环路和阜阳路的交叉路口
     注意：第二种方式并不总是有返回结果，只有当地址库中存在该地址描述时才有返回。
     */
    geoCodeOption.address = address
    /**
     地址所在的城市名。可选。
     用于指定上述地址所在的城市，当多个城市都有上述地址时，该参数起到过滤作用。
     注意：指定该字段，不会限制坐标召回城市。
     */
    geoCodeOption.city = city
    /**
     根据地址名称获取地理信息：异步方法，返回结果在BMKGeoCodeSearchDelegate的
     onGetAddrResult里
     geoCodeOption geo检索信息类
     成功返回YES，否则返回NO
     */
    let flag = geoCodeSearch.geoCode(geoCodeOption)
    if flag {
        NSLog("地理编码检索成功")
    } else {
        NSLog("地理检索失败")
    }
  }
  
  /**
   *反地理编码
  */
   @objc func reverseGeoCode(_ lat: Double, _ lng: Double) {
    
    //初始化请求参数类BMKReverseGeoCodeOption的实例
    let reverseGeoCodeOption = BMKReverseGeoCodeSearchOption()
    //待解析的经纬度坐标（必选）
    reverseGeoCodeOption.location = CLLocationCoordinate2D(latitude: CLLocationDegrees(lat), longitude: CLLocationDegrees(lng))
    //是否访问最新版行政区划数据（仅对中国数据生效）
    reverseGeoCodeOption.isLatestAdmin = true
    /**
     根据地理坐标获取地址信息：异步方法，返回结果在BMKGeoCodeSearchDelegate的
     onGetAddrResult里
     reverseGeoCodeOption 反geo检索信息类
     成功返回YES，否则返回NO
     */
    let flag = geoCodeSearch.reverseGeoCode(reverseGeoCodeOption)
    if flag {
        NSLog("反地理编码检索成功")
    } else {
        NSLog("反地理编码检索失败")
    }
  }
  
  //MARK:BMKLocationManagerDelegate
  /**
   @brief 该方法为BMKLocationManager提供设备朝向的回调方法
   @param manager 提供该定位结果的BMKLocationManager类的实例
   @param heading 设备的朝向结果
   */
  func bmkLocationManager(_ manager: BMKLocationManager, didUpdate heading: CLHeading?) {
      NSLog("用户方向更新")
  }
  
  /**
   @brief 连续定位回调函数
   @param manager 定位 BMKLocationManager 类
   @param location 定位结果，参考BMKLocation
   @param error 错误信息。
   */
  func bmkLocationManager(_ manager: BMKLocationManager, didUpdate location: BMKLocation?, orError error: Error?) {
    if((error?.localizedDescription) != nil) {
      return
    };
    var data: Dictionary<String, Any> = [:];
    let rgcData = location?.rgcData;
    data["locationDescribe"] = rgcData?.locationDescribe
    data["adCode"] = rgcData?.adCode
    data["city"] = rgcData?.city
    data["cityCode"] = rgcData?.cityCode
    data["country"] = rgcData?.country
    data["countryCode"] = rgcData?.countryCode
    data["Province"] = rgcData?.province
    data["district"] = rgcData?.district
    data["street"] = rgcData?.street
    data["streetNumber"] = rgcData?.streetNumber
    data["latitude"] = location?.location?.coordinate.latitude
    data["longitude"] = location?.location?.coordinate.longitude
    data["buildingId"] = location?.buildingID
    data["buildingName"] = location?.buildingName
    sendJsEvent(name: "baiduMapLocation", data: data);
  }
  
  //MARK:BMKGeoCodeSearchDelegate
  /**
   正向地理编码检索结果回调
   @param searcher 检索对象
   @param result 正向地理编码检索结果
   @param error 错误码，@see BMKCloudErrorCode
   */
  func onGetGeoCodeResult(_ searcher: BMKGeoCodeSearch!, result: BMKGeoCodeSearchResult!, errorCode error: BMKSearchErrorCode) {
    if error == BMK_SEARCH_NO_ERROR {
      var data: Dictionary<String, Any> = [:]
      data["level"] = result.level
      data["latitude"] = result.location.latitude
      data["longitude"] = result.location.longitude
      sendJsEvent(name: "baiduMapGeocode", data: data);
    }
  }
  
  //MARK:BMKGeoCodeSearchDelegate
  /**
   反向地理编码检索结果回调
   @param searcher 检索对象
   @param result 反向地理编码检索结果
   @param error 错误码，@see BMKCloudErrorCode
   */
  func onGetReverseGeoCodeResult(_ searcher: BMKGeoCodeSearch!, result: BMKReverseGeoCodeSearchResult!, errorCode error: BMKSearchErrorCode) {
    if error == BMK_SEARCH_NO_ERROR {
      let addressDetail = result.addressDetail;
      var data: Dictionary<String, Any> = [:]
      data["latitude"] = result.location.latitude
      data["longitude"] = result.location.longitude
      data["address"] = result.address
      data["locationDescribe"] = result.sematicDescription
      data["adCode"] = addressDetail?.adCode
      data["city"] = addressDetail?.city
      data["cityCode"] = result.cityCode
      data["country"] = addressDetail?.country
      data["province"] = addressDetail?.province
      data["district"] = addressDetail?.district
      data["street"] = addressDetail?.streetName
      data["countryCode"] = addressDetail?.countryCode
      data["town"] = addressDetail?.town
      sendJsEvent(name: "baiduMapReverseGeoCode", data: data);
    }
  }
  
  /**
   @brief 当定位发生错误时，会调用代理的此方法
   @param manager 定位 BMKLocationManager 类
   @param error 返回的错误，参考 CLError
   */
  func bmkLocationManager(_ manager: BMKLocationManager, didFailWithError error: Error?) {
      NSLog("定位失败")
  }
  
  /**
   鉴权结果回调
   @param iError 鉴权结果错误码信息，0代表鉴权成功
   */
  func onCheckPermissionState(_ iError: BMKLocationAuthErrorCode) {
    if BMKLocationAuthErrorCode.success == iError {
          print("百度定位授权成功")
      } else {
          print("百度定位授权失败：%d", iError)
      }
  }
  
  func sendJsEvent(name: String, data: Any) {
    if(hasListeners) {
      self.sendEvent(withName: name, body: data)
    }
  }
  
}
