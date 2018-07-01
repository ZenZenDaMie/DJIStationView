package com.example.rsj.DJIStationView;

/**
 * Created by rsj on 2018/1/7.
 */

public class JsonBean {


    /**
     * root : true
     * admin : true
     * user : true
     * success : true
     */

    private boolean root;
    private boolean admin;
    private boolean user;
    private boolean success;

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
