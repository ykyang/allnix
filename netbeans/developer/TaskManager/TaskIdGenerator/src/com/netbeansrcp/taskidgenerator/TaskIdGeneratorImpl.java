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
package com.netbeansrcp.taskidgenerator;

import com.netbeansrcp.taskidgenerator.api.IdValidator;
import com.netbeansrcp.taskidgenerator.api.TaskIdGenerator;
import java.util.Random;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
@ServiceProvider(service = TaskIdGenerator.class)
public class TaskIdGeneratorImpl implements TaskIdGenerator {

  private Random random = new Random();

  public String generateId() {
    Lookup.Result<IdValidator> rslt = 
      Lookup.getDefault().lookupResult(IdValidator.class);

    String id = null; 
    boolean valid = false; 
    while (!valid) {
      id = this.getId();
      valid = true;
      for (IdValidator validator : rslt.allInstances()) {
        valid = valid & validator.validate(id);
      }
    }
    return id;

    
//    String id = "000000" + random.nextInt();
//    id = id.substring(id.length() - 6);
//    return id;
  }
 
  private String getId() {
    String id = "000000" + this.random.nextInt();
    return id.substring(id.length() - 6);
  }

}
