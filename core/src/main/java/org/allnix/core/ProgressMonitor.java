/*
 * Copyright 2016 Yi-Kun Yang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.allnix.core;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public interface ProgressMonitor {
  /**
   * Start a new job
   * 
   * @param name Name of the job
   * @param totalWork Total amount of work to be done
   */
  void beginTask(String name, int totalWork);
  /**
   * Check if the job is canceled
   * 
   * @return true if the job is canceled
   */
  boolean isCanceled(); 
  /**
   * Set the cancel flag to be true
   */
  void cancel();
  /**
   * Current task name
   * 
   * @param name Task name
   */
  void subTask(String name);
  /**
   * Amount of additional work done since last call
   * 
   * @param value Amount of new work done
   */
  void worked(int value);
  /**
   * Tell the progress monitor that the work is done
   * <p>
   * No additional effect when called second time
   */
  void done();
  
}
