package com.rawwheel.workhelper.connection.storepool;



import com.rawwheel.workhelper.connection.storesql.JdbcSqlData;
import com.rawwheel.workhelper.connection.storesql.JdbcSqlTemplate;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ConPoolBlockingQueue extends Thread{

    private  static volatile  ConPoolBlockingQueue conPoolBlockingQueue;



    private final  static  int defaultBlockingQueueSize=100;

    private final  static int maxBlockingQueueSize=200;

    private int blockQueueSize;

    private final  static  Long flashGetBlockConTaskTime=100L;

    private  static BlockingQueue<JdbcSqlData> blockingQueue;

    //暂时先写成组合关系，后期用scheduler调度，代码有望简化
    private final JdbcPool jdbcPool=JdbcPool.getInstance();



    private ConPoolBlockingQueue(){
         blockingQueue=new LinkedBlockingDeque<JdbcSqlData>(defaultBlockingQueueSize);
         System.out.println(" -> 线程池阻塞队列启动"+"\n 阻塞队列监控线程启动");
        this.start();

    }

    public static ConPoolBlockingQueue getInstance(){
        if (conPoolBlockingQueue==null){
            synchronized (ConPoolBlockingQueue.class){
                if (conPoolBlockingQueue==null){
                    conPoolBlockingQueue=new ConPoolBlockingQueue();
                }
            }
        }
        return conPoolBlockingQueue;
    }

    public void joinQueue(JdbcSqlData jdbcSqlData){

       blockingQueue.add(jdbcSqlData);
        System.out.println("  \n.. 加入阻塞队列，队列长度为："+blockingQueue.size());
    }


    @Override
    public void run() {
        while(true) {

            //判断阻塞队列是否为空
            if (blockingQueue.isEmpty()) {

                synchronized (this) {

                    System.out.println(" 阻塞队列为空，休眠200ms");
                    try {
                        this.wait(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } else {

                try {

                    //先获取连接
                    Connection con = jdbcPool.getConnection();

                    //如果连接池的连接为空，则休眠200ms
                    if (con == null) {

                        synchronized (this) {

                            System.out.println("   ..连接池中无连接，休息200ms");
                            this.wait(flashGetBlockConTaskTime);

                        }
                    }
                    else {

                        System.out.println("  - 有连接，poll出 queue第一个出列做sql操作");

                        JdbcSqlData jdbcSqlData = blockingQueue.poll();
                        jdbcSqlData.setConnection(con);
                        JdbcSqlTemplate.getWords(jdbcSqlData);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
