package com.rn.s.baidumap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.views.view.ReactViewGroup;
import com.rn.s.baidumap.R;

import java.util.HashMap;

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
    private Boolean propActive;
    private int zIndex;
    private int infoWindowYOffset = 0;
    private int infoWindowMinHeight = 100;
    private int infoWindowMinWidth = 200;
    private int infoWindowTextSize = 16;
    private InfoWindow mInfoWindow;
    private BaiduMap mBaiduMap;
    private int iconHeight = 0;

    private DataSource<CloseableReference<CloseableImage>> dataSource;
    private volatile boolean loadingImage = false;
    private DraweeHolder<?> imageHolder;
    private final ControllerListener<ImageInfo> imageControllerListener =
            new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(
                        String id,
                        final ImageInfo imageInfo,
                        Animatable animatable) {
                    CloseableReference<CloseableImage> imageReference = null;
                    try {
                        imageReference = dataSource.getResult();
                        if (imageReference != null) {
                            CloseableImage image = imageReference.get();
                            if (image != null && image instanceof CloseableStaticBitmap) {
                                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                if (bitmap != null) {
                                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                    iconBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                                }
                            }
                        }
                    } finally {
                        dataSource.close();
                        if (imageReference != null) {
                            CloseableReference.closeSafely(imageReference);
                        }
                        loadingImage = false;
                    }
                }
            };


    public OverlayMarker(Context context) {
        super(context);
        init();
    }

    protected void init() {
        GenericDraweeHierarchy genericDraweeHierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFadeDuration(0)
                .build();
        imageHolder = DraweeHolder.create(genericDraweeHierarchy, getContext());
        imageHolder.onAttach();
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

    public void setIcon(String uri) {
        if (uri == null) {
            iconBitmapDescriptor = null;
        } else if (uri.startsWith("http://") || uri.startsWith("https://") ||
                uri.startsWith("file://") || uri.startsWith("asset://")) {
            loadingImage = true;
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(uri))
                    .build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setControllerListener(imageControllerListener)
                    .setOldController(imageHolder.getController())
                    .build();
            imageHolder.setController(controller);
        } else {
            iconBitmapDescriptor = getBitmapDescriptorByName(uri);
        }
    }

    public BitmapDescriptor getIcon() {
        if (iconBitmapDescriptor != null) {
            return iconBitmapDescriptor;
        } else {
            return BitmapDescriptorFactory.fromResource(R.drawable.marker);
        }
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

    public void setPropActive(boolean active) {
        this.propActive = active;
    }

    public boolean getPropActive() {
        return this.propActive;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }
    public int getIconHeight() {
        return this.iconHeight;
    }

    public void setInfoWindowYOffset(int infoWindowYOffset) {
        this.infoWindowYOffset = infoWindowYOffset;
        if(mInfoWindow != null) {
            mInfoWindow.setYOffset(getInfoWindowYOffset());
        }
    }

    public int getInfoWindowYOffset() {
        int iconHeight = getIconHeight() + 5;
        this.infoWindowYOffset = this.infoWindowYOffset + -iconHeight;
        return this.infoWindowYOffset;
    }

    @Override
    public Object getOverlayView() {
        return marker;
    }

    @Override
    public void addTopMap(final BaiduMap baiduMap) {
        //不管图片是否加载成功，先会创建一个Marker显示默认图片，加载成功后替换默认图片
        if(marker == null) {
            addOverlay(baiduMap);
        }
        if (loadingImage) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(loadingImage) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    marker.setIcon(getIcon());
                }
            }).start();
        }
    }

    public void addOverlay(BaiduMap baiduMap) {
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
            }
            marker.remove();
            marker = null;
            mInfoWindow = null;
        }
    }

    private BitmapDescriptor getBitmapDescriptorByName(String name) {
        return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(name));
    }

    private int getDrawableResourceByName(String name) {
        return getResources().getIdentifier(
                name,
                "drawable",
                getContext().getPackageName());
    }
}
