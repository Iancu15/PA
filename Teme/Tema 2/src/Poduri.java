import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Poduri {
	int matrixHeight;
	int matrixWidth;
	Pair<Integer, Integer> gigelInitPos;
	char[][] bridgeMatrix;
	int[][] matrix;
	Queue<Pair<Integer, Integer>> queue;
	Integer minimumNumberOfBridges;

	public static void main(String[] args) throws IOException {
		Poduri poduri = new Poduri();
		poduri.run();
	}

	private void run() throws IOException {
		readInput();
		bridges();
		writeOutput();
	}

	/**
	 * Citeste matricea de poduri, o pastrez in forma din fisier.
	 * @throws FileNotFoundException	exceptie pentru scanner
	 */
	private void readInput() throws FileNotFoundException {
		File inputFile = new File("poduri.in");
		Scanner sc = new Scanner(inputFile);
		matrixHeight = sc.nextInt();
		matrixWidth = sc.nextInt();
		gigelInitPos = new Pair<>(sc.nextInt(), sc.nextInt());
		bridgeMatrix = new char[matrixHeight + 1][matrixWidth + 1];
		sc.nextLine();
		for (int i = 1; i <= matrixHeight; i++) {
			String row = sc.nextLine();
			for (int j = 1; j <= matrixWidth; j++) {
				bridgeMatrix[i][j] = row.charAt(j - 1);
			}
		}

		sc.close();
	}

	/**
	 * Parcurge matricea folosind o coada, proceseaza vecinii in functie de tipul podului
	 * pe care se afla gigel la pozitia extrasa din coada. Daca aceasta este un '.',
	 * atunci nu va intra pe niciun if si va fi ignorata.
	 */
	private void bridges() {
		queue = new LinkedList<>();
		queue.add(gigelInitPos);
		matrix = new int[matrixHeight + 1][matrixWidth + 1];
		matrix[gigelInitPos.getX()][gigelInitPos.getY()] = 0;
		minimumNumberOfBridges = Integer.MAX_VALUE;
		while (!queue.isEmpty()) {
			Pair<Integer, Integer> pos = queue.poll();
			char bridge = bridgeMatrix[pos.getX()][pos.getY()];
			if (bridge == 'V') {
				traverseVBridge(pos);
				continue;
			}

			if (bridge == 'O') {
				traverseOBridge(pos);
				continue;
			}

			// un pod dublu este un pod vertical si un pod orizontal
			if (bridge == 'D') {
				traverseVBridge(pos);
				traverseOBridge(pos);
				continue;
			}
		}
	}

	/**
	 * Traverseaza podul in sus si in jos(fiind pod vertical). Daca mergand in oricare din cele 2
	 * directii s-a iesit de pe grid, inseamna ca s-a gasit un drum si actualizez minim-ul. Daca
	 * inca e pe grid, atunci pun la pozitia respectiva din matrice valoarea matricei de start
	 * incrementata si adaug pozitia in coada.
	 * @param pos	pozitia de start de pe care se pleaca in cele 2 directii
	 */
	private void traverseVBridge(Pair<Integer, Integer> pos) {
		int x = pos.getX();
		int y = pos.getY();
		int newMatrixValue = matrix[x][y] + 1;

		// daca cumva drumul curent este deja mai mare decat cel mai mic drum
		// gasit pana acum, atunci nu mai are rost sa il parcurg si il ignor
		if (newMatrixValue >= minimumNumberOfBridges) {
			return;
		}

		if (x + 1 > matrixHeight) {
			updateMinimum(newMatrixValue);
		} else if (matrix[x + 1][y] == 0 && bridgeMatrix[x + 1][y] != '.') {
			matrix[x + 1][y] = newMatrixValue;
			queue.add(new Pair<Integer, Integer>(x + 1, y));
		}

		if (x - 1 < 1) {
			updateMinimum(newMatrixValue);
		} else if (matrix[x - 1][y] == 0 && bridgeMatrix[x - 1][y] != '.') {
			matrix[x - 1][y] = newMatrixValue;
			queue.add(new Pair<Integer, Integer>(x - 1, y));
		}
	}

	/**
	 * Traverseaza podul in dreapta si in stanga(fiind pod orizontal). Daca mergand in oricare din
	 * cele 2 directii s-a iesit de pe grid, inseamna ca s-a gasit un drum si actualizez minim-ul.
	 * Daca inca e pe grid, atunci pun la pozitia respectiva din matrice valoarea matricei de start
	 * incrementata si adaug pozitia in coada.
	 * @param pos	pozitia de start de pe care se pleaca in cele 2 directii
	 */
	private void traverseOBridge(Pair<Integer, Integer> pos) {
		int x = pos.getX();
		int y = pos.getY();
		int newMatrixValue = matrix[x][y] + 1;

		// idem
		if (newMatrixValue >= minimumNumberOfBridges) {
			return;
		}

		if (y + 1 > matrixWidth) {
			updateMinimum(newMatrixValue);
		} else if (matrix[x][y + 1] == 0 && bridgeMatrix[x][y + 1] != '.') {
			matrix[x][y + 1] = newMatrixValue;
			queue.add(new Pair<Integer, Integer>(x, y + 1));
		}

		if (y - 1 < 1) {
			updateMinimum(newMatrixValue);
		} else if (matrix[x][y - 1] == 0 && bridgeMatrix[x][y - 1] != '.') {
			matrix[x][y - 1] = newMatrixValue;
			queue.add(new Pair<Integer, Integer>(x, y - 1));
		}
	}

	/**
	 * Actualizeaza drumul minim daca noul drum gasit este mai mic decat acesta.
	 * @param value		noul drum gasit
	 */
	private void updateMinimum(int value) {
		if (value < minimumNumberOfBridges) {
			minimumNumberOfBridges = value;
		}
	}

	private void writeOutput() throws IOException {
		File outputFile = new File("poduri.out");
		Writer writer = new FileWriter(outputFile, false);

		// daca drumul nu a fost actualizat deloc, inseamna ca nu s-a gasit niciun drum
		// valid, asa ca afisez -1 ca in enunt
		if (minimumNumberOfBridges == Integer.MAX_VALUE) {
			minimumNumberOfBridges = -1;
		}

		writer.write(minimumNumberOfBridges.toString());
		writer.close();
	}
}
