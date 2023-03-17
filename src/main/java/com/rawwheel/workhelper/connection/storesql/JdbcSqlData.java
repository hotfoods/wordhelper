package com.rawwheel.workhelper.connection.storesql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcSqlData {

    private String sql;

    private Connection connection;

    public JdbcSqlData(String sql,Connection connection){
        this.sql=sql;
        this.connection=connection;
    }

    public void setConnection(Connection con) {
        this.connection=con;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getSql() {
        return sql;
    }
}
