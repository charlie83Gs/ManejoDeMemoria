/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Replacement.ReplacementPolicy;
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
    int pageFaults = 0;    
    int pageHits = 0;

    

    public Simulation(BackingStore store, MainMemory memory, PlacementPolicy placementPolicy, ReplacementPolicy replacementPolicy) {
        this.store = store;
        this.memory = memory;
        this.processes = new ArrayList<>();
        this.placementPolicy = placementPolicy;
        this.replacementPolicy = replacementPolicy;
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
            if(memory.readPage(page)){
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
                    //System.out.println("swap "+ swapedIndex + " --- " + page.getPhysicalPosition());
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
