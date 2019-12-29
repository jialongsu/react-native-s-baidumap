package com.rn.s.baidumap.mapview;

import android.graphics.Color;

import com.rn.s.baidumap.view.OverlayPolyline;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujialong on 2019/7/9.
 */

public class OverlayPolylineManager extends SimpleViewManager<OverlayPolyline> {

    @Override
    public String getName() {
        return "RCTPolylineView";
    }

    @Override
    protected OverlayPolyline createViewInstance(ThemedReactContext reactContext) {
        return new OverlayPolyline(reactContext);
    }

    @ReactProp(name = "points")
    public void setPoints(OverlayPolyline overlayPolyline, ReadableArray points) {
        List list = new ArrayList();
        for (int i = 0, len = points.size(); i < len; i++){
            ReadableMap item = points.getMap(i);
            LatLng latLng = new LatLng(item.getDouble("latitude"), item.getDouble("longitude"));
            list.add(latLng);
        }
        overlayPolyline.setPoints(list);
    }

    @ReactProp(name = "color")
    public void setColor(OverlayPolyline overlayPolyline, String color) {
        overlayPolyline.setColor(Color.parseColor(color));
    }

    @ReactProp(name = "width")
    public void setColor(OverlayPolyline overlayPolyline, int width) {
        overlayPolyline.setLineWidth(width);
    }
}
