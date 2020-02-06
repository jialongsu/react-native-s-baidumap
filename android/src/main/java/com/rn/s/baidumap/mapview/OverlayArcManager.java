package com.rn.s.baidumap.mapview;

import android.graphics.Color;

import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.rn.s.baidumap.view.OverlayArc;

import java.util.ArrayList;
import java.util.List;

public class OverlayArcManager extends SimpleViewManager<OverlayArc> {

    @Override
    public String getName() {
        return "RCTArcView";
    }

    @Override
    protected OverlayArc createViewInstance(ThemedReactContext reactContext) {
        return new OverlayArc(reactContext);
    }

    @ReactProp(name = "points")
    public void setPoints(OverlayArc overlayArc, ReadableArray points) {
        List list = new ArrayList();
        for (int i = 0, len = points.size(); i < len; i++){
            ReadableMap item = points.getMap(i);
            LatLng latLng = new LatLng(item.getDouble("latitude"), item.getDouble("longitude"));
            list.add(latLng);
        }
        overlayArc.setPoints(list);
    }

    @ReactProp(name = "color")
    public void setColor(OverlayArc overlayArc, String color) {
        overlayArc.setColor(Color.parseColor(color));
    }

    @ReactProp(name = "width")
    public void setWidth(OverlayArc overlayArc, int width) {
        overlayArc.setWidth(width);
    }
}
