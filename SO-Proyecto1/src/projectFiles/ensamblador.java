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

public class Ensamblador extends Thread {
    
    private int dayDuration;
    private double dailyProduce=0.5;
    private int tablas=1;
    private int patas=4;
    private int tornillos=40;
    private Semaphore mutexEnsamblador;

    private Semaphore mutexTablas; //Mutex
    private Semaphore semTablas; //Productor
    private Semaphore semEnsTablas; //Consumidor

    private Semaphore mutexPatas; //Mutez
    private Semaphore semPatas; //Productor
    private Semaphore semEnsPatas; //Consumidor

    private Semaphore mutexTornillos; //Mutex
    private Semaphore semTornillos; //Productor
    private Semaphore semEnsTornillos; //Consumidor

    public Ensamblador(int dayDuration,int numAssemblers, int maxAssemblers, Semaphore mutexEnsamblador, Semaphore mutexTablas, Semaphore semTablas, Semaphore semEnsTablas, Semaphore mutexPatas, Semaphore semPatas, Semaphore semEnsPatas, Semaphore mutexTornillos, Semaphore semTornillos, Semaphore semEnsTornillos) {
        this.dayDuration = dayDuration;
        this.mutexEnsamblador = mutexEnsamblador;
        this.mutexTablas = mutexTablas;
        this.semTablas = semTablas;
        this.semEnsTablas = semEnsTablas;
        this.mutexPatas = mutexPatas;
        this.semPatas = semPatas;
        this.semEnsPatas = semEnsPatas;
        this.mutexTornillos = mutexTornillos;
        this.semTornillos = semTornillos;
        this.semEnsTornillos = semEnsTornillos;
    }
    
    public void run(){
        
        while(true){
            try{
                //hacemos aquire a los semáforos de productor
                semEnsTablas.acquire(tablas);
                semEnsPatas.acquire(patas);
                semEnsTornillos.acquire(tornillos);
                
                mutexTablas.acquire();
                //Saco la cantidad de tablas que necesito
                
                Interfaz.tablasDisp-= tablas;
                Interfaz.avTablas.setText(Integer.toString(Interfaz.tablasDisp));
                
                mutexTablas.release();
                semTablas.release(tablas); //liberamos semáforo de productor
                
                mutexPatas.acquire();
                //Saco la cantidad de patas que necesito
                Interfaz.patasDisp-= patas;
                Interfaz.avPatas.setText(Integer.toString(Interfaz.patasDisp));
                
                mutexPatas.release();
                semPatas.release(patas); //liberamos semáforo de productor
                
                mutexTornillos.acquire();
                //Saco la cantidad de tornillos que necesito
                Interfaz.tornillosDisp-= tornillos;
                Interfaz.avTornillos.setText(Integer.toString(Interfaz.tornillosDisp));
                
                mutexTornillos.release();
                semTornillos.release(tornillos); //liberamos semáforo de productor
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                
                mutexEnsamblador.acquire();
                
                Interfaz.pahlsDisp +=1;
                Interfaz.avPahls.setText(Integer.toString(Interfaz.pahlsDisp));
                
                mutexEnsamblador.release();
                
            }catch(Exception e){
                
            }
        }
    }

    public int getDayDuration() {
        return dayDuration;
    }

    public void setDayDuration(int dayDuration) {
        this.dayDuration = dayDuration;
    }


    public void setDailyProduce(int dailyProduce) {
        this.dailyProduce = dailyProduce;
    }

    public int getTablas() {
        return tablas;
    }

    public void setTablas(int tablas) {
        this.tablas = tablas;
    }

    public int getPatas() {
        return patas;
    }

    public void setPatas(int patas) {
        this.patas = patas;
    }

    public int getTornillos() {
        return tornillos;
    }

    public void setTornillos(int tornillos) {
        this.tornillos = tornillos;
    }
    
}
