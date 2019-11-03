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
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Charlie
 */
public class LocalLFU implements ReplacementPolicy, Observer<Page>{
    HashMap<Process, HashMap<Page,Integer>> procMap;
    
    public LocalLFU() {
        procMap = new HashMap<>();
    }
    
        
    @Override
    public int fetch(MainMemory men, Process proc) {
        HashMap<Page, Integer> uses = getUsesMap(proc);
        
        Page[] pages = proc.getPages();
        
        int minUses = -1;
        Page leastUsed = null;
        for(Page page : pages){
            int totalUses = getUses(page,uses);
            if( totalUses < minUses && totalUses > 0){
                //System.out.println("uses: " + totalUses);

                minUses = totalUses;
                leastUsed = page;
            }
        }
        if(leastUsed == null) leastUsed = men.getRandomPage();
        //reset lest used page
        uses.put(leastUsed, 0);
        
        int index = Arrays.asList(men.getPages()).indexOf(leastUsed);
        return index;
        
    }
    
    private HashMap<Page, Integer> getUsesMap(Process proc){
        if(!procMap.containsKey(proc)){
            procMap.put(proc,  new HashMap<>());
        }
        
        return procMap.get(proc);
    }
    
    private int getUses(Page page, HashMap<Page,Integer> uses){
        if(!uses.containsKey(page)){
            uses.put(page, 0);
        }
        
        return uses.get(page);
    }
    
    @Override
    public void notify(Page object) {
        HashMap<Page, Integer> uses = getUsesMap(object.getOwner());
        int totalUses = getUses(object,uses);
        uses.put(object, totalUses + 1);
    }
    
}
