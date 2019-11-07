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
public class LFU implements ReplacementPolicy, Observer<Page>{
    HashMap<Page,Integer> uses;
    
    public LFU() {
        uses = new HashMap<>();
    }
    
        
    @Override
    public int fetch(MainMemory men, Process proc) {
        Page[] pages = men.getPages();
        
        int minUses = Integer.MAX_VALUE;
        int minVisits = Integer.MAX_VALUE;

        Page leastUsed = null;
        for(Page page : pages){
            int totalUses = getUses(page);
            int lastVisit = page.getOwner().getLastVisit(page);
            boolean higherPriority = proc.getPriority() <= page.getOwner().getPriority();
            if( totalUses <= minUses  && lastVisit < minVisits && higherPriority ){
                minUses = totalUses;
                minVisits = lastVisit;
                leastUsed = page;
            }
        }
        
        if(leastUsed == null) leastUsed = men.getRandomPage();
        
        //reset lest used page
        uses.put(leastUsed, 0);
        
        int index = Arrays.asList(men.getPages()).indexOf(leastUsed);
        //if(index == -1) return fetch(men,proc);
        return index;
        
    }
    
    private int getUses(Page page){
        if(!uses.containsKey(page)){
            uses.put(page, 0);
        }
        
        return uses.get(page);
    }

    @Override
    public void notify(Page object) {
        int totalUses = getUses(object);
        uses.put(object, totalUses + 1);
    }
    
    @Override
    public String toString() {
        return "LFU";
    }
    
}
