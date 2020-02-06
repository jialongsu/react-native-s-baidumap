package com.rn.s.baidumap.view;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;


public class OverlayCircle extends View implements OverlayView{

    private Circle mCircle;
    private LatLng center;
    private int fillColor = 0xAAFF0000;
    private int color = 0xAAFF0000;
    private int width = 4;
    private int radius = 1400;


    public OverlayCircle(Context context) {
        super(context);
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        if(mCircle != null) {
            mCircle.setFillColor(getFillColor());
        }
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setColor(int color) {
        this.color = color;
        if(mCircle != null) {
            mCircle.setStroke(new Stroke(getWidth(), getColor()));
        }
    }

    public int getColor() {
        return color;
    }

    public void setWidth(int width) {
        this.width = width;
        if(mCircle != null) {
            mCircle.setStroke(new Stroke(getWidth(), getColor()));
        }
    }

    public void setRadius(int radius) {
        this.radius = radius;
        if(mCircle != null) {
            mCircle.setRadius(getRadius());
        }
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public Object getOverlayView() {
        return mCircle;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        //构造CircleOptions对象
        CircleOptions mCircleOptions = new CircleOptions().center(getCenter())
                .radius(getRadius())
                .fillColor(getFillColor()) //填充颜色
                .stroke(new Stroke(getWidth(), getColor())); //边框宽和边框颜色

        mCircle = (Circle)baiduMap.addOverlay(mCircleOptions);
    }

    @Override
    public void remove() {
        if(mCircle != null) {
            mCircle.remove();
            mCircle = null;
        }
    }
}
