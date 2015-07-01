import java.io.File;

class Controller {
	Window window;
	Board board;

	Controller(Window window) {
		this.window = window;
	}

	public void open(File file) {
		window.reset();
		String filename = file.getName();
		write("Opened file \"" + filename + "\".");
		window.enableNextButton(false);

		board = new Board(file, this);
		Boolean successfullyRead = board.readFile();
		if (successfullyRead) {
			window.drawFilled();
			window.enableSolveButton(true);
		} else write("Could not translate the file.");
	}

	public void solve() {
		write("\nFinding Solution ...");
		window.enableNextButton(true);
		window.enableSolveButton(false);
		board.solveSudoku();
	}

	public void next() {
		Solution s = board.getSolutions().get();
		char[][] chars;
		if (s != null) {
			chars = s.getChars();
			window.drawNext(chars);
		}
	}

	public void write(String s) {
		window.appendLog(s);
	}

	public int getBoardSize() {
		return board.getSize();
	}

	public int getBoxRows() {
		return board.getBoxRows();
	}

	public int getBoxColumns() {
		return board.getBoxColumns();
	}

	public Square[][] getSquares() {
		return board.getSquares();
	}

	public int getBoxIndex(int row, int column) {
		return board.getBoxIndex(row, column);
	}

	public boolean noSolutionsLeft() {
		if (board.getSolutions().isEmpty()) {
			return true;
		} return false;
	}
}
