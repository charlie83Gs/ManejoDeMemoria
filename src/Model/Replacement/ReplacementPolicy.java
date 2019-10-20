/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Replacement;

import Model.MainMemory;

/**
 *
 * @author Charlie
 */
public interface ReplacementPolicy {
    public int fetch(MainMemory men);
    
}
