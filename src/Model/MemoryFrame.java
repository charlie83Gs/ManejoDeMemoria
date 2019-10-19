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
public class MemoryFrame {
    private Page logicalPage;
    private int realPage;
    private boolean onMemory;
    private boolean dirty;
    private int lastVisit;
    private int fetched;
    private int visits;

    public MemoryFrame(Page logicalPage, int realPage) {
        this.logicalPage = logicalPage;
        this.realPage = realPage;
    }
    
}
