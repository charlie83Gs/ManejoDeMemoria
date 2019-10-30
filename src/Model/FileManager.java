/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Jil
 */
public class FileManager {

    private String path, output;
    private BackingStore store;
    
    public FileManager(String path, BackingStore store) {
        this.path = path;
        this.store = store;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output.toString().trim();
    }
    
    public ArrayList<Process> readProcesos(){
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
        int[] usage;
        for(int i = 0; i < json.length(); i++){
            tmp = json.getJSONObject(i);
            
            usage = new int[tmp.getJSONArray("fetchlist").length()];
            for(int j = 0; j < usage.length; j++){
                usage[j] = tmp.getJSONArray("fetchlist").getInt(j);
            }

            result.add(new Process(tmp.getInt("id"),
                                    new FetchList(usage),
                                    tmp.getInt("pages"),
                                    tmp.getInt("prior"),
                                    this.store));
        }
        return result;
    }
    
    public void write(String response){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(this.output));
            writer.write(response);
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
}
