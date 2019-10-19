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
public class FetchList {
    private int[] usage;
    private int actual;

    public FetchList(int[] usage, int actual) {
        this.usage = usage;
        this.actual = actual;
    }
    
    /**
     * 
     * @return returns next fetch -1 if there are no more calls for this process
     */
    public int getNext(){
        //when all calls are used return invalid
        if(usage.length <= ++actual ) 
            return -1;
        
        return usage[actual];
    }
}
