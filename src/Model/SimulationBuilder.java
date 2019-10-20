/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import sun.java2d.cmm.Profile;

/**
 *
 * @author Charlie
 */
public class SimulationBuilder {
    private int DEFAULT_PAGE_SIZE = 128;
    int store;
    int memory;
    PageProfile profile;

    public SimulationBuilder() {
        profile = new PageProfile(DEFAULT_PAGE_SIZE);
    }
    
    
    
    public void setProfile(PageProfile profile){
        this.profile = profile;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
    

    
    public Simulation getResult(){
        //create storage mechanisms acording to page size
        
        BackingStore storeObject = new BackingStore(store, profile);
        MainMemory memoryObject = new MainMemory(memory, profile);
        
        return new Simulation(storeObject, memoryObject);
    }
}
