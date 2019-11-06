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
import Model.Process;
import InputOutput.FileManager;
import Model.MainMemory;
import Model.Page;
import Model.PageProfile;
import Model.PlacementPolicyType;
import Model.Replacement.ReplacementPolicy;
import Model.Replacement.ReplacementScope;
import Model.ReplacementPolicyType;
import Model.Simulation;
import Model.SimulationBuilder;
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
    float dispSpeed = 40;
    int width = 900, height = 600;
    int multiprogramming = 3;
    
    String processPath = "", fetchListPath = "";
    
    Simulation sim = null;
    GDropList RepScopePicker;
    GDropList RepPolicyPicker;
    GDropList PlacementPolicyPicker;
    
    GButton configButt, nextButt, resetButt, fullSimulation, add, succ;
    GTextArea processesInfo;
    GWindow window, initialConfigW;
        
    public static void main(String[] args){       
        PApplet.main("Main.ManejoDeMemoria");
    }

    public void settings(){
        size(width, height);
    }
    
    @Override
    public void setup() {
        colorMode(RGB);
        this.createInitialConfigWindow();
        
        //initialize items
        configButt = new GButton(this, 10, this.height - 40, 80, 30, "Configuration");
        nextButt = new GButton(this, 410, this.height - 40, 80, 30, "Next step");
        resetButt = new GButton(this, this.width - 90, 10, 80, 30, "Reset");
        fullSimulation = new GButton(this, this.width - 90, this.height - 50, 80, 40, "Full simulation");
        add = new GButton(this, 170, 110, 20, 20, "+");
        succ = new GButton(this, 150, 110, 20, 20, "-");
        processesInfo = new GTextArea(this, 20, 130, 450, 350);
        processesInfo.setEnabled(false);
        this.setVisibility(false);
        
        
        
    }
    
    public void setUpSim(){
        sim = new TestSimulation().getSimulation();
        sim.setProcesses(this.loadProcesses(sim.getStore()));
        sim.updateOnMemoryList(this.multiprogramming);
    }
    
    public void handleButtonEvents(GButton button, GEvent event) {
        switch(button.getText()){
            case "+":
                this.handleDegree(1);
                break;
            case "-":
                this.handleDegree(-1);
                break;
            case "Configuration":
                createWindows();
                break;
            case "Process path":
                selectInput("Seleccione el archivo con la información de los procesos", "processPathSelected");
                break;
            case "Requests path":
                selectInput("Seleccione el archivo con la información de los procesos", "fetchPathSelected");
                break;
            case "Next step":
                sim = TestSimulation.TestTimeStep(sim, sim.getOnMemory().size());
                if(this.sim.cleanOnMemoryList()){
                    this.sim.updateOnMemoryList(this.multiprogramming);
                }
                break;
            case "Full simulation":
                sim = TestSimulation.TestFullSteps(sim, sim.getOnMemory().size());
                break;
            case "Guardar":
                this.setConfig();
                this.window.close();
                break;
            case "Cancelar":
                this.window.close();
                break;
            case "Reset":
                this.disp = 20;
                this.setUpSim();
                break;
            case "Ok":
                this.setVisibility(true);
                this.setUpSim();
                this.initialConfigW.forceClose();
                break;
        }
    }
    
    public void setVisibility(boolean visibility){
        this.configButt.setVisible(visibility);
        this.nextButt.setVisible(visibility);
        this.resetButt.setVisible(visibility);
        this.fullSimulation.setVisible(visibility);
        this.add.setVisible(visibility);
        this.succ.setVisible(visibility);
        this.processesInfo.setVisible(visibility);
    }
    
    @Override
    public void draw() {
        background(255);
        
        
        if(sim != null){
            displayMemoryArray(sim.getMemory().getPages(),500, 20 + (int)disp);
            displayMemoryArray(sim.getStore().getPages(),600, 20 + (int)disp);
            displayHeader(500, 5);
            text("Simlation cicle: " + Clock.getInstance().getTime(), 20, 20);
            text("Placement policy: " + sim.getPlacementPolicy().toString(), 20, 40);
            text("Replacement scope: " + sim.getScope().toString(), 20, 60);
            text("Replacement policy: " + sim.getReplacementPolicy().toString(), 20, 80);
            text("Degree of multiprogramming: " + this.multiprogramming, 20, 100);
            
            this.processesInfo.setText("All processes:" + "\n" +  
                                            this.processListToString(sim.getProcesses()) + "\n \n" + 
                                        "Processes on memory:" + "\n" + 
                                            this.processListToString(sim.getOnMemory()) + "\n \n" + 
                                        "Finished processes:" + "\n" +
                                            this.processListToString(sim.getFinished()));

        }
    }

    public ArrayList<Model.Process> loadProcesses(BackingStore bk){
        FileManager fm = new FileManager(bk);
        ArrayList<Model.Process> procesos = null;
        
        if(this.fetchListPath.equals("") || this.processPath.equals("")){
            System.out.println("SE HAN CARGADO LOS ARCHIVOS DESDE EL PATH POR DEFECTO");
            procesos = fm.readData("procesos.txt", "fetchlist.txt");
        }
        else{
            System.out.println("se han cargado los archivos desde el path selecionado");
            procesos = fm.readData(this.processPath, this.fetchListPath);
        }
        
        return procesos;
    }
    
    public void handleDegree(int val){
        if(this.sim != null){
            if(this.multiprogramming > 1 && val < 0){
                this.multiprogramming = this.multiprogramming + val;
            }
            else if(this.multiprogramming < 20 && val > 0){
                this.multiprogramming = this.multiprogramming + val;
            }
            this.sim.updateOnMemoryList(this.multiprogramming);
        }
    }
    
    public String processListToString(ArrayList<Process> toDraw){
        String result = "";
        for(int i = 0; i < toDraw.size(); i++){
            result += "     " + toDraw.get(i).toStringGrafico() + "\n";
        }
        return result;
    }
    
    public void setConfig(){
        
        SimulationBuilder simBuilder =new SimulationBuilder();
        sim.setScope(ReplacementScope.valueOf(RepScopePicker.getSelectedText()));
        sim.setPlacementPolicy(simBuilder.getPlacementPolicy(PlacementPolicyType.valueOf(PlacementPolicyPicker.getSelectedText())));
        simBuilder.setReplacementPolicy(ReplacementPolicyType.valueOf(RepPolicyPicker.getSelectedText()));
        sim.setReplacementPolicy(simBuilder.getReplacementPolicy(sim.getMemory()));
        
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
        disp -= event.getCount() * dispSpeed;
    }
    
    public void displayMemoryArray(Page[] pages,int x, int y){
        Page currentPage;
        for (int i = 0; i < pages.length; i++){
            currentPage = pages[i];
            if(currentPage != null && currentPage.getOwner() != null){
                fill(pages[i].getOwner().getR(),
                        pages[i].getOwner().getG(),
                        pages[i].getOwner().getB());
            }
            else{
                fill(180);
            }
            stroke(0);
            rect(x, y + i *PAGE_SIZE - PAGE_SIZE/2, PAGE_SIZE*4,PAGE_SIZE);
            fill(0);
            String currentText = "empty";
            if(currentPage!= null) currentText = currentPage.toString();
            text(i + "->" + currentText, x, 3 + y + i *PAGE_SIZE);
            
        }
    }
    
    public void createInitialConfigWindow() {
        this.initialConfigW = GWindow.getWindow(this, "Configuración inicial", 500, 50, 477, 538, JAVA2D);
        this.initialConfigW.addDrawHandler(this, "config_draw");
        this.initialConfigW.setActionOnClose(GWindow.KEEP_OPEN);        
        G4P.registerSketch(this.initialConfigW);
        
        new GButton(this.initialConfigW, this.initialConfigW.width - 90, this.initialConfigW.height - 40, 80, 30, "Ok");
        new GButton(this.initialConfigW, 0, this.initialConfigW.height - 40, 80, 30, "Process path");
        new GButton(this.initialConfigW, 90, this.initialConfigW.height - 40, 80, 30, "Requests path");        
    }
    
    public void createWindows() {
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
        
        
        
        new GButton(window, window.width - 180, window.height - 40, 80, 30, "Guardar");
        new GButton(window, window.width - 90, window.height - 40, 80, 30, "Cancelar");
        
    }
    
    public void handleDropListEvents(GDropList list, GEvent event){  
        if (list == RepScopePicker){
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
    }
    
    public void config_draw(PApplet appc, GWinData data){
        appc.background(255);
    }
    
}