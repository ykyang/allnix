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
package com.netbeansrcp.priorityfilter;

import com.netbeansrcp.taskmodel.api.Task;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author Yi-Kun Yang &gt;ykyang@gmail.com&lt;
 */
public class PriorityFilterChildren extends FilterNode.Children {
  private Task.Priority priority;
  public PriorityFilterChildren(Node node, Task.Priority priority) {
    super(node);
    this.priority = priority;
  }

  @Override
  protected Node copyNode(Node node) {
    return new PriorityFilterNode(node, priority);
  }
  
  @Override
  protected Node[] createNodes(Node node) {
    Task task = node.getLookup().lookup(Task.class);
    
    if ( task != null && priority.compareTo(task.getPrio()) <= 0) {
      return new Node[] { copyNode(node) };
    } else {
      return new Node[]{};
    }
  }
}
