# react-native-s-baidumap
百度地图 React Native 模块，同时支持ios和android，react native 0.60.0+。

支持模块：
- 基础定位 ✅
- 基础地图 ✅
- 个性化地图
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
- 路线规划

## 开发计划
版本1.1.0：
 - Marker 支持RN图片本地资源与网络图片
 - 路线规划
 - 点聚合

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
点击找到本项目node_modules下的react-native-s-baidumap -> lib -> iosLib -> RNSBaidumap,
将整个RNSBaidumap文件夹导入。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191227144139261.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3VuaGFwcHlfbG9uZw==,size_16,color_FFFFFF,t_70)

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

