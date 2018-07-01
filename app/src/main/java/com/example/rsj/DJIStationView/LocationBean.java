package com.example.rsj.DJIStationView;

/**
 * Created by rsj on 2018/1/15.
 */

public class LocationBean {
    /**
     * stationid : 112122
     * create_time : 2018-01-10 22:42:42
     * update_time : 2018-01-10 22:42:42
     * lat : 121.4054413
     * lon : 31.3235207
     */

    private String stationid;
    private String create_time;
    private String update_time;
    private double lat;
    private double lon;

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
