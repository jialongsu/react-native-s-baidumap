import React, {Component} from 'react';
import {requireNativeComponent, View} from 'react-native';
import PropTypes from 'prop-types';

export default class Polyline extends Component {
  static propsTypes = {
    ...View.propTypes,
    /**
     *折线坐标点列表
     *{
     *  latitude: 39.993607,
     *  longitude: 116.404844,
     *}
     */
    points: PropTypes.arrayOf(
      PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      }),
    ),
    /**
     *颜色
     */
    color: PropTypes.string,
    /**
     *宽度
     */
    width: PropTypes.number,
  };

  static defaultProps = {
    points: [],
    color: '#000000',
    width: 6,
  };

  render() {
    return <BaiduMapOverlayPolyline {...this.props} />;
  }
}

const BaiduMapOverlayPolyline = requireNativeComponent(
  'RCTPolylineView',
  Polyline,
);
