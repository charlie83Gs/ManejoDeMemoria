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
import java.util.Arrays;
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
    boolean loop = false;
    int bgColor = 230;
    
    String processPath = "procesos.txt", fetchListPath = "fetchlist.txt";
    
    Simulation sim = null;
    GDropList fetchPolicyPicker,
            PlacementPolicyPicker,
            RepPolicyPicker,
            ResidentSetPicker,
            RepScopePicker,
            CleaningPolicyPicker,
            LoadControlPicker;
    
    GButton configButt, nextButt, resetButt, fullSimulation, cancelarBtn;
    GTextArea processesInfo;
    GWindow window, initialConfigW;
    GLabel multiText, procPathText, fetchPathText;
    GTextField memFisicaText, memVirtualText, pagSizeText, processIdText;
        
    public static void main(String[] args){       
        PApplet.main("Main.ManejoDeMemoria");
    }

    public void settings(){
        size(width, height);
    }
    
    @Override
    public void setup() {
        colorMode(RGB);
        this.createWindows();
        this.cancelarBtn.setVisible(false);
        //initialize items
        configButt = new GButton(this, 10, this.height - 40, 80, 30, "Configuration");
        nextButt = new GButton(this, 410, this.height - 40, 80, 30, "Next step");
        resetButt = new GButton(this, this.width - 90, 10, 80, 30, "Reset");
        fullSimulation = new GButton(this, this.width - 90, this.height - 50, 80, 40, "Full simulation");
        processIdText = new GTextField(this, 410, this.height - 65, 80, 20);
        processesInfo = new GTextArea(this, 20, 130, 450, 350);
        processesInfo.setEnabled(false);
        
        this.setVisibility(false);
       
        
        
    }
    
    public void setUpSim(){
        Clock.getInstance().resetTime();
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
                this.loop = true;
                createWindows();
                this.setVisibility(false);
                break;
            case "Process path":
                selectInput("Seleccione el archivo con la información de los procesos", "processPathSelected");
                break;
            case "Requests path":
                selectInput("Seleccione el archivo con la información de los procesos", "fetchPathSelected");
                break;
            case "Next step":
                if(this.isNumeric(this.processIdText.getText()) && sim.isInMemory(Integer.valueOf(this.processIdText.getText()))){
                    sim = TestSimulation.TestTimeStep(sim, Integer.valueOf(this.processIdText.getText()));
                    if(this.sim.cleanOnMemoryList()){
                        this.sim.updateOnMemoryList(this.multiprogramming);
                    }
                }
                else{
                    this.showPopUpErr("El proceso a ejecutar no está cargado en memoria");
                }
                break;
            case "Full simulation":
                sim = TestSimulation.TestFullSteps(sim, sim.getOnMemory().size());
                break;
            case "Guardar":
                if(false/*this.processPath.equals("") || this.fetchListPath.equals("")*/){
                    this.showPopUpErr("Debe escoger un path para cargar los procesos y la lista de requests");
                    return;
                }
                if(true/*this.isNumeric(this.memFisicaText.getText()) 
                            && this.isNumeric(this.memVirtualText.getText()) 
                            && this.isNumeric(this.pagSizeText.getText())*/){  
                    
                    this.setVisibility(true);
                    this.setUpSim();
                    this.setConfig();
                    this.window.forceClose();
                    this.loop = true;
                }
                else{
                    this.showPopUpErr("Los campos de memoria y paginas deben ser numéricos");
                }
                break;
            case "Cancelar":
                this.loop = true;
                this.setVisibility(true);
                this.window.forceClose();
                break;
            case "Reset":
                
                this.disp = 20;
                this.setUpSim();
                break;
        }
    }
    
    public void setVisibility(boolean visibility){
        this.configButt.setVisible(visibility);
        this.nextButt.setVisible(visibility);
        this.resetButt.setVisible(visibility);
        this.fullSimulation.setVisible(visibility);
        this.processesInfo.setVisible(visibility);
        this.processIdText.setVisible(visibility);
    }
    
    @Override
    public void draw() {
        background(bgColor);
        multiText.setText("Degree of multiprogramming: " + this.multiprogramming);
        
        if(loop){
            displayMemoryArray(sim.getMemory().getPages(),500, 20 + (int)disp);
            displayMemoryArray(sim.getStore().getPages(),600, 20 + (int)disp);
            displayHeader(500, 5);
            text("Simlation cicle: " + Clock.getInstance().getTime(), 20, 20);
            text("Placement policy: " + sim.getPlacementPolicy().toString(), 20, 40);
            text("Replacement scope: " + sim.getScope().toString(), 20, 60);
            text("Replacement policy: " + sim.getReplacementPolicy().toString(), 20, 80);
            text("Degree of multiprogramming: " + this.multiprogramming, 20, 100);
            text("Process id to execute", 380, this.height - 85, 150, 25);
            
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
            procesos = fm.readData("procesos.txt", "fetchlist.txt");
        }
        else{
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
        fill(bgColor);
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
    
    public void showPopUpErr(String message) {
        GWindow errWindow = GWindow.getWindow(this, "Error", 750, 400, 200, 200, JAVA2D);
        errWindow.addDrawHandler(this, "config_draw");
        errWindow.setActionOnClose(GWindow.CLOSE);
    
        new GLabel(errWindow, 0, 0, 200, 200, message);
    }
    
    
    public void createWindows() {
        window = GWindow.getWindow(this, "Confirugation", 750, 200, 450, 550, JAVA2D);
        window.addDrawHandler(this, "config_draw");
        window.setActionOnClose(GWindow.KEEP_OPEN);
        
        G4P.registerSketch(window);
        
        //initialize items
        int pickerX = 10;
        int pickerY = 50;
        int pickerWidth = 150;
        int pickerHeight = 100;
        int optionsPerPage = 4;
        
        //FetchPolicy
        List<String> fetchPolicyStrings = Arrays.asList("Demand", "Prepaging");
        new GLabel(window, pickerX, pickerY - 35,300, 50, "FetchPolicy");
        this.fetchPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.fetchPolicyPicker.setItems(fetchPolicyStrings, 0);
        
        new GLabel(window, window.width - (pickerX + 220), pickerY, 150, 20, "Physical memory size:");
        this.memFisicaText = new GTextField(window, window.width - (pickerX + 100), pickerY, 100, 20);
        
        pickerY += 50;
        
        //Placement policy
        List<String> placementPolicyStrings = Stream.of(PlacementPolicyType.values()).map(Enum::name).collect(Collectors.toList());
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Placement Policy");
        this.PlacementPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight, optionsPerPage);
        this.PlacementPolicyPicker.setItems(placementPolicyStrings, 0); 
        
        new GLabel(window, window.width - (pickerX + 210), pickerY, 150, 20, "Virtual memory size:");
        this.memVirtualText = new GTextField(window, window.width - (pickerX + 100), pickerY, 100, 20);
        
        pickerY += 50;
        
        //replacement policy
        List<String> repPolicyStrings = Stream.of(ReplacementPolicyType.values()).map(Enum::name).collect(Collectors.toList());
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Replacement Policy");
        this.RepPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight, optionsPerPage);
        this.RepPolicyPicker.setItems(repPolicyStrings, 0); 
        
        new GLabel(window, window.width - (pickerX + 160), pickerY, 150, 20, "Page size:");
        this.pagSizeText = new GTextField(window, window.width - (pickerX + 100), pickerY, 100, 20);
        
        pickerY += 50;
        
        //residentSet
        List<String> residentSetStrings = Arrays.asList("Fixed", "Variable");
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Resident set management");
        this.ResidentSetPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.ResidentSetPicker.setItems(residentSetStrings, 0);
        
        pickerY += 50;
        
        //replacement scope
        List<String> scopeStrings = Stream.of(ReplacementScope.values()).map(Enum::name).collect(Collectors.toList());
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Replacement Scope");
        this.RepScopePicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.RepScopePicker.setItems(scopeStrings, 0);
        
        pickerY += 50;
        
        //Cleaning policy
        List<String> cleaningStrings = Arrays.asList("Demand", "Pre-cleaning");
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Cleaning policy");
        this.CleaningPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.CleaningPolicyPicker.setItems(cleaningStrings, 0);
        
        pickerY += 50;
        
        this.multiText = new GLabel(window, pickerX, pickerY - 35, 300, 50, "Degree of multiprogramming: " + this.multiprogramming);
        new GButton(window, 150, pickerY, 20, 20, "+");
        new GButton(window, 130, pickerY, 20, 20, "-");
        
        pickerY += 45;
        
        this.procPathText = new GLabel(window, pickerX, pickerY - 35, 500, 50, "Path de procesos: " + this.processPath);
        pickerY += 30;
        this.fetchPathText = new GLabel(window, pickerX, pickerY - 35, 500, 50, "Path de requests: " + this.fetchListPath);
        
        new GButton(window, window.width - 180, window.height - 40, 80, 30, "Guardar");
        this.cancelarBtn = new GButton(window, window.width - 90, window.height - 40, 80, 30, "Cancelar");
        new GButton(window, 0, window.height - 40, 80, 30, "Process path");
        new GButton(window, 90, window.height - 40, 80, 30, "Requests path");
        
        
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
           this.procPathText.setText("Path de procesos: " + this.processPath);
        } 
    }
    
    public void fetchPathSelected(File selection) {
        if (selection != null){
           System.out.println("fetchpath: " + selection.getAbsolutePath());
           fetchListPath = selection.getAbsolutePath();
           this.fetchPathText.setText("Path de requests: " + this.fetchListPath);
        } 
    }
    
    public void update_config(){
        ReplacementScope scope = ReplacementScope.values()[RepScopePicker.getSelectedIndex()];
        ReplacementPolicyType repPolicy = ReplacementPolicyType.values()[RepPolicyPicker.getSelectedIndex()];
        PlacementPolicyType placePolicy = PlacementPolicyType.values()[PlacementPolicyPicker.getSelectedIndex()];
    }
    
    public void config_draw(PApplet appc, GWinData data){
        appc.background(bgColor);
    }
    
    public boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
    
    public void handleTextEvents(GEditableTextControl textcontrol, GEvent event) { /* code */ }
}