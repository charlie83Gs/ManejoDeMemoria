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
public class Process {
    private int id;
    private FetchList fetchlist;
    private Page[] pages;
    private MemoryTable memoryTable;
    private int priority;

    public Process(int id, FetchList fetchlist, Page[] pages, MemoryTable memoryTable, int priority) {
        this.id = id;
        this.fetchlist = fetchlist;
        this.pages = pages;
        this.memoryTable = memoryTable;
        this.priority = priority;
    }
    
}
