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
public class ProdTablas extends Thread{
    
    private int dayDuration;
    private double dailyProduce =0.33;
    private Semaphore mutex;
    private Semaphore semPiece;
    private Semaphore semEnsamblador;
    private boolean stop;

    public ProdTablas( int dayDuration, Semaphore mutex, Semaphore semPiece, Semaphore semEnsamblador) {


        this.dayDuration = dayDuration;
        this.mutex = mutex;
        this.semPiece = semPiece;
        this.semEnsamblador = semEnsamblador;
        
    }
    
    public void run() {
        while (!stop) {
            try {
                semPiece.acquire();
                
                        
                Thread.sleep(1000*dayDuration*3);
                mutex.acquire();
                Interfaz.tablasDisp++;
                Interfaz.avTablas.setText(Integer.toString(Interfaz.tablasDisp));

                mutex.release();
                semEnsamblador.release();

            } catch (Exception e) {

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