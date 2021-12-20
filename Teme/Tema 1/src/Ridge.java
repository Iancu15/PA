import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

class Peak {
	private int height;
	private int fuelCost;

	public Peak(int height, int fuelCost) {
		this.height = height;
		this.fuelCost = fuelCost;
	}

	public int getHeight() {
		return height;
	}

	public int getFuelCost() {
		return fuelCost;
	}
}

public class Ridge {
	int numberOfPeaks;
	long[] peakHeights;
	long[] peakFuelCosts;
	Long minFuelCost;

	public static void main(String[] args) throws IOException {
		Ridge ridge = new Ridge();
		ridge.run();
	}

	public void run() throws FileNotFoundException, IOException {
		readInput();
		calculateFuelCost();
		writeOutput();
	}

	private void writeOutput() throws IOException {
		File outputFile = new File("ridge.out");
		Writer writer = new FileWriter(outputFile, false);
		writer.write(minFuelCost.toString());
		writer.close();
	}

	private void readInput() throws FileNotFoundException {
		File inputFile = new File("ridge.in");
		Scanner sc = new Scanner(inputFile);
		numberOfPeaks = sc.nextInt();
		peakHeights = new long[numberOfPeaks];
		peakFuelCosts = new long[numberOfPeaks];
		for (int i = 0; i < numberOfPeaks; i++) {
			long height = sc.nextLong();
			peakHeights[i] = height;
			peakFuelCosts[i] = sc.nextLong();
		}

		sc.close();
	}

	private long min(long a, long b) {
		if (a < b) {
			return a;
		}

		return b;
	}

	private void calculateFuelCost() {
		minFuelCost = 0L;
		for (int i = 0; i < numberOfPeaks - 1; i++) {
			if (peakHeights[i] != peakHeights[i + 1]) {
				continue;
			}

			// calculez costul crescator si anume costul spargerii primului duplicat
			int start = i, end, j = i;
			long ascendingCost = peakFuelCosts[i];
			long minDoubleExcavateAscCost = Long.MAX_VALUE;
			int minDoubleExcvAscIndex = -1, minDoubleExcvDescIndex = -1;
			if (i > 0) {
				while (peakHeights[j - 1] == peakHeights[j] - 1) {
					long currentFuelCost = peakFuelCosts[j - 1];
					if (peakHeights[j - 1] == 0) {
						currentFuelCost = Long.MAX_VALUE / 4;
					}

					// se poate sparge sirul crescator prematur daca e mai ieftin sa sapam unul
					// dintre varfuri de 2 ori in loc de tot sirul
					long doubleExcavateCost = 2 * currentFuelCost + ascendingCost;
					if (doubleExcavateCost < minDoubleExcavateAscCost) {
						minDoubleExcavateAscCost = doubleExcavateCost;
						minDoubleExcvAscIndex = j - 1;
					}

					ascendingCost += currentFuelCost;
					j--;
					if (j == 0) {
						break;
					}
				}
			}

			boolean choosedToDoubleExcavateAsc = false, choosedToDoubleExcavateDesc = false;
			if (minDoubleExcavateAscCost <= ascendingCost) {
				choosedToDoubleExcavateAsc = true;
				ascendingCost = minDoubleExcavateAscCost;
			}

			int startOfAscend = j, endOfDescend = -1;
			boolean wasAscendChosen = false, wasDescendChosen = false;
			if (ascendingCost <= 2 * peakFuelCosts[i]) {
				wasAscendChosen = true;
			} else {
				choosedToDoubleExcavateAsc = false;
			}

			// primul duplicat mereu se va afla in cazul 1, asa ca continui cu cazul 2
			// pentru cazul 1 se poate fie sa se mearga pe spargerea sirului crescator, fie sa
			// sape o data primul duplicat
			long case1Cost = min(ascendingCost, 2 * peakFuelCosts[i]);
			long case2Cost = 0;
			int whichCase = 2;
			i++;
			if (i < numberOfPeaks - 1) {
				// cazurile se interschimba din 2 din 2 si se aduna la fiecare pas duplicatul
				// la cazul corespunzator
				while (peakHeights[i] == peakHeights[i + 1]) {
					if (whichCase == 1) {
						case1Cost += peakFuelCosts[i];
						whichCase = 2;
					} else {
						case2Cost += peakFuelCosts[i];
						whichCase = 1;
					}

					i++;
					if (i == numberOfPeaks - 1) {
						break;
					}
				}

				// calculez costul descrescator al ultimului duplicat
				long descendingCost = peakFuelCosts[i];
				int startOfDescend = i;
				end = startOfDescend;
				int descendIndex = i;
				long minDoubleExcavateDescCost = Long.MAX_VALUE;
				if (descendIndex < numberOfPeaks - 1) {
					while (peakHeights[descendIndex + 1] == peakHeights[descendIndex] - 1) {
						long currentFuelCost = peakFuelCosts[descendIndex + 1];
						if (peakHeights[descendIndex + 1] == 0) {
							currentFuelCost = Long.MAX_VALUE / 4;
						}

						// se poate sparge sirul descrescator prematur daca e mai ieftin sa sapam 
						// unul dintre varfuri de 2 ori in loc de tot sirul
						long doubleExcavateCost;
						doubleExcavateCost = 2 * currentFuelCost + descendingCost;
						if (doubleExcavateCost < minDoubleExcavateDescCost) {
							minDoubleExcavateDescCost = doubleExcavateCost;
							minDoubleExcvDescIndex = descendIndex + 1;
						}

						descendingCost += currentFuelCost;
						descendIndex++;
						if (descendIndex == numberOfPeaks - 1) {
							break;
						}
					}
				}

				if (minDoubleExcavateDescCost < descendingCost) {
					choosedToDoubleExcavateDesc = true;
					descendingCost = minDoubleExcavateDescCost;
				}

				endOfDescend = descendIndex;
				if (descendingCost <= 2 * peakFuelCosts[startOfDescend]) {
					wasDescendChosen = true;
				} else {
					choosedToDoubleExcavateDesc = false;
				}

				// dupa procesarea penultimului duplicat, in while-ul de la inceput de if,
				// se schimba cazul pe cel al ultimului duplicat
				long minLastCost = min(descendingCost, 2 * peakFuelCosts[startOfDescend]);
				if (whichCase == 1) {
					case1Cost += minLastCost;
				} else {
					case2Cost += minLastCost;
				}
			} else {
				// daca al doilea duplicat este ultimul element din vector, atunci el va fi
				// bagat in cazul 2
				case2Cost = peakFuelCosts[numberOfPeaks - 1];
				end = numberOfPeaks - 1;
			}

			// cazul mai ieftin este adaugat la minFuelCost si se stocheaza primul duplicat
			// ce urmeaza sa fie sapat din cazul ales
			int firstExcavated;
			if (case1Cost < case2Cost) {
				whichCase = 1;
				firstExcavated = start;
				minFuelCost += case1Cost;
			} else {
				whichCase = 2;
				firstExcavated = start + 1;
				minFuelCost += case2Cost;
			}

			if (start == firstExcavated && wasAscendChosen == false) {
				peakHeights[firstExcavated] -= 2;
				firstExcavated += 2;
			}

			// sap duplicatele din cazul care a fost mai ieftin
			int excavatedIndex = firstExcavated;
			for (; excavatedIndex <= end; excavatedIndex += 2) {
				peakHeights[excavatedIndex]--;
			}

			// sap elementele din sirul crescator daca suntem pe cazul 1
			if (whichCase == 1 && wasAscendChosen) {
				if (choosedToDoubleExcavateAsc) {
					startOfAscend = minDoubleExcvAscIndex + 1;
					peakHeights[minDoubleExcvAscIndex] -= 2;
				}

				for (int ascendPeak = startOfAscend; ascendPeak < start; ascendPeak++) {
					peakHeights[ascendPeak]--;
				}
			}

			// sap elementele din sirul descrescator daca ultimul duplicat apartine cazului ales
			int descendParity = (end - start) % 2;
			if ((whichCase == 1 && descendParity == 0) || (whichCase == 2 && descendParity == 1)) {
				if (choosedToDoubleExcavateDesc) {
					endOfDescend = minDoubleExcvDescIndex - 1;
					peakHeights[minDoubleExcvDescIndex] -= 2;
				}

				if (wasDescendChosen) {
					for (int descendPeak = end + 1; descendPeak <= endOfDescend; descendPeak++) {
						peakHeights[descendPeak]--;
					}
				} else if (end < numberOfPeaks - 1) {
					peakHeights[end]--;
				}
			}
		}
	}
}
