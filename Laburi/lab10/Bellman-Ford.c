#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
void print_result(int* d, int number_of_nodes) {
    for (int i = 0; i < number_of_nodes; i++) {
        char character = i + 64;
        if (i == 0) {
            character = 'S';
        }

        printf("S -> %c: %d\n", character, d[i]);
    }
}

int main() {
    int number_of_nodes = 5;
    int* d = (int*) malloc(sizeof(int) * number_of_nodes);
    int** adj_matrix = (int**) malloc(sizeof(int*) * number_of_nodes);
    for (int i = 0; i < number_of_nodes; i++) {
        adj_matrix[i] = (int*) malloc(sizeof(int) * number_of_nodes);
        for (int j = 0; j < number_of_nodes; j++) {
            adj_matrix[i][j] = INT_MIN;
        }

        d[i] = INT_MAX;
    }

    // Precizare: 0 este S, 1 este A, 2 este B, 3 este C, 4 este D
    adj_matrix[0][1] = 7;
    adj_matrix[0][4] = 6;
    adj_matrix[1][2] = 9;
    adj_matrix[1][3] = -3;
    adj_matrix[2][0] = 2;
    adj_matrix[2][3] = 7;
    adj_matrix[3][4] = -2;
    adj_matrix[4][2] = -4;
    adj_matrix[4][1] = 8;
    adj_matrix[4][3] = 5;
    d[0] = 0;

    for (int iter = 1; iter < number_of_nodes; iter++) {
        for (int u = 0; u < number_of_nodes; u++) {
            for (int v = 0; v < number_of_nodes; v++) {
                int edge = adj_matrix[u][v];
                if (edge != INT_MIN && (d[v] > d[u] + edge)) {
                    d[v] = d[u] + edge;
                }
            }
        }
    }

    for (int iter = 1; iter < number_of_nodes; iter++) {
        for (int u = 0; u < number_of_nodes; u++) {
            for (int v = 0; v < number_of_nodes; v++) {
                int edge = adj_matrix[u][v];
                if (edge != INT_MIN && (d[v] > d[u] + edge)) {
                    printf("Am gasit un ciclu negativ!\n");
                    return 0;
                }
            }
        }
    }

    print_result(d, number_of_nodes);
}