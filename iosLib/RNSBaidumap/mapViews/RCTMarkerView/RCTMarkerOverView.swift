//
//  RCTMarker.swift
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/16.
//  Copyright © 2019年 Facebook. All rights reserved.
//

import UIKit

open class RCTMarkerOverView: UIView{

  var annotation: CustomAnnotation?
  var baiduMapView: BMKMapView?
  
  //props
  @objc var title: NSString = "" {
    didSet {
      annotation?.title = title as String;
    }
  }
  
  @objc var location: NSDictionary? {
    didSet {
      if(location?["latitude"] != nil) {
        let lat:Double = location?["latitude"] as! Double;
        let lng:Double = location?["longitude"] as! Double;
        annotation?.coordinate = CLLocationCoordinate2D(latitude: lat, longitude: lng)
      }
    }
  }
  
  @objc var icon: NSString? {
    didSet {
      if(self.icon?.hasPrefix("http://") ?? false || self.icon?.hasPrefix("https://") ?? false
        || self.icon?.hasPrefix("file://") ?? false || self.icon?.hasPrefix("asset://") ?? false) {
        self.downloadedFrom(imageurl: self.icon! as String);
      }else{
        annotation?.icon = UIImage(named:self.icon! as String)
      }
//      if(annotation?.annotationView != nil) {
//        //设置mark图片
//        self.downloadedFrom(imageurl: self.icon! as String);
//        annotation?.annotationView?.image = UIImage(named: self.icon! as String)
//      }else{
//        annotation?.icon = UIImage(named:self.icon! as String)
//      }
    }
  }
  
  @objc var draggable: Bool = false {
    didSet {
      if(annotation?.annotationView != nil) {
        //设置mark是否可拖拽
        annotation?.annotationView?.isDraggable = self.draggable
      }else{
        annotation?.draggable = self.draggable
      }
    }
  }
  
  @objc var active: Bool = false {
    didSet {
      if(annotation?.annotationView != nil) {
        //设置是否显示infowind,
        annotation?.annotationView?.isSelected = self.active;
        annotation?.annotationView?.canShowCallout = self.active;
      }else{
        annotation?.active = self.active
      }
    }
  }
  
  @objc var zIndex: NSNumber = 0 {
    didSet {
      if(annotation?.annotationView != nil) {
        //设置annotationView展示优先级
        annotation?.annotationView?.displayPriority = BMKFeatureDisplayPriority(truncating: self.zIndex)
      }else{
        annotation?.zIndex = self.zIndex
      }
    }
  }
  
  @objc var infoWindowYOffset: NSNumber = 0 {
    didSet {
      if(annotation?.annotationView != nil) {
        annotation?.annotationView?.calloutOffset = CGPoint(x: 0, y: Int(truncating: self.infoWindowYOffset));
      }else{
        annotation?.infoWindowYOffset = self.infoWindowYOffset
      }
    }
  }
  
  public override init(frame: CGRect) {
    super.init(frame: CGRect.zero);
    annotation = CustomAnnotation.init()
  }

  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  public func addToMap(_ mapView: BMKMapView!) {
    baiduMapView = mapView;
    mapView.addAnnotation(annotation)
  }
  
  /**
   加载网络图片与RN本地图片
   */
  func downloadedFrom(imageurl : String){
      //创建URL对象
      let url = URL(string: imageurl)!
      //创建请求对象
      let request = URLRequest(url: url)

      let session = URLSession.shared
      let dataTask = session.dataTask(with: request, completionHandler: {
          (data, response, error) -> Void in
          if error != nil{
              print(error.debugDescription)
          }else{
              //将图片数据赋予UIImage
              let img = UIImage(data:data!)
              // 这里需要改UI，需要回到主线程
              DispatchQueue.main.async(execute: {
                self.annotation?.annotationView?.image = img
              })
          }
      }) as URLSessionTask
      //使用resume方法启动任务
      dataTask.resume()
  }

//  public func remove(_ mapView: BMKMapView!) {
//    print("annotations-start", mapView.annotations.count)
////    mapView.removeAnnotation(annotation)
//    self.removeFromSuperview();
//    print("annotations-end", mapView.annotations.count)
//  }
  
}
