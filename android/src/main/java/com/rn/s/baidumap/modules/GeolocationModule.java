package com.rn.s.baidumap.modules;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * Created by sujialong on 2019/7/10.
 */

public class GeolocationModule extends ReactContextBaseJavaModule implements MKOfflineMapListener {

    private Promise geoPromise;
    private ReactApplicationContext mReactContext;
    private LocationClient mLocationClient = null;
    private LocationClientOption mClientOption = null;
    private GeoCoder mCoder = GeoCoder.newInstance();;
    private MKOfflineMap mOffline = null;
    private boolean isDownLoadedOfflineMap = false;
    private boolean downLoadOfflineMap = true;

    public GeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        mLocationClient = new LocationClient(reactContext);
        mClientOption = new LocationClientOption();
        mClientOption.setCoorType("bd09ll");
//        mClientOption.setOpenAutoNotifyMode(); //打开后位置移动会返回位置信息
        mClientOption.setScanSpan(8000);
        mClientOption.setOpenGps(true);
        mClientOption.setEnableSimulateGps(true);
        mClientOption.setIsNeedAddress(true);
        mClientOption.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(mClientOption);
        //定位监听
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                WritableMap data = Arguments.createMap();
                data.putString("addrStr", location.getAddrStr());
                data.putString("locationDescribe", location.getLocationDescribe());
                data.putString("adCode", location.getAdCode());
                data.putString("city", location.getCity());
                data.putString("cityCode", location.getCityCode());
                data.putString("country", location.getCountry());
                data.putString("countryCode", location.getCountryCode());
                data.putString("Province", location.getProvince());
                data.putString("district", location.getDistrict());
                data.putInt("locationWhere", location.getLocationWhere());
                data.putString("street", location.getStreet());
                data.putString("streetNumber", location.getStreetNumber());
                data.putString("coordinateType", location.getCoorType());
                data.putDouble("radius", location.getRadius());
                data.putDouble("latitude", location.getLatitude());
                data.putDouble("longitude", location.getLongitude());
                data.putDouble("speed", location.getSpeed());
                data.putDouble("direction", location.getDirection());
                data.putString("buildingId", location.getBuildingID());
                data.putString("buildingName", location.getBuildingName());
                data.putInt("locationType", location.getLocType());
                onSendEvent("baiduMapLocation", data);
            }
        });
        //地理编码监听
        mCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                WritableMap data = Arguments.createMap();
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                    data.putInt("errcode", -1);
                    geoPromise.reject("-1", geoCodeResult.error + "");
                } else {
                    LatLng latLng = geoCodeResult.getLocation();
                    data.putDouble("longitude", latLng.longitude);
                    data.putDouble("latitude", latLng.latitude);
                    geoPromise.resolve(data);
                }
//                onSendEvent("baiduMapGeocode", data);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                WritableMap data = Arguments.createMap();
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                    data.putInt("errcode", -1);
                    geoPromise.reject("-1", reverseGeoCodeResult.error + "");
                } else {
                    LatLng latLng = reverseGeoCodeResult.getLocation();
                    ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
                    data.putDouble("longitude", latLng.longitude);
                    data.putDouble("latitude", latLng.latitude);
                    data.putString("address", reverseGeoCodeResult.getAddress());
                    data.putString("locationDescribe", reverseGeoCodeResult.getSematicDescription());
                    data.putInt("adCode", reverseGeoCodeResult.getAdcode());
                    data.putInt("cityCode", reverseGeoCodeResult.getCityCode());
                    data.putString("city", addressComponent.city);
                    data.putString("country", addressComponent.countryName);
                    data.putString("province", addressComponent.province);
                    data.putString("district", addressComponent.district);
                    data.putString("street", addressComponent.street);
                    data.putString("streetNumber", addressComponent.streetNumber);
                    data.putInt("countryCode", addressComponent.countryCode);
                    data.putString("town", addressComponent.town);

                    WritableArray list = Arguments.createArray();
                    List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                    if(poiList != null && poiList.size() > 0) {
                        for (PoiInfo info: poiList) {
                            WritableMap attr = Arguments.createMap();
                            attr.putString("name", info.name);
                            attr.putString("address", info.address);
                            attr.putString("city", info.city);
                            attr.putDouble("latitude", info.location.latitude);
                            attr.putDouble("longitude", info.location.longitude);
                            list.pushMap(attr);
                        }
                        data.putArray("poiList", list);
                    }

                    //下载离线地图
                    if(!isDownLoadedOfflineMap && downLoadOfflineMap) {
                        downLoadOfflineMap(addressComponent.city);
                    }

                    geoPromise.resolve(data);
                }
//                onSendEvent("baiduMapReverseGeoCode", data);
            }
        });
    }

    @Override
    public String getName() {
        return "BaiduGeolocationModule";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("BMK09LL", 1);
        constants.put("BMK09MC", 2);
        constants.put("GCJ02", 3);
        constants.put("WGS84", 4);
        return constants;
    }

    @ReactMethod
    public void setOptions(ReadableMap options) {
        LocationClientOption clientOption = mClientOption;
        int scanSpan = 8000;
        boolean openGps = true;
        String coorType = "bd09ll";

        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        if(options.hasKey("coorType")) {
            int coorTypeNumber = options.getInt("coorType");
            switch (coorTypeNumber) {
                case 1:
                    coorType = "BD09ll";
                    break;
                case 2:
                    coorType = "BD09";
                    break;
                case 3:
                    coorType = "GCJ02";
                    break;
                default:
                    coorType = "WGS84";
                    break;
            }
        }
        if(options.hasKey("scanSpan")) {
            scanSpan = options.getInt("scanSpan");
        }
        if(options.hasKey("openGps")) {
            openGps = options.getBoolean("openGps");
        }
        if(options.hasKey("downLoadOfflineMap")) {
            downLoadOfflineMap = options.getBoolean("downLoadOfflineMap");
        }
        clientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        clientOption.setCoorType(coorType);
        clientOption.setScanSpan(scanSpan);
        clientOption.setOpenGps(openGps);
        clientOption.setIsNeedAddress(true);
        clientOption.setIsNeedLocationDescribe(true);
        clientOption.setEnableSimulateGps(true);
        if(options.hasKey("needDeviceDirect")){
            clientOption.setNeedDeviceDirect(options.getBoolean("needDeviceDirect"));
        }
//        clientOption.setOpenAutoNotifyMode();//打开后位置移动会返回位置信息， setScanSpan会失效
        mLocationClient.setLocOption(clientOption);
    }

    @ReactMethod
    public void start() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        mLocationClient.start();
    }

    @ReactMethod
    public void stop() {
        mLocationClient.stop();
    }

    @ReactMethod
    public void geocode(String address, String city, Promise promise) {
        geoPromise = promise;
        mCoder.geocode(new GeoCodeOption().address(address).city(city));
    }

    @ReactMethod
    public void reverseGeoCode(double lat, double lng, Promise promise) {
        geoPromise = promise;
        mCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(lat, lng)));
    }

    public void downLoadOfflineMap(String cityName) {
        ArrayList<MKOLUpdateElement> elements = mOffline.getAllUpdateInfo();
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityName);
        boolean isDownLoaded = false;

        if(elements != null && elements.size() > 0) {
            for (MKOLUpdateElement element : elements){
                if(element.cityName.equals(cityName)) {
                    isDownLoaded = true;
                }
            }
        }
        if(records != null && records.size() > 0 && !isDownLoaded) {
            mOffline.start(records.get(0).cityID);
        }
    }

    public void onSendEvent(String eventName, WritableMap writableMap) {
        mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, writableMap);
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
//                    Log.d("OfflineDemo", String.format("%s : %d%%", update.cityName, update.ratio));
                    isDownLoadedOfflineMap = true;
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }
}
