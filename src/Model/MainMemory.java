/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Charlie
 */
public class MainMemory implements Writable , Observable<Page>{
    private int size;    
    private Page[] pages;
    private PageProfile profile;
    private ArrayList<Observer<Page>> swapObservers;    
    private ArrayList<Observer<Page>> accesObservers;

    public MainMemory(int size, PageProfile profile) {
        this.size = size;
        this.profile = profile;
        this.pages = new Page[Math.abs(size/profile.getSize())];
        System.out.println("Total pages: " + Math.abs(size/profile.getSize()));
        swapObservers = new ArrayList<>();
        accesObservers = new ArrayList<>();
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
        notifySwap(page);
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
        accesObservers.add(newObserver);
    }
    
    
    public void subscribeSwap(Observer<Page> newObserver) {
        swapObservers.add(newObserver);
    }
    
    public void subscribeAcces(Observer<Page> newObserver) {
        accesObservers.add(newObserver);
    }


    @Override
    public void notifyAllObservers(Page page) {
        for(Observer<Page> observer : accesObservers){
            observer.notify(page);
        }
    }
    
    public void notifyAcess(Page page) {
        notifyAllObservers(page);
    }
    
    public void notifySwap(Page page) {
        for(Observer<Page> observer : swapObservers){
            observer.notify(page);
        }
    }


    //simply sasys is page is on memory
    @Override
    public boolean readPage(Page page) {
        boolean res = 0 <= Arrays.asList(pages).indexOf(page);
        //notify a read executed to this page
        notifyAcess(page);
        //if page is on memory visit it 
        if(res){
            
            page.getOwner().visit(page);
        }
        
        return res;
    }
    
    
    public Page getRandomPage(){
        int next = (int)(Math.random() * (pages.length-1));
        
        return pages[next];
    }
    
   
}
