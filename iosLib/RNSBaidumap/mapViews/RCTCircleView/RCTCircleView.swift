//
//  RCTCircleView.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

@objc(RCTCircleView)
class RCTCircleView: RCTViewManager{
  
  override func view() -> UIView? {
    return RCTCircleOverView()
  }
  
  override class func requiresMainQueueSetup() -> Bool {
    return false;
  }
  
}
