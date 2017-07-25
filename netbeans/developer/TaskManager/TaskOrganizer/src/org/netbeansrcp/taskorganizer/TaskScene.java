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
package org.netbeansrcp.taskorganizer;

import com.netbeansrcp.taskmodel.api.Task;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.util.List;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.vmd.VMDGraphScene;
import org.netbeans.api.visual.vmd.VMDNodeWidget;
import org.netbeans.api.visual.vmd.VMDPinWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;

/**
 *
 * @author Yi-Kun Yang &lt;ykyang at gmail.com&gt;
 */
public class TaskScene extends VMDGraphScene {

  private LayerWidget mainLayer;

  public TaskScene() {
    mainLayer = new LayerWidget(this);

    addChild(mainLayer);

    getActions().addAction(ActionFactory.createAcceptAction(
      new AcceptProvider() {
      @Override
      public void accept(Widget widget, Point point, Transferable transferable) {
        Node node = NodeTransfer.node(transferable, NodeTransfer.DND_COPY);
        Task task = node.getLookup().lookup(Task.class);
        createNode(task.getName());
        List<Task> children = task.getChildren();
        for ( Task t : children) {
          createPin(task.getName(), t.getName());
        }
      }

      @Override
      public ConnectorState isAcceptable(Widget widget, Point point,
                                         Transferable transferable) {
        Node node = NodeTransfer.node(transferable, NodeTransfer.DND_COPY);
        if ( node != null && node.getLookup().lookup(Task.class) != null) {
          return ConnectorState.ACCEPT;
        } else {
          return ConnectorState.REJECT_AND_STOP;
        }
      }
    }));
  }
  
  private void createNode(String nodeId) {
    VMDNodeWidget widget = (VMDNodeWidget) addNode(nodeId);
    widget.setNodeName(nodeId);
  }
  
  private void createPin(String nodeId, String pinId) {
    VMDPinWidget widget = (VMDPinWidget) addPin(nodeId, pinId);
    widget.setPinName(pinId);
  }
}
