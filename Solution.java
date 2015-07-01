import java.util.Iterator;

// Inneholder En gitt loesning til sudokubrettet.
class Solution {
	private int size;
	private char[][] chars;

	Solution(Square[][] squares) {
		size = squares.length;
		chars = new char[size][size];

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				chars[i][j] = Integer.toString(squares[i][j].getNumber(), Character.MAX_RADIX).toUpperCase().charAt(0);
			}
		}
	}

	/** 
	 * Returnerer en skrivbar string-representasjon av loesningen. Her bruker jeg en char-array med
	 * alle tegn fra 0-9 og A-Z for ogsaa aa kunne representere verdier over 9.
	 * @param graphic Bestemmer om utskriften skal vaere paa En linje eller i et rutenett.
	 */
	public String toString() {
		String string = "";

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				string += chars[i][j];
			} string += "//";
		} return string;
	}

	public char[][] getChars() {
		return chars;
	}
}

// FIFO-beholder.
class SolutionContainer implements Iterable<Solution> {
	private final int MAX_SOLUTIONS = 750;
	private double count;
	private Node head;
	private Node tail;
	private Board board;

	SolutionContainer(Board board) {
		this.board = board;
		head = null;
		tail = null;
		count = 0;
	}

	public void insert(Solution solution) {
		if (count < MAX_SOLUTIONS) {
			if (head == null) {
				head = new Node(solution);
				board.writeThis(solution);
			} else {
				head.insert(solution);
				board.writeThis(solution);
			}
		} count++;
	}

	/** Returnerer neste element i listen. */
	public Solution get() {
		if (head == null) {
			return null;
		} else {
			Node n = head;
			head = n.next;
			return n.solution;
		}
	}

	public double getCount() {
		return count;
	}

	public boolean isEmpty() {
		if (head == null) {
			return true;
		} return false;
	}

	public void reset() {
		
	}


	public Iterator<Solution> iterator() {
		return new SolutionIterator();
	}

	private class SolutionIterator implements Iterator<Solution> {
		Node temp = head;

		public boolean hasNext() {
			return temp != null;
		}

		public Solution next() {
			Solution s = temp.solution;
			temp = temp.next;
			return s;
		}

		public void remove() { }
	}

	

	class Node {
		Node next;
		Solution solution;

		Node(Solution solution) {
			this.solution = solution;

		}

		public void insert(Solution solution) {
			if (next == null) {
				next = new Node(solution);
			} else {
				next.insert(solution);
			}
		}
	}
}
