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
public class MainMemory implements Writable{
    private int size;    
    private Page[] pages;
    private PageProfile profile;

    public MainMemory(int size, PageProfile profile) {
        this.size = size;
        this.profile = profile;
        this.pages = new Page[Math.abs(size/profile.getSize())];
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
    }
    
    public int getAvailable(){
        return getLength(pages);
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
    
}
