import javax.swing.UIManager;

/* Oblig 5
 * Kjetil Svalestuen / kjetisva
 * 
 * Jeg tok meg den frihet aa endre navnet paa metoden "fillInnRemainingOfBoard()" til det noe
 * enklere "findRemaining()" til tross for at dette staar som et krav i oppgaven.
 *
 * I obligen har jeg brukt en enkel traad-loesning slik at sudokubrettets loesninger
 * blir skrevet ut saa fort de er funnet.
 */

public class Oblig5 {
	public static void main(String[] args) {
		try { // Setter Look And Feel til standard for operativsystemet.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		new Window("Sudoku Solver");
	}
}
