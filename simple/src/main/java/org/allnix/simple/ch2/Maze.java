package org.allnix.simple.ch2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Maze {
	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch2.Maze runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch2.Maze runApp
	 * 
	 * @param args
	 */
	static public void main(String[] args) {
		Maze maze = new Maze();
		System.out.println(maze.toString());
	}
	
	public enum Cell {
		EMPTY, BLOCKED, START, GOAL, PATH;
		
		public String toString() {
			switch (this) {
			case EMPTY:
				return " ";
			case BLOCKED:
				return "X";
			case START:
				return "S";
			case GOAL:
				return "G";
			case PATH:
				return "*";
			}
			
			return null;
		}
	}
	
	private final int rowCount, columnCount;
	private final MazeLocation start, goal;
	private Cell[][] grid;
	
	public Maze() {
		this(10,10,new MazeLocation(0, 0), new MazeLocation(9,9), 0.2);
	}
	
	public Maze(int rowCount, int columnCount,
			MazeLocation start, MazeLocation goal,
			double sparseness) {
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		// TODO: Check for null?
		this.start = start;
		this.goal = goal;
		
		//> Fill the grid with empty cells
		grid = new Cell[rowCount][columnCount];
		for (Cell[] row : grid) {
			Arrays.fill(row, Cell.EMPTY);
		}
		
		randomFill(sparseness);
		
		grid[start.row][start.column] = Cell.START;
		grid[goal.row][goal.column] = Cell.GOAL;
	}
	
	public List<MazeLocation> successors(MazeLocation ml) {
		List<MazeLocation> locations = new ArrayList<>();
		
		int r,c; // row and column to be tested
		boolean blocked;
		
		r = ml.row + 1;
		c = ml.column;
		blocked = grid[r][c] == Cell.BLOCKED;
		if (r < rowCount && !blocked) {
			locations.add(new MazeLocation(r, c));
		}
		
		r = ml.row - 1;
		c = ml.column;
		blocked = grid[r][c] == Cell.BLOCKED;
		if (-1 < r && !blocked) {
			locations.add(new MazeLocation(r, c));
		}
		
		r = ml.row;
		c = ml.column + 1;
		blocked = grid[r][c] == Cell.BLOCKED;
		if (c < columnCount && !blocked) {
			locations.add(new MazeLocation(r, c));
		}
		
		r = ml.row;
		c = ml.column - 1;
		blocked = grid[r][c] == Cell.BLOCKED;
		if (-1 < c && !blocked) {
			locations.add(new MazeLocation(r, c));
		}
		
		return locations;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Cell[] row : grid) {
			for (Cell cell : row) {
				sb.append(cell.toString());
			}
			sb.append(System.lineSeparator());
		}
		
		return sb.toString();
	}
	
	private void randomFill(double sparseness) {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				if (Math.random() < sparseness) {
					grid[row][column] = Cell.BLOCKED;
				}
			}
		}
	}
	
	private boolean goalTest(MazeLocation ml) {
		// goal could be null
		return goal.equals(ml);
	}
	
}
