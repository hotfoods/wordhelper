package com.rawwheel.workhelper.connection.storepool;


import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

//@Component

//线程池存在同步问题 和 唯一性，用单例模式 和 加互斥锁解决
public class JdbcPool implements DataSource {



    //双锁单例模式
    private static volatile JdbcPool jdbcPool;

    private JdbcPool(){
        try {
            Class.forName(driver);
            System.out.println(" 创建数据库连接池");
            for (int i=0;i<defaultPoolSize;i++){
                Connection con= DriverManager.getConnection(url,username,pass);
                System.out.println(" 获取到第"+i+"个连接");
                connections.add(con);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  static JdbcPool getInstance(){
        if (jdbcPool==null){
            synchronized (JdbcPool.class){
                if (jdbcPool==null){
                    jdbcPool=new JdbcPool();
                }
            }
        }
        return jdbcPool;
    }

    private ConcurrentLinkedQueue<Connection> connections=new ConcurrentLinkedQueue<>();

    private final  static int defaultPoolSize=10;


    private final static int maxPoolSize=20;



    private final static String driver="com.mysql.cj.jdbc.Driver";
    private final static String username="root";
    private final static String pass="1111";
    private final static String url="jdbc:mysql://127.0.0.1:3306/wordhelper";



    @Override
    public Connection getConnection() throws SQLException {
        if (connections.size()>0) {

                final Connection con = connections.poll();
                System.out.println(" 连接池的大小为：" + connections.size());

                //调用代理类 proxy 代理connection 使得在调用 close函数时 ，不关闭连接，而是 添加为 queue 连接池中
                return (Connection) Proxy.newProxyInstance(
                        JdbcPool.class.getClassLoader(),
                        con.getClass().getInterfaces(),
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                if (!method.getName().equals("close")) {
                                    return method.invoke(con, args);
                                } else {

                                    connections.add(con);
                                    System.out.println(con + "返还与连接池中  --线程池数量为  ：" + connections.size());

                                    return null;
                                }
                            }
                        }
                );

        }
        else {
            System.out.println( " 数据库连接池满，请等待");
            return null;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
