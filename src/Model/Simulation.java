/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Replacement.ReplacementPolicy;
import Model.Replacement.ReplacementScope;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    int degreeOfMultiprograming;
    int prepaging;
    int pageFaults = 0;    
    int pageHits = 0;
    
    boolean precleaning = false;
    

    
    public Simulation(BackingStore store, MainMemory memory, PlacementPolicy placementPolicy, ReplacementPolicy replacementPolicy, ReplacementScope scope, int multiprograming, int prepaging) {
        this.store = store;
        this.memory = memory;
        this.processes = new ArrayList<>();
        this.placementPolicy = placementPolicy;
        this.replacementPolicy = replacementPolicy;
        this.scope = scope;
        this.degreeOfMultiprograming = multiprograming;
        this.prepaging = prepaging;
        
        sortByPriority();
    }
    
    private void prepageProcess(Process p){
        if(prepaging < 1 ) return;
        
        int localPrepage = Math.min(prepaging, p.getAvailablePages());
        System.out.println("prepaging " + localPrepage + "---" +  p.getAvailablePages());
        for (int j = 0; j < localPrepage; j++) {
            
            int freeIndex = placementPolicy.getNext(this);
            Page page = p.getPage(j);
            //just notify the replacement algorithms
            memory.readPage(page);
            //prepage desired page
            putPageInMemory(page, p);
        }
    }
    
    
    
    private void sortByPriority(){
        
        Comparator<Process> procComparator = new Comparator<Process>(){

        public int compare(Process o1, Process o2)
        {
           int pages1 = o1.getAvailablePages(); 
           int pages2 = o2.getAvailablePages();
           
           if(pages1 == pages2) return 0;
           if(pages1 > pages2) return 1;
           return -1;
        }
        };
        Collections.sort(processes, procComparator);
        
    
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
    
    public boolean isInMemory(int procId){
    
        for(Process p: this.onMemory){
            if(procId == p.getId()){
                return true;
            }
        }
        return false;
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
    
    //must add process to memory
    //must prepage when a process is added to memory
    public void updateOnMemoryList(int multiProgrammingDegree){
        
        if(this.onMemory.size() < multiProgrammingDegree){
            int toLoad = multiProgrammingDegree - this.onMemory.size(), toLoadIndex;
            for(int i = 0; i < toLoad; i++){
                toLoadIndex = this.getHighestPriorityProcess(this.processes);
                if(toLoadIndex >= 0){
                    Process loaded = this.processes.remove(toLoadIndex);
                    this.onMemory.add(loaded);
                    prepageProcess(loaded);
                }
            }
        }
        
        else if(this.onMemory.size() > multiProgrammingDegree){
            int toUnload = this.onMemory.size() - multiProgrammingDegree, toUnloadIndex;
            for(int i = 0; i < toUnload; i++){
                toUnloadIndex = this.getLowestPriorityProcess(this.onMemory);
                if(toUnloadIndex >= 0){
                    
                    Process unloaded = this.onMemory.remove(toUnloadIndex);
                    this.processes.add(unloaded);
                    //this.memory.unloadProcess(unloaded);
                }
            }
        }
    }
    
    public boolean cleanOnMemoryList(){
        boolean cleaned = false;
        for(int i = 0; i < this.onMemory.size(); i++){
            if(this.onMemory.get(i).hasFinished()){
                Process unloaded = this.onMemory.get(i);
                System.out.println("process:" + unloaded.getId() + " has finished");
                cleaned = true;
                //clean this process memory
                this.memory.unloadProcess(unloaded);
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
    
    public void preClean(){
        Page[] pages = memory.getPages();
        
        for (Page page : pages) {
            if(page != null){
                page.setDirty(false);
            }
        }
    }
    
    
    //simulate with Process
    public void simulate(Process process){
        
        if(!process.hasFinished()){
            int next = process.getNext();
            Page page = process.getPage(next);
            
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
                putPageInMemory(page, process);  
            }
            //preclean every 5 ciles if enabled
            int time = Clock.getInstance().getTime();
            if(time > 0 && time % 5 == 0 && precleaning) preClean();
            
            
            Clock.getInstance().simulate(1);
        }
    }
    
    //loads or swaps according to free space on ram
    public void putPageInMemory(Page page, Process process){
        int freeIndex = placementPolicy.getNext(this);
        if(freeIndex >= 0 && (scope == ReplacementScope.GLOBAL || process.getAvailablePages() > 0)){

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
    

    public int getPageFaults() {
        return pageFaults;
    }

    public int getPageHits() {
        return pageHits;
    }

    public boolean isPrecleaning() {
        return precleaning;
    }

    public void setPrecleaning(boolean precleaning) {
        this.precleaning = precleaning;
    }
    
    
    
}
