/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.BackingStore;
import Model.FetchList;
import Model.MemorySwaper;
import Model.Page;
import Model.PageProfile;
import Model.PlacementPolicyType;
import Model.Process;
import Model.Simulation;
import Model.SimulationBuilder;
import java.util.Random;

/**
 *
 * @author Charlie
 */
public class TestSimulation {
    private static int PAGES = 10;
    public static void TestMemorySwap(int processesAmount){
        Process[] process = new Process[processesAmount];
        int SIM = 4000;
        PageProfile profile = new PageProfile(64);
        
        SimulationBuilder simBuilder =new SimulationBuilder();
        simBuilder.setProfile(profile);
        simBuilder.setMemory(64000);
        simBuilder.setStore(128000);

        Simulation sim = simBuilder.getResult();
        
        for (int i=0; i<process.length; i++) 
        { 
            process[i] = new Process(i,FetchList.CreateRandomFetchList(2000, 20),PAGES,2,sim.getStore());
        }
            
        //simulate 4000 memory swaps
        Random r=new Random();
        while(SIM-- > 0){
            Process nextProcess = process[r.nextInt(process.length)];
            Page loaded = nextProcess.getPage(r.nextInt(PAGES));
            
            int memoryPosition = r.nextInt(sim.getMemory().getPageCount());
            MemorySwaper.SwapIn(sim,memoryPosition ,loaded.getPhysicalPosition());
            //System.out.println("Swaping " + memoryPosition + "  --  " + loaded.getPhysicalPosition());
        }
        
        System.out.println("Finished swap test ");
    }
    
    public static void TestTimeStep(int processesAmount){
        Process[] process = new Process[processesAmount];
        int SIM = 15000;
        PageProfile profile = new PageProfile(64);
        
        SimulationBuilder simBuilder =new SimulationBuilder();
        simBuilder.setProfile(profile);
        simBuilder.setMemory(64000);
        simBuilder.setStore(128000);
        simBuilder.setPlacementPolicy(PlacementPolicyType.NEXT_AVAILLABLE);

        Simulation sim = simBuilder.getResult();
        
        for (int i=0; i<process.length; i++) 
        { 
            process[i] = new Process(i,FetchList.CreateRandomFetchList(2000, PAGES),PAGES,2,sim.getStore());
            sim.addProcess(process[i]);
        }
        
        
        //simulate SIM times steps swaps
        Random r=new Random();
        while(SIM-- > 0){
            sim.simulate(r.nextInt(processesAmount));
        }   

        
        
        
        System.out.println("Finished step test ");
    }
    
    
}