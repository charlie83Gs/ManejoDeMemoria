/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.BackingStore;
import Model.FetchList;
import InputOutput.FileManager;
import Model.MemorySwaper;
import Model.Page;
import Model.PageProfile;
import Model.PlacementPolicyType;
import Model.Process;
import Model.Replacement.ReplacementScope;
import Model.ReplacementPolicyType;
import Model.Simulation;
import Model.SimulationBuilder;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONObject;

/**
 *
 * @author Charlie
 */
public class TestSimulation {
    private static int PAGES = 20;
    static int SIM = 1500;
    
    public Simulation getSimulation(){
        PageProfile profile = new PageProfile(64);
        
        SimulationBuilder simBuilder = new SimulationBuilder();
        simBuilder.setProfile(profile);
        simBuilder.setMemory(64000);
        simBuilder.setStore(128000);
        simBuilder.setPlacementPolicy(PlacementPolicyType.NEXT_AVAILLABLE);
        simBuilder.setReplacementPolicy(ReplacementPolicyType.SECOND_CHANCE);
        simBuilder.setReplacementScope(ReplacementScope.LOCAL);
        SIM = 1500;
        
        return simBuilder.getResult();
    }
    
    
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
            process[i] = new Process(i,FetchList.CreateRandomFetchList(2000, 20),PAGES,2,sim.getStore(),10);
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
    
    /*
    public static Simulation TestTimeStep(int processesAmount){
        Process[] process = new Process[processesAmount];
        PageProfile profile = new PageProfile(64);
        
        SimulationBuilder simBuilder =new SimulationBuilder();
        simBuilder.setProfile(profile);
        simBuilder.setMemory(64000);
        simBuilder.setStore(128000);
        simBuilder.setPlacementPolicy(PlacementPolicyType.NEXT_AVAILLABLE);
        simBuilder.setReplacementPolicy(ReplacementPolicyType.SECOND_CHANCE);
        simBuilder.setReplacementScope(ReplacementScope.LOCAL);

        Simulation sim = simBuilder.getResult();
        
        FileManager fm = new FileManager(sim.getStore());
        ArrayList<Process> procesos = fm.readData("C:\\Users\\J\\Documents\\GitHub\\ManejoDeMemoria\\procesos.txt", "C:\\Users\\J\\Documents\\GitHub\\ManejoDeMemoria\\fetchlist.txt");
        for(Process p: procesos){
            sim.addProcess(p);
        }
        
        for (int i=0; i<process.length; i++) 
        { 
            process[i] = new Process(i,FetchList.CreateRandomFetchList(3000, PAGES),PAGES,2,sim.getStore(),50);
           
            sim.addProcess(process[i]);
        }
        
        

        
        //simulate SIM times steps swaps
        Random r=new Random();
        while(SIM-- > 0){
            sim.simulate(r.nextInt(processesAmount));
        }   

        
        
        System.out.println("Hits: " + sim.getPageHits());
        System.out.println("Faults: " + sim.getPageFaults());
        System.out.println("Finished step test ");
        
        return sim;
    }
    */
    
    public static Simulation TestTimeStep(Simulation sim, int proccessId){
        
        //simulate SIM times steps swaps
        if(SIM > 0){
            for(Process p: sim.getOnMemory()){
                if(p.getId() == proccessId){
                    sim.simulate(p);
                    SIM--;//si no encuentra el proceso estaría restando al SIM y no debería porque no se ejecutó nada en ese ciclo
                }
            }
        }   
        
        System.out.println("Hits: " + sim.getPageHits());
        System.out.println("Faults: " + sim.getPageFaults());
        System.out.println("Finished step test ");
        
        return sim;
    }
    
    public static Simulation TestFullSteps(Simulation sim, int multiprogramming){
        
        //simulate SIM times steps swaps
        Random r = new Random();
        while(SIM-- > 0 && multiprogramming > 0){
            sim.simulate(r.nextInt(multiprogramming));
            if(sim.cleanOnMemoryList()){
                sim.updateOnMemoryList(multiprogramming);
            }
        }   
        
        System.out.println("Hits: " + sim.getPageHits());
        System.out.println("Faults: " + sim.getPageFaults());
        System.out.println("Finished step test ");
        
        return sim;
    }
}
