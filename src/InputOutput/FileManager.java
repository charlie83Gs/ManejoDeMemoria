/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputOutput;

import Model.BackingStore;
import Model.FetchList;
import Model.Process;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//import jdk.internal.net.http.common.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jil
 */
public class FileManager {

    private BackingStore store;
    
    public FileManager(BackingStore store) {
        this.store = store;
    }
    
    public ArrayList<Process> readData(String pathProcesos, String pathFetchList){
        ArrayList<Process> procesos = this.readProcesos(pathProcesos);
        ArrayList<FetchListPair> fetchList = this.readFetchList(pathFetchList);
        
        for(FetchListPair fetchL : fetchList){
            for(Process p : procesos){
                if(p.getId() == fetchL.getProcessId()){
                    p.setFetchlist(fetchL.getFetchL());
                }
            }
        }
        
        return procesos;
    }
    
    private ArrayList<Process> readProcesos(String path){
        BufferedReader br = null;
        String lista = "";
        try {
            br = new BufferedReader(new java.io.FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            lista = sb.toString();
        } catch (IOException ex) {
            System.out.println("formato del archivo incorrecto");
        }
        
        JSONArray json = new JSONArray(lista);
        ArrayList<Process> result = new ArrayList();
        JSONObject tmp;
        for(int i = 0; i < json.length(); i++){
            
            tmp = json.getJSONObject(i);
            result.add(new Process(tmp.getInt("id"),
                                    tmp.getInt("pages"),
                                    tmp.getInt("prior"),
                                    this.store,0));
        }
        return result;
    }
    
    private ArrayList<FetchListPair> readFetchList(String path){
        BufferedReader br = null;
        String lista = "";
        try {
            br = new BufferedReader(new java.io.FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            lista = sb.toString();
        } catch (IOException ex) {
            System.out.println("formato del archivo incorrecto");
        }
        
        JSONArray json = new JSONArray(lista);
        JSONObject tmp;
        ArrayList<FetchListPair> fetchList = new ArrayList();
        int[] usage;
        for(int i = 0; i < json.length(); i++){
            tmp = json.getJSONObject(i);
            
            usage = new int[tmp.getJSONArray("fetchlist").length()];
            for(int j = 0; j < usage.length; j++){
                usage[j] = tmp.getJSONArray("fetchlist").getInt(j);
            }

            fetchList.add(new FetchListPair(tmp.getInt("processId"), new FetchList(usage)));
        }
        
        return fetchList;
    }
    
    
    public void write(String output, String response){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(output));
            writer.write(response);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}
