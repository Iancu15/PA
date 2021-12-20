import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

public class Trigigel {
	long length;
	Long numberOfCombinations;
	long [][] matrix;

	public static void main(String[] args) throws IOException {
		Trigigel trigigel = new Trigigel();
		trigigel.run();
	}

	public void run() throws IOException, FileNotFoundException {
		readInput();
		calculateCombinations();
		writeOutput();
	}

	private void writeOutput() throws IOException {
		File outputFile = new File("trigigel.out");
		Writer writer = new FileWriter(outputFile, false);
		writer.write(numberOfCombinations.toString());
		writer.close();
	}

	private void readInput() throws FileNotFoundException {
		File inputFile = new File("trigigel.in");
		Scanner sc = new Scanner(inputFile);
		length = sc.nextLong();
		sc.close();
	}

	/**
	 * aplicarea modulo 1000000007 asupra inmultirii dintre parametrii conform proprietatii
	 * de inmultire de la clase de resturi
	 */
	long modProduct(long a, long b) {
		return ((a % 1000000007) * (b % 1000000007)) % 1000000007;
	}

	/**
	 * aplicarea modulo 1000000007 asupra sumei dintre parametrii conform proprietatii de
	 * adunare de la clase de resturi
	 */
	long modSum(long a, long b) {
		return ((a % 1000000007) + (b % 1000000007)) % 1000000007;
	}

	/**
	 * Inmulteste matricile a si b primite ca parametru, de dimensiune size x size, si intoarce
	 * matricea rezultata
	 */
	long[][] mul(long[][] a, long[][] b, int size) {
		long[][] result = new long[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					result[i][j] = modSum(result[i][j], modProduct(a[i][k], b[k][j]));
				}
			}
		}

		return result;
	}

	/**
	 * Intoarce matricea identitate de dimensiune size x size
	 */
	long[][] identityMatrix(int size) {
		long[][] I = new long[size][size];
		for (int i = 0; i < size; i++) {
			I[i][i] = 1;
		}

		return I;
	}

	/**
	 * Ridica matricea a de dimensiune size x size, primita ca parametru, la puterea n
	 */
	long[][] power(long[][] a, long n, int size) {
		if (n == 0) {
			return identityMatrix(size);
		}

		// Daca n se imparte exact la 2, atunci calculez M^(n/2) si intorc M^(n/2) * M^(n/2)
		// altfel calculez M^((n - 1)/2) si intorc M^((n - 1)/2) * M^((n - 1)/2) * M
		if (n % 2 == 0) {
			long[][] M = power(a, n / 2, size);
			return mul(M, M, size);
		} else {
			long[][] M = power(a, (n - 1) / 2, size);
			return mul(a, mul(M, M, size), size);
		}
	}

	/**
	 * @param a			vectorul de dimensiune 1 x size
	 * @param b			matricea de dimnesiune size x size
	 * @return			rezultatul inmultirii a * b si anume un vector 1 x size
	 */
	long[] mulVectorMatrix(long[] a, long[][] b, int size) {
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				result[i] = modSum(result[i], modProduct(a[j], b[j][i]));
			}
		}

		return result;
	}

	private void calculateCombinations() {
		// creez matricea M din cadrul rezolvarii si calculez M^(N-2)
		long[][] matrix = new long[3][3];
		matrix[1][0] = 1;
		matrix[2][1] = 1;
		matrix[0][2] = 1;
		matrix[2][2] = 1;
		matrix = power(matrix, length - 2, 3);

		// creez G(2) si il inmultesc cu matricea calculata M^(N-2) pentru a face rost de
		// primul termen din suma lui G(N)
		long[] vector = new long[3];
		vector[0] = 0;
		vector[1] = 1;
		vector[2] = 2;
		final long[] firstTerm = mulVectorMatrix(vector, matrix, 3);

		// creez vectorul [0 0 1] si matricea (M - I)^-1
		long[] addOneVector = new long[3];
		addOneVector[2] = 1;
		long[][] inverseMatrix = new long[3][3];
		inverseMatrix[0][1] = 1;
		inverseMatrix[0][2] = 1;
		inverseMatrix[1][2] = 1;
		inverseMatrix[2][0] = 1;
		inverseMatrix[2][1] = 1;
		inverseMatrix[2][2] = 1;

		// scad matricea identitate din M^(N-2) si o inmultesc cu matricea inversa
		// rezultatul il inmultesc cu vectorul [0 0 1] pentru a face rost de al doilea termen
		matrix[0][0]--;
		matrix[1][1]--;
		matrix[2][2]--;
		long[] secondTerm = mulVectorMatrix(addOneVector, mul(matrix, inverseMatrix, 3), 3);

		// calculez ultimii 3 termeni si apoi suma acestora va fi numarul total de combinatii
		long firstBitComb = modSum(firstTerm[2], secondTerm[2]);
		long secondBitComb = modSum(firstTerm[1], secondTerm[1]);
		long thirdBitComb = modSum(firstTerm[0], secondTerm[0]);
		numberOfCombinations = modSum(firstBitComb, modSum(secondBitComb, thirdBitComb));
	}
}