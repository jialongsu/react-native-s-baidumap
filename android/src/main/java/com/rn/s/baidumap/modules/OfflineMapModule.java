package com.rn.s.baidumap.modules;

import android.util.Log;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class OfflineMapModule extends ReactContextBaseJavaModule implements MKOfflineMapListener {

    private Promise globalPromise;
    private MKOfflineMap mOffline = null;

    public OfflineMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
    }

    @NonNull
    @Override
    public String getName() {
        return "OfflineMapModule";
    }

    @ReactMethod
    public void start(int cityId) {
        mOffline.start(cityId);
    }

    @ReactMethod
    public void update(int cityId) {
        mOffline.update(cityId);
    }

    @ReactMethod
    public void stop(int cityId) {
        mOffline.pause(cityId);
    }

    @ReactMethod
    public void remove(int cityId) {
        mOffline.remove(cityId);
    }

    @ReactMethod
    public void getHotCityList(Promise promise) {
        globalPromise = promise;
        // 获取热闹城市列表
        ArrayList<MKOLSearchRecord> records = mOffline.getHotCityList();
        WritableArray array = Arguments.createArray();
        if (records != null) {
            array = this.groupCityList(records);
        }
        groupResult(array);
    }

    @ReactMethod
    public void getOfflineAllCityList(Promise promise) {
        globalPromise = promise;
        // 获取热闹城市列表
        ArrayList<MKOLSearchRecord> records = mOffline.getOfflineCityList();
        WritableArray array = Arguments.createArray();
        if (records != null) {
            array = this.groupCityList(records);
        }
        groupResult(array);
    }

    @ReactMethod
    public void searchCity(String city, Promise promise) {
        globalPromise = promise;
        // 获取热闹城市列表
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city);
        if (records != null) {
            groupResult(this.groupCityList(records));
        }else{
            promise.reject("-1", city+"不支持城市离线地图");
        }
    }

    @ReactMethod
    public void getDownloadedCityList(Promise promise) {
        globalPromise = promise;
        // 获取热闹城市列表
        ArrayList<MKOLUpdateElement> records = mOffline.getAllUpdateInfo();
        WritableArray array = Arguments.createArray();
        if (records != null) {
            for (MKOLUpdateElement item : records) {
                WritableMap writableMap = Arguments.createMap();
                writableMap.putInt("cityID", item.cityID);
                writableMap.putString("cityName", item.cityName);
                writableMap.putInt("ratio", item.ratio);
                writableMap.putInt("status", item.status);
                writableMap.putBoolean("update", item.update);
                writableMap.putString("size", this.formatDataSize(item.size));
                writableMap.putString("serversize", this.formatDataSize(item.serversize));
                writableMap.putInt("level", item.level);
                writableMap.putDouble("longitude", item.geoPt.longitude);
                writableMap.putDouble("latitude", item.geoPt.latitude);
                array.pushMap(writableMap);
            }
        }
        groupResult(array);
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                Log.i("test", "TYPE_DOWNLOAD_UPDATE");
                // 处理下载进度更新提示
                if (update != null) {
                    Log.d("OfflineDemo", String.format("%s : %d%%", update.cityName, update.ratio));
//                    isDownLoadedOfflineMap = true;
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                Log.i("test", "TYPE_NEW_OFFLINE");
                // 有新离线地图安装
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                Log.i("test", "TYPE_VER_UPDATE");
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }

    public WritableArray groupCityList(ArrayList<MKOLSearchRecord> records) {
        WritableArray array = Arguments.createArray();
        for (MKOLSearchRecord item : records) {
            WritableMap writableMap = Arguments.createMap();
            int cityType = item.cityType;
            writableMap.putInt("cityType", cityType);
            writableMap.putInt("cityID", item.cityID);
            writableMap.putString("cityName", item.cityName);
            writableMap.putString("dataSize", this.formatDataSize(item.dataSize));
            if(cityType == 1) {
                writableMap.putArray("childCities", groupCityList(item.childCities));
            }
            array.pushMap(writableMap);
        }
        return array;
    }

    public String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    public void groupResult(WritableArray writableArray) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putArray("list", writableArray);
        writableMap.putInt("code", 0);
        globalPromise.resolve(writableMap);
    }

}
