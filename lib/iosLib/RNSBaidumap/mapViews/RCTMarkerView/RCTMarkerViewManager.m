//
//  RCTMarkerViewManager.m
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/16.
//  Copyright © 2019年 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RCTMarkerView, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(title, NSString)
RCT_EXPORT_VIEW_PROPERTY(icon, NSString)
RCT_EXPORT_VIEW_PROPERTY(location, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(draggable, BOOL)
RCT_EXPORT_VIEW_PROPERTY(active, BOOL)
RCT_EXPORT_VIEW_PROPERTY(zIndex, NSNumber)

@end
