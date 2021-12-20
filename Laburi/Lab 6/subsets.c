#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int* vectordup(int* vector, int size) {
    int* vectordup = (int*) calloc(size, sizeof(int));
    for (int i = 0; i < size; i++) {
        vectordup[i] = vector[i];
    }

    return vectordup;
}

void print_sequence(int* set, int* sequence, int size) {
    int null = 1;
    for (int i = 0; i < size; i++) {
        if (sequence[i]) {
            printf("%d ", set[i]);
            null = 0;
        }
    }

    if (null == 1) {
        printf("multimea vida");
    }

    printf("\n");
}

void generate_subsets(int* set, int* sequence, int size, int index) {
    if (index == size) {
        print_sequence(set, sequence, size);
        return;
    }

    int* element_not_included = vectordup(sequence, size);
    int* element_included = vectordup(sequence, size);
    element_not_included[index] = 0;
    generate_subsets(set, element_not_included, size, index + 1);
    element_included[index] = 1;
    generate_subsets(set, element_included, size, index + 1);
}

int main(int argc, char *argv[]) {
    FILE* file = fopen("multime", "r");
    fseek(file, 0, SEEK_END);
    int fileSize = ftell(file);
    fseek(file, 0, SEEK_SET);
    char *line = malloc(fileSize + 1);
    fread(line, 1, fileSize, file);
    line[fileSize] = 0;
    fclose(file);

    char* token = strtok(line, " ");
    int size = atoi(token);
    int* set = (int*)malloc(sizeof(int) * size);
    for (int i = 0; i < size; i++) {
        token = strtok(NULL, " ");
        set[i] = atoi(token);
    }

    int* sequence = (int*)calloc(size, sizeof(int));
    generate_subsets(set, sequence, size, 0);
    free(set);
    free(sequence);
}