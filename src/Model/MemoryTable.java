/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author curso
 */
public class MemoryTable {
    private HashMap<Page,MemoryFrame> frames;
    private int size = -1;

    public MemoryTable(Page[] pages) {
        this.frames = new HashMap<>();
        //create memory frames from pages
        for (int i = 0; i < pages.length; i++) {
            MemoryFrame newFrame = new MemoryFrame(pages[i],-1);
            frames.put(pages[i], newFrame);
            
        }
    }
    
    public void setFixedSize(int newSize){
        this.size = newSize;
    }
    /**
     * @deprecated 
     * @param oldPage
     * @param newPage 
     */
    public void pageSwap(Page oldPage, Page newPage){
        
        //load new Frame to memory
        load(newPage,oldPage);
        
        //send old memory to backing store
        store(oldPage);
        
    }
    
    /**
     * This will modify real page position required to load into memory
     * @param page 
     */
    public boolean ownsPage(Page page){
        return frames.containsKey(page);
    }
    
    public void store(Page page){
        MemoryFrame oldFrame = frames.get(page);
        oldFrame.toStore();
    }
    
    public void load(Page oldPage,Page newPage){
        MemoryFrame oldFrame = frames.get(oldPage);
        MemoryFrame newFrame = frames.get(newPage);
        newFrame.toMemory(oldFrame.getRealPage(),Clock.getInstance().getTime());
    }
    
    public void load(int realPage,Page newPage){
        MemoryFrame newFrame = frames.get(newPage);
        newFrame.toMemory(realPage,Clock.getInstance().getTime());
    }
    
    public void visit(Page page){
        MemoryFrame frame = frames.get(page);
        frame.visit(Clock.getInstance().getTime());
    }
    
}
