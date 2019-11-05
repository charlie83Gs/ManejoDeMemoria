/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Replacement;

import Model.MainMemory;
import Model.Observer;
import Model.Page;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

/**
 *
 * @author Charlie
 */
public class LocalLRU implements ReplacementPolicy, Observer<Page>{
    private HashMap<Model.Process ,ArrayList<Page>> arrays; 
    //private ArrayList<Page> pages;
    
    public LocalLRU(){
        //pages = new ArrayList();   
        arrays = new HashMap<>();
    }
    
    @Override
    public int fetch(MainMemory men, Model.Process proc) {
        ArrayList<Page> pages = getArray(proc);
        if(pages.isEmpty()) pages.add(men.getRandomPage());
        
        
        return Arrays.asList(men.getPages()).indexOf(pop(proc));
       
    }

    //reads page acces
    @Override
    public void notify(Page object) {
        ArrayList<Page> pages = getArray(object.getOwner());
        int index = pages.indexOf(object);
        if(index == -1){//pageFault la pagina no está en la lista
            push(object);
        }
        else{//la pagina está en la lista pero debe ser cambiada de lugar a la más reciente
            push(pages.remove(index));
        }
        
    }
    
    public void push(Page page){
        ArrayList<Page> pages = getArray(page.getOwner());
        pages.add(page);
    }
    
    public Page pop(Model.Process proc){
        ArrayList<Page> pages = getArray(proc);
        return pages.remove(0);
    }
    
    
    private ArrayList<Page> getArray(Model.Process proc){
        //instantiate a queue for process if it does not extits
        if(!arrays.containsKey(proc)){
            ArrayList<Page> newArray = new ArrayList<>();
            arrays.put(proc, newArray);
        }
        return arrays.get(proc);
    }
    
    @Override
    public String toString() {
        return "LRU";
    }
    
}
