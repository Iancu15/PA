#include<stdlib.h>
#include<stdio.h>
#include<string.h>

int paritySum(int* numbers, int size) {
    int* pare = malloc(sizeof(int) * size);
    int* impare = malloc(sizeof(int) * size);
    
    if (numbers[0] % 2 == 0) {
        pare[0] = 1;
        impare[0] = 0;
    } else {
        pare[0] = 0;
        impare[0] = 1;
    }

    for (int i = 1; i < size; i++) {
        if (numbers[i] % 2 == 0) {
            // pentru fiecare subsir par anterior
            // se mai poate forma unul cu noul numar par
            // de asemenea mai se poate forma un subsir doar cu el singur
            pare[i] = 2 * pare[i - 1] + 1;

            // cum par + impar -> impar, atunci pentru
            // fiecare subsir impar anterior se poate adauga noul
            // numar par pentru a forma la fel de multe subsiruri impare
            impare[i] = 2 * impare[i - 1];
        } else {
            // impar + impar -> par, asa ca se poate adauga numarul
            // impar la toate subsirurile impare si se vor face la fel
            // de multe siruri pare
            pare[i] = pare[i - 1] + impare[i - 1];

            // par + impar -> impar, asa ca pentru toate subsirurile
            // pare daca adaugam numarul impar se vor face siruri impare
            // se poate forma si un sir doar cu el singur
            impare[i] = impare[i - 1] + pare[i - 1] + 1;
        }
    }

    int result = pare[size - 1];
    free(pare);
    free(impare);
    return result;
}

int threeSum(int* numbers, int size) {
    int** D = (int**)malloc(sizeof(int*) * 3);
    for (int i = 0; i < 3; i++) {
        D[i] = (int*)malloc(sizeof(int) * size);
    }

    if (numbers[0] % 3 == 0) {
        D[0][0] = 1;
        D[1][0] = 0;
        D[2][0] = 0;
    } else if (numbers[0] % 3 == 1) {
        D[0][0] = 0;
        D[1][0] = 1;
        D[2][0] = 0;
    } else {
        D[0][0] = 0;
        D[1][0] = 0;
        D[2][0] = 1;
    }

    for (int i = 1; i < size; i++) {
        if (numbers[i] % 3 == 0) {
            D[0][i] = 1 + 2 * D[0][i - 1];
            D[1][i] = 2 * D[1][i - 1];
            D[2][i] = 2 * D[2][i - 1];
        } else if (numbers[i] % 3 == 1) {
            D[0][i] = D[0][i - 1] + D[2][i - 1];
            D[1][i] = 1 + D[0][i - 1] + D[1][i - 1];
            D[2][i] = D[2][i - 1] + D[1][i - 1];
        } else {
            D[0][i] = D[0][i - 1] + D[1][i - 1];
            D[1][i] = D[2][i - 1] + D[1][i - 1];
            D[2][i] = 1 + D[2][i - 1] + D[0][i - 1]; 
        }
    }

    int result = D[0][size - 1];
    for (int i = 0; i < 3; i++) {
        free(D[i]);
    }
    free(D);
    return result;
}

/**
 * In fisier formatul e dimensiunea sirului de numere
 * urmat de un spatiu si apoi urmat de sirul de numere
 * despartit si el de spatii
 */
int main(int argc, char *argv[]) {
    FILE* file = fopen("sir_numere", "r");
    fseek(file, 0, SEEK_END);
    int fileSize = ftell(file);
    fseek(file, 0, SEEK_SET);
    char *line = malloc(fileSize + 1);
    fread(line, 1, fileSize, file);
    line[fileSize] = 0;
    fclose(file);

    char* token = strtok(line, " ");
    int size = atoi(token);
    int* numbers = (int*)malloc(sizeof(int) * size);
    for (int i = 0; i < size; i++) {
        token = strtok(NULL, " ");
        numbers[i] = atoi(token);
    }

    int evenSum = paritySum(numbers, size);
    int dividedby3Sum = threeSum(numbers, size);
    printf("Suma para: %d\n", evenSum);
    printf("Suma divizibila cu 3: %d\n", dividedby3Sum);
    free(numbers);
}