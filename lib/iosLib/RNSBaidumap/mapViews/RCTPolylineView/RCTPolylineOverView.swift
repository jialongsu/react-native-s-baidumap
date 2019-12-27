//
//  RCTMarker.swift
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/16.
//  Copyright © 2019年 Facebook. All rights reserved.
//

import UIKit

open class RCTPolylineOverView: UIView{

  lazy var polyline: CustomBMKPolyline = {
    return CustomBMKPolyline()
  }()
  var baiduMapView: BMKMapView?
  
  //props
  @objc var points: [NSDictionary] = [] {
    didSet {
      var coords = [CLLocationCoordinate2D]()
      for item in self.points {
        coords.append(CLLocationCoordinate2D(latitude: item["latitude"] as! Double, longitude: item["longitude"] as! Double))
      }
      self.polyline.setPolylineWithCoordinates(&coords, count: coords.count)
    }
  }
  
  @objc var color: String = "" {
     didSet {
        if(self.polyline.polylineView != nil) {
          self.polyline.polylineView!.strokeColor = UIColor.colorWithHex(hexStr: self.color)
        }else{
          self.polyline.color = self.color
        }
     }
   }
  
  @objc var width: NSNumber = 4 {
    didSet {
      if(self.polyline.polylineView != nil) {
        self.polyline.polylineView!.lineWidth = CGFloat(truncating: self.width)
      }else{
        self.polyline.width = self.width
      }
    }
  }

  public override init(frame: CGRect) {
    super.init(frame: CGRect.zero);
  }

  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  public func addToMap(_ mapView: BMKMapView!) {
    baiduMapView = mapView;
    mapView.add(polyline)
  }

//  public func remove(_ mapView: BMKMapView!) {
//    print("annotations-start", mapView.annotations.count)
////    mapView.removeAnnotation(annotation)
//    self.removeFromSuperview();
//    print("annotations-end", mapView.annotations.count)
//  }
  
}
