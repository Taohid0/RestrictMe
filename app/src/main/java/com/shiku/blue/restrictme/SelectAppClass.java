package com.shiku.blue.restrictme;

/**
 * Created by BLUE on 4/1/2016.
 */
public class SelectAppClass {
    private int _id;
    private String _workName;

    public SelectAppClass() {
    }

    public SelectAppClass(String workName) {
        this._workName = workName;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_app(String workName) {
        this._workName = workName;
    }

    public int get_id() {
        return _id;
    }

    public String get_workName() {
        return _workName;
    }
}
