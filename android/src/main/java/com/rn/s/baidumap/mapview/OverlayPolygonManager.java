package com.rn.s.baidumap.mapview;

import android.graphics.Color;

import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.rn.s.baidumap.view.OverlayCircle;
import com.rn.s.baidumap.view.OverlayPolygon;

import java.util.ArrayList;
import java.util.List;

public class OverlayPolygonManager extends SimpleViewManager<OverlayPolygon> {

    @Override
    public String getName() {
        return "RCTPolygonView";
    }

    @Override
    protected OverlayPolygon createViewInstance(ThemedReactContext reactContext) {
        return new OverlayPolygon(reactContext);
    }

    @ReactProp(name = "points")
    public void setPoints(OverlayPolygon overlayPolygon, ReadableArray points) {
        List list = new ArrayList();
        for (int i = 0, len = points.size(); i < len; i++){
            ReadableMap item = points.getMap(i);
            LatLng latLng = new LatLng(item.getDouble("latitude"), item.getDouble("longitude"));
            list.add(latLng);
        }
        overlayPolygon.setPoints(list);
    }

    @ReactProp(name = "color")
    public void setColor(OverlayPolygon overlayPolygon, String color) {
        overlayPolygon.setColor(Color.parseColor(color));
    }

    @ReactProp(name = "width")
    public void setWidth(OverlayPolygon overlayPolygon, int width) {
        overlayPolygon.setWidth(width);
    }

    @ReactProp(name = "fillColor")
    public void setFillColor(OverlayPolygon overlayPolygon, String fillColor) {
        overlayPolygon.setFillColor(Color.parseColor(fillColor));
    }
}
