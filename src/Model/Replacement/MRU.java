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

/**
 *
 * @author Charlie
 */
public class MRU implements ReplacementPolicy, Observer<Page>{
    private ArrayList<Page> mostRecentyUsed;

    public MRU() {
        mostRecentyUsed = new ArrayList<>();
    }
    
    
    
    
    
    @Override
    public int fetch(MainMemory men, Process proc) {
        Page recentPage = getMostRecentlyUsed();
        return Arrays.asList(men.getPages()).indexOf(recentPage);
    }

    @Override
    public void notify(Page object) {
        mostRecentyUsed.remove(object);
        mostRecentyUsed.add(object);
    }
    
    public Page getMostRecentlyUsed(){
        //get the prcess before current execution
        //currently visited page is alredy on memory
        return mostRecentyUsed.get(mostRecentyUsed.size()-2);
    }
    
    
    
}
