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
public class LocalReplacementFactory extends AbstractReplacementFactory {

    @Override
    public ReplacementPolicy getLRU(MainMemory men) {
        LocalLRU newPolicy = new LocalLRU();
        men.subscribeAcces(newPolicy);
        return newPolicy;
    }

    @Override
    public ReplacementPolicy getFIFO(MainMemory men) {
        LocalFiFO newPolicy = new LocalFiFO();
        men.subscribeSwap(newPolicy);
        return newPolicy;
        
    }

    @Override
    public ReplacementPolicy getLFU(MainMemory men) {
        LocalLFU newPolicy = new LocalLFU();
        men.subscribeAcces(newPolicy);
        return newPolicy;
    }

    @Override
    public ReplacementPolicy getMRU(MainMemory men) {
        LocalMRU newPolicy = new LocalMRU();
        men.subscribeAcces(newPolicy);
        return newPolicy;
    }

    @Override
    public ReplacementPolicy getSecondChance(MainMemory men) {
        LocalSecondChance newPolicy = new LocalSecondChance();
        men.subscribeSwap(newPolicy);
        return newPolicy;
    }

    
}
