// Har subklasser som inneholder en mengde med soekbare tall.
abstract class Container {
	int[] numbers;

	Container(int size) {
		numbers = new int[size];
	}

	/** Sjekker om mengden inneholder et gitt tall. */
	public boolean contains(int number) {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == number) return true;
		} return false;
	}
	
	/** Legger til et nummer i denne beholderen. */
	public void addNumber(int number) {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == 0) {
				numbers[i] = number;
				return;
			}
		}
	}
	
	/** Fjerner et gitt nummer fra denne beholderen. */
	public void removeNumber(int number) {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == number) {
				numbers[i] = 0;
				return;
			}
		}
	}
}

class Box extends Container {

	Box(int size) {
		super(size);
	}
}

class Column extends Container {

	Column(int size) {
		super(size);
	}
}

class Row extends Container {

	Row(int size) {
		super(size);
	}
}
