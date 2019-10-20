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
public class MemoryFrame {
    private Page logicalPage;
    private int realPage;
    private boolean onMemory;
    private boolean dirty;
    private int lastVisit;
    private int fetched;
    private int visits;

    public MemoryFrame(Page logicalPage, int realPage) {
        this.logicalPage = logicalPage;
        this.realPage = realPage;
    }
    
    public void visit(int step){
        visits ++;
        lastVisit = step;
        //onMemory = true;
        
    
    }
    /**
     * @return this function sets variables as if its moved to main memory
     */
    public void toMemory(int realPage,int step){
        this.realPage = realPage;
        onMemory = true;
        lastVisit = step;
        fetched = step;
        visits = 0;
    }
    /**
     * @return this function sets variables as if its moved to store
     */
    public void toStore(){
        realPage = -1;
        onMemory = false;
        dirty = false;
        lastVisit = 0;
        fetched = 0;
        visits = 0;
    }
   

    public Page getLogicalPage() {
        return logicalPage;
    }

    public void setLogicalPage(Page logicalPage) {
        this.logicalPage = logicalPage;
    }

    public int getRealPage() {
        return realPage;
    }

    public void setRealPage(int realPage) {
        this.realPage = realPage;
    }

    public boolean isOnMemory() {
        return onMemory;
    }

    public void setOnMemory(boolean onMemory) {
        this.onMemory = onMemory;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public int getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(int lastVisit) {
        this.lastVisit = lastVisit;
    }

    public int getFetched() {
        return fetched;
    }

    public void setFetched(int fetched) {
        this.fetched = fetched;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
    
    
}
