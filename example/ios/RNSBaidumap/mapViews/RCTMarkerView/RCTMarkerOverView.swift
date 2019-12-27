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
      let lat:Double = location?["latitude"] as! Double;
      let lng:Double = location?["longitude"] as! Double;
      annotation?.coordinate = CLLocationCoordinate2D(latitude: lat, longitude: lng)
    }
  }
  
  @objc var icon: NSString? {
    didSet {
      if(annotation?.annotationView != nil) {
        //设置mark图片
        annotation?.annotationView?.image = UIImage(named: self.icon! as String)
      }else{
        annotation?.icon = self.icon! as String
      }
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
        annotation?.annotationView?.setSelected(self.active, animated: false)
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

//  public func remove(_ mapView: BMKMapView!) {
//    print("annotations-start", mapView.annotations.count)
////    mapView.removeAnnotation(annotation)
//    self.removeFromSuperview();
//    print("annotations-end", mapView.annotations.count)
//  }
  
}
