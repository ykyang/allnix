package org.allnix.swing;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

public class LearnJInternalFrame {

	static public void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			JDesktopPane desktop = new JDesktopPane();
			
			JInternalFrame iframe = new JInternalFrame("View 1", true, true, true, true);
			
		});
	}
}
