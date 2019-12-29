'use strict';
import React, {Component} from 'react';
import {StyleSheet, View, Button} from 'react-native';
// import {
//   walkingRouteSearch,
//   drivingRouteSearch,
//   bikingRouteSearch,
//   transitRoutePlan,
// } from './RoutePlanSearchModule';
import {
  MapView,
  Marker,
  Polyline,
  RoutePlanSearch,
} from 'react-native-s-baidumap';

const {
  walkingRouteSearch,
  drivingRouteSearch,
  bikingRouteSearch,
  transitRoutePlan,
} = RoutePlanSearch;

export default class Page extends Component {
  state = {
    startPointsLocation: {
      latitude: 39.913607,
      longitude: 116.404844,
    },
    endPointsLocation: {
      latitude: 39.913707,
      longitude: 116.404944,
    },
    pointList: [],
  };

  constructor(props) {
    super(props);
    const startPointsLocation = {
      latitude: 39.913607,
      longitude: 116.404844,
    };
    const endPointsLocation = {
      latitude: 39.913707,
      longitude: 116.404944,
    };
    this.state = {
      resultString: '',
      startPointsLocation,
      endPointsLocation,
      pointList: [startPointsLocation, endPointsLocation],
    };
  }

  onWalkingRoute = () => {
    walkingRouteSearch({
      // startCity: '杭州',
      startAddres: '西湖',
      // endCity: '杭州',
      endAddres: '博地中心',
      city: '杭州',
    }).then(res => {
      console.log('walkingRouteSearch', res);
      this.groupData(res);
    });
  };

  onDrivingRoute = () => {
    drivingRouteSearch({
      // startCity: '杭州',
      startAddres: '西湖',
      // endCity: '杭州',
      city: '杭州',
      endAddres: '博地中心',
      // startLocation: {
      //   latitude: 30.201758579993438,
      //   longitude: 120.24196537387117,
      // },
      // endLocation: {
      //   latitude: 30.243286201021842,
      //   longitude: 120.25870080552207,
      // },
    }).then(res => {
      console.log('drivingRouteSearch', res);
      this.groupData(res);
    });
  };

  onBikingRoute = () => {
    bikingRouteSearch({
      startCity: '杭州',
      startAddres: '西湖',
      endCity: '杭州',
      endAddres: '博地中心',
    }).then(res => {
      console.log('bikingRouteSearch', res);
      this.groupData(res);
    });
  };

  onTransitRoute = () => {
    transitRoutePlan({
      city: '杭州',
      startAddres: '中栋国际',
      endAddres: '博地中心',
      // startLocation: {
      //   latitude: 30.201758579993438,
      //   longitude: 120.24196537387117,
      // },
      // endLocation: {
      //   latitude: 30.243286201021842,
      //   longitude: 120.25870080552207,
      // },
    }).then(res => {
      console.log('transitRoutePlan', res);
      this.groupData(res);
    });
  };

  onTapZoomToSpanMarkers = () => {
    const {pointList} = this.state;
    this.isSetZoomToSpanMarkers = true;
    this.mapView.setZoomToSpanMarkers(pointList);
  };

  groupData = res => {
    const {code, routes} = res;
    let startPointsLocation = {};
    let endPointsLocation = {};
    const pointList = [];
    if (code === 0) {
      const firstRoutes = routes[0]; //取第一条线路
      const startWayPoints = firstRoutes.allStep[0].wayPoints;
      const endAllStep = firstRoutes.allStep;
      const endWayPoints = endAllStep[endAllStep.length - 1].wayPoints;
      startPointsLocation = startWayPoints[startWayPoints.length - 1];
      endPointsLocation = endWayPoints[endWayPoints.length - 1];
      const allStep = firstRoutes.allStep;
      allStep.map(zItem => {
        const {wayPoints} = zItem;
        wayPoints.map(jItem => {
          pointList.push(jItem);
        });
      });
      this.setState({
        startPointsLocation,
        endPointsLocation,
        pointList,
      });
    }
  };

  setMapView = view => {
    this.mapView = view;
  };

  render() {
    const {startPointsLocation, endPointsLocation, pointList} = this.state;

    return (
      <View style={styles.con}>
        <View style={styles.buttonCon}>
          <Button title={'步行路线'} onPress={this.onWalkingRoute} />
          <Button title={'驾车路线'} onPress={this.onDrivingRoute} />
          <Button title={'骑行路线'} onPress={this.onBikingRoute} />
          <Button title={'市内公交路线'} onPress={this.onTransitRoute} />
          <Button
            title={'显示所有Marker点在视图内'}
            onPress={this.onTapZoomToSpanMarkers}
          />
        </View>
        <MapView
          style={styles.con}
          ref={this.setMapView}
          baiduMapType={1}
          centerLatLng={startPointsLocation}
          zoom={14}>
          <Marker
            icon={'start_mark'}
            active={true}
            title={'start_mark'}
            location={startPointsLocation}
          />
          <Marker icon={'end_mark'} location={endPointsLocation} />
          <Polyline width={4} color={'#000000'} points={pointList} />
        </MapView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  con: {
    flex: 1,
  },
  buttonCon: {
    flexDirection: 'row',
    alignItems: 'center',
    flexWrap: 'wrap',
    justifyContent: 'space-evenly',
  },
});
