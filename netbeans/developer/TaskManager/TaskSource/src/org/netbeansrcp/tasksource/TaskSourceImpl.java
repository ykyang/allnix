/*
 * Copyright 2017 Yi-Kun Yang.
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
package org.netbeansrcp.tasksource;

import java.util.ArrayList;
import java.util.List;
import org.netbeansrcp.tasksource.api.TaskSource;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class TaskSourceImpl implements TaskSource {

  @Override
  public Lookup getLookup() {
    List<Lookup> list = new ArrayList<>();
    Lookup lookup;
    
//    lookup = WindowManager.getDefault()
//      .findTopComponent("TaskEditorTopComponent").getLookup();
//    list.add(lookup);

//    lookup = WindowManager.getDefault()
//      .findTopComponent("TaskDuplicatorTopComponent").getLookup();
//    list.add(lookup);
    
    ProxyLookup pl = new ProxyLookup(list.toArray(new Lookup[2]));
    
    return pl;
  }
  
}
