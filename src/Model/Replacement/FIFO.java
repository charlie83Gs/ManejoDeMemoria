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
        Page nextPage = pages.remove();
        //ineficient search for index
        int index = Arrays.asList(memory.getPages()).indexOf(nextPage);
        return index;
    }

    @Override
    public void notify(Page object) {
        //a page has been loaded to memory
        pages.add(object);
        
    }
    
}
