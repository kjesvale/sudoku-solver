class Solver extends Thread {
	Board board;
	Square square;
	double startTime;
	double endTime;

	Solver(Square square, Board board) {
		this.square = square;
		this.board = board;
	}

	public void run() {
		startTime = System.nanoTime();
		square.fillRemaining();
		endTime = System.nanoTime();
		board.writeStatistics();
	}

	public double getRunTime() {
		return ((endTime - startTime) / 1000000);
	}
}
