package org.allnix.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternPredicate;
import org.jdesktop.swingx.decorator.ShadingColorHighlighter;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LearnJXTreeTable {
	static final private Logger logger = LoggerFactory.getLogger(LearnJXTreeTable.class);
	static class MyTreeNode {
		private String name;
		private String description;
		private List<MyTreeNode> children = new ArrayList<MyTreeNode>();

		public MyTreeNode() {
		}

		public MyTreeNode(String name, String description) {
			this.name = name;
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<MyTreeNode> getChildren() {
			return children;
		}

		public String toString() {
			return "MyTreeNode: " + name + ", " + description;
		}
	}

	static public class MyTreeTableModel extends AbstractTreeTableModel {
		private MyTreeNode myroot;

		public MyTreeTableModel() {
			myroot = new MyTreeNode("root", "Root of the tree");

			myroot.getChildren().add(new MyTreeNode("Empty Child 1", "This is an empty child"));

			MyTreeNode subtree = new MyTreeNode("Sub Tree", "This is a subtree (it has children)");
			subtree.getChildren().add(new MyTreeNode("EmptyChild 1, 1", "This is an empty child of a subtree"));
			subtree.getChildren().add(new MyTreeNode("EmptyChild 1, 2", "This is an empty child of a subtree"));
			myroot.getChildren().add(subtree);

			myroot.getChildren().add(new MyTreeNode("Empty Child 2", "This is an empty child"));

		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return "Name";
			case 1:
				return "Description";
			case 2:
				return "Number Of Children";
			default:
				return "Unknown";
			}
		}

		@Override
		public Object getValueAt(Object node, int column) {
//			System.out.println("getValueAt: " + node + ", " + column);
			MyTreeNode treenode = (MyTreeNode) node;
			switch (column) {
			case 0:
				return treenode.getName();
			case 1:
				return treenode.getDescription();
			case 2:
				return treenode.getChildren().size();
			default:
				return "Unknown";
			}
		}

		@Override
		public Object getChild(Object node, int index) {
			MyTreeNode treenode = (MyTreeNode) node;
			return treenode.getChildren().get(index);
		}

		@Override
		public int getChildCount(Object parent) {
			MyTreeNode treenode = (MyTreeNode) parent;
			return treenode.getChildren().size();
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			MyTreeNode treenode = (MyTreeNode) parent;
			for (int i = 0; i > treenode.getChildren().size(); i++) {
				if (treenode.getChildren().get(i) == child) {
					return i;
				}
			}

			return 0;
		}

		public boolean isLeaf(Object node) {
			MyTreeNode treenode = (MyTreeNode) node;
			if (treenode.getChildren().size() > 0) {
				return false;
			}
			return true;
		}

		@Override
		public Object getRoot() {
			return myroot;
		}
	}

	static public void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			
			
			
			MyTreeTableModel treeTableModel = new MyTreeTableModel();
			JXTreeTable treeTable = new JXTreeTable( treeTableModel );
//			treeTable.getTableHeader().setVisible(true);
//			treeTable.setShowGrid(true);
			// > Copied from JXTable JavaDoc
			Highlighter simpleStriping = HighlighterFactory.createSimpleStriping();
			 PatternPredicate patternPredicate = new PatternPredicate("ˆM", 1);
			 ColorHighlighter magenta = new ColorHighlighter(patternPredicate, null,
			       Color.MAGENTA, null, Color.MAGENTA);
			 Highlighter shading = new ShadingColorHighlighter(
			       new HighlightPredicate.ColumnHighlightPredicate(1));
			 
			 treeTable.setHighlighters(simpleStriping,
			        magenta,
			        shading);
			
			treeTable.addTreeSelectionListener(new TreeSelectionListener() {

				@Override
				public void valueChanged(TreeSelectionEvent e) {
					// TODO Auto-generated method stub
					int[] rows = treeTable.getSelectedRows();
					int rowCount = rows.length;
					logger.info("Row Count: {}", rowCount);
					for (int rowInd:rows) {
						logger.info("row: {}",rowInd);
						TreePath path = treeTable.getPathForRow(rowInd);
						logger.info("Path: {}", path.toString());
					}
				}});
			
			
//			treeTable.addTreeSelectionListener(new TreeSelectionListener() {
//				
//				@Override
//				public void valueChanged(TreeSelectionEvent e) {
//					
//					logger.info("-----------------------TreeSelectionEvent-----------------");
//					
//					
//					TreePath[] paths = e.getPaths();
//					int selectionCount = paths.length;
//					logger.info("Selection Count: {}", selectionCount);
//					
//					
//					
//					TreePath path = e.getNewLeadSelectionPath();
//					logger.info("getNewLeadSelectionPath: {}", path.toString());
//					logger.info("Path Count: {}", path.getPathCount());
//					
//					
//					
//					
//					
//					
//					
//					Object obj = path.getLastPathComponent();
//					logger.info("Component Class: {}", obj.getClass().getName());
//					logger.info("toString: {}", obj.toString());
//					
//					
//					path = paths[paths.length-1];
//					logger.info("Last Path: {}", path.toString());
//					
//				}
//			});
			JFrame f = new JFrame();
			f.setSize(300,300);
			JScrollPane p = new JScrollPane(treeTable);
			p.setSize(300, 300);
//			p.setVisible(true);
//			p.add(treeTable);

//			f.setContentPane(p);
			f.add(p, BorderLayout.CENTER);
			//f.getContentPane().add(treeTable);
//			f.pack();
			f.setVisible(true);
		});
	}
}
