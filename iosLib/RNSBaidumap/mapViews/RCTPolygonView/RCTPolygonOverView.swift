//
//  RCTPolygonOverView.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

open class RCTPolygonOverView: UIView{

  lazy var polygon: CustomBMKPolygon = {
    return CustomBMKPolygon()
  }()
  var baiduMapView: BMKMapView?
  
  //props
  @objc var points: [NSDictionary] = [] {
    didSet {
      let _points = UnsafeMutablePointer<CLLocationCoordinate2D>.allocate(capacity: self.points.count)
      for (index, item) in self.points.enumerated() {
        _points.advanced(by: index).pointee = CLLocationCoordinate2D(latitude: item["latitude"] as! Double, longitude: item["longitude"] as! Double)
      }
      self.polygon.setPolygonWithCoordinates(_points, count: self.points.count)
    }
  }
  
  @objc var color: String = "" {
     didSet {
      if(self.polygon.polygonView != nil) {
          self.polygon.polygonView!.strokeColor = UIColor.colorWithHex(hexStr: self.color)
        }else{
          self.polygon.color = self.color
        }
     }
   }
  
  @objc var width: NSNumber = 4 {
    didSet {
      if(self.polygon.polygonView != nil) {
        self.polygon.polygonView!.lineWidth = CGFloat(truncating: self.width)
      }else{
        self.polygon.width = self.width
      }
    }
  }
  
  @objc var fillColor: String = "" {
    didSet {
       if(self.polygon.polygonView != nil) {
        self.polygon.polygonView!.fillColor = UIColor.colorWithHex(hexStr: self.fillColor)
       }else{
         self.polygon.fillColor = self.fillColor
       }
    }
  }
  
  @objc var fillColorAlpha: NSNumber = 0.5 {
    didSet {
      self.polygon.fillColorAlpha = Float(truncating: self.fillColorAlpha)
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
    mapView.add(self.polygon)
  }
  
}
