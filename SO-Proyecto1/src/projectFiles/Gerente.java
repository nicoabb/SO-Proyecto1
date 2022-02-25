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
public class Gerente extends Thread{
    private int dayDuration;
    private int daysToDeliver;
    private boolean stop;
    private Semaphore mutex;
    public Gerente(int dayDuration, Semaphore mutex, int daysToDeliver){
        
        this.daysToDeliver = daysToDeliver;
        this.dayDuration = dayDuration;
        this.stop = false;
        this.mutex = mutex;
    }
    
    public void run(){
        while(!this.stop){
            
            try{
                Interfaz.managerTxt.setText("ZZZ");
                
                mutex.acquire();
                Interfaz.managerTxt.setText("TRABAJANDO");
                if (Integer.parseInt(Interfaz.delivery.getText()) == 0){
                    Interfaz.pahlsDisp = 0; 
                    Interfaz.avPahls.setText(Integer.toString(Interfaz.pahlsDisp));
                    Interfaz.daysToDeliver = daysToDeliver;
                    Interfaz.delivery.setText(Integer.toString(daysToDeliver));

                }else{
                    Thread.sleep((dayDuration*1000)/3);
                
            }
                mutex.release();
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
