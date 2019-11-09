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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//import sun.java2d.cmm.Profile;

public class ManejoDeMemoria extends PApplet {
    final int PAGE_SIZE = 18;
    float disp = 20;
    float dispSpeed = 40;
    int width = 910, height = 600;
    int multiprogramming = 3, residentSet = 3;
    boolean loop = false;
    int bgColor = 230;
    
    String processPath = "procesos.txt",
            fetchListPath = "fetchlist.txt";
    
    Simulation sim = null;
    GDropList fetchPolicyPicker,
            PlacementPolicyPicker,
            RepPolicyPicker,
            ResidentSetPicker,
            RepScopePicker,
            CleaningPolicyPicker,
            LoadControlPicker;
    
    GButton configButt,
            nextButt,
            resetButt,
            fullSimulation,
            cancelarBtn;
    
    GButton helpProcessesInfo,
            helpConfig,
            helpIdProcess,
            helpIterations,
            helpNextSetp,
            helpReset,
            helpFullSimulation,
            helpStadisticsInfo;
    
    GTextArea processesInfo,
              estadisticInfo;
    
    GWindow window, initialConfigW;
    GLabel multiText, residentSetText, procPathText, fetchPathText;

    GTextField memFisicaText,
            memVirtualText,
            pagSizeText,
            processIdText,
            iterationsText,
            residentSetTextInput;
        
    GWindow errWindow, manualWindow;
    GTextArea errWindowLbl, infoManual;
    
    SimulationBuilder builder;
    
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
        configButt = new GButton(this, 10, this.height - 40, 85, 30, "Configuración");
        nextButt = new GButton(this, 390, this.height - 40, 80, 30, "Ejecutar");
        resetButt = new GButton(this, this.width - 110, 10, 80, 30, "Resetear");
        fullSimulation = new GButton(this, this.width - 110, this.height - 50, 80, 40, "Full simulation");
        processIdText = new GTextField(this, 240, this.height - 65, 80, 20);
        iterationsText = new GTextField(this, 365, this.height - 65, 80, 20);
        iterationsText.setText("1");
        processesInfo = new GTextArea(this, 20, 130, 450, 350);
        processesInfo.setEnabled(false);
        estadisticInfo = new GTextArea(this, 680, 130, 200, 350);
        estadisticInfo.setEnabled(false);
        
        //help buttons
        this.helpProcessesInfo = new GButton(this, 470, 135, 30, 20, "11?");
        this.helpConfig = new GButton(this, 95, this.height - 40, 30, 20, "12?");
        this.helpIdProcess = new GButton(this, 325, this.height - 65, 30, 20, "13?"); 
        this.helpIterations = new GButton(this, 445, this.height - 65, 30, 20, "14?"); 
        this.helpNextSetp = new GButton(this, 470, this.height - 30, 30, 20, "15?");
        this.helpReset = new GButton(this, this.width - 30, 10, 30, 20, "16?");
        this.helpStadisticsInfo = new GButton(this, 875, 135, 30, 20, "17?"); 
        this.helpFullSimulation = new GButton(this, this.width - 30, this.height - 50, 30, 20, "18?");
        this.setVisibility(false);
       
        errWindow = GWindow.getWindow(this, "", 750, 400, 500, 500, JAVA2D);
        errWindow.setActionOnClose(GWindow.KEEP_OPEN);
        errWindow.setVisible(false);
        new GButton(this.errWindow, this.errWindow.width - 45, this.errWindow.height - 30, 40, 25, "Ok");
    
        errWindowLbl = new GTextArea(errWindow, 0, 0, 500, 500);
        errWindowLbl.setEnabled(false);
        
        manualWindow = GWindow.getWindow(this, "Manual de usuario", 600, 100, 700, 800, JAVA2D);
        manualWindow.setActionOnClose(GWindow.KEEP_OPEN);
        manualWindow.setVisible(false);
        new GButton(this.manualWindow, this.manualWindow.width - 45, this.manualWindow.height - 30, 40, 25, "Ok");
    
        infoManual = new GTextArea(manualWindow, 0, 0, 700, 800);
        infoManual.setEnabled(false);
        
        
        
    }
    //the new sim is created here
    public void setUpSim(){
        Clock.getInstance().resetTime();
        
        
        
        //sim = new TestSimulation().getSimulation();
        sim = builder.getResult();
        
        //update resident size for each process
        ArrayList<Process> newProcesses = this.loadProcesses(sim.getStore());
        //!ATENTION 
        //must use resident size instead of 3
        if(this.residentSetTextInput.getText() != ""){
            this.residentSet = Integer.valueOf(this.residentSetTextInput.getText());
        }
        for (Process newProcess : newProcesses) {
            newProcess.setTotalPages(residentSet);
        }
        
        sim.setProcesses(newProcesses);
        
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
            case "Configuración":
                this.loop = true;
                createWindows();
                this.setVisibility(false);
                break;
            case "Cargar procesos":
                selectInput("Seleccione el archivo con la información de los procesos", "processPathSelected");
                break;
            case "Cargar requests":
                selectInput("Seleccione el archivo con la información de los procesos", "fetchPathSelected");
                break;
            case "Ejecutar":
                if(this.isNumeric(this.processIdText.getText()) 
                        && this.isNumeric(this.iterationsText.getText())
                        && sim.isInMemory(Integer.valueOf(this.processIdText.getText()))){
                    int iterations = Integer.valueOf(this.iterationsText.getText());
                    for(int i = 0; i < iterations; i++){
                        sim = TestSimulation.TestTimeStep(sim, Integer.valueOf(this.processIdText.getText()));
                        if(this.sim.cleanOnMemoryList()){
                            this.sim.updateOnMemoryList(this.multiprogramming);
                        }
                    }
                    this.estadisticInfo.setText("Page-faults: " + this.sim.getPageFaults() + "\n"
                                                + "Hits: " + this.sim.getPageHits());
                    
                }
                else{
                    this.showPopUpErr("Error", "El proceso a ejecutar no está cargado en memoria o el numero de iteraciones no > 0");
                }
                break;
            case "Full simulation":
                sim = TestSimulation.TestFullSteps(sim, sim.getOnMemory().size());
                this.estadisticInfo.setText("Page-faults: " + this.sim.getPageFaults() + "\n"
                                                + "Hits: " + this.sim.getPageHits());
                break;
            case "Guardar":
                if(this.processPath.equals("") || this.fetchListPath.equals("")){
                    this.showPopUpErr("Error", "Debe escoger un path para cargar los procesos y la lista de requests");
                    return;
                }
                if(this.isNumeric(this.memFisicaText.getText()) 
                            && this.isNumeric(this.memVirtualText.getText()) 
                            && this.isNumeric(this.pagSizeText.getText())){  
                    if(this.RepScopePicker.getSelectedText().equals("LOCAL") && !this.isNumeric(this.residentSetTextInput.getText())){
                        this.showPopUpErr("Error", "Cuando el replacement scope es local hay que indicar una cantidad al resident set");
                        return;
                    }
                    
                    this.setVisibility(true);
                    this.setConfig();
                    this.setUpSim();
                    
                    this.window.forceClose();
                    this.loop = true;
                }
                else{
                    this.showPopUpErr("Error", "Los campos de memoria y paginas deben ser numéricos");
                }
                break;
            case "Cancelar":
                this.loop = true;
                this.setVisibility(true);
                this.window.forceClose();
                break;
            case "Resetear":
                this.disp = 20;
                this.estadisticInfo.setText("");
                this.setUpSim();
                break;
            case "Manual de usuario":
                this.manualWindow.setVisible(true);
                break;
            case "Ok":
                this.errWindow.setVisible(false);
                this.manualWindow.setVisible(false);
                break;
            case "1?":
                this.showPopUpErr("Info", this.HfetchPolicy);//fetch policy picker
                break;
            case "2?":
                this.showPopUpErr("Info", this.HplacementPolicy);//placement policy
                break;
            case "3?":
                this.showPopUpErr("Info", this.HreplacementPolicy);//replacement policy
                break;
            case "4?":
                this.showPopUpErr("Info", this.HresidentSet);//resident set
                break;
            case "5?":
                this.showPopUpErr("Info", this.HreplacementScope);//replacement scope
                break;
            case "6?":
                this.showPopUpErr("Info", this.Hcleaningpolicy);//cleaning policy
                break;
            case "7?":
                this.showPopUpErr("Info", this.Hmultiprogramming);//multi programming
                break;
            case "8?":
                this.showPopUpErr("Info", this.HPhysicalMemSize);//physical memory size
                break;
            case "9?":
                this.showPopUpErr("Info", this.HVirtualMemSize);//virtual memory size
                break;
            case "10?":
                this.showPopUpErr("Info", this.HPageSize);//page size
                break;
            case "11?":
                this.showPopUpErr("Info", this.HProcessesInfo);//processes info
                break;
            case "12?":
                this.showPopUpErr("Info", this.HConfig);//config butt
                break;
            case "13?":
                this.showPopUpErr("Info", this.HidToExecute);//process id to execute
                break;
            case "14?":
                this.showPopUpErr("Info", this.Hiterations);//iterations to simulate
                break;
            case "15?":
                this.showPopUpErr("Info", this.HExecute);//execute button
                break;
            case "16?":
                this.showPopUpErr("Info", this.HReset);//reset button
                break;
            case "17?":
                this.showPopUpErr("Info", this.HStadisticsInfo);//stadistics info
                break;
            case "18?":
                this.showPopUpErr("Info", this.HFullSimulation);//full simulation
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
        this.iterationsText.setVisible(visibility);
        this.estadisticInfo.setVisible(visibility);
        
        this.helpConfig.setVisible(visibility);
        this.helpFullSimulation.setVisible(visibility);
        this.helpIdProcess.setVisible(visibility);
        this.helpIterations.setVisible(visibility);
        this.helpNextSetp.setVisible(visibility);
        this.helpProcessesInfo.setVisible(visibility);
        this.helpReset.setVisible(visibility);
        this.helpStadisticsInfo.setVisible(visibility);
    }
    
    @Override
    public void draw() {
        background(bgColor);
        this.multiText.setText("Degree of multiprogramming: " + this.multiprogramming);
        
        if(this.RepScopePicker.getSelectedText().equals("GLOBAL")){
            this.residentSetText.setText("Resident set: fixed");
            this.residentSetTextInput.setVisible(false);
        }
        else{
            this.residentSetText.setText("Resident set: variable");
            this.residentSetTextInput.setVisible(true);
        }
        
        //System.out.println(this.window.mouseX + ", " + this.window.mouseY);
       
        if(loop){
            displayMemoryArray(sim.getMemory().getPages(),500, 20 + (int)disp);
            displayMemoryArray(sim.getStore().getPages(),600, 20 + (int)disp);
            displayHeader(500, 5);
            text("Simlation cicle: " + Clock.getInstance().getTime(), 20, 20);
            text("Placement policy: " + sim.getPlacementPolicy().toString(), 20, 40);
            text("Replacement scope: " + sim.getScope().toString(), 20, 60);
            text("Replacement policy: " + sim.getReplacementPolicy().toString(), 20, 80);
            text("Degree of multiprogramming: " + this.multiprogramming, 20, 100);
            text("ProcesoId a ejecutar", 240, this.height - 85, 150, 25);
            text("Iteraciones a simular", 365, this.height - 85, 150, 25);
            
            
            text("Información de procesos:", 20, 110, 150, 25);
            text("Información de estadisticas:", 680, 110, 150, 25);
            
            this.processesInfo.setText("Procesos:" + "\n" +  
                                            this.processListToString(sim.getProcesses()) + "\n \n" + 
                                        "Procesos en memoria:" + "\n" + 
                                            this.processListToString(sim.getOnMemory()) + "\n \n" + 
                                        "Procesos finalizados:" + "\n" +
                                            this.processListToString(sim.getFinished()));

        }
    }

    public ArrayList<Model.Process> loadProcesses(BackingStore bk){
        FileManager fm = new FileManager(bk);
        ArrayList<Model.Process> procesos = null;
        
        if(this.fetchListPath.equals("") || this.processPath.equals("")){
            procesos = fm.readData("procesos.txt", "fetchlist.txt", Integer.valueOf(this.pagSizeText.getText()));
        }
        else{
            procesos = fm.readData(this.processPath, this.fetchListPath, Integer.valueOf(this.pagSizeText.getText()));
        }
        
        return procesos;
    }
    
    public void handleDegree(int val){
        if(this.multiprogramming > 1 && val < 0){
            this.multiprogramming = this.multiprogramming + val;
        }
        else if(this.multiprogramming < 20 && val > 0){
            this.multiprogramming = this.multiprogramming + val;

        }
        if(sim != null){
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
    
    //builder is configured here
    public void setConfig(){
        
        builder = new SimulationBuilder();
        ReplacementScope scope = ReplacementScope.valueOf(RepScopePicker.getSelectedText());
        PlacementPolicyType placementPolicy = PlacementPolicyType.valueOf(PlacementPolicyPicker.getSelectedText());
        ReplacementPolicyType replacementPolicy = ReplacementPolicyType.valueOf(RepPolicyPicker.getSelectedText());
        int pageSize = Integer.parseInt(pagSizeText.getText());
        int memorySize = Integer.parseInt(memFisicaText.getText());
        int backingStoreSize = Integer.parseInt(memVirtualText.getText());
        
        System.out.println("page -> " + pagSizeText.getText() + " -> " + pageSize);
        System.out.println("memory -> " + memFisicaText.getText() + " -> " + memorySize);
        System.out.println("store -> " + memVirtualText.getText() + " -> " + backingStoreSize);
        
        PageProfile profile = new PageProfile(pageSize);
        
        builder.setPlacementPolicy(placementPolicy);
        builder.setReplacementScope(scope);
        builder.setReplacementPolicy(replacementPolicy);
        builder.setProfile(profile);
        builder.setMemory(memorySize);
        builder.setStore(backingStoreSize);
        //!ATTENTION must get prepagin value from memory
        builder.setPrepaging(3);
        
        builder.setPrecleaning(CleaningPolicyPicker.getSelectedIndex() == 1);
        
        
        
        //SimulationBuilder simBuilder =new SimulationBuilder();
        //sim.setScope(scope);
        //sim.setPlacementPolicy(simBuilder.getPlacementPolicy(placementPolicy));
        //simBuilder.setReplacementPolicy(replacementPolicy);
        //sim.setReplacementPolicy(simBuilder.getReplacementPolicy(sim.getMemory()));
        
        
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
            String currentText = "vacía";
            if(currentPage!= null) currentText = currentPage.toString();
            text(i + " -> " + currentText, x, 3 + y + i *PAGE_SIZE);
            
        }
    }
    
    public void showPopUpErr(String tittle, String message) {
        this.errWindow.setVisible(true);
        this.errWindow.setTitle(tittle);
        
        this.errWindowLbl.setText(message);
    }
    
    
    public void createWindows() {
        window = GWindow.getWindow(this, "Configuración", 750, 200, 450, 550, JAVA2D);
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
        

        new GButton(window, pickerX + pickerWidth, pickerY, 30, 20, "1?");
        
        new GLabel(window, window.width - (pickerX + 240), pickerY, 150, 20, "Physical memory size:");
        this.memFisicaText = new GTextField(window, window.width - (pickerX + 120), pickerY, 100, 20);this.memFisicaText.setText("8192");
        new GButton(window, window.width - (pickerX + 120) + 100, pickerY, 30, 20, "8?");

        
        pickerY += 50;
        
        //Placement policy
        List<String> placementPolicyStrings = Stream.of(PlacementPolicyType.values()).map(Enum::name).collect(Collectors.toList());
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Placement Policy");
        this.PlacementPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight, optionsPerPage);
        this.PlacementPolicyPicker.setItems(placementPolicyStrings, 0); 
        

        new GButton(window, pickerX + pickerWidth, pickerY, 30, 20, "2?");
        
        new GLabel(window, window.width - (pickerX + 240), pickerY, 150, 20, "Virtual memory size:");
        this.memVirtualText = new GTextField(window, window.width - (pickerX + 120), pickerY, 100, 20);this.memVirtualText.setText("4096");
        new GButton(window, window.width - (pickerX + 120) + 100, pickerY, 30, 20, "9?");

        
        pickerY += 50;
        
        //replacement policy
        List<String> repPolicyStrings = Stream.of(ReplacementPolicyType.values()).map(Enum::name).collect(Collectors.toList());
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Replacement Policy");
        this.RepPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight, optionsPerPage);
        this.RepPolicyPicker.setItems(repPolicyStrings, 0); 
        

        new GButton(window, pickerX + pickerWidth, pickerY, 30, 20, "3?");
        
        new GLabel(window, window.width - (pickerX + 190), pickerY, 150, 20, "Page size:");
        this.pagSizeText = new GTextField(window, window.width - (pickerX + 120), pickerY, 100, 20);this.pagSizeText.setText("32");
        new GButton(window, window.width - (pickerX + 120) + 100, pickerY, 30, 20, "10?");

        
        pickerY += 50;
        /*
        //residentSet
        List<String> residentSetStrings = Arrays.asList("Fixed", "Variable");
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Resident set management");
        this.ResidentSetPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.ResidentSetPicker.setItems(residentSetStrings, 0);
        
        new GButton(window, pickerX + pickerWidth, pickerY, 30, 20, "4?");
        
        pickerY += 50;
        */
        //replacement scope
        List<String> scopeStrings = Stream.of(ReplacementScope.values()).map(Enum::name).collect(Collectors.toList());
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Replacement Scope");
        this.RepScopePicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.RepScopePicker.setItems(scopeStrings, 0);
        
        new GButton(window, pickerX + pickerWidth, pickerY, 30, 20, "5?");
        
        residentSetTextInput = new GTextField(window, 200, 200, 100, 20);
        residentSetTextInput.setText(Integer.toString(this.residentSet));
        
        
        pickerY += 50;
        
        //Cleaning policy
        List<String> cleaningStrings = Arrays.asList("Demand", "Pre-cleaning");
        new GLabel(window, pickerX, pickerY - 35,300, 50, "Cleaning policy");
        this.CleaningPolicyPicker = new GDropList(window, pickerX, pickerY, pickerWidth, pickerHeight,optionsPerPage);
        this.CleaningPolicyPicker.setItems(cleaningStrings, 0);
        
        new GButton(window, pickerX + pickerWidth, pickerY, 30, 20, "6?");
        
        pickerY += 50;
        
        this.multiText = new GLabel(window, pickerX, pickerY - 35, 300, 50, "Degree of multiprogramming: " + this.multiprogramming);
        this.residentSetText = new GLabel(window, 200, 170, 300, 50, "Resident set: ");
        new GButton(window, 180, pickerY, 30, 20, "7?");
        new GButton(window, 150, pickerY, 20, 20, "+");
        new GButton(window, 130, pickerY, 20, 20, "-");
        
        
        pickerY += 45;
        
        this.procPathText = new GLabel(window, pickerX, pickerY - 35, 500, 50, "Path de procesos: " + this.processPath);
        pickerY += 30;
        this.fetchPathText = new GLabel(window, pickerX, pickerY - 35, 500, 50, "Path de requests: " + this.fetchListPath);
        
        new GButton(window, window.width - 180, window.height - 40, 80, 30, "Guardar");
        this.cancelarBtn = new GButton(window, window.width - 90, window.height - 40, 80, 30, "Cancelar");
        new GButton(window, 0, window.height - 40, 80, 30, "Cargar procesos");
        new GButton(window, 90, window.height - 40, 80, 30, "Cargar requests");
        
        new GButton(window, window.width - 85, 10, 80, 30, "Manual de usuario");
        
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
    
    public static String readFileAsString(String fileName){ 
        try {
            String data = "";
            data = new String(Files.readAllBytes(Paths.get(fileName))); 
            return data;
        } catch (IOException ex) {
            return "";
        }
    } 
    
    public void handleTextEvents(GEditableTextControl textcontrol, GEvent event) { /* code */ }
    
    //---------------------------------Strings de ayuda
    String HfetchPolicy = "Fetch policy indica cómo es que se van a escoger las paginas durante la ejecución:\n \n" +
"	demanda: carga la pagina por petición\n \n" +
"	prepagin: carga varias paginas a la vez por petición",
            
           HplacementPolicy = "Indica la politica de ubicación de páginas en la memoria:\n" +
"\n \n" +
"first available: Ubica la página en el primer campo de memoria libre que se encuentra recorriendo la lista de memoria secuencialmente\n" +
"\n \n" +
"next available: Lleva un contador de la última página que se ubicó en la memoria y a la hora de insertar una nueva la inserta en la posición contador+1 y lo incrementa",
            
           HreplacementPolicy = "Indica cómo se van a reemplazar las páginas a la hora de un page-fault:\n" +
"\n \n" +
"LRU: Least Recently Used como lo dice su nombre se basa en reemplazar la página menos recientemente utilizada de la lista de páginas en memoria, puede ser implementado con timestamps\n" +
"\n \n" +
"FIFO: First in First out este algoritmo es uno de los más básicos, consiste en reemplazar la primera página que entró a la lista de páginas en memoria\n" +
"\n \n" +
"LFU: Least Frequently Used para este algoritmo se reemplaza la página que se ha utilizado menor cantidad de veces, puede ser implementado manteniendo un contador de utilización para cáda página en memoria el cuál se actualiza con cada request\n" +
"\n \n" +
"MRU: Most Recently Used es una variación del algoritmo LRU citado anteriormente se basa en reemplazar la página más recientemente utilizada, igualmente puede ser implementado con timestamps\n" +
"\n \n" +
"SECOND_CHANCE: Este algoritmo es una extencion del algoritmo FIFO, en el cual las paginas tienen una \n"
            + "segunda oportunidad, al estar al tope de la cola si tienen la bandera de segunda oportunidad se apaga \n"
            + "y se envia la pagina al fondo de la cola, si al estar al principio de la cola esta pagina no tiene \n"
            + "la bandera de segunda oportunidad encendida es remplazada \n",
            
           HresidentSet = "Asigna una cantidad de memoria maxima para un proceso si \n \n"  +
           "el replacement scope es local, si este es global el resident set es variable \n \n" +
            "ya que el algoritmo de remplazo selecionara una pagina de entre todas las paginas \n \n "
            + "en memoria segun el criterio correspondiente",
            
           HreplacementScope = "A la hora de que ocurre un page fault hay dos opciones para reemplazar la página\n \n" +
"local: sólo se puede reemplazar una página que pertenezca al proceso que está generando el request esto hace que el resident set pueda ser variable automaticamente para los procesos\n \n" +
"global: se puede reemplazar una página de cuálquier proceso\n" +
"lo anterior partiendo del hecho de que la página a reemplazar será indicada por el\n" +
"algoritmo de replacement seleccionado, esto settea al resident set fixed para los procesos",
            
           Hcleaningpolicy = "Cuando se utiliza on demand las paginas son guardadas al sacarlas de memoria,"
            + "cuando se utiliza pre-cleaning las paginas son guardadas en el backing store cada cierto tiempo",
            
           Hmultiprogramming = "Indica la cantidad de procesos que pueden estár en memoria a la vez ejecutando",
           
           HPhysicalMemSize = "Indica el tamaño de memoria fisica dentro de la simulación",
            
           HVirtualMemSize = "Indica el tamaño de memoria virtual dentro de la simulación",
            
           HPageSize = "Dada la cantidad de páginas se divide la memoria () entre dicha cantidad para obtener el tamaño de página a utilizar durante la simulación",
            
           HProcessesInfo = "Presenta la información de los procesos que no están cargados en memoria, los que están cargados " +
"en memoria junto con sus requisiciones " +
"y los que ya terminaron de ejecutar",
            
           HConfig = "Lleva a la pantalla de configuración, la cual permite seleccionar " +
"todos los datos de la simulación, para cambiarlos, debe seleccionar " +
"guardar, de lo contrario (cancelar) no aplicará los cambios",
            
           HidToExecute = "Este campo recibe un número entero mayor a 0 con el id del " +
"proceso que se desea ejecutar",
            
           Hiterations = "Este campo recibe un número entero mayor a 0 con la cantidad " +
"de iteraciones a simular del proceso seleccionado por id",
            
           HReset = "Vuelve a cargar los procesos de los archivos especificados y deja " +
" la simulación en tiempo 0 para volver a ejecutar",
           
           HStadisticsInfo = "Contiene la información actual de la simulación, page faults, hits y " +
"procentajes de uso de paginas",
            
           HFullSimulation = "Simula toda la ejecución de los procesos seleccionando aleatoreamente el " +
"siguiente proceso a ejecutar en cada iteración hasta terminar",
            
           HExecute = "Ejecuta el proceso seleccionado por id la cantidad de iteraciones seleccionadas " +
"arriba",
           ManualDeUsuario = "";
    
    
    
    
    
    
    
    
}