/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author Charlie
 */
public class MainMemory implements Writable , Observable<Page>{
    private int size;    
    private Page[] pages;
    private PageProfile profile;
    private ArrayList<Observer<Page>> observers;
    public MainMemory(int size, PageProfile profile) {
        this.size = size;
        this.profile = profile;
        this.pages = new Page[Math.abs(size/profile.getSize())];
        observers = new ArrayList<>();
    }

    
    public int getSize() {
        return size;
    }
    
    public int getPageCount(){
        return pages.length;
    }

    public Page[] getPages() {
        return pages;
    }

    @Override
    public Page getPage(int index) {
        return pages[index];
    }

    @Override
    public void setPage(int index, Page page) {
        pages[index] = page;
        //send a signal that this page was modified
        notifyAllObservers(page);
    }
    
    public int getAvailable(){
        //all pages - ocuppied pages
        return this.pages.length - getLength(pages);
    }
    
    //count not null method from 
    //https://stackoverflow.com/questions/7466102/function-like-array-length-that-doesnt-include-null-elements
    private static <T> int getLength(T[] arr){
        int count = 0;
        for(T el : arr)
            if (el != null)
                ++count;
    return count;
}
 
    @Override
    public void subscribe(Observer<Page> newObserver) {
        observers.add(newObserver);
    }

    @Override
    public void notifyAllObservers(Page page) {
        for(Observer<Page> observer : observers){
            observer.notify(page);
        }
    }
   
}
