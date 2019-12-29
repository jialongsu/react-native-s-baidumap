//
//  RCTMarker.swift
//  reactNativeSBaiduMap
//
//  Created by Arno on 2019/7/16.
//  Copyright © 2019年 Facebook. All rights reserved.
//

import UIKit

open class CustomAnnotation: BMKPointAnnotation{

  public var draggable: Bool = false
  public var active: Bool = false
  public var zIndex: NSNumber = 0
  public var icon: UIImage?
  public var infoWindowYOffset: NSNumber = 0
  public var annotationView: BMKPinAnnotationView?
  
}
