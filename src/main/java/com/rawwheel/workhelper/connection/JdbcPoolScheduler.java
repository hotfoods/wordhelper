package com.rawwheel.workhelper.connection;

import com.rawwheel.workhelper.connection.storepool.ConPoolBlockingQueue;
import com.rawwheel.workhelper.connection.storepool.JdbcPool;
import com.rawwheel.workhelper.connection.storesql.JdbcSqlData;
import com.rawwheel.workhelper.connection.storesql.JdbcSqlTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.*;

//该调度器用于监控数据库连接池，阻塞队列
public class JdbcPoolScheduler {

    private  JdbcPool jdbcPool=JdbcPool.getInstance();

    private  ConPoolBlockingQueue conPoolBlockingQueue=ConPoolBlockingQueue.getInstance();



    public static void main(String[] args) throws SQLException {
        JdbcPoolScheduler jdbcPoolScheduler=new JdbcPoolScheduler();

        BlockingQueue<Runnable> workQueue=new ArrayBlockingQueue<Runnable>(10);

        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(10,20,1L, TimeUnit.SECONDS,workQueue);

        Runnable doCon=new Runnable() {
            @Override
            public void run() {
                try {
                    JdbcSqlData jdbcSqlData=new JdbcSqlData("select * from enwords limit 1 ;",null);

                    Connection con=jdbcPoolScheduler.jdbcPool.getConnection();
                    if (con!=null){

                        jdbcSqlData.setConnection(con);
                        JdbcSqlTemplate.getWords(jdbcSqlData);
                    }
                    else {
                        jdbcPoolScheduler.conPoolBlockingQueue.joinQueue(jdbcSqlData);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i=0;i<20;i++){

            poolExecutor.execute(doCon);
            System.out.println("线程池线程： "+poolExecutor.getTaskCount());
        }


    }
}
