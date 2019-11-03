/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Replacement;

import Model.MainMemory;
import Model.Observer;
import Model.Page;
import Model.Process;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Charlie
 */
public class LocalMRU implements ReplacementPolicy, Observer<Page>{
    HashMap<Process, ArrayList<Page>> arrays; 

    public LocalMRU() {
        arrays = new HashMap<>();
    }
    
    
    
    
    
    @Override
    public int fetch(MainMemory men, Model.Process proc) {
        ArrayList<Page> mostRecentyUsed = getProcMRUArray(proc);
        Page recentPage = getMostRecentlyUsed(proc);
        if(recentPage == null){
            recentPage = men.getRandomPage();
        }
        mostRecentyUsed.remove(recentPage);
        return Arrays.asList(men.getPages()).indexOf(recentPage);
    }

    @Override
    public void notify(Page object) {
        ArrayList<Page> mostRecentyUsed = getProcMRUArray(object.getOwner());
               
        mostRecentyUsed.remove(object);
        mostRecentyUsed.add(object);
    }
    
    private ArrayList<Page> getProcMRUArray(Process proc){
        if(!arrays.containsKey(proc)){
            arrays.put(proc,new ArrayList<>());
        }
        
        return arrays.get(proc);
    }
    
    public Page getMostRecentlyUsed(Process proc){
        ArrayList<Page> mostRecentyUsed = getProcMRUArray(proc);
        //if object has not enought pages get other
        if(mostRecentyUsed.size() < 2 ) return null;
        return mostRecentyUsed.get(mostRecentyUsed.size()-2);
    }
    
    
    
}
