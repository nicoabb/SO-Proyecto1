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
    private Semaphore semPiece;
    private Semaphore semEnsamblador;
    private boolean stop;

    public Productor(String name, double dailyProduce, int dayDuration, Semaphore mutex, Semaphore semPiece, Semaphore semEnsamblador) {

        this.name = name;
        this.dailyProduce = dailyProduce;
        this.dayDuration = dayDuration;
        this.mutex = mutex;
        this.semPiece = semPiece;
        this.semEnsamblador = semEnsamblador;
        
    }
    
    public void run() {
        while (true) {
            try {
                semPiece.acquire();
                if(name=="tablas"){
                    System.out.println("Sleep" + name);
                    System.out.println(dayDuration);
                    System.out.println(dailyProduce);
                    System.out.println(Math.round((dayDuration * 1000) / dailyProduce));
                }
                        
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                
                switch (name) {
                    case "patas":
                        Interfaz.patasDisp++;
                        System.out.println("PATA");
                        Interfaz.avPatas.setText(Integer.toString(Interfaz.patasDisp));
                        break;
                    case "tornillos":
                        Interfaz.tornillosDisp++;
                        System.out.println("TORNILLO");
                        Interfaz.avTornillos.setText(Integer.toString(Interfaz.tornillosDisp));
                        break;
                    case "tablas":
                        System.out.println("TABLA");
                        Interfaz.tablasDisp++;
                        Interfaz.avTablas.setText(Integer.toString(Interfaz.tablasDisp));
                        break;
                }
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
