package org.allnix.classic.ch2;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * State for the missionaries and cannibals problem
 * 
 * @author ykyang@gmail.com
 *
 */
public class MCState {
	/**
	 * ./gradlew -PmainClass=org.allnix.simple.ch2.MCState runApp
	 * gradlew.bat -PmainClass=org.allnix.simple.ch2.MCState runApp
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MCState start = new MCState(MAX_NUM, MAX_NUM, true);
		Node<MCState> solution = Search.bfs(start, MCState::goalTest, MCState::successors);
		if (solution == null) {
			System.out.println("No solution found!");
		} else {
			Collection<MCState> path = Search.nodeToPath(solution);
			displaySolution(path);
		}
	}
	
	private static final int MAX_NUM = 3;
	private final int wm; // west bank missionaries
	private final int wc; // west bank cannibals
	private final int em; // east bank missionaries
	private final int ec; // east bank cannibals

	private boolean boat; // true -> boat is on the west side
	
	private boolean isBoatOnWestSide() {
		return boat;
	}

	public MCState(int wm, int wc, boolean boat) {
		super();
		this.wm = wm;
		this.wc = wc;
		this.em = MAX_NUM - wm;
		this.ec = MAX_NUM - wc;
		this.boat = boat;
	}
	
	public static List<MCState> successors(MCState mcs) {
		List<MCState> list = new ArrayList<>();
		
		MCState state = null;
		boolean newboat = !mcs.boat;
		if (mcs.isBoatOnWestSide()) {
			// move 2 m
			if (mcs.wm > 1) {
				state = new MCState(mcs.wm - 2, mcs.wc, newboat);
				list.add(state);
			}
			// move 1 m
			if (mcs.wm > 0) { // W: m, mm, mmm
				state = new MCState(mcs.wm - 1, mcs.wc, newboat);
				list.add(state);
			}
			// move 2 c
			if (mcs.wc > 1) {
				state = new MCState(mcs.wm, mcs.wc - 2, newboat);
				list.add(state);
			}
			// move 1 c
			if (mcs.wc > 0) {
				state = new MCState(mcs.wm, mcs.wc - 1, newboat);
				list.add(state);
			}
			// move 1 m & 1 c
			if (mcs.wc > 0 && mcs.wm > 0) {
				state = new MCState(mcs.wm - 1, mcs.wc - 1, newboat);
				list.add(state);
			}
		} else { // boat on east bank
			// move 2 m
			if (mcs.em > 1) {
				state = new MCState(mcs.wm + 2, mcs.wc, newboat);
				list.add(state);
			}
			// move 1 m
			if (mcs.em > 0) {
				state = new MCState(mcs.wm + 1, mcs.wc, newboat);
				list.add(state);
			}
			// move 2 c
			if (mcs.ec > 1) {
				state = new MCState(mcs.wm, mcs.wc + 2, newboat);
				list.add(state);
			}
			// move 1 c
			if (mcs.ec > 0) {
				state = new MCState(mcs.wm, mcs.wc + 1, newboat);
				list.add(state);
			}
			// move 1 m & 1 c
			if (mcs.ec > 0 && mcs.em > 0) {
				state = new MCState(mcs.wm + 1, mcs.wc + 1, newboat);
				list.add(state);
			}
		}
		
		list.removeIf(Predicate.not(MCState::isLegal));
		
		return list;
	}
	
	public boolean goalTest() {
		return isLegal() && em == MAX_NUM && ec == MAX_NUM;
	}
	
	public boolean isLegal() {
		if (0 < wm && wm < wc) {
			return false;
		}
		if (0 < em && em < ec) {
			return false;
		}
		
		return true;
	}
	
	public static void displaySolution(Collection<MCState> list) {
		if (list.isEmpty()) {
			return;
		}
		
		List<MCState> path = new ArrayList<>(list);
		
		PrintStream out = System.out;
		
		MCState oldState = path.get(0);
		out.println(oldState);
		
		for (MCState currentState : path.subList(1,  path.size())) {
			if (currentState.isBoatOnWestSide()) {
				// Boat is now on the west means oldState is on the east
				out.printf("%d missionaries and %d cannibals moved from the east bank to the west bank\n",
						oldState.em - currentState.em,
						oldState.ec - currentState.ec);
			} else {
				out.printf("%d missionaries and %d cannibals moved from the west bank to the east bank\n",
						oldState.wm - currentState.wm,
						oldState.wc - currentState.wc);
			}
			out.println(currentState);
			oldState = currentState;
		}
		
	}
	
	public String toString() {
		return String.format(
				"West bank: %d missionaries, %d cannibals.\n"
				+ "East bank: %d missionaries, %d cannibals.\n"
				+ "The boat is on the %s bank.\n",
				wm, wc, em, ec,
				isBoatOnWestSide() ? "west":"east");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (boat ? 1231 : 1237);
		result = prime * result + ec;
		result = prime * result + em;
		result = prime * result + wc;
		result = prime * result + wm;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCState other = (MCState) obj;
		if (boat != other.boat)
			return false;
		if (ec != other.ec)
			return false;
		if (em != other.em)
			return false;
		if (wc != other.wc)
			return false;
		if (wm != other.wm)
			return false;
		return true;
	}
	
	
}
