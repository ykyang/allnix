package org.allnix.swing;


import javax.swing.*;

public class IFrame
{
  public static void main(String[] args)
  {
//    try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
//    catch(Exception ex){}
    JFrame f=new JFrame();
    f.setContentPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
      createFrame("Left"), createFrame("right") ));
    f.setSize(300, 300);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
  }

  private static JInternalFrame createFrame(String title)
  {
    final JInternalFrame f1 = new JInternalFrame(title, true, true);
    f1.setVisible(true);
    f1.getContentPane().add(new JLabel(title));
    return f1;
  }
}

