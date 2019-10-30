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
import Model.BackingStore;
import Model.Clock;
import Model.FileManager;
import Model.PageProfile;
import processing.core.PApplet;

public class ManejoDeMemoria extends PApplet {
    
    public static void main(String[] args){
        
        TestSimulation.TestTimeStep(10);
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
        
        text("Simlation cicle: " + Clock.getInstance().getTime(), 20, 20);
    }
}