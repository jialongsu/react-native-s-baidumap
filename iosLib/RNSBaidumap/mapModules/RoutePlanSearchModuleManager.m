//
//  RoutePlanSearchModuleManager.m
//  reactNativeSBaidumap
//
//  Created by Arno on 2019/12/28.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RoutePlanSearchModule, NSObject)

RCT_EXTERN_METHOD(
  walkingRouteSearch:(NSDictionary *)options
  :(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)
RCT_EXTERN_METHOD(
  drivingRouteSearch:(NSDictionary *)options
  :(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)
RCT_EXTERN_METHOD(
  bikingRouteSearch:(NSDictionary *)options
  :(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)
RCT_EXTERN_METHOD(
  transitRoutePlan:(NSDictionary *)options
  :(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)

+ (BOOL)requiresMainQueueSetup{
  return NO;
}

@end
