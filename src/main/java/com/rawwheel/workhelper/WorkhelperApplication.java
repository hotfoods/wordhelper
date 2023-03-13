package com.rawwheel.workhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkhelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkhelperApplication.class, args);
        Run thread=new Run();
        thread.start();
    }
    static class Run extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                System.out.println(" --- ");
                try {
                    this.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
