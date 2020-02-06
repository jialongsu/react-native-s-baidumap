//
//  CustomBMKPolygon.swift
//  reactNativeSBaidumap
//
//  Created by Arno on 2020/2/6.
//  Copyright © 2020 Facebook. All rights reserved.
//

import UIKit

open class CustomBMKPolygon: BMKPolygon {
  
  public var polygonView: BMKPolygonView?
  public var width: NSNumber = 5
  public var color: String = "#000000"
  public var fillColor: String = "#000000"
  public var fillColorAlpha: Float = 0.5
  
}
