import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Lego {
	int maximumDimension;
	int numberOfLegoParts;
	int maximumNumberOfLegoPart;
	Integer biggestNumberOfConsComb;
	List<Integer> resultedDimensions;

	public static void main(String[] args) throws IOException {
		Lego lego = new Lego();
		lego.run();
	}

	private void run() throws IOException {
		readInput();
		List<Integer> dimensions = new ArrayList<>();

		// in enunt se precizeaza ca dimensiunea 1 ar trebuie sa fie in rezultat
		// asa ca o adaug implicit
		dimensions.add(1);
		biggestNumberOfConsComb = 0;
		legoBacktracking(dimensions, 2);
		writeOutput();
	}

	private void readInput() throws FileNotFoundException {
		File inputFile = new File("lego.in");
		Scanner sc = new Scanner(inputFile);
		maximumDimension = sc.nextInt();
		numberOfLegoParts = sc.nextInt();
		maximumNumberOfLegoPart = sc.nextInt();

		sc.close();
	}

	/**
	 * Produce prin tehnica backtracking toate posibilitatile de dimensiuni.
	 * @param dimensions	multimea curenta de dimensiuni
	 * @param dim			dimensiunea curenta a posibilitatii curente
	 */
	private void legoBacktracking(List<Integer> dimensions, int dim) {
		// daca s-a ajuns la final inseamna ca aceasta este o solutie posibila
		// actualizez lista rezultata de dimensiuni luand in calcul solutia curenta
		if (dimensions.size() == numberOfLegoParts) {
			updateDimList(dimensions);
			return;
		}

		// ignor posibilitatile pentru care in multime fac parte dimensiuni
		// mai mari decat maximul setat de problema
		if (dim > maximumDimension) {
			return;
		}

		// bifurc posibilitatea in cele 2 cazuri si anume dimensiunea curenta este
		// adaugata sau dimensiunea curenta este ignorata
		List<Integer> addDim = new ArrayList<>(dimensions);
		addDim.add(dim);
		legoBacktracking(addDim, dim + 1);
		List<Integer> dontAddDim = new ArrayList<>(dimensions);
		legoBacktracking(dontAddDim, dim + 1);
	}

	/**
	 * Actualizeaza lista rezultata de dimensiuni cu solutia nou gasita daca numarul de
	 * elemente consecutive formate din suma de dimensiuni al acesteia este mai mare.
	 * @param dimensions	solutia nou gasita de dimensiuni
	 */
	private void updateDimList(List<Integer> dimensions) {
		int numberOfConsComb = calculateNumberOfComb(dimensions);
		if (numberOfConsComb > biggestNumberOfConsComb) {
			biggestNumberOfConsComb = numberOfConsComb;
			resultedDimensions = dimensions;
		}

		// daca au acelasi numar de elemente consecutive, atunci o actualizez cu solutia gasita
		// daca aceasta este mai mica lexicografic
		if (numberOfConsComb == biggestNumberOfConsComb) {
			int i = 1;
			// parcurg cele 2 liste pana se gaseste o pozitie pentru care cele 2 dimensiuni
			// omoloage nu sunt egale
			while (dimensions.get(i) == resultedDimensions.get(i)) {
				i++;
				if (i == numberOfLegoParts) {
					return;
				}
			}

			// daca in pozitia diferita dimensiunea solutiei este mai mica decat dimensiunea
			// rezultatului, atunci este mai mica lexicografic si actualizez rezultatul
			if (dimensions.get(i) < resultedDimensions.get(i)) {
				resultedDimensions = dimensions;
			}
		}
	}

	/**
	 * Calculeaza numarul de combinatii de adunari intre dimensiuni in limita maximului de parti
	 * din enunt.
	 * @param dimensions	multimea de dimensiuni
	 * @return				numarul de combinatii consecutive
	 */
	private int calculateNumberOfComb(List<Integer> dimensions) {
		int maxDim = getMaxDim(dimensions);
		dimensions = multiplicateList(dimensions);
		int size = dimensions.size();
		List<List<Integer>> combMatrix = new ArrayList<>();
		// umplu coloana pentru numarul 0 cu 0-uri
		for (int i = 0; i <= size; i++) {
			List<Integer> sumList = new ArrayList<>();
			sumList.add(0);
			combMatrix.add(sumList);
		}

		// ultimul numar este dim_max * T
		int numberOfComb = maxDim * maximumNumberOfLegoPart;
		for (int currentDimComb = 1; currentDimComb <= numberOfComb; currentDimComb++) {
			// adaug -1 pentru prima linie cu elementul neutru de dimensiune 0
			combMatrix.get(0).add(-1);

			// pentru fiecare dimensiune aplic relatia de recurenta pentru a umple celula
			// pentru noul numar(noua coloana)
			for (int dimIndex = 1; dimIndex <= size; dimIndex++) {
				int previousComb = combMatrix.get(dimIndex - 1).get(currentDimComb);
				int currComb = previousComb;
				if (dimensions.get(dimIndex - 1) <= currentDimComb) {
					int startSum = currentDimComb - dimensions.get(dimIndex - 1);
					int startComb = combMatrix.get(dimIndex - 1).get(startSum);

					// daca cumva combinatia de start este -1 (nu s-a gasit solutie) sau deja e
					// o solutie completa(este egala cu T), atunci se va folosi valoarea de
					// la linia precedenta, altfel(daca se intra pe if), atunci se adauga
					// dimensiunea liniei curente la combinatia start incrementand numarul de
					// dimensiuni
					if (startComb != - 1 && startComb < maximumNumberOfLegoPart) {
						currComb = startComb + 1;
					}
				}

				// adaug celula calculata
				combMatrix.get(dimIndex).add(currComb);
			}
		}

		return calculateNumberOfConsComb(combMatrix, numberOfComb);
	}

	/**
	 * Cauta in multimea de dimensiuni dimensiunea cea mai mare.
	 * @param dimensions	multimea de dimensiuni
	 * @return				dimensiunea cea mai mare
	 */
	private int getMaxDim(List<Integer> dimensions) {
		int maxDim = 0;
		for (Integer dim : dimensions) {
			maxDim = max(maxDim, dim);
		}

		return maxDim;
	}

	/**
	 * Creeaza o lista de dimensiuni in care fiecare element din multimea de dimensiuni apare
	 * de numarul maxim de parti de lego, care pot participa la o combinatie, ori.
	 * @param dimensions	multimea de dimensiuni
	 * @return				lista de dimensiuni multiplicata
	 */
	private List<Integer> multiplicateList(List<Integer> dimensions) {
		List<Integer> replicatedList = new ArrayList<>();
		for (Integer dim : dimensions) {
			List<Integer> replicatedDim = Collections.nCopies(maximumNumberOfLegoPart, dim);
			replicatedList.addAll(replicatedDim);
		}

		return replicatedList;
	}

	/**
	 * Afla secventa consecutiva de lungime maxima.
	 * @param combMatrix		matricea calculata
	 * @param numberOfComb		numarul de numere
	 * @return					lungimea secventei gasite
	 */
	private int calculateNumberOfConsComb(List<List<Integer>> combMatrix, int numberOfComb) {
		// in numberOfConsComb retin lungimea secventei consecutive curente, se reseteaza cand
		// s-a terminat secventa(numarul consecutiv nu se poate crea) si se foloseste pentru
		// secventa urmatoare
		// in numberOfMaxConsComb retin secventa de lungime maxima
		int numberOfMaxConsComb = 0;
		int numberOfConsComb = 0;
		int numberOfRows = combMatrix.size();
		for (int combIndex = 1; combIndex <= numberOfComb; combIndex++) {
			if (combMatrix.get(numberOfRows - 1).get(combIndex) != -1) {
				numberOfConsComb++;
				continue;
			}

			numberOfMaxConsComb = max(numberOfMaxConsComb, numberOfConsComb);
			numberOfConsComb = 0;
		}

		numberOfMaxConsComb = max(numberOfMaxConsComb, numberOfConsComb);
		return numberOfMaxConsComb;
	}
	
	private int max(int a, int b) {
		if (a > b) {
			return a;
		}

		return b;
	}

	private void writeOutput() throws IOException {
		File outputFile = new File("lego.out");
		Writer writer = new FileWriter(outputFile, true);
		writer.write(biggestNumberOfConsComb.toString() + "\n");
		for (Integer dim : resultedDimensions) {
			writer.write(dim.toString() + " ");
		}

		writer.close();
	}
}