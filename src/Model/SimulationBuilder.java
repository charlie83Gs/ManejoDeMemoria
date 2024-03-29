/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Replacement.AbstractReplacementFactory;
import Model.Replacement.GlobalReplacementFactory;
import Model.Replacement.LocalReplacementFactory;
import Model.Replacement.ReplacementPolicy;
import Model.Replacement.ReplacementScope;
import java.util.ArrayList;
//import sun.java2d.cmm.Profile;

/**
 *
 * @author Charlie
 */
public class SimulationBuilder {
    private int DEFAULT_PAGE_SIZE = 128;
    int store;
    int memory;
    int prepaging;
    int multiprograming;
    boolean precleaning ;
    PageProfile profile;
    PlacementPolicy placementPolicy;
    ReplacementScope replacementScope = ReplacementScope.GLOBAL;
    AbstractReplacementFactory replacementFactory;
    ReplacementPolicyType replacementPolicyType = ReplacementPolicyType.FIFO;
    
    public SimulationBuilder() {
        profile = new PageProfile(DEFAULT_PAGE_SIZE);
        //default placement policy
        placementPolicy = new FirstAvailable();
        //create corresponding replacement factory for replacement scope
        setReplacementScope(replacementScope);
        prepaging = 0;
        multiprograming = 3;
        precleaning = false;
    }
    
    
    public void setProfile(PageProfile profile){
        this.profile = profile;
    }
    
    public void setPlacementPolicy(PlacementPolicyType type){
        if(type == PlacementPolicyType.FIRST_AVAILABLE){
            placementPolicy = new FirstAvailable();
        }else if(type == PlacementPolicyType.NEXT_AVAILLABLE){
            placementPolicy = new NextAvailable();
        }
    }
    
    public PlacementPolicy getPlacementPolicy(PlacementPolicyType type){
        if(type == PlacementPolicyType.FIRST_AVAILABLE){
            return new FirstAvailable();
        }else if(type == PlacementPolicyType.NEXT_AVAILLABLE){
            return new NextAvailable();
        }
        return null;
    }

    public void setStore(int store) {
        this.store = store;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
    
    public void setReplacementScope(ReplacementScope scope){
        replacementScope = scope;
        if(scope == ReplacementScope.GLOBAL){
            replacementFactory = new GlobalReplacementFactory();
        }else if(scope == ReplacementScope.LOCAL){
            replacementFactory = new LocalReplacementFactory();
        }
    }

    public void setReplacementPolicy(ReplacementPolicyType replacementPolicyType) {
        this.replacementPolicyType = replacementPolicyType;
    }
    
    public ReplacementPolicy getReplacementPolicy(MainMemory memory){
        if(replacementPolicyType == ReplacementPolicyType.FIFO){
            return replacementFactory.getFIFO(memory);
        }else if(replacementPolicyType == ReplacementPolicyType.LFU){
            return replacementFactory.getLFU(memory);
        }else if(replacementPolicyType == ReplacementPolicyType.LRU){
            return replacementFactory.getLRU(memory);
        }else if(replacementPolicyType == ReplacementPolicyType.MRU){
            return replacementFactory.getMRU(memory);
        }else if(replacementPolicyType == ReplacementPolicyType.SECOND_CHANCE){
            return replacementFactory.getSecondChance(memory);
        }
        return null;
        
    }

    public void setPrepaging(int prepaging) {
        this.prepaging = prepaging;
    }

    public void setMultiprograming(int multiprograming) {
        this.multiprograming = multiprograming;
    }

    public void setPrecleaning(boolean precleaning) {
        this.precleaning = precleaning;
    }
    
    
    
    
    
    
    
    public Simulation getResult(){
        //create storage mechanisms acording to page size
        
        BackingStore storeObject = new BackingStore(store, profile);
        MainMemory memoryObject = new MainMemory(memory, profile);
        ReplacementPolicy replacementPolicyObject = getReplacementPolicy(memoryObject);
        
        Simulation sim =  new Simulation(storeObject, memoryObject, placementPolicy,replacementPolicyObject,replacementScope,multiprograming,prepaging);
        sim.setPrecleaning(precleaning);
        return sim;
    }
}
