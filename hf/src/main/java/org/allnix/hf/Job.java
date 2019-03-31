package org.allnix.hf;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Job {
    private String id = UUID.randomUUID().toString();
//    private Boolean done;
    private AtomicBoolean done = new AtomicBoolean(false); 
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public boolean isDone() {
        return done.get();
    }
    
    public void setDone(boolean value) {
        done.set(value);
    }
}
