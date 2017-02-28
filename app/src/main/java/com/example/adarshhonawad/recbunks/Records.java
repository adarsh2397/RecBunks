package com.example.adarshhonawad.recbunks;

import java.io.Serializable;

/**
 * Created by Adarsh Honawad on 01-07-2016.
 */
public class Records implements Serializable{
    private int _id;
    String _subjectName;
    int _noOfBunks;
    int _maxBunks;

    public Records(){}

    public Records(String _subjectName,int _noOfBunks,int _maxBunks){
        this._subjectName = _subjectName;
        this._noOfBunks = _noOfBunks;
        this._maxBunks = _maxBunks;
    }

    public int get_id() {
        return _id;
    }

    public int get_maxBunks() {
        return _maxBunks;
    }

    public int get_noOfBunks() {
        return _noOfBunks;
    }

    public String get_subjectName() {
        return _subjectName;
    }

    public void set_noOfBunks(int _noOfBunks) {
        this._noOfBunks = _noOfBunks;
    }

    public void set_subjectName(String _subjectName) {
        this._subjectName = _subjectName;
    }

    public void set_maxBunks(int _maxBunks) {
        this._maxBunks = _maxBunks;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
