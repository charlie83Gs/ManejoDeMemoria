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
import InputOutput.FileManager;
import Model.MainMemory;
import Model.Page;
import Model.PageProfile;
import Model.PlacementPolicyType;
import Model.Replacement.ReplacementPolicy;
import Model.Replacement.ReplacementScope;
import Model.ReplacementPolicyType;
import Model.Simulation;
import processing.core.PApplet;
import processing.event.MouseEvent;
import g4p_controls.*;
import java.io.File;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManejoDeMemoria extends PApplet {
    final int PAGE_SIZE = 18;
    float disp = 20;
    float dispSpeed = 20;
    int width = 900, height = 600;
    Simulation sim;
    GDropList RepScopePicker;
    GDropList RepPolicyPicker;
    GDropList PlacementPolicyPicker;
    GSlider multiprograming;
    
    String processPath, fetchListPath;
    
    
    GButton configButt,
            processButt,
            fetchListButt,
            simulateNext;
    
    GWindow window;
    
    ArrayList<Process> processes;
    
    public static void main(String[] args){       
        PApplet.main("Main.ManejoDeMemoria");
        
        
    }


    public void settings(){
        size(width, height);
        
    }
    
    @Override
    public void setup() {
        processes = new ArrayList<>();
        
        sim = new TestSimulation().getSimulation();
        FileManager fm = new FileManager(sim.getStore());
        ArrayList<Model.Process> procesos = fm.readData("C:\\Users\\J\\Documents\\GitHub\\ManejoDeMemoria\\procesos.txt", "C:\\Users\\J\\Documents\\GitHub\\ManejoDeMemoria\\fetchlist.txt");
        for(Model.Process p: procesos){
            sim.addProcess(p);
        }
        
        
        stroke(155, 0, 0);
        
        //initialize items
       
       
        configButt = new GButton(this, this.width - 75, this.height - 50, 75, 50, "configButt");
        processButt = new GButton(this, 0, this.height - 50, 75, 50, "Process path");
        fetchListButt = new GButton(this, 75, this.height - 50, 75, 50, "Fetch list path");
        simulateNext = new GButton(this, 350, 300, 50, 100, "next");
        
    }
    
    public void handleButtonEvents(GButton button, GEvent event) {
        switch(button.getText()){
            case "configButt":
                createWindows();
                break;
            case "Process path":
                selectInput("Seleccione el archivo con la información de los procesos", "processPathSelected");
                break;
            case "Fetch list path":
                selectInput("Seleccione el archivo con la información de los procesos", "fetchPathSelected");
                break;
            case "next":
                sim = TestSimulation.TestTimeStep(sim, sim.getProcesses().size());
                break;
                
        
        }
    }
    
    public void config_draw(PApplet appc, GWinData data){
        appc.background(255);
    }
    void createWindows() 
    {
        window = GWindow.getWindow(this, "Help", 500, 50, 477, 538, JAVA2D);
        window.addDrawHandler(this, "config_draw");
        window.setActionOnClose(GWindow.CLOSE_WINDOW);
        G4P.registerSketch(window);
        
        //initialize items
        int pickerX = 10;
        int pickerY = 30;
        int pickerWidth = 150;
        int pickerHeight = 100;
        int optionsPerPage = 4;
        
        //replacement scope
        List<String> scopeStrings = Stream.of(ReplacementScope.values())
                                .map(Enum::name)
                                .collect(Collectors.toList());
        
        new GLabel(window, pickerX, pickerY,300, 50, "Replacement Scope");
        RepScopePicker = new GDropList(window, pickerX, pickerY+40, pickerWidth, pickerHeight,optionsPerPage);
        RepScopePicker.setItems(scopeStrings, 0); 
        
        //replacement policy
        List<String> repPolicyStrings = Stream.of(ReplacementPolicyType.values())
                                .map(Enum::name)
                                .collect(Collectors.toList());
        
        new GLabel(window, pickerX, pickerY + 50,300, 50, "Replacement Policy");
        RepPolicyPicker = new GDropList(window, pickerX, pickerY +90, pickerWidth, pickerHeight, optionsPerPage);
        RepPolicyPicker.setItems(repPolicyStrings, 0); 
        
        //Placement policy
        List<String> fetchPolicyStrings = Stream.of(PlacementPolicyType.values())
                                .map(Enum::name)
                                .collect(Collectors.toList());
        
        new GLabel(window, pickerX, pickerY + 100,300, 50, "Placement Policy");
        PlacementPolicyPicker = new GDropList(window, pickerX, pickerY +140, pickerWidth, pickerHeight, optionsPerPage);
        PlacementPolicyPicker.setItems(fetchPolicyStrings, 0); 
        
        new GLabel(window, pickerX, pickerY + 100,300, 50, "Degree of multiprograming");
        
        multiprograming = new GSlider(window,pickerX, pickerY + 200,300, 20, 100);
        
        
    }

    @Override
    public void draw() {
        background(255,255,255);
        
        
        displayMemoryArray(sim.getMemory().getPages(),500, 20 + (int)disp);
        displayMemoryArray(sim.getStore().getPages(),600, 20 + (int)disp);
        displayHeader(500, 5);
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
    
    public void handleDropListEvents(GDropList list, GEvent event)
    { 
        
        if (list == RepScopePicker)
        {
          println(RepScopePicker.getSelectedIndex());    
        }
    }
    
    public void processPathSelected(File selection) {
        if (selection != null){
           System.out.println("processpath: " + selection.getAbsolutePath());
           processPath = selection.getAbsolutePath();
        } 
    }
    
    public void fetchPathSelected(File selection) {
        if (selection != null){
           System.out.println("fetchpath: " + selection.getAbsolutePath());
           fetchListPath = selection.getAbsolutePath();
        } 
    }
    
    public void update_config(){
        ReplacementScope scope = ReplacementScope.values()[RepScopePicker.getSelectedIndex()];
        ReplacementPolicyType repPolicy = ReplacementPolicyType.values()[RepPolicyPicker.getSelectedIndex()];
        PlacementPolicyType placePolicy = PlacementPolicyType.values()[PlacementPolicyPicker.getSelectedIndex()];
        int degreOfMultiprog = multiprograming.getValueI();
    }
    


}