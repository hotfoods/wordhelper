package com.rawwheel.workhelper.connection.jdbcutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcPoolUtils {

    public static void release(Connection con , Statement st, ResultSet rs) throws SQLException {
        if (rs!=null){
            rs.close();
            rs=null;
        }

        if (st!=null){
            st.close();

        }

        if (con!=null){
            con.close();
        }

    }
}
