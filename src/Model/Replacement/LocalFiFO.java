/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Replacement;

import Model.MainMemory;
import Model.Observer;
import Model.Page;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

/**
 *
 * @author Charlie
 */
public class LocalFiFO implements ReplacementPolicy, Observer<Page>{
    private HashMap<Model.Process ,Queue<Page>> queues; 

    public LocalFiFO() {
        queues = new HashMap<>();
    }

    @Override
    public int fetch(MainMemory mem, Model.Process proc) {
        MainMemory memory = mem;
        //instantiate a queue for process if it does not extits 
        Queue<Page> currentQueue = getQueue(proc);
        //if queue is empty
        if(currentQueue.isEmpty()) currentQueue.add(memory.getRandomPage());
        
        
        
        Page nextPage = currentQueue.remove();
        //ineficient search for index
        int index = Arrays.asList(memory.getPages()).indexOf(nextPage);
        return index;
        
    }
    
    @Override
    public void notify(Page object) {
        Model.Process owner = object.getOwner();
        Queue<Page> currentQueue = getQueue(owner);
        //a page has been loaded to memory
        currentQueue.add(object);
        
    }
    
    private Queue<Page> getQueue(Model.Process proc){
        //instantiate a queue for process if it does not extits
        if(!queues.containsKey(proc)){
            Queue<Page> newQueue = new ArrayDeque<>();
            queues.put(proc, newQueue);
        }
        return queues.get(proc);
    }
    
    
}
