//
//  RCTMarkerViewManager.m
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/16.
//  Copyright © 2019年 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RCTPolylineView, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(points, NSArray)
RCT_EXPORT_VIEW_PROPERTY(color, NSString)
RCT_EXPORT_VIEW_PROPERTY(width, NSNumber)

@end
