/**
 * @format
 */

import {AppRegistry} from 'react-native';
import {name as appName} from './app.json';
// import App from './App';
import App from './app/Router';

/**
 * 关闭警告提示
 * @type {boolean}
 */
console.disableYellowBox = true;
if (process.env.NODE_ENV !== 'development') {
  console.log = function() {};
  console.warn = function() {};
  console.debug = function() {};
}

AppRegistry.registerComponent(appName, () => App);
