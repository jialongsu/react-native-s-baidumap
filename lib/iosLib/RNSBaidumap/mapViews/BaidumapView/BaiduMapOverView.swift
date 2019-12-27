//
//  BaiduMapBaseVIew.swift
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/15.
//  Copyright © 2019年 Facebook. All rights reserved.
//
import UIKit

class BaiduMapOverView: BMKMapView, BMKMapViewDelegate, BMKPoiSearchDelegate{
  
  @objc public var onMapClick: RCTBubblingEventBlock!
  @objc public var onMapLongClick: RCTBubblingEventBlock!
  @objc public var onMapDoubleClick: RCTBubblingEventBlock!
  @objc public var onMapPoiClick: RCTBubblingEventBlock!
  @objc public var onMapStatusChangeStart: RCTBubblingEventBlock!
  @objc public var onMapStatusChange: RCTBubblingEventBlock!
  @objc public var onMapStatusChangeFinish: RCTBubblingEventBlock!
  @objc public var onMapLoaded: RCTBubblingEventBlock!
  @objc public var onMarkerDragStart: RCTBubblingEventBlock!
  @objc public var onMarkerDrag: RCTBubblingEventBlock!
  @objc public var onMarkerDragEnd: RCTBubblingEventBlock!
  
  public override init(frame: CGRect) {
    super.init(frame: CGRect.zero)
    self.delegate = self;
  }
  
  required public init?(coder aDecoder: NSCoder) {
    fatalError("init(coder:) has not been implemented")
  }
  
  override open func viewWillAppear() {
       super.viewWillAppear()
       //当mapView即将被显示的时候调用，恢复之前存储的mapView状态
       self.viewWillAppear()
   }
   
  override open func viewWillDisappear() {
       super.viewWillDisappear()
       //当mapView即将被隐藏的时候调用，存储当前mapView的状态
       self.viewWillDisappear()
   }
  
  @objc
  func setZoom(_ zoom: Float) -> Void {
    self.zoomLevel = zoom
  }
  
  @objc
  func setZoomMaxLevel(_ zoom: Float) -> Void {
    self.maxZoomLevel = zoom
  }
  
  @objc
  func setZoomMinLevel(_ zoom: Float) -> Void {
    self.minZoomLevel = zoom
  }
  
  @objc
  func setBaiduMapType(_ type: NSNumber) -> Void {
    var mapType: BMKMapType;
    switch type {
    case 1:
      //设置当前地图类型为标准地图
      mapType = BMKMapType.standard
    case 2:
      //设置当前地图类型为卫星地图
      mapType = BMKMapType.satellite
    default:
      //设置当前地图类型为空白地图
      mapType = BMKMapType.none
      break;
    }
    self.mapType = mapType;
  }
  
  @objc
  func setCenterLatLng(_ center: NSDictionary!) -> Void {
    let lat:Double = center?["latitude"] as! Double;
    let lng:Double = center?["longitude"] as! Double;
    self.centerCoordinate = CLLocationCoordinate2DMake(lat, lng);
  }
  
  @objc
  func setZoomControlsVisible(_ zoomControlsVisible: Bool) -> Void {
    self.showMapScaleBar = zoomControlsVisible;
  }

  //设置地图使显示区域显示所有annotations,如果数组中只有一个则直接设置地图中心为annotation的位置
  @objc
  func setZoomToSpanMarkers(_ annotations: NSArray!) -> Void {
    self.showAnnotations(self.annotations, animated: true)
  }
  
  @objc
  func setMapCenter(_ center: NSDictionary!) -> Void {
    let lat:Double = center?["latitude"] as! Double;
    let lng:Double = center?["longitude"] as! Double;
    self.centerCoordinate = CLLocationCoordinate2DMake(lat, lng);
  }
  
  public func mapView(_ mapView: BMKMapView!, onClickedMapBlank coordinate: CLLocationCoordinate2D) {
    self.onMapClick(["latitude": coordinate.latitude, "longitude":coordinate.longitude]);
  }
  
  public func mapview(_ mapView: BMKMapView!, onLongClick coordinate: CLLocationCoordinate2D) {
    self.onMapLongClick(["latitude": coordinate.latitude, "longitude":coordinate.longitude]);
  }
  
  public func mapview(_ mapView: BMKMapView!, onDoubleClick coordinate: CLLocationCoordinate2D) {
    self.onMapDoubleClick(["latitude": coordinate.latitude, "longitude":coordinate.longitude]);
  }
  
  public func mapView(_ mapView: BMKMapView!, onClickedMapPoi mapPoi: BMKMapPoi!) {
    let data = [
      "name": mapPoi.text!,
      "id": mapPoi.uid!,
      "latitude": mapPoi.pt.latitude,
      "longitude": mapPoi.pt.longitude
      ] as [String : Any];
    self.onMapPoiClick(data);
  }
  
  public func mapStatusDidChanged(_ mapView: BMKMapView!) {
    let latlng: CLLocationCoordinate2D! = mapView.getMapStatus().targetGeoPt;
    let data = [
      "latitude": latlng.latitude,
      "longitude": latlng.longitude,
      "zoomLevel": self.zoomLevel
      ] as [String : Any];
    if(self.onMapStatusChange != nil) {
      self.onMapStatusChange(data);
    }
  }
  
  public func mapView(_ mapView: BMKMapView!, regionWillChangeAnimated animated: Bool) {
    let latlng: CLLocationCoordinate2D! = mapView.getMapStatus().targetGeoPt;
    let data = [
      "latitude": latlng.latitude,
      "longitude": latlng.longitude,
      "zoomLevel": self.zoomLevel
      ] as [String : Any];
    self.onMapStatusChangeStart(data);
  }
  
  public func mapView(_ mapView: BMKMapView!, regionDidChangeAnimated animated: Bool, reason: BMKRegionChangeReason) {
    let latlng: CLLocationCoordinate2D! = mapView.getMapStatus().targetGeoPt;
    let data = [
      "latitude": latlng.latitude,
      "longitude": latlng.longitude,
      "zoomLevel": self.zoomLevel
      ] as [String : Any];
     self.onMapStatusChangeFinish(data);
  }
  
  public func mapViewDidFinishLoading(_ mapView: BMKMapView!) {
    let data = [:] as [String : Any];
    self.onMapLoaded(data)
  }
  
  public func mapView(_ mapView: BMKMapView!, annotationView view: BMKAnnotationView!, didChangeDragState newState: UInt, fromOldState oldState: UInt) {
    let latlng: CLLocationCoordinate2D! = view.annotation.coordinate;
    let data = [
      "latitude": latlng.latitude,
      "longitude": latlng.longitude
      ] as [String : Any];
    if(newState == BMKAnnotationViewDragStateStarting) {
      self.onMarkerDragStart(data)
    }else if(newState == BMKAnnotationViewDragStateDragging) {
      self.onMarkerDrag(data)
    }else{
      //BMKAnnotationViewDragStateEnding
      self.onMarkerDragEnd(data)
    }
  }
  
  //MARK:BMKMapViewDelegate
  /**
   根据anntation生成对应的annotationView
   @param mapView 地图View
   @param annotation 指定的标注
   @return 生成的标注View
   */
  public func mapView(_ mapView: BMKMapView!, viewFor annotation: BMKAnnotation!) -> BMKAnnotationView! {
    let newAnnotation = annotation as? CustomAnnotation;
    let annotationViewIdentifier = "com.Baidu.BMKPinAnnotation";
    var annotationView: BMKPinAnnotationView? = mapView.dequeueReusableAnnotationView(withIdentifier: annotationViewIdentifier) as? BMKPinAnnotationView
    if annotationView == nil {
      annotationView = BMKPinAnnotationView.init(annotation: annotation, reuseIdentifier: annotationViewIdentifier)
      //设置mark图片
      annotationView?.image = UIImage(named: newAnnotation?.icon ?? "")
      //当设为YES并实现了setCoordinate:方法时，支持将annotationView在地图上拖动
      annotationView?.isDraggable = newAnnotation!.draggable
      //默认为NO,当为YES时为会弹出气泡
      annotationView?.isSelected = newAnnotation!.active
      //当选中其他annotation时，当前annotation的泡泡是否隐藏，默认值为NO
      annotationView?.hidePaopaoWhenSelectOthers = false
      //annotationView展示优先级
      annotationView?.displayPriority = BMKFeatureDisplayPriority(truncating: newAnnotation!.zIndex)
      //保存当前annotationView，用以刷新视图
      newAnnotation?.annotationView = annotationView;
      return annotationView
    }
    return nil
  }
  
//MARK:BMKMapViewDelegate
/**
 根据overlay生成对应的BMKOverlayView
 @param mapView 地图View
 @param overlay 指定的overlay
 @return 生成的覆盖物View
 */
  public func mapView(_ mapView: BMKMapView!, viewFor overlay: BMKOverlay!) -> BMKOverlayView! {
    if overlay.isKind(of: BMKPolyline.self) {
      let newOverlay = overlay as! CustomBMKPolyline
      //初始化一个overlay并返回相应的BMKPolylineView的实例
      let polylineView = BMKPolylineView(polyline: overlay as? BMKPolyline)
      //设置polylineView的画笔颜色
      polylineView?.strokeColor = UIColor.colorWithHex(hexStr: newOverlay.color)
      //设置polylineView的画笔宽度为32
      polylineView?.lineWidth = newOverlay.width as! CGFloat
      newOverlay.polylineView = polylineView!;
      return polylineView
    }
    return nil
  }
  
  open override func didAddSubview(_ subview: UIView) {
    if(subview is RCTMarkerOverView) {
      let _subview = subview as? RCTMarkerOverView;
      _subview?.addToMap(self)
    }else if(subview is RCTPolylineOverView) {
      let _subview = subview as? RCTPolylineOverView;
      _subview?.addToMap(self)
    }
  }
  
  open override func willRemoveSubview(_ subview: UIView) {
      if(subview is RCTMarkerOverView) {
        let _subview = subview as? RCTMarkerOverView;
        self.removeAnnotation(_subview?.annotation)
      }else if(subview is RCTPolylineOverView) {
        let _subview = subview as? RCTPolylineOverView;
        self.remove(_subview?.polyline)
      }
  }

}
