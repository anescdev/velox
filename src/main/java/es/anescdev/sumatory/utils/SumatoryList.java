package es.anescdev.sumatory.utils;

import java.util.ArrayList;
import java.util.Collection;

import es.anescdev.sumatory.model.Sumatory;

/**
 * @author AnesCDev
 */
public class SumatoryList extends ArrayList<Sumatory>{

    /**
     * @param c
     */
    public SumatoryList(Collection<? extends Sumatory> c) {
        super(c);
    }

    /**
     * 
     */
    public SumatoryList() {
    }

    /**
     * @param initialCapacity
     */
    public SumatoryList(int initialCapacity) {
        super(initialCapacity);
    }
    
    
}
