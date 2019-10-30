/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

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

    public Process(int id, FetchList fetchlist, int pages, int prority,BackingStore store) {
        this.id = id;
        this.fetchlist = fetchlist;
        this.pages = new Page[pages];
        this.priority = priority;
        
        
        for (int i = 0; i < this.pages.length; i++) {
           this.pages[i] = store.allocatePage(this, i); // storing random integers in an array
        
        }
        //create new memory table
        this.memoryTable = new MemoryTable(this.pages);
    }
    
    public boolean isOnMemory(Page page){
        return memoryTable.isOnMemory(page);
    }
    
    public Page getPage(int index){
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
    
    public int getNext(){
        return fetchlist.getNext();
    }
    
    public boolean hasFinished(){
        return fetchlist.isFinished();
    }

    @Override
    public String toString() {
        return "Process{" + "id=" + id + ", fetchlist=" + fetchlist + ", pages=" + pages + ", memoryTable=" + memoryTable + ", priority=" + priority + '}';
    }
    
    
    
}
