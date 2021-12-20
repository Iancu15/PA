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

typedef struct edge {
    int u;
    int v;
    int cost;
} Edge;

typedef struct EdgeVector {
    unsigned int number_of_edges;
    unsigned int capacity;
    Edge* edges;
} EdgeVector;

void add_edge(EdgeVector* edges, int u, int v, int cost) {
    edges->edges[edges->number_of_edges].u = u;
    edges->edges[edges->number_of_edges].v = v;
    edges->edges[edges->number_of_edges].cost = cost;
    edges->number_of_edges++;
    if (edges->number_of_edges == edges->capacity) {
        edges->capacity *= 2;
        edges->edges = realloc(edges->edges, edges->capacity * sizeof(Edge));
    }
}

void remove_edge(EdgeVector* edges, int u, int v) {
    if (edges->number_of_edges == 0) {
        return;
    }

    for (int i = 0; i < edges->number_of_edges; i++) {
        if (edges->edges[i].u == u && edges->edges[i].v == v) {
            if (i != edges->number_of_edges - 1) {
                edges->edges[i] = edges->edges[edges->number_of_edges - 1];
            }

            edges->number_of_edges--;
        }
    }
}

EdgeVector* create_edge_vector() {
    EdgeVector* edge_vector = (EdgeVector*) malloc(sizeof(EdgeVector));
    edge_vector->number_of_edges = 0;
    edge_vector->capacity = 10;
    edge_vector->edges = (Edge*) malloc(sizeof(Edge) * edge_vector->capacity);
    return edge_vector;
}

void print_tree(EdgeVector* tree_edges) {
    printf("Tree's edges:\n");
    for (int edge_index = 0; edge_index < tree_edges->number_of_edges; edge_index++) {
        Edge edge = tree_edges->edges[edge_index];
        printf("(%c, %c)\n", edge.u + 97, edge.v + 97);
    }
}

int main() {
    int number_of_nodes = 7;
    int* d = (int*) malloc(sizeof(int) * number_of_nodes);
    int* pq = (int*) malloc(sizeof(int) * number_of_nodes);
    int* p = (int*) malloc(sizeof(int) * number_of_nodes);
    int* visisted = (int*) malloc(sizeof(int) * number_of_nodes);
    int** adj_matrix = (int**) malloc(sizeof(int*) * number_of_nodes);
    for (int i = 0; i < number_of_nodes; i++) {
        adj_matrix[i] = (int*) malloc(sizeof(int) * number_of_nodes);
        for (int j = 0; j < number_of_nodes; j++) {
            adj_matrix[i][j] = -1;
        }

        d[i] = INT_MAX;
        pq[i] = 0;
        p[i] = -1;
        visisted[i] = 0;
    }

    // Precizare: mapare a..g -> 0..6 
    adj_matrix[0][1] = 7;
    adj_matrix[0][3] = 5;
    adj_matrix[1][0] = 7;
    adj_matrix[1][3] = 9;
    adj_matrix[1][2] = 8;
    adj_matrix[1][4] = 7;
    adj_matrix[2][1] = 8;
    adj_matrix[2][4] = 5;
    adj_matrix[3][0] = 5;
    adj_matrix[3][1] = 9;
    adj_matrix[3][4] = 15;
    adj_matrix[3][5] = 6;
    adj_matrix[4][1] = 7;
    adj_matrix[4][2] = 5;
    adj_matrix[4][3] = 15;
    adj_matrix[4][5] = 8;
    adj_matrix[4][6] = 9;
    adj_matrix[5][3] = 6;
    adj_matrix[5][4] = 8;
    adj_matrix[5][6] = 11;
    adj_matrix[6][4] = 9;
    adj_matrix[6][5] = 11;

    // se incepe din nodul d -> 3 (l-am pus pe sine parinte pentru a nu
    // da seg fault cand apelez adj_matrix[3, p[3]])
    d[3] = 0;
    pq[3] = 1;
    p[3] = 3;
    EdgeVector* tree_edges = create_edge_vector();
    while (queue_not_empty(pq, number_of_nodes)) {
        int node = extract_min(pq, d, number_of_nodes);
        visisted[node] = 1;
        pq[node] = 0;
        add_edge(tree_edges, node, p[node], adj_matrix[node][p[node]]);
        for (int neigh = 0; neigh < number_of_nodes; neigh++) {
            int edge = adj_matrix[node][neigh];
            if (!visisted[neigh] && edge != -1 && (d[neigh] > edge)) {
                d[neigh] = edge;
                pq[neigh] = 1;
                p[neigh] = node;
            }
        }
    }

    remove_edge(tree_edges, 3, 3);
    print_tree(tree_edges);
}