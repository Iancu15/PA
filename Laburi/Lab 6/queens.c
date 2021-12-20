#include <stdlib.h>
#include <stdio.h>

int* vectordup(int* vector, int size) {
    int* vectordup = (int*) calloc(size, sizeof(int));
    for (int i = 0; i < size; i++) {
        vectordup[i] = vector[i];
    }

    return vectordup;
}

void print_solution(int* solution, int size) {
    for (int i = 0; i < size; i++) {
        // +1 ca indexam de la 0 si sa arate mai normal
        // la afisare cu pozitii >= 1
        printf("%d ", solution[i] + 1);
    }

    printf("\n");
}

int generate_queens(int* solution, int size, int index) {
    if (size == index) {
        print_solution(solution, size);
        return 1;
    }

    for (int position = 0; position < size; position++) {
        int is_valid = 1;
        for (int queen_index = 0; queen_index < index; queen_index++) {
            // sa nu fie pe aceeasi linie
            if (position == solution[queen_index]) {
                is_valid = 0;
                break;
            }

            // sa nu fie pe aceeasi diagonala
            if (abs(queen_index - index) == abs(position - solution[queen_index])) {
                is_valid = 0;
                break;
            }
        }

        if (is_valid == 0) {
            continue;
        }

        int* new_solution = vectordup(new_solution, size);
        new_solution[index] = position;
        if (generate_queens(new_solution, size, index + 1) == 1)  {
            return 1;
        }
    }

    return 0;
}

int main(int argc, char* argv[]) {
    int table_size = atoi(argv[1]);
    if (table_size <= 0) {
        printf("Dimensiunea nu este valida!\n");
        exit(-1);
    }

    int* solution = (int*)malloc(table_size * sizeof(int));
    if (generate_queens(solution, table_size, 0) == 0) {
        printf("Nu a fost gasita o solutie!\n");
    }
}