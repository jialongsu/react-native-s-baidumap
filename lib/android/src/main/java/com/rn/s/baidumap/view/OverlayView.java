package com.rn.s.baidumap.view;

import com.baidu.mapapi.map.BaiduMap;

/**
 * Created by sujialong on 2019/7/9.
 */

public interface OverlayView {
    void addTopMap(BaiduMap baiduMap);
    void remove();
    public abstract Object getOverlayView();
}
