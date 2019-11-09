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
    private boolean dirty = false;

    public Page(Process owner, int logicalPosition, int physicalPosition) {
        this.owner = owner;
        this.logicalPosition = logicalPosition;
        this.physicalPosition = physicalPosition;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public Process getOwner() {
        return owner;
    }

    public void setOwner(Process owner) {
        this.owner = owner;
    }

    public int getLogicalPosition() {
        return logicalPosition;
    }

    public void setLogicalPosition(int logicalPosition) {
        this.logicalPosition = logicalPosition;
    }

    public int getPhysicalPosition() {
        return physicalPosition;
    }

    public void setPhysicalPosition(int physicalPosition) {
        this.physicalPosition = physicalPosition;
    }

    @Override
    public String toString() {
        if(this.dirty){
            return "P" + owner.getId() + " : " + logicalPosition + " *";
        }
        return "P" + owner.getId() + " : " + logicalPosition;
    }
   
    
    
}
