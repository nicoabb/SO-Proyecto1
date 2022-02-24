/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectFiles;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño y Christian Behrens
 */
public class Productor extends Thread{
    
    private String name;
    private int dayDuration;
    private double dailyProduce;
    private Semaphore mutex;
    private Semaphore semPieces;
    private Semaphore semEnsamblador;

    public Productor(int storage, int dailyProducton, int maxStorage, int quantity, int time, int numProducers, int maxProducers) {

        this.name = name;
        this.dayDuration = dayDuration;
        this.dailyProduce = dailyProduce;
        this.mutex = mutex;
        this.semPieces = semPieces;
        this.semEnsamblador = semEnsamblador;
        
    }
    
    public void run() {
        while (true) {
            try {
                semPieces.acquire();
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                switch (this.name) {
                    case "patas":
                        Interfaz.patasDisp++;
                        break;
                    case "tornillo":
                        Interfaz.tornillosDisp++;
                        break;
                    case "tablas":
                        Interfaz.tablasDisp++;
                        break;
                }
                mutex.release();
                semEnsamblador.release();

            } catch (Exception e) {

            }
        }
    }
    
    public void callAssembler() {
        
    }
    
    
}
