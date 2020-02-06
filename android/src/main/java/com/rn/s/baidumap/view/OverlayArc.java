package com.rn.s.baidumap.view;

import android.content.Context;
import android.view.View;

import com.baidu.mapapi.map.Arc;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class OverlayArc extends View implements OverlayView{

    private Arc mArc;
    private List<LatLng> points;
    private LatLng pointsStart;
    private LatLng pointsCenter;
    private LatLng pointsEnd;
    private int color = 0xAAFF0000;
    private int width = 4;


    public OverlayArc(Context context) {
        super(context);
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
        this.pointsStart = points.get(0);
        this.pointsCenter = points.get(1);
        this.pointsEnd = points.get(2);
        if(mArc != null) {
            mArc.setPoints(this.pointsStart, this.pointsCenter, this.pointsEnd);
        }
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setColor(int color) {
        this.color = color;
        if(mArc != null) {
            mArc.setColor(color);
        }
    }

    public int getColor() {
        return color;
    }

    public void setWidth(int width) {
        this.width = width;
        if(mArc != null) {
            mArc.setWidth(width);
        }
    }

    @Override
    public Object getOverlayView() {
        return mArc;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        //构造ArcOptions对象
        OverlayOptions mArcOptions = new ArcOptions()
                .color(getColor())
                .width(this.width)
                .points(this.pointsStart, this.pointsCenter, this.pointsEnd);

        mArc = (Arc)baiduMap.addOverlay(mArcOptions);
    }

    @Override
    public void remove() {
        if(mArc != null) {
            mArc.remove();
            mArc = null;
        }
    }
}
