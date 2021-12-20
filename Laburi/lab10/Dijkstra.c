#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
int queue_not_empty(int* pq, int number_of_nodes) {
    for (int i = 0; i < number_of_nodes; i++) {
        if (pq[i] == 1) {
            return 1;
        }
    }

    return 0;
}

int extract_min(int* pq, int* d, int number_of_nodes) {
    int min_value = INT_MAX;
    int min_node;
    for (int i = 0; i < number_of_nodes; i++) {
        if (d[i] < min_value && pq[i] == 1) {
            min_value = d[i];
            min_node = i;
        }
    }

    return min_node;
}

void print_result(int* d, int number_of_nodes) {
    for (int i = 0; i < number_of_nodes; i++) {
        printf("A -> %c: %d\n", i + 65, d[i]);
    }
}

void print_queue(int* pq, int number_of_nodes) {
    for (int i = 0; i < number_of_nodes; i++) {
        printf("%c: %d ", i + 65, pq[i]);
    }

    printf("\n");
}

int main() {
    int number_of_nodes = 5;
    int* d = (int*) malloc(sizeof(int) * number_of_nodes);
    int* pq = (int*) malloc(sizeof(int) * number_of_nodes);
    int* visisted = (int*) malloc(sizeof(int) * number_of_nodes);
    int** adj_matrix = (int**) malloc(sizeof(int*) * number_of_nodes);
    for (int i = 0; i < number_of_nodes; i++) {
        adj_matrix[i] = (int*) malloc(sizeof(int) * number_of_nodes);
        for (int j = 0; j < number_of_nodes; j++) {
            adj_matrix[i][j] = -1;
        }

        d[i] = INT_MAX;
        pq[i] = 0;
        visisted[i] = 0;
    }

    // Precizare: 0 este A, 1 este B, 2 este C, 3 este D, 4 este E
    adj_matrix[0][1] = 10;
    adj_matrix[0][4] = 5;
    adj_matrix[1][2] = 1;
    adj_matrix[1][4] = 2;
    adj_matrix[2][0] = 3;
    adj_matrix[2][3] = 4;
    adj_matrix[3][0] = 7;
    adj_matrix[3][2] = 6;
    adj_matrix[4][1] = 3;
    adj_matrix[4][2] = 9;
    adj_matrix[4][3] = 2;
    d[0] = 0;
    pq[0] = 1;

    while (queue_not_empty(pq, number_of_nodes)) {
        int node = extract_min(pq, d, number_of_nodes);
        visisted[node] = 1;
        pq[node] = 0;
        for (int neigh = 0; neigh < number_of_nodes; neigh++) {
            int edge = adj_matrix[node][neigh];
            if (!visisted[neigh] && edge != -1 && (d[neigh] > d[node] + edge)) {
                d[neigh] = d[node] + edge;
                pq[neigh] = 1;
            }
        }
    }

    print_result(d, number_of_nodes);
}