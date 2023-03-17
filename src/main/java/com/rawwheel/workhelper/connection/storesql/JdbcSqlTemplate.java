package com.rawwheel.workhelper.connection.storesql;

import com.rawwheel.workhelper.connection.jdbcutils.JdbcPoolUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class JdbcSqlTemplate{


    public static void getWords(JdbcSqlData jdbcSqlData) throws SQLException {


        Connection con=jdbcSqlData.getConnection();
        PreparedStatement pmt=con.prepareStatement(jdbcSqlData.getSql());
        ResultSet rs=pmt.executeQuery();
        while (rs.next()){
            System.out.println("words: "+rs.getString(1)+" \ntranslate: "+rs.getString(2)+"\n");
        }

        JdbcPoolUtils.release(con,pmt,rs);
    }

    public static void getJdbcDataDo(JdbcSqlData jdbcSqlData){

    }
}
