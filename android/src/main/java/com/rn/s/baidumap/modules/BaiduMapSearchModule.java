package com.rn.s.baidumap.modules;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.List;

/**
 * Created by sujialong on 2019/7/10.
 */

public class BaiduMapSearchModule extends ReactContextBaseJavaModule {

    private Promise searchPromise;
    private ReactApplicationContext mReactContext;
    private PoiSearch mPoiSearch = PoiSearch.newInstance();
    private SuggestionSearch mSuggestionSearch = SuggestionSearch.newInstance();
    private int type = 0;

    public BaiduMapSearchModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
        //poi搜索
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                WritableArray data = Arguments.createArray();
                WritableMap writableMap = Arguments.createMap();

                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<PoiInfo> poiList = poiResult.getAllPoi();
                    for (PoiInfo info: poiList) {
                        WritableMap attr = Arguments.createMap();
                        attr.putString("name", info.name);
                        attr.putString("address", info.address);
                        attr.putString("city", info.city);
                        attr.putString("province", info.province);
                        attr.putString("uid", info.uid);
                        attr.putDouble("latitude", info.location.latitude);
                        attr.putDouble("longitude", info.location.longitude);
                        data.pushMap(attr);
                    }
                    writableMap.putInt("type", type);
                    writableMap.putInt("code", 1000);
                    writableMap.putArray("poiList", data);
                    searchPromise.resolve(writableMap);
//                onSendEvent("BaiduPoiSearch", writableMap);
                }else {
                    searchPromise.reject("-1", "搜索失败");
                }

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        //地点检索输入提示检索
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                WritableArray data = Arguments.createArray();
                WritableMap writableMap = Arguments.createMap();

                if (suggestionResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    List<SuggestionResult.SuggestionInfo> poiLIst = suggestionResult.getAllSuggestions();
                    for (SuggestionResult.SuggestionInfo info: poiLIst) {
                        WritableMap attr = Arguments.createMap();
                        attr.putString("address", info.getAddress());
                        attr.putString("city", info.getCity());
                        attr.putString("district", info.getDistrict());
                        attr.putString("uid", info.getUid());
                        attr.putString("key", info.getKey());
                        attr.putString("name", info.getKey());
                        attr.putDouble("latitude", info.getPt().latitude);
                        attr.putDouble("longitude", info.getPt().longitude);
                        data.pushMap(attr);
                    }
                    writableMap.putInt("type", type);
                    writableMap.putInt("code", 1000);
                    writableMap.putArray("poiList", data);
                    searchPromise.resolve(writableMap);
//                onSendEvent("BaiduRequestSuggestion", writableMap);
                }else {
                    searchPromise.reject("-1", "搜索失败");
                }

            }
        });
    }

    @Override
    public String getName() {
        return "BaiduMapSearch";
    }

    /**
     * POI城市内检索（关键字检索）
     * @param city
     * @param keyword
     * @param pageNum
     */
    @ReactMethod
    public void searchInCity(String city, String keyword, int pageNum, Promise promise) {
        type = 0;
        searchPromise = promise;
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(city) //必填
                .keyword(keyword) //必填
                .pageNum(pageNum));
    }

    /**
     *周边检索
     * @param options
     */
    @ReactMethod
    public void searchNearby(ReadableMap options, Promise promise) {
        double lat = options.getDouble("latitude");
        double lng = options.getDouble("longitude");
        String keyword = options.getString("keyword");
        int radius = 1000;
        int pageNum = 20;
        type = 1;
        searchPromise = promise;

        if(options.hasKey("radius")) {
            radius = options.getInt("radius");
        }if(options.hasKey("pageNum")) {
            pageNum = options.getInt("pageNum");
        }
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .location(new LatLng(lat, lng)) //必填
                .radius(radius)
                .keyword(keyword)
                .pageNum(pageNum));
    }

    /**
     * 输入提示检索
     * @param city
     * @param keyword
     */
    @ReactMethod
    public void requestSuggestion(String city, String keyword, Promise promise) {
        searchPromise = promise;
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                .city(city)
                .keyword(keyword));
    }

    public void onSendEvent(String eventName, WritableMap writableMap) {
        mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, writableMap);
    }

}
