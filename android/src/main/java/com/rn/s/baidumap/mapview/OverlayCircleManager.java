package com.rn.s.baidumap.mapview;

import android.graphics.Color;

import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.rn.s.baidumap.view.OverlayArc;
import com.rn.s.baidumap.view.OverlayCircle;

import java.util.ArrayList;
import java.util.List;

public class OverlayCircleManager extends SimpleViewManager<OverlayCircle> {

    @Override
    public String getName() {
        return "RCTCircleView";
    }

    @Override
    protected OverlayCircle createViewInstance(ThemedReactContext reactContext) {
        return new OverlayCircle(reactContext);
    }

    @ReactProp(name = "circleCenter")
    public void setCircleCenter(OverlayCircle overlayCircle, ReadableMap latlng) {
        if(latlng != null && latlng.toHashMap().size() > 0) {
            double latitude = latlng.getDouble("latitude");
            double longitude = latlng.getDouble("longitude");
            overlayCircle.setCenter(new LatLng(latitude, longitude));
        }
    }

    @ReactProp(name = "color")
    public void setColor(OverlayCircle overlayCircle, String color) {
        overlayCircle.setColor(Color.parseColor(color));
    }

    @ReactProp(name = "width")
    public void setWidth(OverlayCircle overlayCircle, int width) {
        overlayCircle.setWidth(width);
    }

    @ReactProp(name = "radius")
    public void setRadius(OverlayCircle overlayCircle, int radius) {
        overlayCircle.setRadius(radius);
    }

    @ReactProp(name = "fillColor")
    public void setFillColor(OverlayCircle overlayCircle, String fillColor) {
        overlayCircle.setFillColor(Color.parseColor(fillColor));
    }
}
