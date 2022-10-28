package com.example;

import java.util.List;

public class Line {
    // private int id;
    private String name;
    // public Line(int id, String name){
    //     this.id = id;
    //     this.name = name;
    // };
    private List<String> stationIds;
    public Line(String name, List<String> stationIds){
        this.name = name;
        this.stationIds = stationIds;
    };

}
