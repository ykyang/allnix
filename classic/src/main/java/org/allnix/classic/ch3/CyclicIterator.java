package org.allnix.classic.ch3;

import java.util.Collection;
import java.util.Iterator;

/**
 * An iterator that cycle through a collection over and over.
 * 
 * @author ykyang@gmail.com
 *
 * @param <T>
 */
public class CyclicIterator<T> implements Iterator<T> {
    private Collection<T> collection;
    private Iterator<T> iterator;
    
    /**
     * 
     * @param c The collection that the iterator will cycle through
     */
    public CyclicIterator(Collection<T> c) {
        collection = c;
        iterator = c.iterator();
    }
    
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (iterator.hasNext()) {
        } else {
            //> cycle
            iterator = collection.iterator();
        }
        
        return iterator.next();
    }

}
