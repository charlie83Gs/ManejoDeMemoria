/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Replacement.ReplacementPolicy;
import Model.Replacement.ReplacementScope;
import java.util.ArrayList;

/**
 *
 * @author Charlie
 */
public class Simulation implements Swapable {
    BackingStore store;
    MainMemory memory;
    ArrayList<Process> processes, onMemory = new ArrayList(), finished = new ArrayList();
    PlacementPolicy placementPolicy;
    ReplacementPolicy replacementPolicy;
    ReplacementScope scope;
    int pageFaults = 0;    
    int pageHits = 0;

    public Simulation(BackingStore store, MainMemory memory, PlacementPolicy placementPolicy, ReplacementPolicy replacementPolicy, ReplacementScope scope) {
        this.store = store;
        this.memory = memory;
        this.processes = new ArrayList<>();
        this.placementPolicy = placementPolicy;
        this.replacementPolicy = replacementPolicy;
        this.scope = scope;
    }

    public ArrayList<Process> getOnMemory() {
        return onMemory;
    }

    public void setOnMemory(ArrayList<Process> onMemory) {
        this.onMemory = onMemory;
    }

    public ArrayList<Process> getFinished() {
        return finished;
    }

    public void setFinished(ArrayList<Process> finished) {
        this.finished = finished;
    }

    public int getHighestPriorityProcess(ArrayList<Process> listaProcesos){
        
        int lowestPriority = Integer.MAX_VALUE, index = -1;
        for(int i = 0; i < listaProcesos.size(); i++){
            if(listaProcesos.get(i).getPriority() < lowestPriority){
                index = i;
                lowestPriority = listaProcesos.get(i).getPriority();
            }
        }
        
        return index;
    }
    
    public int getLowestPriorityProcess(ArrayList<Process> listaProcesos){
        
        int lowestPriority = -1, index = -1;
        for(int i = 0; i < listaProcesos.size(); i++){
            if(listaProcesos.get(i).getPriority() > lowestPriority){
                index = i;
                lowestPriority = listaProcesos.get(i).getPriority();
            }
        }
        
        return index;
    }
    
    public void updateOnMemoryList(int multiProgrammingDegree){
        
        if(this.onMemory.size() < multiProgrammingDegree){
            int toLoad = multiProgrammingDegree - this.onMemory.size(), toLoadIndex;
            for(int i = 0; i < toLoad; i++){
                toLoadIndex = this.getHighestPriorityProcess(this.processes);
                if(toLoadIndex >= 0){
                    this.onMemory.add(this.processes.remove(toLoadIndex));
                }
            }
        }
        else if(this.onMemory.size() > multiProgrammingDegree){
            int toUnload = this.onMemory.size() - multiProgrammingDegree, toUnloadIndex;
            for(int i = 0; i < toUnload; i++){
                toUnloadIndex = this.getLowestPriorityProcess(this.onMemory);
                if(toUnloadIndex >= 0){
                    this.processes.add(this.onMemory.remove(toUnloadIndex));
                }
            }
        }
    }
    
    public boolean cleanOnMemoryList(){
        boolean cleaned = false;
        for(int i = 0; i < this.onMemory.size(); i++){
            if(this.onMemory.get(i).hasFinished()){
                System.out.println("process:" + this.onMemory.get(i).getId() + " has finished");
                cleaned = true;
                this.finished.add(this.onMemory.remove(i));
            }
        }
        return cleaned;
    }
    
    public void setPlacementPolicy(PlacementPolicy placementPolicy) {
        this.placementPolicy = placementPolicy;
    }

    public void setReplacementPolicy(ReplacementPolicy replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public void setScope(ReplacementScope scope) {
        this.scope = scope;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
        this.onMemory = new ArrayList();
        this.finished = new ArrayList();
        this.processes = processes;
    }

    public PlacementPolicy getPlacementPolicy() {
        return placementPolicy;
    }

    public ReplacementPolicy getReplacementPolicy() {
        return replacementPolicy;
    }

    public ReplacementScope getScope() {
        return scope;
    }

    @Override
    public MainMemory getMemory() {
        return memory;
    }

    @Override
    public BackingStore getStore() {
        return store;
    }
    
    public void addProcess(Process process){
        processes.add(process);
    }
    
    public boolean removeProcess(Process process){
        return processes.remove(process);
    }
    
    //simulate with index
    public void simulate(int processIndex){
        if(processIndex < this.onMemory.size())
            simulate(this.onMemory.get(processIndex));
    }
    
    //simulate with Process
    public void simulate(Process process){
        
        if(!process.hasFinished()){
            int next = process.getNext();
            Page page = process.getPage(next);
            int freeIndex = placementPolicy.getNext(this);
            //System.out.println(freeIndex);
            //System.out.println("n " + next);
            //if there are free pages
            
            //visit page if posible
            if(memory.readPage(page) && !(scope == ReplacementScope.LOCAL && process.getAvailablePages() < 1)){
                pageHits++;
                //System.out.println("Page hit!!");
            //page fault
            }else{
                pageFaults++;
                //System.out.println(pageFaults);
                //load page
                if(freeIndex >= 0){

                    MemorySwaper.SwapIn(this, freeIndex, page.getPhysicalPosition());

                //if there are not more free pages
                //page swap
                }else{
                    //execute replacement policy
                    int swapedIndex = replacementPolicy.fetch(memory, process);
                    System.out.println("swap "+ swapedIndex + " --- " + page.getPhysicalPosition());
                    MemorySwaper.SwapIn(this, swapedIndex, page.getPhysicalPosition());
                }

               
            }
            Clock.getInstance().simulate(1);
        }
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public int getPageHits() {
        return pageHits;
    }
    
}
