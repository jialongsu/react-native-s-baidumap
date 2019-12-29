

# react-native-s-baidumap
百度地图 React Native 模块，同时支持ios和android，react native 0.60.0+。

支持模块：
- 基础定位 ✅
- 基础地图 ✅
- 个性化地图 ✅
- 离线地图
- 绘制点标记 ✅
- 绘制线 ✅
- 绘制弧线和面
- 自定义Infowindow
- 点聚合
- 绘制overlay
- POI检索 ✅
- 地点检索输入提示检索 ✅
- 地理编码 ✅
- 路线规划 ✅

## DEMO预览
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191229135710379.gif)


## 安装

```
npm i react-native-s-baidumap
```
或

```
yarn add react-native-s-baidumap
```
## 配置
如果你使用的react native的版本>=0.60.0,则无需做多余配置，只需要配置百度地图申请的key。

## Android配置：

在 AndroidManifest 中添加：
```js
<application>
    <meta-data
        android:name="com.baidu.lbsapi.API_KEY"
        android:value="你的key" />
</application>
```
## IOS配置
**第一步：**
在Podfile文件中加入以下代码：

```js
  pod 'BaiduMapKit', '5.1.0'
  pod 'BMKLocationKit', '1.8.0'
```
**第二步：**
在运行命令

```
cd ios && pod install
```
**第三步：**
等待安装成功后，进入ios工程文件夹，会看到一个.xcworkspace 结尾的文件 ，双击打开
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191227143620784.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3VuaGFwcHlfbG9uZw==,size_16,color_FFFFFF,t_70)
**第四步：**
选中项目，右键添加文件
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191227143814712.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3VuaGFwcHlfbG9uZw==,size_16,color_FFFFFF,t_70)
点击找到本项目node_modules下的react-native-s-baidumap -> iosLib -> RNSBaidumap,
将整个RNSBaidumap文件夹导入。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191227171950125.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3VuaGFwcHlfbG9uZw==,size_16,color_FFFFFF,t_70)
**第五步：**
在AppDelegate.m文件中添加百度地图配置
在头部引入：

```js
#import <BaiduMapAPI_Base/BMKBaseComponent.h>
```
在didFinishLaunchingWithOptions方法中配置：

```js
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  
  // 要使用百度地图，请先启动BaiduMapManager
  BMKMapManager *mapManager = [[BMKMapManager alloc] init];
  // 如果要关注网络及授权验证事件，请设定generalDelegate参数
  BOOL ret = [mapManager start:@"你申请的key"  generalDelegate:nil];
  if (!ret) {
    NSLog(@"baidumap manager start failed!");
  }
  
  ..........
}
```
到此配置结束。


## MapView Props 属性
|Prop|Type  |Default|Description|
|--|--|--|--|
| zoomControlsVisible | bool |false |显示放大缩小按钮|
| isTrafficEnabled | bool |false |显示交通路线|
| isBaiduHeatMapEnabled | bool |false |显示百度热力图层|
| baiduMapType | number |1 |地图类型 1: 标准地图 2: 卫星地图 3:空白地图|
| locationEnabled | bool |false |显示当前定位位置|
| zoom | number |18 |地图缩放级别[4,21]|
| zoomMinLevel | number |4 |地图最小缩放级别，最小4|
| zoomMaxLevel | number |21 |地图最大缩放级别，最大21|
| centerLatLng | object |{} |地图中心点位置{latitude: ..., longitude: ...}|
| zoomGesturesEnabled | bool |true |是否允许缩放手势|
| scrollGesturesEnabled | bool |true |是否允许拖拽手势|
| overlookingGesturesEnabled | bool |true |是否允许俯视手势|
| rotateGesturesEnabled | bool |true |否允许旋转手势|
| mapCustomStyleFileName | string |”“ |用于设置个性化地图的样式文件|
| onMapLoaded | func |()=>{} |地图加载完成回调|
| onMapClick | func |()=>{} |点击地图回调|
| onMapPoiClick | func |()=>{} |点击地图地点回调|
| onMapLongClick | func |()=>{} |长按地图回调|
| onMapDoubleClick | func |()=>{} |双击地图回调|
| onMapStatusChangeStart | func |()=>{} |地图状态开始变化回调|
| onMapStatusChange | func |()=>{} |地图状态变化中回调|
| onMapStatusChangeFinish | func |()=>{} |地图状态变化结束回调|
| onMarkerClick | func |()=>{} |点击marker回调|
| onMarkerDragStart | func |()=>{} |拖拽marker开始回调|
| onMarkerDrag | func |()=>{} |拖拽marker中回调|
| onMarkerDragEnd | func |()=>{} |拖拽marker结束回调|

## MapView  方法
|Methed| Description |Result |
|--|--|--|
| setCenter({latitude, longitude}) | 设置地图中心点位置 |
| setZoom(number) | 设置地图缩放 |
| setZoomToSpanMarkers( [{latitude, longitude}]) | 地图中所有的marker点显示在视图内 |
## Marker属性
|Prop|Type  |Default|Description|
|--|--|--|--|
| title | string |”“ |infowindow内容|
| location | object |{latitude, longitude}|坐标|
| icon | any ||Marker图片，支持本地与远程|
| draggable | bool |false|是否可拖拽|
| active | bool |false|是否显示infowind|
| infoWindowYOffset | number |0|infowind y轴偏移,正数向下移动，负数向上移动|
| perspective | bool |false|是否开启近大远小效果, 仅android|
| alpha | number |0|透明度, 仅android|
| rotate | number |0|旋转角度, 仅android|
| flat | bool |false|是否平贴地图, 仅android|
| infoWindowMinHeight | number |100|infoWindow 最小高度, 仅android|
| infoWindowMinWidth | number |200|infoWindow 最小宽度, 仅android|
| infoWindowTextSize | number |16|infoWindow 字体, 仅android|

## Polyline属性
|Prop|Type  |Default|Description|
|--|--|--|--|
| points | array |[] |折线坐标点列表,[{latitude, longitude}]|
| color | string |#000000 |线条颜色，需要完整的十六进制值，#000这种解析不了|
| width | number |6 |线条宽度|

## Geolocation Methods
## 定位方法
|Method|Description  |Result|
|--|--|--|
| initSDK(key) | ios使用定位前需要调用该方法 | |
| start ()|开始持续定位 | |
| stop() |停止持续定位 | |
| addListener (func)|定位成功监听 | geolocation result|
## 地理编码
|Method|Description  |Result|
|--|--|--|
| geocode(address, city) | 地理编码方法 | |
| addListener |地理编码成功监听 | {longitude, latitude}|

## 逆地理编码
|Method|Description  |Result|
|--|--|--|
| reverseGeoCode(lat, lng) | 逆地理编码方法 | |
| addListener |逆地理编码成功监听 | geolocation result|

## Search Methods
## POI检索
|Method|Description  |Result|
|--|--|--|
| searchInCity(city, keyword, pageNum): Promise | POI城市内检索（关键字检索） | search result|
| searchNearby({latitude, longitude, keyword, pageNum, radius}): Promise |周边检索 | geolocation result|
## 地点检索
|Method|Description  |Result|
|--|--|--|
| requestSuggestion(city, keyword): Promise | 输入提示检索 | search result|
## RoutePlan Methods
|Method|Description  |Result|
|--|--|--|
| **walkingRouteSearch**({startCity: 起点城市, startAddres: 起点位置, endCity: 终点城市, endAddres: 终点位置, city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city}): Promise | 步行路线规划 | Route result|
| **drivingRouteSearch**({startCity: 起点城市, startAddres: 起点位置, endCity: 终点城市, endAddres: 终点位置, city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city, trafficPolicyType: 是否开起路况, drivingPolicyType: 驾车策略}): Promise | 驾车路线规划 | Route result|
| **bikingRouteSearch**({startCity: 起点城市, startAddres: 起点位置, endCity: 终点城市, endAddres: 终点位置, city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city, ridingType:  骑行类型（0：普通骑行模式，1：电动车模式）}): Promise | 骑行路线规划 | Route result|
| **transitRoutePlan**({startCity: 起点城市, startAddres: 起点位置, endCity: 终点城市, endAddres: 终点位置, city: 起点与终点是同一个城市, city, startCity, endCity同时使用，起点与终点都使用city, policyType:  换乘策略}): Promise | 市内公交路线规划 | Route result|