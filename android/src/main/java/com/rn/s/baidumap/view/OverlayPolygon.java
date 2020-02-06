package com.rn.s.baidumap.view;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.List;


public class OverlayPolygon extends View implements OverlayView{

    private Polygon mPolygon;
    private List<LatLng> points;
    private int fillColor = 0xAAFF0000;
    private int color = 0xAAFF0000;
    private int width = 4;

    public OverlayPolygon(Context context) {
        super(context);
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
        if(mPolygon != null) {
            mPolygon.setPoints(getPoints());
        }
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        if(mPolygon != null) {
            mPolygon.setFillColor(getFillColor());
        }
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setColor(int color) {
        this.color = color;
        if(mPolygon != null) {
            mPolygon.setStroke(new Stroke(getWidth(), getColor()));
        }
    }

    public int getColor() {
        return color;
    }

    public void setWidth(int width) {
        this.width = width;
        if(mPolygon != null) {
            mPolygon.setStroke(new Stroke(getWidth(), getColor()));
        }
    }

    @Override
    public Object getOverlayView() {
        return mPolygon;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        //构造PolygonOptions
        PolygonOptions mPolygonOptions = new PolygonOptions()
                .points(getPoints())
                .fillColor(getFillColor()) //填充颜色
                .stroke(new Stroke(getWidth(), getColor())); //边框宽度和颜色

        mPolygon = (Polygon)baiduMap.addOverlay(mPolygonOptions);
    }

    @Override
    public void remove() {
        if(mPolygon != null) {
            mPolygon.remove();
            mPolygon = null;
        }
    }
}
