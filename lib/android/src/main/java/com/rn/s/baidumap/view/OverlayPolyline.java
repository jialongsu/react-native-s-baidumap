package com.rn.s.baidumap.view;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by sujialong on 2019/7/9.
 */

public class OverlayPolyline extends View implements OverlayView {

    private Polyline mPolyline;
    private List<LatLng> points;
    private int color = 0xAAFF0000;
    private int lineWidth;

    public OverlayPolyline(Context context) {
        super(context);
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        if(mPolyline != null) {
            mPolyline.setWidth(lineWidth);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        if(mPolyline != null) {
            mPolyline.setColor(color);
        }
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
        if(mPolyline != null) {
            mPolyline.setPoints(points);
        }
    }

    @Override
    public Object getOverlayView() {
        return mPolyline;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(getLineWidth())
                .color(getColor())
                .points(getPoints());
        mPolyline = (Polyline)baiduMap.addOverlay(mOverlayOptions);
    }

    @Override
    public void remove() {
        if(mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
    }
}
