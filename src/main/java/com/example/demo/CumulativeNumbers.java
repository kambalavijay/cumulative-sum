package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;

@RestController
@RequestMapping("/cumulative")
public class CumulativeNumbers {

    @Autowired
    DataSource datasource;

    @GetMapping
    public Cumulation getCumulative(){

        int total = executeQueryForAll();
        int _5min_num_total = executeQueryByMin(5);
        int _10min_num_total = executeQueryByMin(10);
        int _30min_num_total = executeQueryByMin(30);


        Cumulation cumulation = new Cumulation();
        cumulation.setTotalSeen(total);
        cumulation.set_5minSeen(_5min_num_total);
        cumulation.set_10minSeen(_10min_num_total);
        cumulation.set_30minSeen(_30min_num_total);

        return cumulation;
    }

    private int executeQueryByMin(int min){

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = datasource.getConnection();
            String query = "SELECT SUM(num) from num_time_table where TIMESTAMPDIFF(MINUTE, _timestamp, now()) > ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, min);
            stmt.executeQuery();
            rs = stmt.getResultSet();
            rs.next();
            return  rs.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    private int executeQueryForAll(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = datasource.getConnection();
            stmt = conn.createStatement();
            String totalQuery = "select SUM(num) from num_time_table";
            stmt.executeQuery(totalQuery);
            rs = stmt.getResultSet();
            rs.next();
            return  rs.getInt(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
