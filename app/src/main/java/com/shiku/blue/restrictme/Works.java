package com.shiku.blue.restrictme;

/**
 * Created by BLUE on 3/26/2016.
 */
public class Works {

    private int _id;
    private String _workName;

    public Works(){
    }

    public Works(String workName){
        this._workName = workName;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_workName(String workName) {
        this._workName = workName;
    }

    public int get_id() {
        return _id;
    }

    public String get_workName() {
        return _workName;
    }

}
