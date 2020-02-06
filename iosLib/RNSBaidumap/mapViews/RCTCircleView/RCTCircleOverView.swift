//
//  RCTCircleOverView.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

open class RCTCircleOverView: UIView{

  lazy var circle: CustomBMKCircle = {
    return CustomBMKCircle()
  }()
  var baiduMapView: BMKMapView?
  
  //props
  @objc var circleCenter: NSDictionary = [:] {
    didSet {
      self.circle.setCircleWithCenterCoordinate(CLLocationCoordinate2D(latitude: self.circleCenter["latitude"] as! Double, longitude: self.circleCenter["longitude"] as! Double), radius: self.circle.radius)
    }
  }
  
  @objc var color: String = "" {
     didSet {
        if(self.circle.circleView != nil) {
          self.circle.circleView!.strokeColor = UIColor.colorWithHex(hexStr: self.color)
        }else{
          self.circle.color = self.color
        }
     }
   }
  
  @objc var width: NSNumber = 4 {
    didSet {
      if(self.circle.circleView != nil) {
        self.circle.circleView!.lineWidth = CGFloat(truncating: self.width)
      }else{
        self.circle.width = self.width
      }
    }
  }
  
  @objc var fillColor: String = "" {
    didSet {
       if(self.circle.circleView != nil) {
        self.circle.circleView!.fillColor = UIColor.colorWithHex(hexStr: self.fillColor)
       }else{
         self.circle.fillColor = self.fillColor
       }
    }
  }
  
  @objc var radius: NSNumber = 1400 {
    didSet {
      self.circle.radius = CLLocationDistance(truncating: self.radius)
    }
  }
  
  @objc var fillColorAlpha: NSNumber = 0.5 {
    didSet {
      self.circle.fillColorAlpha = Float(truncating: self.fillColorAlpha)
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
    mapView.add(self.circle)
  }
  
}

