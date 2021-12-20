import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Stock implements Comparable<Stock> {
	private int currentValue;
	private int worstValue;
	private int bestValue;

	public Stock(int currentValue, int worstValue, int bestValue) {
		this.currentValue = currentValue;
		this.worstValue = worstValue;
		this.bestValue = bestValue;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public int getWorstValue() {
		return worstValue;
	}

	public int getBestValue() {
		return bestValue;
	}

	@Override
	public int compareTo(Stock o) {
		int s1Loss = this.currentValue - this.worstValue;
		int s2Loss = o.currentValue - o.worstValue;
		return s2Loss - s1Loss;
	}
}

public class Stocks {
	int numberOfStocks;
	int budget;
	int maxLoss;
	List<Stock> stockList;
	int[][] previousStockMatrix;
	int[][] currentStockMatrix;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Stocks stocks = new Stocks();
		stocks.run();
	}

	public void run() throws FileNotFoundException, IOException {
		readInput();
		obtainResultList();
		writeOutput();
	}

	private void writeOutput() throws IOException {
		Integer maxProfit = currentStockMatrix[budget][maxLoss];
		File outputFile = new File("stocks.out");
		Writer writer = new FileWriter(outputFile, false);
		writer.write(maxProfit.toString());
		writer.close();
	}

	private void readInput() throws FileNotFoundException {
		File inputFile = new File("stocks.in");
		Scanner sc = new Scanner(inputFile);
		numberOfStocks = sc.nextInt();
		budget = sc.nextInt();
		maxLoss = sc.nextInt();
		stockList = new ArrayList<>();
		for (int i = 0; i < numberOfStocks; i++) {
			stockList.add(new Stock(sc.nextInt(), sc.nextInt(), sc.nextInt()));
		}

		sc.close();
	}

	/**
	 * Copiaza matricea sursa in matricea destinatie
	 * @param source		matricea sursa
	 * @param destination	matricea destinatie
	 */
	private void copyMatrix(int[][] source, int[][] destination) {
		for (int i = 0; i <= budget; i++) {
			for (int j = 0; j <= maxLoss; j++) {
				destination[i][j] = source[i][j];
			}
		}
	}

	private void obtainResultList() {
		previousStockMatrix = new int[budget + 1][maxLoss + 1];
		currentStockMatrix = new int[budget + 1][maxLoss + 1];
		for (Stock stock : stockList) {
			// copiez informatiile din matricea precedenta in matricea curenta
			copyMatrix(previousStockMatrix, currentStockMatrix);
			int stockProfit = stock.getBestValue() - stock.getCurrentValue();
			int stockLoss = stock.getCurrentValue() - stock.getWorstValue();
			for (int budgetCursor = 1; budgetCursor <= budget; budgetCursor++) {
				// daca costul budgetCursor este mai mic decat costul actiunii, atunci oricum
				// nu are loc si il ignor si trec la urmatorul cost
				if (budgetCursor < stock.getCurrentValue()) {
					continue;
				}

				// pentru toate loss-urile costului curent se calculeaza noul profit posibil
				// adaugand la combinatia [cost curent - cost(stock)][loss curent - loss(stock)]
				// de stock-uri, actiunea iteratiei curente
				// daca noul profit e mai mare, se actualizeaza corespunzator combinatia
				// pentru costul curent si loss-ul curent
				int budgetStart = budgetCursor - stock.getCurrentValue();
				for (int lossIndex = stockLoss; lossIndex <= maxLoss; lossIndex++) {
					int startLoss = lossIndex - stockLoss;
					int newProfit = previousStockMatrix[budgetStart][startLoss] + stockProfit;
					if (newProfit > previousStockMatrix[budgetCursor][lossIndex]) {
						currentStockMatrix[budgetCursor][lossIndex] = newProfit;
					}
				}
			}

			// copiez matricea actualizata, dupa luarea in calcul a actiunii de la iteratie 
			// curenta, in matricea pentru iteratiile precedente spre a fi folosita la iteratia
			// urmatoare
			copyMatrix(currentStockMatrix, previousStockMatrix);
		}
	}
}
