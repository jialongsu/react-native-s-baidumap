//
//  BaiduMapViewManager.m
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/15.
//  Copyright © 2019年 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import <React/RCTLog.h>

@interface RCT_EXTERN_MODULE(BaiduMapView, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(zoom, float)
RCT_EXPORT_VIEW_PROPERTY(zoomMaxLevel, float)
RCT_EXPORT_VIEW_PROPERTY(zoomMinLevel, float)
RCT_EXPORT_VIEW_PROPERTY(isTrafficEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(isBaiduHeatMapEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(zoomControlsVisible, BOOL)
RCT_EXPORT_VIEW_PROPERTY(baiduMapType, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(centerLatLng, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(zoomToSpanMarkers, NSArray)
RCT_EXPORT_VIEW_PROPERTY(zoomGesturesEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(scrollGesturesEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(rotateGesturesEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(overlookingGesturesEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(mapCustomStyleFileName, NSString)
RCT_EXPORT_VIEW_PROPERTY(locationEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(myLocationData, NSDictionary)

RCT_EXPORT_VIEW_PROPERTY(onMapClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapLongClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapDoubleClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapPoiClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapStatusChangeStart, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapStatusChange, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapStatusChangeFinish, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapLoaded, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMarkerDrag, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMarkerClick, RCTBubblingEventBlock)

//RCT_EXPORT_METHOD(setMapCenter:(nonnull NSNumber*) reactTag
//                  centerLatLng:(NSDictionary *)centerLatLng) {
//  NSLog(@"打印log: %@", centerLatLng);
//  [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
//      UIView *view = viewRegistry[reactTag];
//      if (!view || ![view isKindOfClass:[BaiduMapView class]]) {
////          BaiduMapView* mapView = (BaiduMapView*)view;
////          RCTLogError(@"Cannot find NativeView with tag #%@", mapView);
//          return;
//      }
//  }];
//}

@end
