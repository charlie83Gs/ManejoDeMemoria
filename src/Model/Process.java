/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author curso
 */
public class Process {
    private int id;
    private FetchList fetchlist;
    private Page[] pages;
    private MemoryTable memoryTable;
    private int priority;
    int totalPages;
    private int r, g, b;
    private Random rand = new Random();
    

    public Process(int id, FetchList fetchlist, int pages, int priority,BackingStore store, int totalPages) {
        this.id = id;
        this.fetchlist = fetchlist;
        this.pages = new Page[pages];
        this.priority = priority;
        this.totalPages = totalPages;
        
        for (int i = 0; i < this.pages.length; i++) {
           this.pages[i] = store.allocatePage(this, i); // storing random integers in an array
        
        }
        
        this.r = rand.nextInt(255);
        this.g = rand.nextInt(255);
        this.b = rand.nextInt(255);
        
        
        //create new memory table
        this.memoryTable = new MemoryTable(this.pages);
    }
    
    public Process(int id, int pages, int priority,BackingStore store, int totalPages) {
        this.id = id;
        this.fetchlist = fetchlist;
        this.pages = new Page[pages];
        this.priority = priority;
        this.totalPages = totalPages;
        
        
        for (int i = 0; i < this.pages.length; i++) {
           this.pages[i] = store.allocatePage(this, i); // storing random integers in an array
        
        }
        
        Random rand = new Random();
        this.r = 255 - rand.nextInt(200);
        this.g = rand.nextInt(255);
        this.b = rand.nextInt(255);
        
        //create new memory table
        this.memoryTable = new MemoryTable(this.pages);
    }

    public int getPriority() {
        return priority;
    }
    /**
     * 
     * @param page
     * @return last visit to this page
     */
    public int getLastVisit(Page page){
        return memoryTable.getFrame(page).getLastVisit();
    }
    
    public void setFetchlist(FetchList fetchlist) {
        this.fetchlist = fetchlist;
    }
    
    public boolean isOnMemory(Page page){
        return memoryTable.isOnMemory(page);
    }
    
    public Page getPage(int index){
        if(rand.nextFloat() > 0.5){
            pages[index].setDirty(true);
        }
        return pages[index];
    }

    public Page[] getPages() {
        return pages;
    }
    
    public void pageSwap(Page oldPage, Page newPage){
        
        memoryTable.pageSwap(oldPage, newPage);
    }
    
    public void pageLoad(int memoryPosition, Page newPage){
        memoryTable.load(memoryPosition, newPage);
    }
    
    public void pageStore(Page page){
        memoryTable.store(page);
    }
    
    public void visit(Page page){
        memoryTable.visit(page);
    }
    
    public int getNext(){
        return fetchlist.getNext();
    }
    
    public boolean hasFinished(){
        return fetchlist.isFinished();
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    
    
    
    @Override
    public String toString() {
        return "Process{" + "id=" + id + ", fetchlist=" + fetchlist + ", pages=" + pages + ", memoryTable=" + memoryTable + ", priority=" + priority + '}';
    }
    
    public String toStringGrafico(){
        if(this.hasFinished()){
            return "Process: " + id + " priority: " + this.priority;
        }
        else{
            return "P: " + id + " prior: " + this.priority + " requests:" + this.fetchlist.getPendingList();
        }
    }
    
    public int getAvailablePages(){
        int onMemory = 0;
        
        for(Page page : pages){
            if(memoryTable.isOnMemory(page)) onMemory++;
        }
        return totalPages - onMemory;
    }

    public int getId() {
        return id;
    }
    
}
