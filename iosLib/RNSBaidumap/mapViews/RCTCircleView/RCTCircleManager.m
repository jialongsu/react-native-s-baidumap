//
//  RCTCircleManager.m
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(RCTCircleView, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(circleCenter, NSDictionary)
RCT_EXPORT_VIEW_PROPERTY(color, NSString)
RCT_EXPORT_VIEW_PROPERTY(width, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(fillColor, NSString)
RCT_EXPORT_VIEW_PROPERTY(fillColorAlpha, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(radius, NSNumber)

@end
