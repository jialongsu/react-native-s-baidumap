//
//  BaiduGeolocationModuleManager.m
//  project
//
//  Created by Arno on 2019/12/17.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(BaiduGeolocationModule, NSObject)

  RCT_EXTERN_METHOD(setOptions:(NSDictionary *)options)
  RCT_EXTERN_METHOD(initSDK:(NSString *)key)
  RCT_EXTERN_METHOD(start)
  RCT_EXTERN_METHOD(stop)
  RCT_EXTERN_METHOD(
    geocode:(NSString *)address
    :(NSString *)city
  )
  RCT_EXTERN_METHOD(
    reverseGeoCode:(double *)lat
    :(double *)lng
  )

  + (BOOL)requiresMainQueueSetup{
    return NO;
  }

@end
