abstract class Square {
	public Square next;
	protected static Board board;
	private static int size;
	private int number = 0;
	private Row row;
	private Column column;
	private Box box;

	/**
	 * Proever aa settoe alle tall i seg selv (foerst 1, saa 2, saa 3 osv.), og lykkes dette for et
	 * tall, kalles samme metode (fillRemaining) i neste rute (dvs. den til hoeyre). Naar en vann-
	 * rett rad er ferdig (det finnes ingen rute rett til hoeyre), kalles metoden i ruten helt til
	 * venstre i neste rad, osv. Naar et kall paa fillRemaining-metoden i neste rute returnerer,
	 * proever ruten neste tall som enda ikke er proevd, osv. helt til alle tall er proevd i denne
	 * ruten.
	 */
	public void fillRemaining() {
		for (int i = 1; i < (size+1); i++) {
			if (acceptNumber(i)) { 
				setNumber(i);

				if (next == null) {
					board.getSolutions().insert(new Solution(board.getSquares()));
					removeNumber(i);
					return;
				}

				next.fillRemaining();
				removeNumber(i);
			}
		}
	}

	
	/** Setter brett-relasjonen og brettets stoerrelse. */
	public static void setStaticVars(int staticSize, Board staticBoard) {
		size = staticSize;
		board = staticBoard;
	}


	/** 
	 * Sjekker om rutens rad, kolonne og boks inneholder et gitt tall
	 *@return false hvis En av beholderne inneholder tallet
	 *@return true hvis tallet godkjennes.*/
	public boolean acceptNumber(int number) {
		if (row.contains(number) || column.contains(number) || box.contains(number)) return false;
		else return true;
	}


	/** Setter et gitt nummer til ruten og dens rad, kolonne og boks. */
	public void setNumber(int number) {
		this.number = number;
		row.addNumber(number);
		column.addNumber(number);
		box.addNumber(number);
	}
	

	/** Fjerner et gitt nummer fra rutens rad, kolonne og boks. */
	public void removeNumber(int number) {
		row.removeNumber(number);
		column.removeNumber(number);
		box.removeNumber(number);
	}

	/** Setter rutens rad. */
	public void setRow(Row row) {
		this.row = row;
	}

	/** Setter rutens kolonne. */
	public void setColumn(Column column) {
		this.column = column;
	}

	/** Setter rutens boks. */
	public void setBox(Box box) {
		this.box = box;
	}

	/** Returnerer nummeret til ruten. */
	public int getNumber() {
		return number;
	}
}

class FilledSquare extends Square {

	/** 
	 * Overskrider metoden i superklassen og kaller kun paa neste rute, evt. setter inn en
	 * loesning i loesningsbeholderen hvis dette er siste rute.
	 */
	public void fillRemaining() {
		if (next == null) {
			board.getSolutions().insert(new Solution(board.getSquares()));
		} else next.fillRemaining();
	}
}

class FreeSquare extends Square {

}
