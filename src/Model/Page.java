/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author curso
 */
public class Page {
    private Process owner;
    private int logicalPosition;
    private int physicalPosition;

    public Page(Process owner, int logicalPosition, int physicalPosition) {
        this.owner = owner;
        this.logicalPosition = logicalPosition;
        this.physicalPosition = physicalPosition;
    }
    
}
