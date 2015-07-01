import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.text.*;

class Window extends JFrame implements ActionListener{
	Controller controller = new Controller(this);

	JFileChooser chooser;
	JPanel buttonPanel;
	JButton fileButton;
	JButton solveButton;
	JButton nextButton;
	JTextArea logArea;
	JScrollPane logScroll;

	JFrame boardFrame;
	JPanel boardPanel;
	JPanel infoPanel;
	JLabel infoLabel;
	JLabel counterLabel;
	JLabel[][] labels;

	String filename;
	Font plainFont;
	Font boldFont;
	int size;
	int boxRows;
	int boxColumns;
	boolean[][] filled;

	
	/** Oppretter elementer og paneler, setter atributter og legger til knappelyttere. */
	Window(String title) {
		super(title);

		chooser = new JFileChooser();
		fileButton = new JButton("Open");
		solveButton = new JButton("Solve");
		nextButton = new JButton("Next");
		logArea = new JTextArea();
		logScroll = new JScrollPane(logArea);
		buttonPanel = new JPanel();

		chooser.setFileFilter(new FileNameExtensionFilter(".txt files", "txt"));
		solveButton.setEnabled(false);
		nextButton.setEnabled(false);
		logArea.setEnabled(false);

		logArea.setDisabledTextColor(Color.BLACK);
		logArea.setSelectedTextColor(Color.BLACK);
		logArea.setBackground(new Color(240, 240, 240));
		logArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		DefaultCaret logCaret = (DefaultCaret) logArea.getCaret();
		logCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		fileButton.addActionListener(this);
		solveButton.addActionListener(this);
		nextButton.addActionListener(this);

		setLayout();
		arrangePanels();
		setWindowProperties();
	}

	/** Setter stoerrelse og oppfoersel paa vinduet. */
	private void setWindowProperties() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(346, 438));
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/** Setter kanter og layout til vinduet. Jeg bruker BorderLayout rett i JFramen, og velger
	 * dette oppsettet fordi det er veldig enkelt aa jobbe med. */
	private void setLayout() {
		int pad = 8;
		getContentPane().setLayout(new BorderLayout(pad, pad));
		buttonPanel.setLayout(new GridLayout(1, 3, pad, pad));
		buttonPanel.setPreferredSize(new Dimension(0, 50));
		getRootPane().setBorder(BorderFactory.createEmptyBorder(pad, pad, pad, pad));
	}

	/** Setter elementene inn i panelene. */
	private void arrangePanels() {
		buttonPanel.add(fileButton);
		buttonPanel.add(solveButton);
		buttonPanel.add(nextButton);

		add(buttonPanel, BorderLayout.NORTH);
		add(logScroll, BorderLayout.CENTER);
	}
	
	/** Dette er knappelytteren. Jeg bruker getSource()-metoden for aa finne ut av hvilket objekt
	 * (av tre totalt) som ble trykket paa, og kaller metoder deretter. */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// Henter fil fra JFileChooser og foerer kommandoen videre til kontrolleren.
		if (source == fileButton) {
			int returnValue = chooser.showOpenDialog(this);
			
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				filename = file.getName();
				controller.open(file);
			}
		} else if (source == solveButton) {
			controller.solve();

		} else if (source == nextButton) {
			controller.next();
			int counter = Integer.parseInt(counterLabel.getText());
			counterLabel.setText(Integer.toString(++(counter)));
		}
	}
	
	/** Legger til en ny linje i loggen. */
	public void appendLog(String s) {
		logArea.append(s + "\n");
	}

	/** Sletter alt innhold i loggen og setter loesningstelleren til null */
	public void reset() {
		logArea.setText("");
		if (counterLabel != null) counterLabel.setText("0");
	}

	/** Endrer tilstanden til solve-knappen. */
	public void enableSolveButton(boolean enabled) {
		solveButton.setEnabled(enabled);
	}

	/** Endrer tilstanden til next-knappen. */
	public void enableNextButton(boolean enabled) {
		nextButton.setEnabled(enabled);
	}

	/** Klargjoer brettet med de forhaandsfylte numrene. */
	public void drawFilled() {
		Square[][] squares = controller.getSquares();

		size = squares.length;
		boxRows = controller.getBoxRows();
		boxColumns = controller.getBoxColumns();

		int framesize = (360 + (size*16));
		int infoPanelHeight = 22;

		initializeBoard();
		setBoardLayout(framesize, infoPanelHeight);
		setBoardProperties(framesize, infoPanelHeight);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				
				labels[i][j] = new JLabel("", SwingConstants.CENTER);
				labels[i][j].setFont(plainFont);
				
				if (squares[i][j] instanceof FilledSquare) {
					int number = squares[i][j].getNumber();
					labels[i][j].setText(Integer.toString(number, Character.MAX_RADIX).toUpperCase());
					labels[i][j].setFont(boldFont);
					filled[i][j] = true;
					
				} else labels[i][j].setFont(plainFont);

				boardPanel.add(labels[i][j]);
			}
		}

		arrangeBoardPanels();
		drawVisuals();
	}

	/** Initialiserer brettets elementer og paneler. */
	private void initializeBoard() {
		String title = (filename + " - Sudoku Solver");
		int fontsize = (36 - (size / 2));

		filled = new boolean[size][size];
		labels = new JLabel[size][size];
		boardFrame = new JFrame(title);
		boardPanel = new JPanel();
		infoPanel = new JPanel();
		infoLabel = new JLabel("Showing solution");
		counterLabel = new JLabel("0");
		plainFont = new Font("Arial", Font.PLAIN, fontsize);
		boldFont = new Font("Arial", Font.BOLD, fontsize);
	}

	/** Setter opp strukturen til brettet. */
	private void setBoardLayout(int framesize, int infoPanelHeight) {
		boardFrame.setLayout(new BorderLayout());
		boardPanel.setLayout(new GridLayout(size, size));
		infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		boardPanel.setPreferredSize(new Dimension(framesize, framesize));
		infoPanel.setPreferredSize(new Dimension(0, infoPanelHeight));
	}

	/** Setter attributter paa brettet. Stoerrelsen bestemmes av framesize, en variabel som har
	 * et grunntall og skalerer med brettets dimensjoner, og i tillegg legges det til ekstra plass
	 * til infopanelet paa bunn slik at hver rute blir omtrent kvadratisk. */
	private void setBoardProperties(int framesize, int infoPanelHeight) {
		boardFrame.setLocationRelativeTo(null);
		boardFrame.setSize(new Dimension(framesize, framesize + infoPanelHeight));
		boardFrame.setResizable(true);
		boardFrame.setVisible(true);
	}

	/** Setter elementer og paneler paa plass. */
	private void arrangeBoardPanels() {
		infoPanel.add(infoLabel);
		infoPanel.add(counterLabel);
		boardFrame.add(boardPanel, BorderLayout.CENTER);
		boardFrame.add(infoPanel, BorderLayout.SOUTH);
	}

	/** Fargelegger brettet og legger til skiller mellom boksene. Dette er gjort med integer- og
	 * modulomatematikk. Hver rute har kant under seg og til hoyre, slik at den sammenlagte
	 * basiskanten kun er En piksel tykk. Unntaket er hvis ruten ligger helt til hoeyre, da tegnes
	 * ikke en kant til hoeyre for denne. */
	private void drawVisuals() {
		Color filledTileColor = new Color(200, 200, 200);
		Color freeTileColor = Color.WHITE;
		int t = 3; // Boksskille sin tykkelse

		boolean colorFirst = false;
		for (int i = 0; i < size; i++) {
			if (i % boxRows == 0)
				colorFirst = !colorFirst;

			for (int j = 0; j < size; j++) {
				labels[i][j].setOpaque(true);
				int boxIndex = controller.getBoxIndex(i, j);
			
				// Setter boksfarge
				int boxColumnCount = boxIndex % boxRows;
				if (colorFirst) {
					if (boxColumnCount % 2 == 0) labels[i][j].setBackground(filledTileColor);
					else labels[i][j].setBackground(freeTileColor);
				} else {
					if (boxColumnCount % 2 != 0) labels[i][j].setBackground(filledTileColor);
					else labels[i][j].setBackground(freeTileColor);
				}

				// Setter bokskanter
				if ((j % boxColumns == 0) && (boxIndex % boxRows != 0)) {
						drawTileBorder(i, j, 0, t, 1, 1);
					if ((i % boxRows == 0) && (i >= (boxColumns-1)))
						drawTileBorder(i, j, t, t, 1, 1);
				} else if ((i % boxRows == 0) && (i >= (boxColumns-1))) {
						drawTileBorder(i, j, t, 0, 1, 1);
				} else	drawTileBorder(i, j, 0, 0, 1, 1);
			}
		}
	}

	/** Tegner kant paa rute. 
	 * @param i raden til ruten
	 * @param j kolonnen til ruten
	 * @param n, w, s, e tykkelse paa kant over, til venstre, under og til hoeyre for ruten
	 */
	private void drawTileBorder(int i, int j, int n, int w, int s, int e) {
		Color linecolor = Color.BLACK;
		if (j == (size-1)) {
			labels[i][j].setBorder(BorderFactory.createMatteBorder(n, w, s, 0, linecolor));
		} else {
			labels[i][j].setBorder(BorderFactory.createMatteBorder(n, w, s, e, linecolor));
		}
	}

	/** Tegner neste loesning og deaktiverer "next"-knappen 
	 * hvis ingen flere loesninger er tilgjenglige */
	public void drawNext(char[][] chars) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (filled[i][j] == false) {
					labels[i][j].setText(Character.toString(chars[i][j]));
				}
			}
		}

		if (controller.noSolutionsLeft()) {
			nextButton.setEnabled(false);
		}
	}
}
