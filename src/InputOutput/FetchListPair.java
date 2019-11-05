/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InputOutput;

import Model.FetchList;

/**
 *
 * @author J
 */
public class FetchListPair {
    
    private int processId;
    private FetchList fl;

    public FetchListPair(int processId, FetchList fl) {
        this.processId = processId;
        this.fl = fl;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public FetchList getFetchL() {
        return fl;
    }

    public void setFetchL(FetchList fl) {
        this.fl = fl;
    }
    
    
    
}
