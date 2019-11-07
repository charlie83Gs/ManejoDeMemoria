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
 * @author J
 */
public class LRU implements ReplacementPolicy, Observer<Page>{

    private ArrayList<Page> pages;
    
    public LRU(){
        pages = new ArrayList();   
    }
    
    @Override
    public int fetch(MainMemory men, Process proc) {
        Page page = pop(men, proc);
        
        int index =Arrays.asList(men.getPages()).indexOf(page);
        if(index == -1) return fetch(men,proc);
        return index;
        

    }
    
    

    //reads page acces
    @Override
    public void notify(Page object) {
        int index = pages.indexOf(object);
        if(index == -1){//pageFault la pagina no está en la lista
            push(object);
        }
        else{//la pagina está en la lista pero debe ser cambiada de lugar a la más reciente
            push(pages.remove(index));
        }
        
    }
    
    public void push(Page page){
        pages.add(page);
    }
    
    public Page pop(MainMemory men , Process proc){
        int selected = -1;
        for (int i = 0; i < pages.size()-1; i++) {
            Page page = pages.get(i);
            System.out.println("pppp " + page.getOwner().getPriority() +">=" + proc.getPriority());
            if(page.getOwner().getPriority() >= proc.getPriority()){
                selected = i;
                break;
            }
            
        }
        System.out.println("selected -> " + selected );
        if(selected == -1) return men.getRandomPage();
        
        return pages.remove(selected);
    }
    
    @Override
    public String toString() {
        return "LRU";
    }
    
}
