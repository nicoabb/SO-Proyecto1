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
public class productor {
    
    private int storage;
    private int maxStorage;
    private int quantity;
    private int time;
    private int numProducers;
    private int maxProducers;

    public productor(int storage, int maxStorage, int quantity, int time, int numProducers, int maxProducers) {
        
        this.storage = storage;
        this.maxStorage = maxStorage;
        this.quantity = quantity;
        this.time = time;
        this.numProducers = numProducers;
        this.maxProducers = maxProducers;
    }
    
    public void callAssembler() {
        
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getNumProducers() {
        return numProducers;
    }

    public void setNumProducers(int numProducers) {
        this.numProducers = numProducers;
    }

    public int getMaxStorage() {
        return maxStorage;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTime() {
        return time;
    }

    public int getMaxProducers() {
        return maxProducers;
    }
    
    
}
