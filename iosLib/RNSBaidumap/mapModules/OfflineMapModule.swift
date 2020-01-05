//
//  OfflineMapModule.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/1/5.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

@objc(OfflineMapModule)
class OfflineMapModule: NSObject, BMKOfflineMapDelegate {
  
  let kilobyte = 1024
  let megabyte = 1048576
  let gigabyte = 1073741824
  var prmResolve: RCTPromiseResolveBlock!
  var prmReject: RCTPromiseRejectBlock!
  //离线地图类的实例
  lazy var offlineMap: BMKOfflineMap = {
    let newOfflineMap = BMKOfflineMap()
    //设置离线地图类的代理
    newOfflineMap.delegate = self
    return newOfflineMap
  }()
  
  
  @objc func start(_ cityId: NSInteger) {
    offlineMap.start(Int32(cityId))
  }
  
  @objc
  func update(_ cityId: NSInteger) {
    offlineMap.update(Int32(cityId))
  }
  
  @objc
  func stop(_ cityId: NSInteger) {
    offlineMap.pause(Int32(cityId))
  }
  
  @objc
  func remove(_ cityId: NSInteger) {
    offlineMap.remove(Int32(cityId))
  }
  
  @objc
  func getHotCityList(_ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    prmResolve = resolve;
    prmReject = reject;
    let hotCitys = offlineMap.getHotCityList() as! [BMKOLSearchRecord]
    self.groupResult(list: self.groupCityList(records: hotCitys));
  }
  
  @objc
  func getOfflineAllCityList(_ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    prmResolve = resolve;
    prmReject = reject;
    let list = offlineMap.getOfflineCityList() as! [BMKOLSearchRecord]
    self.groupResult(list: self.groupCityList(records: list));
  }
  
  @objc
  func searchCity(_ city: String, _ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    prmResolve = resolve;
    prmReject = reject;
    let list = offlineMap.searchCity(city) as! [BMKOLSearchRecord]
    self.groupResult(list: self.groupCityList(records: list));
  }
  
  @objc
  func getDownloadedCityList(_ resolve:@escaping RCTPromiseResolveBlock, _ reject: @escaping RCTPromiseRejectBlock) {
    prmResolve = resolve;
    prmReject = reject;
    let records = offlineMap.getAllUpdateInfo()
    var list = Array<Dictionary<String, Any>>();
    if(records != nil) {
      let newRecords = records as! [BMKOLUpdateElement]
      for item in newRecords {
        var data = Dictionary<String, Any>();
        data["cityID"] = item.cityID;
        data["cityName"] = item.cityName;
        data["ratio"] = item.ratio;
        data["status"] = item.status;
        data["update"] = item.update;
        data["size"] = self.getDataSizeString(NSInteger(item.size));
        data["serversize"] = self.getDataSizeString(NSInteger(item.serversize));
        data["latitude"] = item.pt.latitude;
        data["longitude"] = item.pt.longitude;
        list.append(data)
      }
    }
    self.groupResult(list: list);
  }
  
  /**
  返回通知结果
  @param type 事件类型
  @param state 事件状态
  type为TYPE_OFFLINE_UPDATE，表示正在下载或更新城市id为state的离线包，
  type为TYPE_OFFLINE_ZIPCNT，表示检测到state个离线压缩包，
  type为TYPE_OFFLINE_ADD，表示新安装的离线地图数目，
  type为TYPE_OFFLINE_UNZIP，表示正在解压第state个离线包，
  type为TYPE_OFFLINE_ERRZIP，表示有state个错误包，
  type为TYPE_VER_NEWVER，表示id为state的城市离线包有新版本，
  type为TYPE_OFFLINE_UNZIPFINISH时，表示扫瞄完成，成功导入state个离线包
  */
  func onGetOfflineMapState(_ type: Int32, withState state: Int32) {
    print("onGetOfflineMapState")
  }
  
  /**
   离线地图数据包大小单位转换
   @param packetSize 离线地图数据包总大小，单位是bytes
   @return 转换单位后的离线地图数据包大小
   */
  func getDataSizeString(_ packetSize: NSInteger) -> String {
      var packetSizeString: String = ""
      if packetSize < kilobyte {
          packetSizeString = String(format: "%ldB", packetSize)
      } else if packetSize < megabyte {
          packetSizeString = String(format: "%ldK", packetSize / kilobyte)
      } else if packetSize < gigabyte {
          if (packetSize % megabyte) == 0 {
          packetSizeString = String(format: "%ldM", megabyte)
          } else {
              var decimal: NSInteger = 0
              var decimalString: String = ""
              decimal = packetSize % megabyte
              decimal /= kilobyte
              if decimal < 10 {
              decimalString = String(format: "%d", 0)
              } else if decimal >= 10 && decimal < 100 {
                  let temp: NSInteger = decimal / 10
                  if temp >= 5 {
                      decimalString = String(format: "%d", 1)
                  } else {
                      decimalString = String(format: "%d", 0)
                  }
              } else if decimal >= 100 && decimal < kilobyte {
                  let temp: NSInteger = decimal / 100
                  if temp >= 5 {
                      decimal = temp + 1
                      if decimal >= 10 {
                          decimal = 9
                      }
                      decimalString = String(format: "%ld", decimal)
                  } else {
                      decimalString = String(format: "%ld", temp)
                  }
              }
              if decimalString.isEmpty {
                  packetSizeString = String(format: "%ldMss", packetSize / megabyte)
              } else {
                  packetSizeString = String(format: "%ld.%@M", packetSize / megabyte, decimalString)
              }
          }
      } else {
          packetSizeString = String(format: "%ldG", packetSize / gigabyte)
      }
      return packetSizeString
  }
  
  func groupCityList(records: [BMKOLSearchRecord]) -> Array<Dictionary<String, Any>> {
    var list = Array<Dictionary<String, Any>>();
    for item in records {
      var data = Dictionary<String, Any>();
      let cityType = item.cityType;
      data["cityType"] = cityType;
      data["cityID"] = item.cityID;
      data["cityName"] = item.cityName;
      data["dataSize"] = self.getDataSizeString(NSInteger(item.size));
      if(cityType == 1) {
        data["childCities"] = self.groupCityList(records: item.childCities as! [BMKOLSearchRecord]);
      }
      list.append(data)
    }
    return list;
  }
  
  public func groupResult(list: Array<Dictionary<String, Any>>) {
    var data = Dictionary<String, Any>();
    data["list"] = list;
    data["code"] = 0;
    prmResolve(data);
  }
  
}
