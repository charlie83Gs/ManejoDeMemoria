/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author Charlie
 */
import processing.core.PApplet;

public class ManejoDeMemoria extends PApplet {
    
    public static void main(String[] args){
        TestSimulation.TestMemorySwap(10);
        PApplet.main("Main.ManejoDeMemoria");
        
       
        
        
    }
    
    public void settings(){
        size(300,300);
    }
    
    @Override
    public void setup() {
        stroke(155, 0, 0);
    }

    @Override
    public void draw() {
        line(mouseX, mouseY, width / 2, height / 2);
    }
}