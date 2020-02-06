//
//  RCTPolygonView.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

@objc(RCTPolygonView)
class RCTPolygonView: RCTViewManager{
  
  override func view() -> UIView? {
    return RCTPolygonOverView()
  }
  
  override class func requiresMainQueueSetup() -> Bool {
    return false;
  }
  
}
