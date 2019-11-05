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
    ArrayList<Process> processes;
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

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
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
        if(processIndex < processes.size())
            simulate(processes.get(processIndex));
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
