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
import Model.MainMemory;
import Model.Page;
import Model.PageProfile;
import Model.Simulation;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class ManejoDeMemoria extends PApplet {
    final int PAGE_SIZE = 18;
    float disp = 0;
    float dispSpeed = 20;
    Simulation sim;
    
    
    public static void main(String[] args){       
        PApplet.main("Main.ManejoDeMemoria");
        
    }
    
    public void settings(){
        size(600,600);
    }
    
    @Override
    public void setup() {
        sim = TestSimulation.TestTimeStep(10);
        stroke(155, 0, 0);
    }

    @Override
    public void draw() {
        background(255,255,255);
        
        
        displayMemoryArray(sim.getMemory().getPages(),300, 20 + (int)disp);
        displayMemoryArray(sim.getStore().getPages(),400, 20 + (int)disp);
        displayHeader(300, 5);
        text("Simlation cicle: " + Clock.getInstance().getTime(), 20, 20);
    }
    
    public void displayHeader(int x, int y){
        noStroke();
        fill(255);
        rect(x, y - 10 , 400, 30);
        fill(0);
        text("Memory                Backing Store",x,y + 10);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event); //To change body of generated methods, choose Tools | Templates.
        disp += event.getCount() * dispSpeed;
    }
    
    
    
    public void displayMemoryArray(Page[] pages,int x, int y){
        Page currentPage;
        for (int i = 0; i < pages.length; i++) {
            currentPage = pages[i];
            fill(180);
            stroke(0);
            rect(x, y + i *PAGE_SIZE - PAGE_SIZE/2, PAGE_SIZE*4,PAGE_SIZE);
            fill(0);
            String currentText = "empty";
            if(currentPage!= null) currentText = currentPage.toString();
            text(i + "->" + currentText, x, 3 + y + i *PAGE_SIZE);
            
        }
        
           
        
        
    }

}