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
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

/**
 *
 * @author Charlie
 */
public class LocalSecondChance implements ReplacementPolicy, Observer<Page>{
    //private Queue<Page> pages;
    HashMap<Page,Boolean> secondChanceFlag;
    HashMap<Process,Queue<Page>> queues;
    
    public LocalSecondChance() {
        secondChanceFlag = new HashMap<>();
        queues = new HashMap<>();
        
    }
    
    //selectes next replaced memory position
    @Override
    public int fetch(MainMemory men, Model.Process proc) {
        MainMemory memory = men;
        Queue<Page> pages = getProcessQueue(proc);
        
        Page nextPage = null;
        if(!pages.isEmpty()){
            nextPage = pages.peek();
            while(nextPage != null && secondChanceFlag.get(nextPage)){
                //System.out.println("p: " + nextPage);
                //lost second chance
                secondChanceFlag.put(nextPage, Boolean.FALSE);
                pages.add(pages.remove());
                nextPage = pages.peek();
            }
            pages.remove();
        }
        
        //if pages is empty get random
        if(nextPage == null)nextPage = men.getRandomPage();
        
        //System.out.println(nextPage);
        
        //ineficient search for index
        int index = Arrays.asList(memory.getPages()).indexOf(nextPage);
        return index;
    }

    @Override
    public void notify(Page object) {
        Queue<Page> pages = getProcessQueue(object.getOwner());
        //a page has been loaded to memory
        secondChanceFlag.put(object, Boolean.TRUE);
        pages.add(object);
    }
    
    public Queue<Page> getProcessQueue(Model.Process proc){
        if(!queues.containsValue(proc)){
            queues.put(proc, new ArrayDeque<>());
        }
        
        return queues.get(proc);
    }

    @Override
    public String toString() {
        return "Second Chance";
    }
    
    
    
}
