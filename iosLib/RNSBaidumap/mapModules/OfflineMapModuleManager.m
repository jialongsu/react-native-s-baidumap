//
//  OfflineMapModuleManager.m
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/1/5.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(OfflineMapModule, NSObject)

RCT_EXTERN_METHOD(start:(NSInteger *)cityId)
RCT_EXTERN_METHOD(update:(NSInteger *)cityId)
RCT_EXTERN_METHOD(stop:(NSInteger *)cityId)
RCT_EXTERN_METHOD(remove:(NSInteger *)cityId)
RCT_EXTERN_METHOD(
  getHotCityList:(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)
RCT_EXTERN_METHOD(
  getOfflineAllCityList:(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)
RCT_EXTERN_METHOD(
  searchCity:(NSString *)city
  :(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)
RCT_EXTERN_METHOD(
  getDownloadedCityList:(RCTPromiseResolveBlock)resolve
  :(RCTPromiseRejectBlock)reject
)

+ (BOOL)requiresMainQueueSetup{
  return NO;
}

@end

