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
public interface Observable<T> {
    public void subscribe(Observer<T> newObserver);
    public void notifyAllObservers(T object);
}
