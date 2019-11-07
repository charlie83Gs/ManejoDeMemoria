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
public class BackingStore implements Writable {
    private int size;
    private Page[] pages;
    private PageProfile profile;
    private int used = 0;
    
    public BackingStore(int size, PageProfile profile) {
        this.size = size;
        this.profile = profile;
        this.pages = new Page[Math.abs(size/profile.getSize())];
    }

    public int getSize() {
        return size;
    }
    
    
    
    public void setPage(int index, Page page){
        pages[index] = page;
    }
    
    public Page getPage(int index){
        return pages[index];
    }

    public Page allocatePage(Process process, int logicalPosition){
        Page newPage = new Page(process,logicalPosition,used);
        pages[used] = newPage;
        used++;
        
        return newPage;
    }

    @Override
    /**
     * @return should not be used
     */
    public boolean readPage(Page page) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Page[] getPages() {
        return pages;
    }
    
    
    
    
}
