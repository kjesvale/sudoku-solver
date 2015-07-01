import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

class Board {
	private Controller controller;
	private SolutionContainer solutions;
	private File file;
	private int boxRows;
	private int boxColumns;
	private int size;
	private Square[][] squares;
	private Column[] columns;
	private Row[] rows;
	private Box[] boxes;
	private double runTime;
	private Solver solver;
	private int writeCount = 0;

	/** 
	 * Starter det hele med aa lage en loesningsbeholder, sette argumentene, lese datafilen og
	 * kjoere fillRemaining()-metoden i ruten oeverst til venstre, for saa aa skrive ut resul-
	 * tatene. Tar tiden paa fillRemaining()-metoden og skriver ut denne til slutt.
	 */
	Board(File file, Controller controller) {
		this.controller = controller;
		this.file = file;
	}

	public void solveSudoku() {
		solutions = new SolutionContainer(this);
		Square square = squares[0][0];
		solver = new Solver(square, this);
		writeCount = 0;
	
		solver.start();
	}

	
	/**
	 * Leser brettet. Metoden starter med aa lese inn stoerrelsen paa boksene. Deretter bruker den
	 * disse opplysningene til aa initialisere brettets variabler. Saa gaar den gjennom hver eneste
	 * linje til filen er tom, og deler opp hver linje i en nummerarray. Lager saa ruter utifra
	 * posisjonen til ruten og det forhaandsfylte nummeret dette kanskje inneholder.
	 *
	 * Om scanneren oppdager "hull" i datafilen, dvs. at tall mangler i en rad eller kolonne, vil
	 * programmet automatisk fylle disse med en dynamisk rute.
	 * @param dataFile filen som inneholder sudokubrettet
	 */
	public boolean readFile() {
		Scanner reader;
		String line = "";

		try {
			reader = new Scanner(file);

			boxRows = Integer.parseInt(reader.nextLine());
			boxColumns = Integer.parseInt(reader.nextLine());
			initialize(boxColumns, boxRows);

			int rowCount = 0;
			int[] numbers;
			while (reader.hasNextLine()) {
				rowCount++;
				line = reader.nextLine();
				int length = line.length();

				numbers = new int[length];
				for (int i = 0; i < length; i++) {
					numbers[i] = convertToNumber(line.substring(i,i+1));
					setSquare((rowCount-1), i, numbers[i]);
				}

				// Fyller kolonner paa raden hvis de mangler i fil.
				if (length < size) {
					for (int i = length; i < size; i++) {
						setSquare((rowCount-1), i, 0);
					}
				}
			}

			// Fyller resten av radene hvis de mangler i fil.
			if (rowCount < size) {
				for (int i = rowCount; i < size; i++) {
					for (int j = 0; j < size; j++) {
						setSquare(i, j, 0);
					}
				}
			}

		} catch (Exception e) {
			return false;
		} return true;
	}


	/** Initialiserer stoerrelsen til nummerbeholdere og oppretter disse. */
	private void initialize(int boxColumns, int boxRows) {
		controller.write("Box size is set to " + boxRows + " x " + boxColumns + ".");
		size = boxColumns * boxRows;
		Square.setStaticVars(size, this);

		// Setter arraystoerrelser
		controller.write("Board size is set to " + size + " x " + size 
							+ " = " + (size * size) + ".");
		squares = new Square[size][size];
		columns = new Column[size];
		rows = new Row[size];
		boxes = new Box[size];

		// Initialiserer kolonne-, rad- og bokobjekter.
		for (int i = 0; i < size; i++) {
			columns[i] = new Column(size);
			rows[i] = new Row(size);
			boxes[i] = new Box(size);
		}
	}


	/**
	 * Oppretter en rute paa gitt rad, kolonne og boks, fyller den med et nummer hvis statisk, og
	 * setter den forrige rutens neste-peker til denne.
	 */
	private void setSquare(int i, int j, int number) {
		if (number == 0) {
			squares[i][j] = new FreeSquare();
		} else {
			squares[i][j] = new FilledSquare();
		}

		squares[i][j].setRow(rows[i]);
		squares[i][j].setColumn(columns[j]);
		squares[i][j].setBox(boxes[getBoxIndex(i, j)]);

		if (squares[i][j] instanceof FilledSquare) {
			squares[i][j].setNumber(number);
		}

		if (j > 0) {
			squares[i][j-1].next = squares[i][j];
		} else if (i > 0) {
			squares[i-1][size-1].next = squares[i][j];
		}
	}


	/** Konverterer et tegn til heltall, slik at programmet slipper aa operere med latin. */
	private int convertToNumber(String character) {
		int number = 0;
		char[] allChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

		if (!character.equals(".")) {
			for (int i = 0; i < allChars.length; i++) {
				if (character.charAt(0) == allChars[i]) {
					number = i;
				}
			}
		} return number;
	}


	/** Skriver ut opp til 750 loesninger i ren tekst */
	public void writeStatistics() {
		runTime = solver.getRunTime();

		controller.write(String.format("\n" + "%s" + "%.0f", "Number of solutions: ", solutions.getCount()));
		controller.write(String.format(
				"%s" + "%.1f" + "%s" + "\n", "Time: ", runTime, " ms"));
	}

	public void writeThis(Solution solution) {
		writeCount++;
		controller.write(writeCount + ": " + solution.toString());
	}

	/** Returnerer brettets stoerrelse. */
	public int getSize() {
		return size;
	}

	public int getBoxRows() {
		return boxRows;
	}

	public int getBoxColumns() {
		return boxColumns;
	}


	/** Returnerer en dobbeltarray med alle brettets ruter. */
	public Square[][] getSquares() {
		return squares;
	}


	/** Regner ut hvilken boks man er i utifra raden og kolonnen.  */
	public int getBoxIndex(int row, int column) {
		int currentRow = row/boxRows;						// Finner vertikal boks
		int currentColumn = column/boxColumns;					// Finner horisontal boks
		return (int) (currentRow * boxRows) + currentColumn;
	}
	
	
	public SolutionContainer getSolutions() {
		return solutions;
	}
}
