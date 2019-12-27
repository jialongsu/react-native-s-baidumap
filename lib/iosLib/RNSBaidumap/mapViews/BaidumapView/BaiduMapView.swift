//
//  BaiduMapVIew.swift
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/15.
//  Copyright © 2019年 Facebook. All rights reserved.
//

import Foundation

@objc(BaiduMapView)
open class BaiduMapView: RCTViewManager{
  
  override open func view() -> UIView? {
    return BaiduMapOverView();
  }
  
  override open class func requiresMainQueueSetup() -> Bool {
    return true;
  }

}
