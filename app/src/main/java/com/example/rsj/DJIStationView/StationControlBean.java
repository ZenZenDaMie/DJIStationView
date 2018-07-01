package com.example.rsj.DJIStationView;

/**
 * Created by rsj on 2018/7/1.
 */

public class StationControlBean {
    /**
     * stationid : 112130
     * uavid : 00000000
     * power : 0
     * create_time : 2018-01-18 17:07:23
     * update_time : 2018-07-01 14:38:51
     * stationstatus : 0
     * stationcontrol : 2
     */

    private int stationid;
    private String uavid;
    private int power;
    private String create_time;
    private String update_time;
    private int stationstatus;
    private String stationcontrol;

    public int getStationid() {
        return stationid;
    }

    public void setStationid(int stationid) {
        this.stationid = stationid;
    }

    public String getUavid() {
        return uavid;
    }

    public void setUavid(String uavid) {
        this.uavid = uavid;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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

    public int getStationstatus() {
        return stationstatus;
    }

    public void setStationstatus(int stationstatus) {
        this.stationstatus = stationstatus;
    }

    public String getStationcontrol() {
        return stationcontrol;
    }

    public void setStationcontrol(String stationcontrol) {
        this.stationcontrol = stationcontrol;
    }
}
