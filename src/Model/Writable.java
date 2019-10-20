/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Charlie
 */
public interface Writable {
    public Page getPage(int index);
    public void setPage(int index, Page page);
}
