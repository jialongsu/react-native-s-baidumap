//
//  RoutePlanSearchModule.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2019/12/28.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation

@objc(RoutePlanSearchModule)
class RoutePlanSearchModule: NSObject, BMKRouteSearchDelegate {
  
  var searchPrmResolve: RCTPromiseResolveBlock! //检索promise成功回调
  var searchPrmReject: RCTPromiseRejectBlock! //检索promise失败回调
  //初始化BMKRouteSearch实例
  lazy var routeSearch: BMKRouteSearch = {
    let routeSearchInstance = BMKRouteSearch()
    //设置公交路径规划的代理
    routeSearchInstance.delegate = self
    return routeSearchInstance
  }()
  //初始化请求参数类步行的实例
  lazy var walkingRoutePlanOption: BMKWalkingRoutePlanOption = {
    let walkingRoutePlanOptionInstance = BMKWalkingRoutePlanOption()
    return walkingRoutePlanOptionInstance;
  }()
  
  /**
   返回步行路线检索结果
   @param searcher 检索对象
   @param result 检索结果，类型为BMKWalkingRouteResult
   @param error 错误码，@see BMKSearchErrorCode
   */
  func onGetWalkingRouteResult(_ searcher: BMKRouteSearch!, result: BMKWalkingRouteResult!, errorCode error: BMKSearchErrorCode) {
    
    if error != BMK_SEARCH_NO_ERROR {
      let res: [String : Any] = [ "code": -1, "msg": "Error:\(error)"];
      searchPrmReject("-1", "Error:\(error)", NSError(domain: "", code: -1, userInfo: res));
      return;
    }
    if error == BMK_SEARCH_NO_ERROR {
      let routes = result.routes;
      var list = Array<Dictionary<String, Any>>();
      
      for item in routes ?? [] {
        let itemData = item;
        var itemObj = Dictionary<String, Any>();
        var stepList = Array<Dictionary<String, Any>>();
        let steps = itemData.steps;
        let duration = itemData.duration;
        
        itemObj["title"] = itemData.title;
        itemObj["distance"] = itemData.distance;
        itemObj["dates"] = duration?.dates;
        itemObj["hours"] = duration?.hours;
        itemObj["minutes"] = duration?.minutes;
        itemObj["seconds"] = duration?.seconds;
        
        for zItem in steps ?? [] {
          let zItemData = zItem as! BMKWalkingStep
          var zItemObj = Dictionary<String, Any>();
          var pointList = Array<Dictionary<String, Any>>();
          var startPointObj = Dictionary<String, Any>();
          var endPointObj = Dictionary<String, Any>();
          let entrace = zItemData.entrace;
          let exit = zItemData.exit;
          
          zItemObj["direction"] = zItemData.direction;
          zItemObj["entraceInstruction"] = zItemData.entraceInstruction;
          zItemObj["exitInstructions"] = zItemData.exitInstruction;
          zItemObj["instructions"] = zItemData.instruction;
          zItemObj["duration"] = zItemData.duration;
          zItemObj["distance"] = zItemData.distance;
          startPointObj["latitude"] = entrace?.location.latitude;
          startPointObj["longitude"] = entrace?.location.longitude;
          endPointObj["latitude"] = exit?.location.latitude;
          endPointObj["longitude"] = exit?.location.longitude;
          pointList.append(startPointObj)
          pointList.append(endPointObj)
          zItemObj["wayPoints"] = pointList;
          stepList.append(zItemObj)
        }
        itemObj["allStep"] = stepList;
        list.append(itemObj)
      }
      groupResult(list: list);
    }
  }
  
  /**
  返回驾车路线检索结果
  @param searcher 检索对象
  @param result 检索结果，类型为BMKWalkingRouteResult
  @param error 错误码，@see BMKSearchErrorCode
  */
  func onGetDrivingRouteResult(_ searcher: BMKRouteSearch!, result: BMKDrivingRouteResult!, errorCode error: BMKSearchErrorCode) {
    if error != BMK_SEARCH_NO_ERROR {
       let res: [String : Any] = [ "code": -1, "msg": "Error:\(error)"];
       searchPrmReject("-1", "Error:\(error)", NSError(domain: "", code: -1, userInfo: res));
       return;
    }
    if error == BMK_SEARCH_NO_ERROR {
      let routes = result.routes;
      var list = Array<Dictionary<String, Any>>();
      
      for item in routes ?? [] {
        let itemData = item;
        var itemObj = Dictionary<String, Any>();
        var stepList = Array<Dictionary<String, Any>>();
        let steps = itemData.steps;
        let duration = itemData.duration;
        
        itemObj["title"] = itemData.title;
        itemObj["distance"] = itemData.distance;
        itemObj["dates"] = duration?.dates;
        itemObj["hours"] = duration?.hours;
        itemObj["minutes"] = duration?.minutes;
        itemObj["seconds"] = duration?.seconds;
        itemObj["lightNum"] = itemData.lightNum; //路线红绿灯个数
        itemObj["congestionDistance"] = itemData.congestionMetres; //路线拥堵米数
        
        for zItem in steps ?? [] {
          let zItemData = zItem as! BMKDrivingStep
          var zItemObj = Dictionary<String, Any>();
          var pointList = Array<Dictionary<String, Any>>();
          var startPointObj = Dictionary<String, Any>();
          var endPointObj = Dictionary<String, Any>();
          let entrace = zItemData.entrace;
          let exit = zItemData.exit;
          
          zItemObj["direction"] = zItemData.direction;
          zItemObj["entraceInstruction"] = zItemData.entraceInstruction;
          zItemObj["exitInstructions"] = zItemData.exitInstruction;
          zItemObj["instructions"] = zItemData.instruction;
          zItemObj["duration"] = zItemData.duration;
          zItemObj["distance"] = zItemData.distance;
          zItemObj["roadLevel"] = zItemData.roadLevel;
          zItemObj["numTurns"] = zItemData.numTurns;
          
          startPointObj["latitude"] = entrace?.location.latitude;
          startPointObj["longitude"] = entrace?.location.longitude;
          endPointObj["latitude"] = exit?.location.latitude;
          endPointObj["longitude"] = exit?.location.longitude;
          pointList.append(startPointObj)
          pointList.append(endPointObj)
          zItemObj["wayPoints"] = pointList;
          stepList.append(zItemObj)
        }
        itemObj["allStep"] = stepList;
        list.append(itemObj)
      }
      groupResult(list: list);
    }
  }
  
  /**
  返回骑行路线检索结果
  @param searcher 检索对象
  @param result 检索结果，类型为BMKWalkingRouteResult
  @param error 错误码，@see BMKSearchErrorCode
  */
  func onGetRidingRouteResult(_ searcher: BMKRouteSearch!, result: BMKRidingRouteResult!, errorCode error: BMKSearchErrorCode) {
    if error != BMK_SEARCH_NO_ERROR {
       let res: [String : Any] = [ "code": -1, "msg": "Error:\(error)"];
       searchPrmReject("-1", "Error:\(error)", NSError(domain: "", code: -1, userInfo: res));
       return;
    }
    if error == BMK_SEARCH_NO_ERROR {
      let routes = result.routes;
      var list = Array<Dictionary<String, Any>>();
      
      for item in routes ?? [] {
        let itemData = item;
        var itemObj = Dictionary<String, Any>();
        var stepList = Array<Dictionary<String, Any>>();
        let steps = itemData.steps;
        let duration = itemData.duration;
        
        itemObj["title"] = itemData.title;
        itemObj["distance"] = itemData.distance;
        itemObj["dates"] = duration?.dates;
        itemObj["hours"] = duration?.hours;
        itemObj["minutes"] = duration?.minutes;
        itemObj["seconds"] = duration?.seconds;
        
        for zItem in steps ?? [] {
          let zItemData = zItem as! BMKRidingStep
          var zItemObj = Dictionary<String, Any>();
          var pointList = Array<Dictionary<String, Any>>();
          var startPointObj = Dictionary<String, Any>();
          var endPointObj = Dictionary<String, Any>();
          let entrace = zItemData.entrace;
          let exit = zItemData.exit;
          
          zItemObj["direction"] = zItemData.direction;
          zItemObj["entraceInstruction"] = zItemData.entraceInstruction;
          zItemObj["exitInstructions"] = zItemData.exitInstruction;
          zItemObj["instructions"] = zItemData.instruction;
          zItemObj["duration"] = zItemData.duration;
          zItemObj["distance"] = zItemData.distance;
          
          startPointObj["latitude"] = entrace?.location.latitude;
          startPointObj["longitude"] = entrace?.location.longitude;
          endPointObj["latitude"] = exit?.location.latitude;
          endPointObj["longitude"] = exit?.location.longitude;
          pointList.append(startPointObj)
          pointList.append(endPointObj)
          zItemObj["wayPoints"] = pointList;
          stepList.append(zItemObj)
        }
        itemObj["allStep"] = stepList;
        list.append(itemObj)
      }
      groupResult(list: list);
    }
  }
  
  /**
  返回市内公交线路检索结果
  @param searcher 检索对象
  @param result 检索结果，类型为BMKWalkingRouteResult
  @param error 错误码，@see BMKSearchErrorCode
  */
  func onGetTransitRouteResult(_ searcher: BMKRouteSearch!, result: BMKTransitRouteResult!, errorCode error: BMKSearchErrorCode) {
     
    if error != BMK_SEARCH_NO_ERROR {
       let res: [String : Any] = [ "code": -1, "msg": "Error:\(error)"];
       searchPrmReject("-1", "Error:\(error)", NSError(domain: "", code: -1, userInfo: res));
       return;
    }
    if error == BMK_SEARCH_NO_ERROR {
      let routes = result.routes;
      var list = Array<Dictionary<String, Any>>();
      
      for item in routes ?? [] {
        let itemData = item;
        var itemObj = Dictionary<String, Any>();
        var stepList = Array<Dictionary<String, Any>>();
        let steps = itemData.steps;
        let duration = itemData.duration;
        
        itemObj["title"] = itemData.title;
        itemObj["distance"] = itemData.distance;
        itemObj["dates"] = duration?.dates;
        itemObj["hours"] = duration?.hours;
        itemObj["minutes"] = duration?.minutes;
        itemObj["seconds"] = duration?.seconds;
        
        for zItem in steps ?? [] {
          let zItemData = zItem as! BMKTransitStep
          var zItemObj = Dictionary<String, Any>();
          var pointList = Array<Dictionary<String, Any>>();
          var startPointObj = Dictionary<String, Any>();
          var endPointObj = Dictionary<String, Any>();
          let entrace = zItemData.entrace;
          let exit = zItemData.exit;
          let vehicleInfo = zItemData.vehicleInfo;

          zItemObj["instructions"] = zItemData.instruction;
          zItemObj["duration"] = zItemData.duration;
          zItemObj["distance"] = zItemData.distance;
          if(vehicleInfo != nil) {
            zItemObj["title"] = vehicleInfo?.title;
            zItemObj["uid"] = vehicleInfo?.uid;
            zItemObj["zonePrice"] = vehicleInfo?.zonePrice;
            zItemObj["totalPrice"] = vehicleInfo?.totalPrice;
            zItemObj["passStationNum"] = vehicleInfo?.passStationNum;
          }
          
          startPointObj["latitude"] = entrace?.location.latitude;
          startPointObj["longitude"] = entrace?.location.longitude;
          endPointObj["latitude"] = exit?.location.latitude;
          endPointObj["longitude"] = exit?.location.longitude;
          pointList.append(startPointObj)
          pointList.append(endPointObj)
          zItemObj["wayPoints"] = pointList;
          stepList.append(zItemObj)
        }
        itemObj["allStep"] = stepList;
        list.append(itemObj)
      }
      groupResult(list: list);
    }
  }
  
  /**
  返回跨城综合公交线路检索结果
  @param searcher 检索对象
  @param result 检索结果，类型为BMKWalkingRouteResult
  @param error 错误码，@see BMKSearchErrorCode
  */
  func onGetMassTransitRouteResult(_ searcher: BMKRouteSearch!, result: BMKMassTransitRouteResult!, errorCode error: BMKSearchErrorCode) {
    print("onGetMassTransitRouteResult")
  }
  
  /**
  返回室内路线路检索结果
  @param searcher 检索对象
  @param result 检索结果，类型为BMKWalkingRouteResult
  @param error 错误码，@see BMKSearchErrorCode
  */
  func onGetIndoorRouteResult(_ searcher: BMKRouteSearch!, result: BMKIndoorRouteResult!, errorCode error: BMKSearchErrorCode) {
    print("onGetIndoorRouteResult")
  }
  
  @objc func walkingRouteSearch(_ options: Dictionary<String, Any>, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    
    searchPrmResolve = resolve;
    searchPrmReject = reject;
    let planNodeMap = getPlanNode(options: options);
    let walkingRoutePlanOption = BMKWalkingRoutePlanOption()
    //检索的起点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    walkingRoutePlanOption.from = planNodeMap["startNode"]
    //检索的终点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    walkingRoutePlanOption.to = planNodeMap["endNode"]
    /**
     发起步行路线检索请求，异步函数，返回结果在BMKRouteSearchDelegate的
     onGetWalkingRouteResult中
    */
    let flag = routeSearch.walkingSearch(walkingRoutePlanOption)
    if flag {
       NSLog("步行检索成功")
    } else {
       NSLog("步行检索失败")
    }
  }
  
  @objc func drivingRouteSearch(_ options: Dictionary<String, Any>, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
  
    searchPrmResolve = resolve;
    searchPrmReject = reject;
    let planNodeMap = getPlanNode(options: options);
    //初始化请求参数类BMKDrivingRoutePlanOption的实例
    let drivingRoutePlanOption = BMKDrivingRoutePlanOption()
    let trafficPolicyType: Int = options["trafficPolicyType"] != nil ? options["trafficPolicyType"] as! Int : 0;
    let drivingPolicyType: Int = options["drivingPolicyType"] != nil ? options["drivingPolicyType"] as! Int : 0;
    //检索的起点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    drivingRoutePlanOption.from = planNodeMap["startNode"]
    //检索的终点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    drivingRoutePlanOption.to = planNodeMap["endNode"]
    /*
    驾车策略，默认使用BMK_DRIVING_TIME_FIRST
    BMK_DRIVING_TIME_FIRST：最短时间
    BMK_DRIVING_BLK_FIRST：躲避拥堵
    BMK_DRIVING_DIS_FIRST：最短路程
    BMK_DRIVING_FEE_FIRST：少走高速
    */
    var drivingPolicy: BMKDrivingPolicy = BMK_DRIVING_TIME_FIRST;
    switch drivingPolicyType {
      case 0:
        drivingPolicy = BMK_DRIVING_TIME_FIRST;
      case 1:
        drivingPolicy = BMK_DRIVING_BLK_FIRST;
      case 2:
        drivingPolicy = BMK_DRIVING_DIS_FIRST;
      case 3:
        drivingPolicy = BMK_DRIVING_FEE_FIRST;
      default:
        drivingPolicy = BMK_DRIVING_TIME_FIRST;
    }
    drivingRoutePlanOption.drivingPolicy = drivingPolicy
    /*
    路线中每一个step的路况，默认使用BMK_DRIVING_REQUEST_TRAFFICE_TYPE_NONE
    BMK_DRIVING_REQUEST_TRAFFICE_TYPE_NONE：不带路况
    BMK_DRIVING_REQUEST_TRAFFICE_TYPE_PATH_AND_TRAFFICE：道路和路况
    */
    var traffic: BMKDrivingRequestTrafficType = BMK_DRIVING_REQUEST_TRAFFICE_TYPE_NONE;
    switch trafficPolicyType {
      case 0:
        traffic = BMK_DRIVING_REQUEST_TRAFFICE_TYPE_NONE;
      case 1:
        traffic = BMK_DRIVING_REQUEST_TRAFFICE_TYPE_PATH_AND_TRAFFICE;
      default:
        traffic = BMK_DRIVING_REQUEST_TRAFFICE_TYPE_NONE;
    }
    drivingRoutePlanOption.drivingRequestTrafficType = traffic
    /**
     发起驾乘路线检索请求，异步函数，返回结果在BMKRouteSearchDelegate的onGetDrivingRouteResult中
     */
    let flag = routeSearch.drivingSearch(drivingRoutePlanOption)
    if flag {
        NSLog("驾车检索成功")
    } else {
        NSLog("驾车检索失败")
    }
  }
  
  @objc func bikingRouteSearch(_ options: Dictionary<String, Any>, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
   
     searchPrmResolve = resolve;
     searchPrmReject = reject;
     let planNodeMap = getPlanNode(options: options);
    //初始化请求参数类BMKRidingRoutePlanOption的实例
    let ridingRoutePlanOption = BMKRidingRoutePlanOption()
    let ridingType: Int = options["ridingType"] != nil ? options["ridingType"] as! Int : 0;
    //检索的起点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    ridingRoutePlanOption.from = planNodeMap["startNode"]
    //检索的终点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    ridingRoutePlanOption.to = planNodeMap["endNode"]
    // 骑行类型（0：普通骑行模式，1：电动车模式）默认是普通模式
    ridingRoutePlanOption.ridingType = ridingType;
    /**
     *发起骑行路线检索请求，异步函数，返回结果在BMKRouteSearchDelegate的onGetRidingRouteResult中
     */
    let flag = routeSearch.ridingSearch(ridingRoutePlanOption)
    if flag {
        NSLog("骑行检索成功")
    } else {
        NSLog("骑行检索失败")
    }
  }
  
  @objc func transitRoutePlan(_ options: Dictionary<String, Any>, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    
    searchPrmResolve = resolve;
    searchPrmReject = reject;
    let planNodeMap = getPlanNode(options: options);
    //初始化请求参数类BMKTransitRoutePlanOption的实例
    let transitRoutePlanOption = BMKTransitRoutePlanOption()
    let policyType: Int = options["policyType"] != nil ? options["policyType"] as! Int : 0;
    //检索的起点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    transitRoutePlanOption.from = planNodeMap["startNode"]
    //检索的终点，可通过关键字、坐标两种方式指定。cityName和cityID同时指定时，优先使用cityID
    transitRoutePlanOption.to = planNodeMap["endNode"]
    /*
     公交检索策略，默认使用BMK_TRANSIT_TIME_FIRST
     BMK_TRANSIT_TIME_FIRST：较快捷(公交)
     BMK_TRANSIT_TRANSFER_FIRST：换乘(公交)
     BMK_TRANSIT_WALK_FIRST：少步行(公交)
     BMK_TRANSIT_NO_SUBWAY：不坐地铁
     */
    var policy: BMKTransitPolicy = BMK_TRANSIT_TIME_FIRST;
    switch policyType {
      case 0:
        policy = BMK_TRANSIT_TIME_FIRST;
      case 1:
        policy = BMK_TRANSIT_TRANSFER_FIRST;
      case 2:
        policy = BMK_TRANSIT_WALK_FIRST;
      case 3:
        policy = BMK_TRANSIT_NO_SUBWAY;
      default:
        policy = BMK_TRANSIT_TIME_FIRST;
    }
    transitRoutePlanOption.transitPolicy = policy
    /**
     室内公交路线检索，异步函数，返回结果在BMKRouteSearchDelegate的onGetTransitRouteResult中
     */
    let flag = routeSearch.transitSearch(transitRoutePlanOption)
    if flag {
        NSLog("市内公交路线检索成功")
    } else {
        NSLog("市内公交路线检索失败")
    }
  }
  
  func getPlanNode(options: Dictionary<String, Any>) -> Dictionary<String, BMKPlanNode> {
    var planNodeMap: Dictionary<String, BMKPlanNode> = Dictionary<String, BMKPlanNode>();
    var msg: String = "";
    let startNode: BMKPlanNode = BMKPlanNode();
    let endNode: BMKPlanNode = BMKPlanNode();
    if((options["startLocation"] != nil) && (options["endLocation"] != nil)) {
      let startLocation = options["startLocation"] as! Dictionary<String, Double>
      let endLocation = options["endLocation"] as! Dictionary<String, Double>
      startNode.pt = CLLocationCoordinate2D(latitude: CLLocationDegrees(startLocation["latitude"] ?? 0), longitude: CLLocationDegrees(startLocation["longitude"] ?? 0))
      endNode.pt = CLLocationCoordinate2D(latitude: CLLocationDegrees(endLocation["latitude"] ?? 0), longitude: CLLocationDegrees(endLocation["longitude"] ?? 0))
    }
//    else {
    var startAddres: String = "";
    var endAddres: String = "";
    var startCity: String = "";
    var endCity: String = "";
    let isHasCity: Bool = options["city"] != nil
    let isHasStartCity: Bool = options["startCity"] != nil
    let isHasEndCity: Bool = options["endCity"] != nil
    
    if(options["startAddres"] != nil) {
      startAddres = options["startAddres"] as! String;
    }else{
      msg = "startAddres参数未传入！";
    }
    if(options["endAddres"] != nil) {
      endAddres = options["endAddres"] as! String;
    }else{
      msg = "endAddres参数未传入！";
    }
    if(isHasStartCity) {
      startCity = options["startCity"] as! String;
    }else{
      msg = !isHasCity ? "startCity参数未传入, startCity与city需传入一个！" : "";
    }
    if(isHasEndCity) {
      endCity = options["endCity"] as! String;
    }else{
      msg = !isHasCity ? "endCity参数未传入, endCity与city需传入一个！" : "";
    }
    if(isHasCity) {
      let city:String = options["city"] as! String;
      startCity = city;
      endCity = city;
      msg = "";
    }else{
      msg = !isHasEndCity || !isHasStartCity ? "city参数未传入, endCity、startCity与city需传入一组！" : "";
    }
    //起点名称
    startNode.name = startAddres
    //起点所在城市，注：cityName和cityID同时指定时，优先使用cityID
    startNode.cityName = startCity
    //终点名称
    endNode.name = endAddres
    //终点所在城市，注：cityName和cityID同时指定时，优先使用cityID
    endNode.cityName = endCity
//    }
    if(msg != "") {
      let res: [String : Any] = [ "code": -1, "msg": "Error:\(msg)"];
      searchPrmReject("-1", "Error:\(msg)", NSError(domain: "", code: -1, userInfo: res));
    }
    planNodeMap["startNode"] = startNode;
    planNodeMap["endNode"] = endNode;
    return planNodeMap;
  }
  
  public func groupResult(list: Array<Dictionary<String, Any>>) {
    var data = Dictionary<String, Any>();
    data["routes"] = list;
    data["code"] = 0;
    searchPrmResolve(data);
  }
  
}
