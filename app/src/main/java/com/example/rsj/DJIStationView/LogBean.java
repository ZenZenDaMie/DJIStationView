package com.example.rsj.DJIStationView;

/**
 * Created by rsj on 2018/1/22.
 */

public class LogBean {
    /**
     * id : 110
     * stationid : 112130
     * create_time : 2018-01-20 18:31:41
     * update_time : 2018-01-20 18:31:41
     * uavid : 8804A0DC
     * stationstatus : 1
     */

    private String id;
    private String stationid;
    private String create_time;
    private String update_time;
    private String uavid;
    private int stationstatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUavid() {
        return uavid;
    }

    public void setUavid(String uavid) {
        this.uavid = uavid;
    }

    public int getStationstatus() {
        return stationstatus;
    }

    public void setStationstatus(int stationstatus) {
        this.stationstatus = stationstatus;
    }
}
