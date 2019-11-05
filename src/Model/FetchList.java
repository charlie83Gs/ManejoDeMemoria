/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Random;

/**
 *
 * @author curso
 */
public class FetchList {
    private int[] usage;
    private int actual;

    public FetchList(int[] usage) {
        this.usage = usage;
        this.actual = 0;
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
    
    public boolean isFinished(){
        return usage.length-1 <= actual;
    }
    /**
     * 
     * @param size number of page calls
     * @param pages amount of process pages
     * @return 
     */
    public static FetchList CreateRandomFetchList(int size,int pages){
        //random array from 
        //https://www.tutorialspoint.com/generate-a-random-array-of-integers-in-java
        Random rd = new Random(); // creating Random object
        int[] fetches = new int[size];
        for (int i = 0; i < fetches.length; i++) {
           fetches[i] = rd.nextInt(pages); // storing random integers in an array
           //System.out.println(fetches[i]); // printing each array element
        }
        
        FetchList newlist = new FetchList(fetches);
        
        
        return newlist;
    }

    @Override
    public String toString() {
        String strUsage = "";
        for(int i: usage){
            strUsage += "," + i;
        }
        
        return "FetchList{" + "usage=" + strUsage + ", actual=" + actual + '}';
    }
    
    
}
