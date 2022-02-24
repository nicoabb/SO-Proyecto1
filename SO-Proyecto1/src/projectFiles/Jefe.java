/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectFiles;

import java.util.concurrent.Semaphore;

/**
 *
 * @author cbcbe
 */
public class Jefe extends Thread{
    private int dayDuration;
    private boolean stop;
    private Semaphore mutex;
    
    public Jefe(int dayDuration, Semaphore mutex){
        
        this.dayDuration = dayDuration;
        this.mutex = mutex;
        
    }
    
    public void run(){
        while(!this.stop){
            try{
                Interfaz.bossTxt.setText("ZZZ");
                
                Thread.sleep((dayDuration*1000)- (dayDuration*1000)/3);
                
                this.mutex.acquire();
                
                Interfaz.bossTxt.setText("Trabajando");
                
                Thread.sleep((dayDuration*1000)/3);
                Interfaz.daysToDeliver--;
                
                Interfaz.delivery.setText(Integer.toString(Interfaz.daysToDeliver));
                this.mutex.release();
                
            }catch(Exception e){
                
            }
        }
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
}
