package com.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Connection;

/**
 * 1. Given station name, output all the station information
 * 
 * 2. Given line name, query out all the station name
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
        // System.out.println( "Hello World!" );
        FileReader fr = new FileReader("train-network.json");
        BufferedReader br = new BufferedReader(fr);
        String jsonString =  br.lines().collect(Collectors.joining());
        System.out.println(jsonString);
        JsonObject JsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        
        List<Station> stationList  = new ArrayList<>();
        JsonArray stationArray = JsonObject.getAsJsonArray("stations");
        for(int i=0; i< stationArray.size();
         i++){
            JsonObject curStation = stationArray.get(i).getAsJsonObject();
            String id = curStation.get("id").getAsString();
            String name = curStation.get("name").getAsString();
            float longitude = curStation.get("longitude").getAsFloat();
            float latitude = curStation.get("latitude").getAsFloat();
            stationList.add(new Station(id, name, longitude, latitude));
         }

         // parse line
         JsonArray lineArray = JsonObject.getAsJsonArray("lines");
         List<Line> lineList = new ArrayList<>();
         for(int i=0;i<lineList.size();
          i++){
            JsonObject curLine = lineArray.get(i).getAsJsonObject();
            String name = curLine.get("name").getAsString();
            JsonArray stationIdArray = curLine.get("stations").getAsJsonArray();

            List<String> lineStations  = new ArrayList<>();
            for(int j=0; j<stationIdArray.size();
            j++){
                String lineStationId = stationIdArray.get(j).getAsString();
                lineStations.add(lineStationId);
            }

            lineList.add(new Line(name, lineStations));
          }

        // connected to mysql
        connect();

        //   Populate the station
        for(int i=0; i<stationList.size(); i++){
            addStation(stationList.get(i));
        }
    }

    private static void addStation(Station station) {
        try {
            String insertQueryStatement = "INSERT  INTO  Station  VALUES  (?,?,?,?)";
            java.sql.PreparedStatement ps = conn.prepareStatement(insertQueryStatement);
            ps.setString(1, station.name);
            ps.setString(2, station.id);
            ps.setFloat(3, station.longitude);
            ps.setFloat(4, station.latitude);            // execute insert SQL statement
            ps.executeUpdate();
        } catch (
        SQLException e) {
            e.printStackTrace();
        }
    }

    static Connection conn =null;
    public static void connect(){
        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/stationdb?autoReconnect=true&useSSL=false", "root", "12345");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }
}
