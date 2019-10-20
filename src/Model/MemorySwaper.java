/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Charlie
 */
public class MemorySwaper {
    public static void SwapIn(Swapable swapable,int memoryPage, int pyshicalPage){
        MainMemory memory = swapable.getMemory();
        BackingStore store = swapable.getStore();
        
        //get loaded page
        Page newPage = store.getPage(pyshicalPage);
        
        //save back page into storage
        Page oldPage = memory.getPage(memoryPage);
        //System.out.println(oldPage);
        if(oldPage != null){
            store.setPage(oldPage.getPhysicalPosition(), oldPage);

            //update page status on owner process
            oldPage.getOwner().pageStore(oldPage);
        }
        //udate process table on owner process
        newPage.getOwner().pageLoad(memoryPage, newPage);
        //swap in page into main memory
        memory.setPage(memoryPage, newPage);
       
        
        
        

    }
    
}
