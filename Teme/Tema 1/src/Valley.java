import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Valley {
	int numberOfPeaks;
	List<Integer> peaks;
	long[] suffixHoursArray;
	int[] excavatedSuffix;
	Long minTotalHours;

	public static void main(String[] args) throws IOException {
		Valley valley = new Valley();
		valley.run();
	}

	public void run() throws IOException, FileNotFoundException {
		readInput();
		processSuffix();
		calculateMinHours();
		writeOutput();
	}

	private void writeOutput() throws IOException {
		File outputFile = new File("valley.out");
		Writer writer = new FileWriter(outputFile, false);
		writer.write(minTotalHours.toString());
		writer.close();
	}

	private void readInput() throws FileNotFoundException {
		File inputFile = new File("valley.in");
		Scanner sc = new Scanner(inputFile);
		numberOfPeaks = sc.nextInt();
		peaks = new ArrayList<>();
		for (int i = 0; i < numberOfPeaks; i++) {
			peaks.add(sc.nextInt());
		}

		sc.close();
	}

	private void processSuffix() {
		long suffixHours = 0;
		int biggerPeak = peaks.get(numberOfPeaks - 1);
		// primul vector retine orele cumulate pentru excavari, al doilea tine inaltimea centrului
		// dupa ce a fost sau nu sapat
		suffixHoursArray = new long[numberOfPeaks];
		excavatedSuffix = new int[numberOfPeaks];
		// Pentru toate centrele posibile de la 1 la n - 2 (indexare de la 0), calculez sufixul
		// aferent incluzandu-le si pe ele ca parte din sufix
		for (int suffixPeak = numberOfPeaks - 2; suffixPeak > 0; suffixPeak--) {
			// fac diferenta dintre centrul curent si varful imediat urmator
			// daca diferenta e mai mare decat 0 inseamna ca trebuie sa sap diferenta ca sa fie
			// la acelasi nivel, adaug diferenta la orele acumulate, asa ca biggerPeak ramane
			// acelasi pentru ca varful curent si varful urmator au aceeasi inaltime
			// daca diferenta e mai mica sau egala decat 0, atunci se pastreaza proprietatea
			// sufix-ului asa ca nu trebuie sa sap nimic, biggerPeak va deveni centrul curent
			int difference = peaks.get(suffixPeak) - biggerPeak;
			if (difference > 0) {
				suffixHours += difference;
			} else {
				biggerPeak = peaks.get(suffixPeak);
			}

			// adaug orele cumulate in vector, biggerPeak-ul la finalul iteratiei este varful
			// dupa ce a fost sapat
			suffixHoursArray[suffixPeak] = suffixHours;
			excavatedSuffix[suffixPeak] = biggerPeak;
		}
	}

	private void calculateMinHours() {
		minTotalHours = Long.MAX_VALUE;
		long prefixHours = 0;
		int biggerPeak = peaks.get(0);
		// se trece prin fiecare centru si se calculeaza prefix-ul, apoi cu informatiile deja
		// retinute dupa procesarea sufix-ului se face descernamantul
		for (int middlePeak = 1; middlePeak < numberOfPeaks - 1; middlePeak++) {
			// daca centrul sapat este mai mare decat urmatorul varf, atunci inseamna ca mai
			// trebuie sapat cu diferenta, diferenta este adaugata la cumul
			int difference = excavatedSuffix[middlePeak] - biggerPeak;
			if (difference > 0) {
				prefixHours += difference;
			}

			// se insumeaza orele deja stiute de la sufix cu orele cumulate de la prefix si
			// daca rezultatul e mai mic decat minim-ul calculat pana acum, se actualizeaza
			// minimul
			long totalHours = prefixHours + suffixHoursArray[middlePeak];
			if (totalHours < minTotalHours) {
				minTotalHours = totalHours;
			}

			// e pentru tura urmatoare care o sa aiba nevoie de prefix-ul actualizat fara a
			// se lua in calcul sufix-ul sapat
			if (difference <= 0) {
				// asemeni cu processSufix se face prefix-ul
				int prefixDifference = peaks.get(middlePeak) - biggerPeak;
				if (prefixDifference > 0) {
					prefixHours += prefixDifference;
				} else {
					biggerPeak = peaks.get(middlePeak);
				}
			} else {
				// daca sufixul sapat era mai mare, atunci se cumuleaza pentru runda urmatoare
				// diferenta dintre centru nesapat si centrul sapat
				prefixHours += peaks.get(middlePeak) - excavatedSuffix[middlePeak];
			}
		}
	}
}
