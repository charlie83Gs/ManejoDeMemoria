/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Charlie
 */
public class Clock {
    private static Clock instance;
    private int time;
    
    private Clock() {
        time = 0;
    }
    
    public static Clock getInstance(){
        if (instance == null){
            instance = new Clock();
        }
        return instance;
    }
    
    public void simulate(int elapsedTime){
        time += elapsedTime;
        
    }

    public int getTime() {
        return time;
    }
    
    
    
}
