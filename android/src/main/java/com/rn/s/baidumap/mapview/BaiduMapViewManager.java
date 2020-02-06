package com.rn.s.baidumap.mapview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.rn.s.baidumap.utils.Utils;
import com.rn.s.baidumap.view.OverlayArc;
import com.rn.s.baidumap.view.OverlayMarker;
import com.rn.s.baidumap.view.OverlayView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by sujialong on 2019/7/8.
 */

public class BaiduMapViewManager extends ViewGroupManager<MapView> implements LifecycleEventListener {

    BaiduMap mBaiduMap;
    MapView mapView;
    ThemedReactContext mReactContext;
    // 定位图层显示方式
    private MyLocationConfiguration.LocationMode mCurrentMode;
    public HashMap<String, OverlayMarker> markerMap = new HashMap<String, OverlayMarker>();
    private static final int SET_MAP_CENTER = 0;
    private static final int SET_MAP_ZOOM = 1;

    @Override
    public String getName() {
        return "BaiduMapView";
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        mapView = new MapView(reactContext);
        mBaiduMap = mapView.getMap();
        mReactContext = reactContext;
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        return mapView;
    }

    @Override
    public void addView(MapView parent, View child, int index) {
        super.addView(parent, child, index);
        if (child instanceof OverlayView) {
            ((OverlayView) child).addTopMap(parent.getMap());
            if(child instanceof OverlayMarker) {
                OverlayMarker overlayMarker = (OverlayMarker) child;
                Marker marker = (Marker) overlayMarker.getOverlayView();
                if(marker != null) {
                    markerMap.put(marker.getId(), (OverlayMarker)child);
                }
            }
        }
    }

    @Override
    public void removeViewAt(MapView mapView, int index) {
        View view = mapView.getChildAt(index);
        if (view instanceof OverlayView) {
            if(view instanceof OverlayMarker) {
                OverlayMarker overlayMarker = (OverlayMarker) view;
                Marker marker = (Marker) overlayMarker.getOverlayView();
                if(marker != null) {
                    markerMap.remove(marker.getId());
                }
            }
            ((OverlayView)view).remove();
        }
//        mapView.removeView(view);
        super.removeViewAt(mapView, index);
    }

    @ReactProp(name = "zoomControlsVisible", defaultBoolean = false)
    public void setZoomControlsVisible(MapView mapView, boolean zoomControlsVisible) {
        mapView.showZoomControls(zoomControlsVisible);
    }

    @ReactProp(name="isTrafficEnabled", defaultBoolean = false)
    public void setTrafficEnabled(MapView mapView, boolean trafficEnabled) {
        mapView.getMap().setTrafficEnabled(trafficEnabled);
    }

    @ReactProp(name="isBaiduHeatMapEnabled", defaultBoolean = false)
    public void setBaiduHeatMapEnabled(MapView mapView, boolean baiduHeatMapEnabled) {
        mapView.getMap().setBaiduHeatMapEnabled(baiduHeatMapEnabled);
    }

    @ReactProp(name = "baiduMapType")
    public void setMapType(MapView mapView, int mapType) {
        mapView.getMap().setMapType(mapType);
    }

    @ReactProp(name="zoomMaxLevel")
    public void setZoomMaxLevel(MapView mapView, float zoom) {
        BaiduMap map = mapView.getMap();
        float minLevel = map.getMinZoomLevel();
        map.setMaxAndMinZoomLevel(zoom, minLevel);
    }

    @ReactProp(name="zoomMinLevel")
    public void setZoomMinLevel(MapView mapView, float zoom) {
        BaiduMap map = mapView.getMap();
        float maxLevel = map.getMaxZoomLevel();
        map.setMaxAndMinZoomLevel(maxLevel, zoom);
    }

    @ReactProp(name="zoom")
    public void setZoom(MapView mapView, float zoom) {
        this.setMapZoom(mapView, zoom);
    }

    @ReactProp(name="locationEnabled", defaultBoolean = false)
    public void setLocationEnabled(MapView mapView, boolean enabled) {
        mapView.getMap().setMyLocationEnabled(enabled);
    }

    @ReactProp(name="myLocationData")
    public void setMyLocationData(MapView mapView, ReadableMap myLocationData) {
        if(myLocationData.hasKey("radius") && myLocationData.hasKey("direction")
        && myLocationData.hasKey("latitude") && myLocationData.hasKey("longitude")) {
            int radius = myLocationData.getInt("radius");
            int direction = myLocationData.getInt("direction");
            double latitude = myLocationData.getDouble("latitude");
            double longitude = myLocationData.getDouble("longitude");
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(radius)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(direction)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            mapView.getMap().setMyLocationData(locData);
            //设置模式
            if(myLocationData.hasKey("locationMode")) {
                int newFillColor = 4521984;
                int newStrokeColor = 4653056;
                int locationMode = 0;
                locationMode = myLocationData.getInt("locationMode");
                switch (locationMode) {
                    case 1:
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        break;
                    case 2:
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        break;
                    default:
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        break;
                }
                if(myLocationData.hasKey("fillColor")) {
                    String fillColor = myLocationData.getString("fillColor");
                    newFillColor = Color.parseColor(fillColor);
                }
                if(myLocationData.hasKey("strokeColor")) {
                    String strokeColor = myLocationData.getString("strokeColor");
                    newStrokeColor = Color.parseColor(strokeColor);
                }
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode,true,null,
                        newFillColor, newStrokeColor));
                MapStatus.Builder builder = new MapStatus.Builder();
                if(myLocationData.hasKey("overlook")) {
                    builder.overlook(myLocationData.getInt("overlook"));
                }
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    @ReactProp(name="centerLatLng")
    public void setCenter(MapView mapView, ReadableMap position) {
        this.setMapCenter(mapView, position);
    }

    @ReactProp(name="zoomGesturesEnabled", defaultBoolean = true)
    public void setZoomGesturesEnabled(MapView mapView, boolean zoomGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setZoomGesturesEnabled(zoomGesturesEnabled);
    }

    @ReactProp(name="scrollGesturesEnabled", defaultBoolean = true)
    public void setScrollGesturesEnabled(MapView mapView, boolean scrollGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setScrollGesturesEnabled(scrollGesturesEnabled);
    }

    @ReactProp(name="overlookingGesturesEnabled", defaultBoolean = true)
    public void setOverlookingGesturesEnabled(MapView mapView, boolean overlookingGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setOverlookingGesturesEnabled(overlookingGesturesEnabled);
    }

    @ReactProp(name="rotateGesturesEnabled", defaultBoolean = true)
    public void setRotateGesturesEnabled(MapView mapView, boolean rotateGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setRotateGesturesEnabled(rotateGesturesEnabled);
    }

    @ReactProp(name="mapCustomStyleFileName")
    public void setMapCustomStyle(MapView mapView, String mapCustomStylePath) {
        boolean isEnable = !mapCustomStylePath.toString().trim().equals("");
        if(isEnable){
            String customStyleFilePath = getCustomStyleFilePath(mReactContext, mapCustomStylePath+".json");
            if (!customStyleFilePath.contains("null")) {
                mapView.setMapCustomStylePath(customStyleFilePath);
            }else{
                Log.i("error", "本地个性化地图模板文件路径没有找到");
            }
        }
        mapView.setMapCustomStyleEnable(isEnable);
    }

    public void setMapCenter(MapView mapView, ReadableMap position) {
        if(position != null && position.toHashMap().size() > 0) {
            double latitude = position.getDouble("latitude");
            double longitude = position.getDouble("longitude");
            LatLng point = new LatLng(latitude, longitude);
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(point)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mapView.getMap().setMapStatus(mapStatusUpdate);
        }
    }

    @ReactProp(name="zoomToSpanMarkers")
    public void setZoomToSpanMarkers(MapView mapView, ReadableArray zoomToSpanMarkers) {
        if (zoomToSpanMarkers.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(int i = 0, len = zoomToSpanMarkers.size(); i< len; i++) {
                ReadableMap item = zoomToSpanMarkers.getMap(i);
                // polyline 中的点可能太多，只按marker 缩放
                builder.include(new LatLng(item.getDouble("latitude"), item.getDouble("longitude")));
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
            MapStatusUpdate msu = MapStatusUpdateFactory.zoomBy(-0.8f);
            mBaiduMap.setMapStatus(msu);
        }
    }

    public void setMapZoom(MapView mapView, float zoom) {
        MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> map = this.CreateMap(
        "setMapCenter", SET_MAP_CENTER,
        "setZoom", SET_MAP_ZOOM
        );

        return map;
    }

    @Override
    public void receiveCommand(MapView mapView, int commandId, @Nullable ReadableArray args) {
        switch (commandId){
            case SET_MAP_CENTER:
                ReadableMap position = args.getMap(0);
                this.setMapCenter(mapView, position);
                break;
            case SET_MAP_ZOOM:
                float zoom = (float) args.getDouble(0);
                this.setMapZoom(mapView, zoom);
                break;
        }
    }

    @Override
    protected void addEventEmitters(final ThemedReactContext reactContext, final MapView view) {
        super.addEventEmitters(reactContext, view);
        final BaiduMap baiduMap = view.getMap();
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                OverlayMarker selMarker = markerMap.get(marker.getId());
                if( selMarker != null) {
                    boolean active = selMarker.getPropActive();
                    if(active) {
                        selMarker.setActive(!selMarker.getActive());
                    }
                }
                onSendEvent(reactContext, view, "onMarkerClick", Arguments.createMap());
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                WritableMap writableMap = Utils.groupLatLng(latLng);
                for (String key : markerMap.keySet()) {
                    OverlayMarker selMarker = markerMap.get(key);
                    if( selMarker != null) {
                        selMarker.setActive(false);
                    }
                }
                //                mBaiduMap.hideInfoWindow();
                onSendEvent(reactContext, view, "onMapClick", writableMap);
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                LatLng latLng = mapPoi.getPosition();
                WritableMap writableMap = Utils.groupLatLng(latLng);
                writableMap.putString("name", mapPoi.getName());
                writableMap.putString("id", mapPoi.getUid());
                onSendEvent(reactContext, view, "onMapPoiClick", writableMap);
            }

        });
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            /**
             * 长按地图
             */
            public void onMapLongClick(LatLng latLng) {
                onSendEvent(reactContext, view, "onMapLongClick", Utils.groupLatLng(latLng));
            }
        });
        baiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            /**
             * 双击地图
             */
            public void onMapDoubleClick(LatLng latLng) {
                onSendEvent(reactContext, view, "onMapDoubleClick", Utils.groupLatLng(latLng));
            }
        });
        /**
         * 地图状态发生变化
         */
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
                WritableMap writableMap = Utils.groupLatLng(status.target);
                writableMap.putDouble("zoomLevel", baiduMap.getMapStatus().zoom);
                onSendEvent(reactContext, view, "onMapStatusChangeStart", writableMap);
            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            public void onMapStatusChangeFinish(MapStatus status) {
                WritableMap writableMap = Utils.groupLatLng(status.target);
                writableMap.putDouble("zoomLevel", baiduMap.getMapStatus().zoom);
                onSendEvent(reactContext, view, "onMapStatusChangeFinish", writableMap);
            }

            public void onMapStatusChange(MapStatus status) {
                WritableMap writableMap = Utils.groupLatLng(status.target);
                writableMap.putDouble("zoomLevel", baiduMap.getMapStatus().zoom);
                onSendEvent(reactContext, view, "onMapStatusChange", writableMap);
            }
        });
        /**
         * 地图加载完成
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                onSendEvent(reactContext, view, "onMapLoaded", Arguments.createMap());
            }
        });
        /**
         * 拖拽marker
         */
        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(Marker marker) {
                //对marker处理拖拽逻辑
                onSendEvent(reactContext, view, "onMarkerDrag",  Utils.groupLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                onSendEvent(reactContext, view, "onMarkerDragEnd",  Utils.groupLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                onSendEvent(reactContext, view, "onMarkerDragStart",  Utils.groupLatLng(marker.getPosition()));
            }
        });
    }

    @Nullable
    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Map<String, String>> map = MapBuilder.of(
            "onMarkerClick", MapBuilder.of("registrationName", "onMarkerClick"),
            "onMapClick", MapBuilder.of("registrationName", "onMapClick"),
            "onMapPoiClick", MapBuilder.of("registrationName", "onMapPoiClick"),
            "onMapLongClick", MapBuilder.of("registrationName", "onMapLongClick"),
            "onMapDoubleClick", MapBuilder.of("registrationName", "onMapDoubleClick"),
            "onMapStatusChangeStart", MapBuilder.of("registrationName", "onMapStatusChangeStart")
        );

        map.putAll(MapBuilder.of(
            "onMapStatusChangeFinish", MapBuilder.of("registrationName", "onMapStatusChangeFinish"),
            "onMapStatusChange", MapBuilder.of("registrationName", "onMapStatusChange"),
            "onMapLoaded", MapBuilder.of("registrationName", "onMapLoaded"),
            "onMarkerDrag", MapBuilder.of("registrationName", "onMarkerDrag"),
            "onMarkerDragEnd", MapBuilder.of("registrationName", "onMarkerDragEnd"),
            "onMarkerDragStart", MapBuilder.of("registrationName", "onMarkerDragStart")
        ));
        return map;
    }

    public static <K, V> Map<K, V> CreateMap(
            K k1, V v1, K k2, V v2) {
        Map map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);

        return map;
    }

    public void onSendEvent(ThemedReactContext reactContext, MapView view,
                            String eventName, WritableMap writableMap) {
        reactContext.getJSModule(RCTEventEmitter.class)
                .receiveEvent(view.getId(), eventName, writableMap);
    }

    @Override
    public void onHostResume() {
        mapView.onResume();
    }

    @Override
    public void onHostPause() {
        mapView.onPause();
    }

    @Override
    public void onHostDestroy() {
        // 清除所有图层
        mapView.getMap().clear();
        // MapView的生命周期与Activity同步，当activity销毁时必须调用MapView.destroy()
        mapView.onDestroy();
    }

    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;

        try {
            inputStream = context.getAssets().open("customConfigdir/" + customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();

            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("CustomMapDemo", "Close stream failed", e);
                return null;
            }
        }

        return parentPath + "/" + customStyleFileName;
    }

}
