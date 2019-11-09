/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Replacement;

import Model.MainMemory;
import Model.Observer;
import Model.Page;
import Model.Simulation;
import Model.Process;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 *
 * @author Charlie
 */
public class FIFO implements ReplacementPolicy, Observer<Page>{
    private Queue<Page> pages;

    public FIFO() {
        pages = new ArrayDeque<>();
        
    }
    
    //selectes next replaced memory position
    @Override
    public int fetch(MainMemory men, Process proc) {
        MainMemory memory = men;
        Page nextPage = getNext(proc);
        


        //ineficient search for index
        int index = Arrays.asList(memory.getPages()).indexOf(nextPage);
        //recursive solution to -1 index
        if(index == -1) return fetch(men,proc);
        System.out.println("i ---------> "+ index);
        return index;
    }

    @Override
    public void notify(Page object) {
        //a page has been loaded to memory
        pages.add(object);
        
    }
    
    
    public Page getNext(Process proc){
        Page[] pageArray = (Page[])pages.toArray(new Page[pages.size()]);
        for (int i = 0 ; i < pageArray.length  ; i++) {
            Page page = pageArray[i];
            if(page.getOwner().getPriority()>= proc.getPriority()){
                pages.remove(page);
                return page;
            }
        }
        return pages.remove();
    }
    
    
    @Override
    public String toString() {
        return "FIFO";
    }
    
}
