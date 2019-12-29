package com.rn.s.baidumap.utils;

import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

/**
 * Created by sujialong on 2019/7/8.
 */

public class Utils {

     public static WritableMap groupLatLng(LatLng latLng) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("latitude", latLng.latitude);
        writableMap.putDouble("longitude", latLng.longitude);
        return writableMap;
    }

}
