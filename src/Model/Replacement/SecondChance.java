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
//
public class SecondChance implements ReplacementPolicy, Observer<Page>{
    private Queue<Page> pages;
    HashMap<Page,Boolean> secondChanceFlag;

    public SecondChance() {
        pages = new ArrayDeque<>();
        secondChanceFlag = new HashMap<>();
        
    }
    
    //selectes next replaced memory position
    @Override
    public int fetch(MainMemory men, Process proc) {
        MainMemory memory = men;
        Page nextPage = pages.peek();
        while(secondChanceFlag.get(nextPage)){
            //System.out.println("p: " + nextPage);
            //lost second chance
            secondChanceFlag.put(nextPage, Boolean.FALSE);
            pages.add(pages.remove());
            nextPage = pages.peek();
        }
        pages.remove();
        //System.out.println(nextPage);
        
        //ineficient search for index
        int index = Arrays.asList(memory.getPages()).indexOf(nextPage);
        return index;
    }

    @Override
    public void notify(Page object) {
        //a page has been loaded to memory
        secondChanceFlag.put(object, Boolean.TRUE);
        pages.add(object);
    }
    
    @Override
    public String toString() {
        return "Second Chance";
    }
}
