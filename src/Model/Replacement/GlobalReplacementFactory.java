/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Replacement;

import Model.MainMemory;
import Model.Simulation;

/**
 *
 * @author Charlie
 */
public class GlobalReplacementFactory extends AbstractReplacementFactory {

    @Override
    public ReplacementPolicy getLRU(MainMemory men) {
        LRU newPolicy = new LRU();
        men.subscribeAcces(newPolicy);
        return newPolicy;    
    }

    @Override
    public ReplacementPolicy getFIFO(MainMemory men) {
        FIFO newPolicy = new FIFO();
        men.subscribeSwap(newPolicy);
        return newPolicy;
    }

    @Override
    public ReplacementPolicy getLFU(MainMemory men) {
        LFU newPolicy = new LFU();
        men.subscribeAcces(newPolicy);
        return newPolicy;
    }

    @Override
    public ReplacementPolicy getMRU(MainMemory men) {
        MRU newPolicy = new MRU();
        men.subscribeAcces(newPolicy);
        return newPolicy;
    }

    @Override
    public ReplacementPolicy getSecondChance(MainMemory men) {
        SecondChance newPolicy = new SecondChance();
        men.subscribeSwap(newPolicy);
        return newPolicy;
        
    }


    
}
