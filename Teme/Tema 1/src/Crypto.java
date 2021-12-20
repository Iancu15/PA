import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Computer implements Comparable<Computer> {
	private int bitcoinCapacity;
	private int upgradeCost;

	public Computer(int bitcoinCapacity, int upgradeCost) {
		this.bitcoinCapacity = bitcoinCapacity;
		this.upgradeCost = upgradeCost;
	}

	public int getBitcoinCapacity() {
		return bitcoinCapacity;
	}

	public int getUpgradeCost() {
		return upgradeCost;
	}

	// folosita de Collections.sort sa ordoneze crescator
	// dupa capacitate
	@Override
	public int compareTo(Computer o) {
		return this.bitcoinCapacity - o.bitcoinCapacity;
	}
}

public class Crypto {
	int numberOfComp;
	int budget;
	List<Computer> computerList;
	Integer maxBitcoinCapacity;

	public static void main(String[] args) throws IOException {
		Crypto crypto = new Crypto();
		crypto.run();
	}

	public void run() throws FileNotFoundException, IOException {
		readInput();
		Collections.sort(computerList);
		calculateCapacity();
		writeOutput();
	}


	private void writeOutput() throws IOException {
		File outputFile = new File("crypto.out");
		Writer writer = new FileWriter(outputFile, false);
		writer.write(maxBitcoinCapacity.toString());
		writer.close();
	}

	private void readInput() throws FileNotFoundException {
		File inputFile = new File("crypto.in");
		Scanner sc = new Scanner(inputFile);
		numberOfComp = sc.nextInt();
		budget = sc.nextInt();
		computerList = new ArrayList<>();
		for (int i = 0; i < numberOfComp; i++) {
			computerList.add(new Computer(sc.nextInt(), sc.nextInt()));
		}

		sc.close();
	}

	/**
	 * Cautarea binara cauta de cate ori pot fi upgradate toate calculatorele precedente astfel
	 * incat sa nu se depaseasca bugetul, intoarce factorul cautat
	 * @param totalUpgradeCosts			Cat s-a cheltuit pana in acest moment
	 * @param capacityDiff				Diferenta ce n-a putut fi completata in intregime
	 * @param currentUpgradeCosts		Costul upgradarii tuturor calculatorele precedente +
	 * 								 	calculatorul curent
	 */
	private int binarySearch(long totalUpgradeCosts, int capacityDiff, int currentUpgradeCosts) {
		int left = 0;
		int right = capacityDiff;
		while (left <= right) {
			int mid = (left + right) / 2;
			if (totalUpgradeCosts + (long) currentUpgradeCosts * mid > budget) {
				right = mid - 1;
				continue;
			}

			if (totalUpgradeCosts + currentUpgradeCosts <= budget) {
				left = mid + 1;
				continue;
			}

			return mid; 
		}

		return (left + right) / 2;
	}

	private void calculateCapacity() {
		int currentUpgradeCosts = 0;
		long totalUpgradeCosts = 0;
		maxBitcoinCapacity = 0;
		int computerIndex;
		for (computerIndex = 0; computerIndex < numberOfComp - 1; computerIndex++) {
			Computer computer = computerList.get(computerIndex);
			currentUpgradeCosts += computer.getUpgradeCost();
			Computer nextComp = computerList.get(computerIndex + 1);
			int capacityDiff = nextComp.getBitcoinCapacity() - computer.getBitcoinCapacity();
			if (capacityDiff > 0) { 
				// daca toate calculatorele precedente + calculatorul curent pot fi upgradate
				// la capacitatea calculatorului urmator, atunci se face asta, se actualizeaza
				// costul total pana in acest moment si se trece la urmatorul calculator
				long totalCurrentUpgradeCosts = (long) currentUpgradeCosts * capacityDiff;
				if (totalUpgradeCosts + totalCurrentUpgradeCosts <= budget) {
					totalUpgradeCosts += totalCurrentUpgradeCosts;
					continue;
				}

				// daca nu, atunci se face o cautare binara intre 0 si diferenta capacitatii
				// pentru a cauta exact si eficient de cate ori pot fi upgradate toate
				// calculatorele precedente fara sa se depaseasca bugetul
				int exactDiff = binarySearch(totalUpgradeCosts, capacityDiff, currentUpgradeCosts);
				maxBitcoinCapacity = computer.getBitcoinCapacity() + exactDiff;
				break;
			}
		}

		// daca s-a ajuns la final inseamna ca toate calculatorele sunt upgradate la capacitatea
		// ultimului calculator, atunci capacitatea maxima va fi capacitatea ultimului calculator
		// + floor-ul impartirii bugetului ramas la costul upgradarii tutoror calculatorelor
		if (computerIndex == numberOfComp - 1) {
			maxBitcoinCapacity = computerList.get(numberOfComp - 1).getBitcoinCapacity();
			currentUpgradeCosts += computerList.get(numberOfComp - 1).getUpgradeCost();
			long leftExtraBudget = budget - totalUpgradeCosts;
			maxBitcoinCapacity += (int) Math.floorDiv(leftExtraBudget, currentUpgradeCosts);
		}
	}
}
