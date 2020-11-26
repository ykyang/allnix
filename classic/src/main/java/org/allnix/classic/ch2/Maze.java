package org.allnix.classic.ch2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
		
		Node<MazeLocation> solution;
		
		// Depth-first
		Node<MazeLocation> solution_1 
			= Search.dfs(maze.getStart(), maze::goalTest, maze::successors);
		if (solution_1 == null) {
			System.out.println("No solution found using depth-first search!");
		} else {
			Collection<MazeLocation> path_1 = Search.nodeToPath(solution_1);
			maze.mark(path_1);
			System.out.println(maze);
			maze.clear(path_1);
		}
		
		// Breadth-first
		Node<MazeLocation> solution_2 
			= Search.bfs(maze.getStart(), maze::goalTest, maze::successors);
		if (solution_2 == null) {
			System.out.println("No solution found using breadth-first search!");
		} else {
			Collection<MazeLocation> path_2 = Search.nodeToPath(solution_2);
			maze.mark(path_2);
			System.out.println(maze);
			maze.clear(path_2);
		}
		
		// A* (astar)
		Node<MazeLocation> sol_astar 
			= solution
			= Search.astar(maze.getStart(), maze::goalTest, maze::successors, 
				maze::manhattanDistance);
		if (solution == null) {
			System.out.println("No solution found using A* search!");
		} else {
			Collection<MazeLocation> path = Search.nodeToPath(solution);
			maze.mark(path);
			System.out.println(maze);
			maze.clear(path);
		}
		
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
	
	public MazeLocation getStart() {
		return start;
	}

	/**
	 * Distance from a location to the goal in straight line
	 * 
	 * @param ml
	 * @return
	 */
	public double euclideanDistance(MazeLocation ml) {
		int deltax = ml.column - goal.column;
		int deltay = ml.row - goal.row;
		
		return Math.sqrt(deltax*deltax + deltay*deltay);
	}
	
	/**
	 * Distance from a location to the goal by the sum
	 * of delta-x and delta-y.
	 * 
	 * @param ml
	 * @return
	 */
	public double manhattanDistance(MazeLocation ml) {
		int deltax = Math.abs(ml.column - goal.column);
		int deltay = Math.abs(ml.row - goal.row);
		
		return deltax + deltay;
	}
	
	public List<MazeLocation> successors(MazeLocation ml) {
		List<MazeLocation> locations = new ArrayList<>();
		
		int r,c; // row and column to be tested
		
		r = ml.row + 1;
		c = ml.column;
		if (r < rowCount && grid[r][c] != Cell.BLOCKED) {
			locations.add(new MazeLocation(r, c));
		}
		
		r = ml.row - 1;
		c = ml.column;
		if (-1 < r && grid[r][c] != Cell.BLOCKED) {
			locations.add(new MazeLocation(r, c));
		}
		
		r = ml.row;
		c = ml.column + 1;
		if (c < columnCount && grid[r][c] != Cell.BLOCKED) {
			locations.add(new MazeLocation(r, c));
		}
		
		r = ml.row;
		c = ml.column - 1;
		if (-1 < c && grid[r][c] != Cell.BLOCKED) {
			locations.add(new MazeLocation(r, c));
		}
		
		return locations;
	}

	/**
	 * Mark the path in the maze 
	 * 
	 * @param path
	 */
	public void mark(Collection<MazeLocation> path) {
		for (MazeLocation ml : path) {
			grid[ml.row][ml.column] = Cell.PATH;
		}
		
		grid[start.row][start.column] = Cell.START;
		grid[goal.row][goal.column] = Cell.GOAL;
	}
	
	public void clear(Collection<MazeLocation> path) {
		for (MazeLocation ml : path) {
			grid[ml.row][ml.column] = Cell.EMPTY;
		}
		
		grid[start.row][start.column] = Cell.START;
		grid[goal.row][goal.column] = Cell.GOAL;
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
