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
        if(pages.size() > proc.getPages().length){
            return Arrays.asList(men.getPages()).indexOf(pop());
        }
        return -1;
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
    
    public Page pop(){
        return pages.remove(0);
    }
    
}
