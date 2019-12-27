package com.rn.s.baidumap.view;

import android.content.Context;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.views.view.ReactViewGroup;
import com.rn.s.baidumap.R;

/**
 * Created by sujialong on 2019/7/9.
 */

public class OverlayMarker extends ReactViewGroup implements OverlayView {

    private Marker marker;
    private String title;
    private LatLng position;
    private BitmapDescriptor iconBitmapDescriptor;
    private Float rotate;
    private Boolean flat;
    private Boolean perspective;
    private Boolean draggable;
    private Boolean active;
    private int zIndex;
    private int infoWindowYOffset = 0;
    private int infoWindowMinHeight = 100;
    private int infoWindowMinWidth = 200;
    private int infoWindowTextSize = 16;
    private InfoWindow mInfoWindow;
    private BaiduMap mBaiduMap;


    public OverlayMarker(Context context) {
        super(context);
    }

    public void setTitle(String title) {
        this.title = title;
        if(mInfoWindow != null && active) {
            createInfoWindow();
        }
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public int getZIndex() {
        return this.zIndex;
    }

    public int getInfoWindowMinHeight() {
        return infoWindowMinHeight;
    }

    public void setInfoWindowMinHeight(int infoWindowMinHeight) {
        this.infoWindowMinHeight = infoWindowMinHeight;
        if(mInfoWindow != null && active) {
            createInfoWindow();
        }
    }

    public int getInfoWindowMinWidth() {
        return infoWindowMinWidth;
    }

    public void setInfoWindowMinWidth(int infoWindowMinWidth) {
        this.infoWindowMinWidth = infoWindowMinWidth;
        if(mInfoWindow != null && active) {
            createInfoWindow();
        }
    }

    public int getInfoWindowTextSize() {
        return infoWindowTextSize;
    }

    public void setInfoWindowTextSize(int infoWindowTextSize) {
        this.infoWindowTextSize = infoWindowTextSize;
        if(mInfoWindow != null && active) {
            createInfoWindow();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setPosition(double latitude, double longitude) {
        position = new LatLng(latitude, longitude);
        if(marker != null) {
            if(mInfoWindow != null && active) {
                createInfoWindow();
            }
            marker.setPosition(position);
        }
    }

    public LatLng getPosition() {
        return position;
    }

    public void setIcon(String image) {
        Context context = getContext();
        int drawable = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
        iconBitmapDescriptor = BitmapDescriptorFactory.fromResource(drawable);
        if(marker != null) {
            marker.setIcon(iconBitmapDescriptor);
        }
    }

    public BitmapDescriptor getIcon() {
        return iconBitmapDescriptor;
    }

    public void setPerspective(boolean perspective) {
        this.perspective = perspective;
        if(marker != null) {
            marker.setPerspective(perspective);
        }
    }

    public boolean getPerspective() {
        return this.perspective;
    }

    public void setRotate(float rotat) {
        this.rotate = rotat;
        if(marker != null) {
            marker.setRotate(rotat);
        }
    }

    public float getRotate() {
        return this.rotate;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
        if(marker != null) {
            marker.setFlat(flat);
        }
    }

    public boolean getFlat() {
        return this.flat;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
        if(marker != null) {
            marker.setDraggable(draggable);
        }
    }

    public boolean getDraggable() {
        return this.draggable;
    }

    public void setActive(boolean active) {
        this.active = active;
        if(mInfoWindow != null) {
            if(active) {
                marker.showInfoWindow(mInfoWindow);
            }else{
                marker.hideInfoWindow();
            }
        }else{
            createInfoWindow();
        }
    }

    public boolean getActive() {
        return this.active;
    }

    public void setInfoWindowYOffset(int infoWindowYOffset) {
        this.infoWindowYOffset = infoWindowYOffset;
        if(mInfoWindow != null) {
            mInfoWindow.setYOffset(infoWindowYOffset);
        }
    }

    public int getInfoWindowYOffset() {
        return this.infoWindowYOffset;
    }

    @Override
    public Object getOverlayView() {
        return marker;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
        MarkerOptions markerOptions = new MarkerOptions()
                .alpha(getAlpha())
                .flat(getFlat())
                .perspective(getPerspective())
                .rotate(getRotate())
                .draggable(getDraggable())
                .position(getPosition())
                .zIndex(getZIndex())
                .icon(getIcon());
        marker = (Marker) baiduMap.addOverlay(markerOptions);
        createInfoWindow();
    }

    public void createInfoWindow() {
        if(active && marker != null) {
            Button mButton = new Button(getContext());
            mButton.setBackgroundResource(R.drawable.callout);
            mButton.setMinHeight(getInfoWindowMinHeight());
            mButton.setMinWidth(getInfoWindowMinWidth());
            mButton.setTextSize(getInfoWindowTextSize());
            mButton.setText(getTitle());
            marker.hideInfoWindow();
            mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(mButton), getPosition(), getInfoWindowYOffset(), new InfoWindow.OnInfoWindowClickListener(){
                @Override
                public void onInfoWindowClick() {
                }
            });
            marker.showInfoWindow(mInfoWindow);
        }
    }

    @Override
    public void remove() {
        if (marker != null) {
            if(active) {
                marker.hideInfoWindow();
//                mBaiduMap.hideInfoWindow(mInfoWindow);
            }
            marker.remove();
            marker = null;
            mInfoWindow = null;
        }
    }
}
