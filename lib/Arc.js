import React, {Component} from 'react';
import {requireNativeComponent, View} from 'react-native';
import PropTypes from 'prop-types';

export default class Arc extends Component {
  static propsTypes = {
    ...View.propTypes,
    /**
     *坐标点列表,长度为3，代表起点，中间点，结束点
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
    ).isRequired,
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
    width: 4,
  };

  render() {
    if(this.props.points.length < 3) return null;
    return <BaiduMapOverlayArc {...this.props} />;
  }
}

const BaiduMapOverlayArc = requireNativeComponent(
  'RCTArcView',
  Arc,
);
