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
public class NextAvailable implements PlacementPolicy {

    int pointer = 0;
    

    @Override
    public int getNext(Simulation sim) {
        MainMemory memory = sim.getMemory();
        while(memory.getAvailable() > 0){
            //if page slot is empty return it's index
            if(memory.getPage(pointer) == null) return pointer;
            
            //go to next page
            pointer = (pointer + 1);
        }
        
        return -1;
    }
    
    @Override
    public String toString() {
        return "Next available";
    }
    
}
