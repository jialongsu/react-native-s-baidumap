import React, {Component} from 'react';
import {requireNativeComponent, View} from 'react-native';
import PropTypes from 'prop-types';

export default class Polygon extends Component {
  static propsTypes = {
    ...View.propTypes,
    /**
     *圆形中点
     */
    points: PropTypes.arrayOf(
      PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      }),
    ).isRequired,
    /**
     *边框颜色
     */
    color: PropTypes.string,
    /**
     *边框宽度
     */
    width: PropTypes.number,
    /**
     *圆形背景颜色
     */
    fillColor: PropTypes.string,
    /**
     *圆形背景颜色透明度，仅ios
     */
    fillColorAlpha: PropTypes.number,
  };

  static defaultProps = {
    points: [],
    color: '#000000',
    width: 4,
    fillColor: '#0eF6000F',
    fillColorAlpha: 0.5,
  };

  render() {
    if(this.props.points.length === 0) return null;
    return <BaiduMapOverlayPolygon {...this.props} />;
  }
}

const BaiduMapOverlayPolygon = requireNativeComponent(
  'RCTPolygonView',
  Polygon,
);
