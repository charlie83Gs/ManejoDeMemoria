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
public abstract class AbstractReplacementFactory {
    public abstract ReplacementPolicy getLRU(MainMemory men);
    public abstract ReplacementPolicy getFIFO(MainMemory men);
    public abstract ReplacementPolicy getLFU(MainMemory men);
    public abstract ReplacementPolicy getMRU(MainMemory men);
    public abstract ReplacementPolicy getSecondChance(MainMemory men);
}
