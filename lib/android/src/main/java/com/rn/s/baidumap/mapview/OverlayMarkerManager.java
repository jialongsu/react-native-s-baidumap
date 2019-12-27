package com.rn.s.baidumap.mapview;

import com.rn.s.baidumap.view.OverlayMarker;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by sujialong on 2019/7/9.
 */

public class OverlayMarkerManager extends SimpleViewManager<OverlayMarker> {
    @Override
    public String getName() {
        return "RCTMarkerView";
    }

    @Override
    protected OverlayMarker createViewInstance(ThemedReactContext reactContext) {
        return new OverlayMarker(reactContext);
    }

    @ReactProp(name = "title")
    public void setTitle(OverlayMarker overlayMarker, String title) {
        overlayMarker.setTitle(title);
    }

    @ReactProp(name = "location")
    public void setLocation(OverlayMarker overlayMarker, ReadableMap position) {
        if(position != null && position.toHashMap().size() > 0) {
            double latitude = position.getDouble("latitude");
            double longitude = position.getDouble("longitude");
            overlayMarker.setPosition(latitude, longitude);
        }
    }

    @ReactProp(name = "icon")
    public void setIcon(OverlayMarker overlayMarker, String uri) {
        overlayMarker.setIcon(uri);
    }

    /**
     * 是否开启近大远小效果
     * @param overlayMarker
     * @param perspective
     */
    @ReactProp(name = "perspective")
    public void setPerspective(OverlayMarker overlayMarker, boolean perspective) {
        overlayMarker.setPerspective(perspective);
    }

    @ReactProp(name = "alpha")
    public void setAlpha(OverlayMarker overlayMarker, float alpha) {
        overlayMarker.setAlpha(alpha);
    }

    @ReactProp(name = "rotate")
    public void setRotate(OverlayMarker overlayMarker, float rotate) {
        overlayMarker.setRotate(rotate);
    }

    /**
     * 是否平贴地图
     * @param overlayMarker
     * @param flat
     */
    @ReactProp(name = "flat")
    public void setFlat(OverlayMarker overlayMarker, boolean flat) {
        overlayMarker.setFlat(flat);
    }

    /**
     * 是否可拖拽
     * @param overlayMarker
     * @param draggable
     */
    @ReactProp(name = "draggable")
    public void setDraggable(OverlayMarker overlayMarker, boolean draggable) {
        overlayMarker.setDraggable(draggable);
    }

    /**
     * 是否显示infowind
     * @param overlayMarker
     * @param active
     */
    @ReactProp(name = "active")
    public void setActive(OverlayMarker overlayMarker, boolean active) {
        overlayMarker.setActive(active);
    }

    /**
     * 设置覆盖物 zIndex
     * @param overlayMarker
     * @param zIndex
     */
    @ReactProp(name = "zIndex")
    public void setZIndex(OverlayMarker overlayMarker, int zIndex) {
        overlayMarker.setZIndex(zIndex);
    }

    /**
     * InfoWindow的YOffset偏移
     * @param overlayMarker
     * @param infoWindowYOffset
     */
    @ReactProp(name = "infoWindowYOffset")
    public void setInfoWindowYOffset(OverlayMarker overlayMarker, int infoWindowYOffset) {
        overlayMarker.setInfoWindowYOffset(infoWindowYOffset);
    }

    @ReactProp(name = "infoWindowMinHeight")
    public void setInfoWindowMinHeight(OverlayMarker overlayMarker, int minHeight) {
        overlayMarker.setInfoWindowMinHeight(minHeight);
    }

    @ReactProp(name = "infoWindowMinWidth")
    public void setInfoWindowMinWidth(OverlayMarker overlayMarker, int minWidth) {
        overlayMarker.setInfoWindowMinWidth(minWidth);
    }

    @ReactProp(name = "infoWindowTextSize")
    public void setInfoWindowTextSize(OverlayMarker overlayMarker, int textSize) {
        overlayMarker.setInfoWindowTextSize(textSize);
    }

}
