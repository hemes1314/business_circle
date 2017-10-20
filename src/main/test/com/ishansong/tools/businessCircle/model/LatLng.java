package com.ishansong.tools.businessCircle.model;

/**
 * 坐标点
 * @author wubin
 * @date 2017/10/2
 **/
public class LatLng {

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    double lat;
    double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
