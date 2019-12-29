//
//  BaiduMapSearchManager.m
//  project
//
//  Created by Arno on 2019/12/17.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(BaiduMapSearch, NSObject)

  RCT_EXTERN_METHOD(
     searchInCity:(NSString *)city
     :(NSString *)keyword
     :(NSInteger *)pageNum
     :(RCTPromiseResolveBlock)resolve
     :(RCTPromiseRejectBlock)reject
   )

  RCT_EXTERN_METHOD(
    searchNearby:(NSDictionary *)options
    :(RCTPromiseResolveBlock)resolve
    :(RCTPromiseRejectBlock)reject
  )

  RCT_EXTERN_METHOD(
    requestSuggestion:(NSString *)city
    :(NSString *)keyword
    :(RCTPromiseResolveBlock)resolve
    :(RCTPromiseRejectBlock)reject
  )

  + (BOOL)requiresMainQueueSetup{
    return NO;
  }

@end
