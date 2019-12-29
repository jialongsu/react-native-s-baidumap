package com.rn.s.baidumap.modules;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.VehicleInfo;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class RoutePlanSearchModule extends ReactContextBaseJavaModule implements OnGetRoutePlanResultListener {

    private RoutePlanSearch mSearch = null;
    private Promise searchPromise;
    // 驾车路线规划参数
    private DrivingRoutePlanOption mDrivingRoutePlanOption;
    // 骑行路线规划参数
    BikingRoutePlanOption bikingRoutePlanOption;
    // 市内公交换乘路线规划参数
    private TransitRoutePlanOption mTransitRoutePlanOption;

    public RoutePlanSearchModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    @NonNull
    @Override
    public String getName() {
        return "RoutePlanSearchModule";
    }

    /**
     * 步行路线结果回调
     * @param walkingRouteResult  步行路线结果
     */
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            searchPromise.reject("-1", walkingRouteResult.error + ":起终点或途经点地址有岐义");
            return;
        }
        if (walkingRouteResult == null || walkingRouteResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            searchPromise.reject("-1", walkingRouteResult.error + ":抱歉，未找到结果");
            return;
        }
        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<WalkingRouteLine> walkingRouteLineList =  walkingRouteResult.getRouteLines();
            WritableArray writableArray = Arguments.createArray();
            for (WalkingRouteLine item : walkingRouteLineList) {
                WritableMap writableMap = Arguments.createMap();
                WritableArray walkingStepList = Arguments.createArray();
                List<WalkingRouteLine.WalkingStep> walkingSteps = item.getAllStep();
                writableMap.putString("title", item.getTitle());
                writableMap.putInt("distance", item.getDistance());
                writableMap.putInt("duration", item.getDuration());

                for (WalkingRouteLine.WalkingStep zItem : walkingSteps) {
                    WritableMap walkingStepMap = Arguments.createMap();
                    WritableArray writablelatLngList = Arguments.createArray();
                    List<LatLng> latLngList = zItem.getWayPoints();
                    walkingStepMap.putInt("direction", zItem.getDirection());//该路段起点方向值
                    walkingStepMap.putString("entranceInstructions", zItem.getEntranceInstructions());//获取路段入口提示信息
                    walkingStepMap.putString("exitInstructions", zItem.getExitInstructions());//获取路段出口指示信息
                    walkingStepMap.putString("instructions", zItem.getInstructions());//获取路段整体指示信息
                    walkingStepMap.putInt("duration", zItem.getDuration());
                    walkingStepMap.putInt("distance", zItem.getDistance());

                    for (LatLng latLng : latLngList) {
                        WritableMap latLngMap = Arguments.createMap();
                        latLngMap.putDouble("latitude", latLng.latitude);
                        latLngMap.putDouble("longitude", latLng.longitude);
                        writablelatLngList.pushMap(latLngMap);
                    }
                    walkingStepMap.putArray("wayPoints", writablelatLngList);
                    walkingStepList.pushMap(walkingStepMap);
                }
                writableMap.putArray("allStep", walkingStepList);
                writableArray.pushMap(writableMap);
            }
            groupResult(writableArray);
        } else {
            searchPromise.reject("-1", walkingRouteResult.error + "");
        }
    }

    /**
     * 驾车路线结果回调
     * @param drivingRouteResult  驾车路线结果
     */
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (drivingRouteResult != null && drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            searchPromise.reject("-1", drivingRouteResult.error + ":起终点或途经点地址有岐义");
            return;
        }
        if (drivingRouteResult == null || drivingRouteResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            searchPromise.reject("-1", drivingRouteResult.error + ":抱歉，未找到结果");
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<DrivingRouteLine> drivingRouteLines =  drivingRouteResult.getRouteLines();
            WritableArray writableArray = Arguments.createArray();
            for (DrivingRouteLine item : drivingRouteLines) {
                WritableMap writableMap = Arguments.createMap();
                WritableArray drivingStepList = Arguments.createArray();
                List<DrivingRouteLine.DrivingStep> drivingSteps = item.getAllStep();

                writableMap.putString("title", item.getTitle());
                writableMap.putInt("distance", item.getDistance());
                writableMap.putInt("duration", item.getDuration());
                writableMap.putInt("congestionDistance", item.getCongestionDistance());//获取拥堵米数
                writableMap.putInt("lightNum", item.getLightNum());//获取路线途中的红绿灯个数

                for (DrivingRouteLine.DrivingStep zItem : drivingSteps) {
                    WritableMap walkingStepMap = Arguments.createMap();
                    WritableArray writablelatLngList = Arguments.createArray();
                    List<LatLng> latLngList = zItem.getWayPoints();
                    walkingStepMap.putInt("direction", zItem.getDirection());//该路段起点方向值
                    walkingStepMap.putString("entranceInstructions", zItem.getEntranceInstructions());//获取路段入口提示信息
                    walkingStepMap.putString("exitInstructions", zItem.getExitInstructions());//获取路段出口指示信息
                    walkingStepMap.putString("instructions", zItem.getInstructions());//获取路段整体指示信息
                    walkingStepMap.putInt("numTurns", zItem.getNumTurns());//路段转弯类型
                    walkingStepMap.putInt("roadLevel", zItem.getRoadLevel());//获取道路类型
                    walkingStepMap.putInt("duration", zItem.getDuration());
                    walkingStepMap.putInt("distance", zItem.getDistance());

                    for (LatLng latLng : latLngList) {
                        WritableMap latLngMap = Arguments.createMap();
                        latLngMap.putDouble("latitude", latLng.latitude);
                        latLngMap.putDouble("longitude", latLng.longitude);
                        writablelatLngList.pushMap(latLngMap);
                    }
                    walkingStepMap.putArray("wayPoints", writablelatLngList);
                    drivingStepList.pushMap(walkingStepMap);
                }
                writableMap.putArray("allStep", drivingStepList);
                writableArray.pushMap(writableMap);
            }
            groupResult(writableArray);
        }

    }

    /**
     * 骑行路线结果回调
     * @param result  骑行路线结果
     */
    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {
        if (result != null && result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            searchPromise.reject("-1", result.error + ":起终点或途经点地址有岐义");
            return;
        }
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            searchPromise.reject("-1", result.error + ":抱歉，未找到结果");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<BikingRouteLine> bikingRouteLines =  result.getRouteLines();
            WritableArray writableArray = Arguments.createArray();
            for (BikingRouteLine item : bikingRouteLines) {
                WritableMap writableMap = Arguments.createMap();
                WritableArray bikingStepList = Arguments.createArray();
                List<BikingRouteLine.BikingStep> bikingSteps = item.getAllStep();

                writableMap.putString("title", item.getTitle());
                writableMap.putInt("distance", item.getDistance());
                writableMap.putInt("duration", item.getDuration());

                for (BikingRouteLine.BikingStep zItem : bikingSteps) {
                    WritableMap bikingStepMap = Arguments.createMap();
                    WritableArray writablelatLngList = Arguments.createArray();
                    List<LatLng> latLngList = zItem.getWayPoints();
                    bikingStepMap.putInt("direction", zItem.getDirection());//该路段起点方向值
                    bikingStepMap.putString("entranceInstructions", zItem.getEntranceInstructions());//获取路段入口提示信息
                    bikingStepMap.putString("exitInstructions", zItem.getExitInstructions());//获取路段出口指示信息
                    bikingStepMap.putString("instructions", zItem.getInstructions());//获取路段整体指示信息
                    bikingStepMap.putString("turnType", zItem.getTurnType());//获取行驶转向方向（如"直行", "左前方转弯"）
                    bikingStepMap.putInt("duration", zItem.getDuration());
                    bikingStepMap.putInt("distance", zItem.getDistance());

                    for (LatLng latLng : latLngList) {
                        WritableMap latLngMap = Arguments.createMap();
                        latLngMap.putDouble("latitude", latLng.latitude);
                        latLngMap.putDouble("longitude", latLng.longitude);
                        writablelatLngList.pushMap(latLngMap);
                    }
                    bikingStepMap.putArray("wayPoints", writablelatLngList);
                    bikingStepList.pushMap(bikingStepMap);
                }

                writableMap.putArray("allStep", bikingStepList);
                writableArray.pushMap(writableMap);
            }
            groupResult(writableArray);
        }
    }

    /**
     * 市内公交路线结果回调
     * @param result  市内公交路线结果
     */
    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result != null && result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            searchPromise.reject("-1", result.error + ":起终点或途经点地址有岐义");
            return;
        }
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            searchPromise.reject("-1", result.error + ":抱歉，未找到结果");
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<TransitRouteLine> transitRouteLines =  result.getRouteLines();
            WritableArray writableArray = Arguments.createArray();

            for (TransitRouteLine item : transitRouteLines) {
                WritableMap writableMap = Arguments.createMap();
                WritableArray transitStepList = Arguments.createArray();
                List<TransitRouteLine.TransitStep> transitSteps = item.getAllStep();

                writableMap.putString("title", item.getTitle());
                writableMap.putInt("distance", item.getDistance());
                writableMap.putInt("duration", item.getDuration());

                for (TransitRouteLine.TransitStep zItem : transitSteps) {
                    WritableMap transitStepMap = Arguments.createMap();
                    WritableArray writablelatLngList = Arguments.createArray();
                    List<LatLng> latLngList = zItem.getWayPoints();
                    VehicleInfo vehicleInfo = zItem.getVehicleInfo();

                    transitStepMap.putInt("duration", zItem.getDuration());
                    transitStepMap.putInt("distance", zItem.getDistance());
                    transitStepMap.putString("instructions", zItem.getInstructions());//获取路段整体指示信息
                    if(vehicleInfo != null) {
                        transitStepMap.putString("title", vehicleInfo.getTitle());//获取该交通路线的名称
                        transitStepMap.putString("uid", vehicleInfo.getUid());//获取该交通路线的标识
                        transitStepMap.putInt("zonePrice", vehicleInfo.getZonePrice());//获取该交通路线的所乘区间的区间价格
                        transitStepMap.putInt("totalPrice", vehicleInfo.getTotalPrice());//获取该交通路线的全程价格
                        transitStepMap.putInt("passStationNum", vehicleInfo.getPassStationNum());//获取该交通路线的所乘站数
                    }

                    for (LatLng latLng : latLngList) {
                        WritableMap latLngMap = Arguments.createMap();
                        latLngMap.putDouble("latitude", latLng.latitude);
                        latLngMap.putDouble("longitude", latLng.longitude);
                        writablelatLngList.pushMap(latLngMap);
                    }
                    transitStepMap.putArray("wayPoints", writablelatLngList);
                    transitStepList.pushMap(transitStepMap);
                }
                writableMap.putArray("allStep", transitStepList);
                writableArray.pushMap(writableMap);
            }
            groupResult(writableArray);
        }
    }

    /**
     * 跨城公交路线结果回调
     * @param result  跨城公交路线结果
     */
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
    }

    /**
     * 室内路线结果回调
     * @param indoorRouteResult  室内路线结果
     */
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @ReactMethod
    public void walkingRouteSearch(ReadableMap params, Promise promise) {

        searchPromise = promise;
        // 设置起终点信息 起点参数
        Map<String, PlanNode> planNodeMap = getPlanNode(params);
        PlanNode startNode = planNodeMap.get("startNode");// 起点参数
        PlanNode endNode = planNodeMap.get("endNode");// 终点参数
        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(startNode) // 起点
                .to(endNode)); // 终点
    }

    @ReactMethod
    public void drivingRouteSearch(ReadableMap params, Promise promise) {
        int trafficPolicyType = params.hasKey("trafficPolicyType") ? params.getInt("trafficPolicyType") : 0;
        int drivingPolicyType = params.hasKey("drivingPolicyType") ? params.getInt("drivingPolicyType") : 0;

        searchPromise = promise;
        if(mDrivingRoutePlanOption == null) {
            mDrivingRoutePlanOption = new DrivingRoutePlanOption();
        }
        // 设置起终点信息 起点参数
        Map<String, PlanNode> planNodeMap = getPlanNode(params);
        PlanNode startNode = planNodeMap.get("startNode");// 起点参数
        PlanNode endNode = planNodeMap.get("endNode");// 终点参数
        // 是否开起路况默认不开启
        DrivingRoutePlanOption.DrivingTrafficPolicy trafficType = DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH;
        switch (trafficPolicyType){
            case 0:
                trafficType = DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH;
                break;
            case 1:
                trafficType = DrivingRoutePlanOption.DrivingTrafficPolicy.ROUTE_PATH_AND_TRAFFIC;
                break;
        }
        mDrivingRoutePlanOption.trafficPolicy(trafficType);
        // 时间优先策略，  默认时间优先
        DrivingRoutePlanOption.DrivingPolicy drivingPolicy = DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST;
        switch (drivingPolicyType){
            case 0:
                // 时间优先策略，  默认时间优先
                drivingPolicy = DrivingRoutePlanOption.DrivingPolicy.ECAR_TIME_FIRST;
                break;
            case 1:
                // 躲避拥堵策略
                drivingPolicy = DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM;
                break;
            case 2:
                //最短距离策略
                drivingPolicy = DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST;
                break;
            case 3:
                // 费用较少策略
                drivingPolicy = DrivingRoutePlanOption.DrivingPolicy.ECAR_FEE_FIRST;
                break;
        }
        mDrivingRoutePlanOption.policy(drivingPolicy);
        // 发起驾车路线规划
        mSearch.drivingSearch(mDrivingRoutePlanOption.from(startNode).to(endNode));
    }

    @ReactMethod
    public void bikingRouteSearch(ReadableMap params, Promise promise) {
        int ridingType = params.hasKey("ridingType") ? params.getInt("ridingType") : 0;

        searchPromise = promise;
        if(bikingRoutePlanOption == null) {
            bikingRoutePlanOption = new BikingRoutePlanOption();
        }
        // 设置起终点信息 起点参数
        Map<String, PlanNode> planNodeMap = getPlanNode(params);
        PlanNode startNode = planNodeMap.get("startNode");// 起点参数
        PlanNode endNode = planNodeMap.get("endNode");// 终点参数
        // 骑行类型（0：普通骑行模式，1：电动车模式）默认是普通模式
        bikingRoutePlanOption.ridingType(ridingType);
        // 发起骑行路线规划检索
        mSearch.bikingSearch(bikingRoutePlanOption.from(startNode).to(endNode));
    }

    @ReactMethod
    public void transitRoutePlan(ReadableMap params, Promise promise) {
        String city = params.getString("city");
        int policyType = params.hasKey("policyType") ? params.getInt("policyType") : 0;

        searchPromise = promise;
        if(mTransitRoutePlanOption == null) {
            mTransitRoutePlanOption = new TransitRoutePlanOption();
        }
        // 设置起终点信息 起点参数
        Map<String, PlanNode> planNodeMap = getPlanNode(params);
        PlanNode startNode = planNodeMap.get("startNode");// 起点参数
        PlanNode endNode = planNodeMap.get("endNode");// 终点参数
        // 设置换乘策略
        TransitRoutePlanOption.TransitPolicy transitPolicy = TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST;
        switch (policyType) {
            case 0:
                // 时间优先策略，默认时间优先
                transitPolicy = TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST;
                break;
            case 1:
                // 最少换乘
                transitPolicy = TransitRoutePlanOption.TransitPolicy.EBUS_TRANSFER_FIRST;
                break;
            case 2:
                // 最少步行距离
                transitPolicy = TransitRoutePlanOption.TransitPolicy.EBUS_WALK_FIRST;
                break;
            case 3:
                // 不含地铁
                transitPolicy = TransitRoutePlanOption.TransitPolicy.EBUS_NO_SUBWAY;
                break;
        }
        mTransitRoutePlanOption.policy(transitPolicy);
        mTransitRoutePlanOption.from(startNode) // 设置起点参数
                .city(city.toString().trim()) // 设置换乘路线规划城市，起终点中的城市将会被忽略，这里写的是终点城市
                .to(endNode); // 设置终点
        // 发起骑行路线规划检索
        mSearch.transitSearch(mTransitRoutePlanOption);
    }

    public Map<String, PlanNode> getPlanNode(ReadableMap params) {
        Map<String, PlanNode> map = new HashMap<String, PlanNode>();
        String msg = "";
        // 设置起终点信息 起点参数
        PlanNode startNode;
        // 终点参数
        PlanNode endNode;

        if(params.hasKey("startLocation") && params.hasKey("endLocation")) {
            ReadableMap startLocation = params.getMap("startLocation");
            ReadableMap endLocation = params.getMap("endLocation");
            LatLng startLatLng = new LatLng(startLocation.getDouble("latitude"), startLocation.getDouble("longitude"));
            LatLng endLatLng = new LatLng(endLocation.getDouble("latitude"), endLocation.getDouble("longitude"));
            startNode = PlanNode.withLocation(startLatLng);
            endNode = PlanNode.withLocation(endLatLng);
        } else {
            String startAddres = "";
            String endAddres = "";
            String startCity = "";
            String endCity = "";
            boolean isHasCity = params.hasKey("city");
            boolean isHasStartCity = params.hasKey("startCity");
            boolean isHasEndCity = params.hasKey("endCity");

            if(params.hasKey("startAddres")) {
                startAddres = params.getString("startAddres");
            }else{
                msg = "startAddres参数未传入！";
            }
            if(params.hasKey("endAddres")) {
                endAddres = params.getString("endAddres");
            }else{
                msg = "endAddres参数未传入！";
            }
            if(isHasStartCity) {
                startCity = params.getString("startCity");
            }else{
                msg = !isHasCity ? "startCity参数未传入, startCity与city需传入一个！" : "";
            }
            if(isHasEndCity) {
                endCity = params.getString("endCity");
            }else{
                msg = !isHasCity ? "endCity参数未传入, endCity与city需传入一个！" : "";
            }
            if(isHasCity) {
                String city = params.getString("city");
                startCity = city;
                endCity = city;
                msg = "";
            }else{
                msg = !isHasEndCity || !isHasStartCity ? "city参数未传入, endCity、startCity与city需传入一组！" : "";
            }
            startNode = PlanNode.withCityNameAndPlaceName(startCity.toString().trim(), startAddres.toString().trim());
            endNode = PlanNode.withCityNameAndPlaceName(endCity.toString().trim(), endAddres.toString().trim());
        }
        if(!msg.equals("")) {
            searchPromise.reject("-1", msg);
        }
        map.put("startNode", startNode);
        map.put("endNode", endNode);
        return map;
    }

    public void groupResult(WritableArray writableArray) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putArray("routes", writableArray);
        writableMap.putInt("code", 0);
        searchPromise.resolve(writableMap);
    }

}
