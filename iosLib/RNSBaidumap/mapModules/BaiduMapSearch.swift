//
//  BaiduMapSearchModule.swift
//  project
//
//  Created by Arno on 2019/12/17.
//  Copyright © 2019 Facebook. All rights reserved.
//

import Foundation

@objc(BaiduMapSearch)
class BaiduMapSearch: NSObject, BMKPoiSearchDelegate, BMKSuggestionSearchDelegate {
  
//  var searchInCityPrmResolve: RCTPromiseResolveBlock! //城市内搜索promise成功回调
//  var searchInCityPrmReject: RCTPromiseRejectBlock! //城市内搜索promise失败回调
//  var searchNearbyPrmResolve: RCTPromiseResolveBlock! //周边检索promise成功回调
//  var searchNearbyPrmReject: RCTPromiseRejectBlock! //周边检索promise失败回调
  var searchPrmResolve: RCTPromiseResolveBlock! //检索promise成功回调
  var searchPrmReject: RCTPromiseRejectBlock! //检索promise失败回调
  var searchType = 0; //搜索类型，用于区分结果类型
  var searchTypeAry: [Int] = []; //搜索类型
  let SEARCH_INCITY = 0; //城市内搜索
  let SEARCH_NEARBY = 1; //周边检索
//  var hasListeners: Bool = false
  
  //初始化BMKPoiSearch实例
  lazy var POISearch: BMKPoiSearch = {
    let poiSearch = BMKPoiSearch()
    //设置POI检索的代理
    poiSearch.delegate = self
    return poiSearch
  }()
  //初始化BMKSuggestionSearch实例
  lazy var suggestionSearch: BMKSuggestionSearch = {
    let suggestionSearch = BMKSuggestionSearch()
    //设置POI检索的代理
    suggestionSearch.delegate = self
    return suggestionSearch
  }()
  
//  @objc override func supportedEvents() -> [String]! {
//     return ["BaiduPoiSearch", "BaiduRequestSuggestion"];
//   }
//
//  override func startObserving() {
//    hasListeners = true
//  }
//
//  override func stopObserving() {
//    hasListeners = false
//  }

  /**
  *城市内搜索
 */
  @objc func searchInCity(_ city: String, _ keyword: String, _ pageNum: Int, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {

      searchPrmResolve = resolve;
      searchPrmReject = reject;
      searchType = SEARCH_INCITY
    
      //初始化请求参数类BMKCitySearchOption的实例
      let cityOption = BMKPOICitySearchOption()
      //检索关键字，必选。举例：天安门
       cityOption.keyword = keyword
       //区域名称(市或区的名字，如北京市，海淀区)，最长不超过25个字符，必选
       cityOption.city = city
       //检索分类，与keyword字段组合进行检索，多个分类以","分隔。举例：美食,酒店
//       cityOption.tags = option.tags
       //区域数据返回限制，可选，为true时，仅返回city对应区域内数据
       cityOption.isCityLimit = true
       /**
        POI检索结果详细程度
        BMK_POI_SCOPE_BASIC_INFORMATION: 基本信息
        BMK_POI_SCOPE_DETAIL_INFORMATION: 详细信息
        */
//       cityOption.scope = BMK_POI_SCOPE_DETAIL_INFORMATION
       //检索过滤条件，scope字段为BMK_POI_SCOPE_DETAIL_INFORMATION时，filter字段才有效
//       cityOption.filter = option.filter
       //分页页码，默认为0，0代表第一页，1代表第二页，以此类推
//       cityOption.pageIndex = option.pageIndex
       //单次召回POI数量，默认为10条记录，最大返回20条
       cityOption.pageSize = pageNum
       /**
        城市POI检索：异步方法，返回结果在BMKPoiSearchDelegate的onGetPoiResult里
        cityOption 城市内搜索的搜索参数类（BMKCitySearchOption）
        成功返回YES，否则返回NO
        */
       let flag = POISearch.poiSearch(inCity: cityOption)
       if flag {
           NSLog("POI城市内检索成功")
       } else {
           NSLog("POI城市内检索失败")
       }
    }
  
  /**
   *周边检索
   */
  @objc func searchNearby(_ options: Dictionary<String, Any>, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {

      searchPrmResolve = resolve;
      searchPrmReject = reject;
      searchType = SEARCH_NEARBY
    
      //初始化请求参数类BMKNearbySearchOption的实例
      let nearbyOption = BMKPOINearbySearchOption()
      /**
      检索关键字，必选。
      在周边检索中关键字为数组类型，可以支持多个关键字并集检索，如银行和酒店。每个关键字对应数组一个元素。
      最多支持10个关键字。
      */
      nearbyOption.keywords = [options["keyword"] as! String]
     //检索中心点的经纬度，必选
     nearbyOption.location = CLLocationCoordinate2D(latitude: CLLocationDegrees(options["latitude"] as! Double), longitude: CLLocationDegrees(options["longitude"] as! Double))
     /**
      检索半径，单位是米。
      当半径过大，超过中心点所在城市边界时，会变为城市范围检索，检索范围为中心点所在城市
      */
      nearbyOption.radius = options["radius"] as! Int
     /**
      检索分类，可选。
      该字段与keywords字段组合进行检索。
      支持多个分类，如美食和酒店。每个分类对应数组中一个元素
      */
//     nearbyOption.tags = option.tags
     /**
      是否严格限定召回结果在设置检索半径范围内。默认值为false。
      值为true代表检索结果严格限定在半径范围内；值为false时不严格限定。
      注意：值为true时会影响返回结果中total准确性及每页召回poi数量，我们会逐步解决此类问题。
      */
//     nearbyOption.isRadiusLimit = option.isRadiusLimit
     /**
      POI检索结果详细程度
      
      BMK_POI_SCOPE_BASIC_INFORMATION: 基本信息
      BMK_POI_SCOPE_DETAIL_INFORMATION: 详细信息
      */
//     nearbyOption.scope = option.scope
     //检索过滤条件，scope字段为BMK_POI_SCOPE_DETAIL_INFORMATION时，filter字段才有效
//     nearbyOption.filter = option.filter
     //分页页码，默认为0，0代表第一页，1代表第二页，以此类推
//     nearbyOption.pageIndex = option.pageIndex
     //单次召回POI数量，默认为10条记录，最大返回20条。
      nearbyOption.pageSize = options["pageNum"] as! Int
     /**
      根据中心点、半径和检索词发起周边检索：异步方法，返回结果在BMKPoiSearchDelegate
      的onGetPoiResult里
      
      nearbyOption 周边搜索的搜索参数类
      成功返回YES，否则返回NO
      */
     let flag = POISearch.poiSearchNear(by: nearbyOption)
     if flag {
         NSLog("POI周边检索成功")
     } else {
         NSLog("POI周边检索失败")
     }
  }
  
  /**
   *输入提示检索
   */
  @objc func requestSuggestion(_ city: String, _ keyword: String, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    
    searchPrmResolve = resolve;
    searchPrmReject = reject;
    //初始化请求参数类BMKSuggestionSearchOption的实例
    let suggestionOption = BMKSuggestionSearchOption()
    //城市名
    suggestionOption.cityname = city
    //检索关键字
    suggestionOption.keyword = keyword
    //是否只返回指定城市检索结果，默认为NO（海外区域暂不支持设置cityLimit）
    suggestionOption.cityLimit = city != ""
    /**
     关键词检索，异步方法，返回结果在BMKSuggestionSearchDelegate
     的onGetSuggestionResult里
     suggestionOption sug检索信息类
     成功返回YES，否则返回NO
     */
    let flag = suggestionSearch.suggestionSearch(suggestionOption)
    if flag {
        NSLog("关键词检索成功")
    } else {
        NSLog("关键词检索失败")
    }
  }
  
    
  //MARK:BMKPoiSearchDelegate
  /**
   POI检索返回结果回调
   @param searcher 检索对象
   @param poiResult POI检索结果列表
   @param error 错误码
   */
  public func onGetPoiResult(_ searcher: BMKPoiSearch!, result poiResult: BMKPOISearchResult!, errorCode: BMKSearchErrorCode) {

    if errorCode == BMK_SEARCH_NO_ERROR {
        var poiList = Array<Dictionary<String, Any>>();
        for index in 0..<poiResult.poiInfoList.count {
          var data = Dictionary<String, Any>();
          //POI信息类的实例
          let POIInfo = poiResult.poiInfoList[index]
          data["name"] = POIInfo.name
          data["address"] = POIInfo.address
          data["city"] = POIInfo.city
          data["province"] = POIInfo.province
          data["uid"] = POIInfo.uid
          data["latitude"] = POIInfo.pt.latitude
          data["longitude"] = POIInfo.pt.longitude
          poiList.append(data);
        }
        let res: [String : Any] = [ "code": 1000, "poiList": poiList, "type": searchType];
//        sendJsEvent(name: "BaiduPoiSearch", data: res);
        searchPrmResolve(res);
    }else{
        let res: [String : Any] = [ "code": -1, "msg": "Error:\(errorCode)", "type": searchType];
        searchPrmReject("-1", "Error:\(errorCode)", NSError(domain: "", code: -1, userInfo: res));
//        sendJsEvent(name: "BaiduPoiSearch", data: res);
    }
  }
  
  //MARK:BMKSuggestionSearchDelegate
 /**
  关键字检索结果回调
  @param searcher 检索对象
  @param result 关键字检索结果
  @param error 错误码，@see BMKCloudErrorCode
  */
  func onGetSuggestionResult(_ searcher: BMKSuggestionSearch!, result: BMKSuggestionSearchResult!, errorCode error: BMKSearchErrorCode) {
    if error == BMK_SEARCH_NO_ERROR {
      var poiList = Array<Dictionary<String, Any>>();
      for (_, item) in result.suggestionList.enumerated() {
        var data = Dictionary<String, Any>();
        let sugInfo = item as BMKSuggestionInfo
        data["name"] = sugInfo.key
        data["key"] = sugInfo.key
        data["district"] = sugInfo.district
        data["address"] = sugInfo.address
        data["city"] = sugInfo.city
        data["latitude"] = sugInfo.location.latitude
        data["longitude"] = sugInfo.location.longitude
        data["uid"] = sugInfo.uid
        poiList.append(data);
      }
      let res: [String : Any] = [ "code": 1000, "poiList": poiList, "type": 2];
//      sendJsEvent(name: "BaiduRequestSuggestion", data: res);
     searchPrmResolve(res);
    }else{
      let res: [String : Any] = [ "code": -1, "msg": "Error:\(error)", "type": 2];
      searchPrmReject("-1", "Error:\(error)", NSError(domain: "", code: -1, userInfo: res));
//      sendJsEvent(name: "BaiduRequestSuggestion", data: res);
    }
  }
  
//  func sendJsEvent(name: String, data: Any) {
//    if(hasListeners) {
//      self.sendEvent(withName: name, body: data)
//    }
//  }
  
}
