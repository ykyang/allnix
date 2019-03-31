package org.allnix.hf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * 
 * 
 * TODO: clean up threadDb
 * TODO: user clearn up jobDb
 * 
 * @author Yi-Kun Yang ykyang@gmail.com
 *
 */
public class DefaultHfService {
    
    private Map<String,Job> jobDb = Collections.synchronizedMap(new HashMap<>());
    private Map<String, Thread> threadDb = Collections.synchronizedMap(new HashMap<>());
    
     
    public boolean isDone(Job job) {
        String id = job.getId();
        job = jobDb.get(id);
        return job.isDone();
    }
    
    public boolean delete(Job job) {
        if (!job.isDone()) {
            return false;
        }
        
        job = jobDb.remove(job.getId());
        threadDb.remove(job.getId());
        return true;
        // TODO: check for null;
    }
    public boolean hasJob(Job job) {
        return jobDb.containsKey(job.getId());
    }
    
    public Job loadData(String uri) {
        final Job job = new Job();
        jobDb.put(job.getId(), job);
        
        
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    job.setDone(true);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };

        Thread thread = new Thread(r);
        thread.start();
        
        threadDb.put(job.getId(), thread);
        
        return job;
    }
    
    
}
