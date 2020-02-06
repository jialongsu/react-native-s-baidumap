//
//  RCTArcOverView.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

open class RCTArcOverView: UIView{

  lazy var arcline: CustomBMKArcline = {
    return CustomBMKArcline()
  }()
  var baiduMapView: BMKMapView?
  
  //props
  @objc var points: [NSDictionary] = [] {
    didSet {
      let _points = UnsafeMutablePointer<CLLocationCoordinate2D>.allocate(capacity: 3)
      _points.advanced(by: 0).pointee = self.getPoint(data: self.points[0])
      _points.advanced(by: 1).pointee = self.getPoint(data: self.points[1])
      _points.advanced(by: 2).pointee = self.getPoint(data: self.points[2])
      self.arcline.setArclineWithCoordinates(_points)
    }
  }
  
  @objc var color: String = "" {
     didSet {
        if(self.arcline.arclineView != nil) {
          self.arcline.arclineView!.strokeColor = UIColor.colorWithHex(hexStr: self.color)
        }else{
          self.arcline.color = self.color
        }
     }
   }
  
  @objc var width: NSNumber = 4 {
    didSet {
      if(self.arcline.arclineView != nil) {
        self.arcline.arclineView!.lineWidth = CGFloat(truncating: self.width)
      }else{
        self.arcline.width = self.width
      }
    }
  }

  public override init(frame: CGRect) {
    super.init(frame: CGRect.zero);
  }

  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  func getPoint(data: NSDictionary) -> CLLocationCoordinate2D {
    return CLLocationCoordinate2D(latitude: data["latitude"] as! Double, longitude: data["longitude"] as! Double)
  }
  
  public func addToMap(_ mapView: BMKMapView!) {
    baiduMapView = mapView;
    mapView.add(self.arcline)
  }
  
}
