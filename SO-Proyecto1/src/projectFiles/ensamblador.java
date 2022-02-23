/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectFiles;

/**
 *
 * @author Nicolás Briceño y Christian Behrens
 */
public class Ensamblador {
    
    private int time;
    private int numDesks;
    private int numAssemblers;
    private int maxAssemblers;
    
    public void takeParts(Productor storage) {
        
    }

    public int getNumDesks() {
        return numDesks;
    }

    public void setNumDesks(int numDesks) {
        this.numDesks = numDesks;
    }

    public int getNumAssemblers() {
        return numAssemblers;
    }

    public void setNumAssemblers(int numAssemblers) {
        this.numAssemblers = numAssemblers;
    }

    public int getTime() {
        return time;
    }

    public int getMaxAssemblers() {
        return maxAssemblers;
    }
    
    
}
