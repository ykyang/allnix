/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansrcp.taskduplicator;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
  dtd = "-//org.netbeansrcp.taskduplicator//TaskDuplicator//EN",
  autostore = false
)
@TopComponent.Description(
  preferredID = "TaskDuplicatorTopComponent",
  //iconBase="SET/PATH/TO/ICON/HERE", 
  persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.netbeansrcp.taskduplicator.TaskDuplicatorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
  displayName = "#CTL_TaskDuplicatorAction",
  preferredID = "TaskDuplicatorTopComponent"
)
@Messages({
  "CTL_TaskDuplicatorAction=TaskDuplicator",
  "CTL_TaskDuplicatorTopComponent=TaskDuplicator Window",
  "HINT_TaskDuplicatorTopComponent=This is a TaskDuplicator window"
})
public final class TaskDuplicatorTopComponent extends TopComponent implements 
  PropertyChangeListener, LookupListener {

  private TaskManager taskManager;
  private Lookup.Result<Task> result;
  private Task task;
  private InstanceContent ic = new InstanceContent();
  
  public TaskDuplicatorTopComponent() {
    taskManager = Lookup.getDefault().lookup(TaskManager.class);
    
    initComponents();
    setName(Bundle.CTL_TaskDuplicatorTopComponent());
    setToolTipText(Bundle.HINT_TaskDuplicatorTopComponent());

    // > Create Lookup and link with InstanceContent
    associateLookup(new AbstractLookup(ic));
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    updateTaskInfo();
  }

  @Override
  public void resultChanged(LookupEvent arg0) {
    Task[] tasks = this.result.allInstances().
      toArray(new Task[1]);
    Task newTask = tasks[tasks.length - 1];
    task.removePropertyChangeListener(this);
    task = newTask;
    task.addPropertyChangeListener(this);

    updateTaskInfo();
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel11 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();
    nameLabel = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    dueDateLabel = new javax.swing.JLabel();
    priorityLabel = new javax.swing.JLabel();
    progressLabel = new javax.swing.JLabel();
    descriptionLabel = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();

    org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jLabel11.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jLabel1.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.nameLabel.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jLabel3.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jLabel4.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jLabel5.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jLabel6.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(dueDateLabel, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.dueDateLabel.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(priorityLabel, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.priorityLabel.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(progressLabel, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.progressLabel.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(descriptionLabel, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.descriptionLabel.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(TaskDuplicatorTopComponent.class, "TaskDuplicatorTopComponent.jButton1.text")); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel6)
              .addComponent(jLabel5)
              .addComponent(jLabel4)
              .addComponent(jLabel3)
              .addComponent(jLabel1))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                  .addComponent(priorityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8))
              .addComponent(dueDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(jButton1)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(nameLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(dueDateLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel4)
          .addComponent(priorityLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(progressLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel6)
          .addComponent(descriptionLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jButton1)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    Task t = taskManager.createTask();
    t.setName(this.task.getName());
    t.setDue(this.task.getDue());
    t.setPrio(this.task.getPrio());
    t.setProgr(this.task.getProgr());
    t.setDescr(this.task.getDescr());
    
    List<Task> tasks = new ArrayList<>(); 
    tasks.add(t);
    // > Make the new task available through Lookup
    this.ic.set(tasks, null);
  }//GEN-LAST:event_jButton1ActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel descriptionLabel;
  private javax.swing.JLabel dueDateLabel;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel nameLabel;
  private javax.swing.JLabel priorityLabel;
  private javax.swing.JLabel progressLabel;
  // End of variables declaration//GEN-END:variables
  @Override
  public void componentOpened() {
    TopComponent taskEditor = WindowManager.getDefault().findTopComponent("TaskEditorTopComponent");
    result = taskEditor.getLookup().lookupResult(Task.class);
    result.addLookupListener(this);
    
    task = (Task)(result.allInstances().toArray())[0];
    task.addPropertyChangeListener(this);
  }

  @Override
  public void componentClosed() {
    task.removePropertyChangeListener(this);
    result = null;
  }

  void writeProperties(java.util.Properties p) {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TODO store your settings
  }

  void readProperties(java.util.Properties p) {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }
  
  private void updateTaskInfo() {
    nameLabel.setText(task.getName());
    dueDateLabel.setText(DateFormat.getInstance().format(task.getDue()));
    priorityLabel.setText("" + task.getPrio());
    progressLabel.setText("" + task.getProgr());
    descriptionLabel.setText(task.getDescr());
  }
}