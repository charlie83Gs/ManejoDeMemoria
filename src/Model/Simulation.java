/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Charlie
 */
public class Simulation implements Swapable {
    BackingStore store;
    MainMemory memory;

    public Simulation(BackingStore store, MainMemory memory) {
        this.store = store;
        this.memory = memory;
    }


    @Override
    public MainMemory getMemory() {
        return memory;
    }

    @Override
    public BackingStore getStore() {
        return store;
    }
    
    
}
