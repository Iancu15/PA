#include <stdlib.h>
#include <stdio.h>

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

int cmpfunc (const void * a, const void * b) {
   return ( (*(Edge*)a).cost - (*(Edge*)b).cost );
}

EdgeVector* create_edge_vector() {
    EdgeVector* edge_vector = (EdgeVector*) malloc(sizeof(EdgeVector));
    edge_vector->number_of_edges = 0;
    edge_vector->capacity = 10;
    edge_vector->edges = (Edge*) malloc(sizeof(Edge) * edge_vector->capacity);
    return edge_vector;
}

void print_tree(EdgeVector* tree_edges) {
    printf("\nTree's edges:\n");
    for (int edge_index = 0; edge_index < tree_edges->number_of_edges; edge_index++) {
        Edge edge = tree_edges->edges[edge_index];
        printf("(%d, %d)\n", edge.u, edge.v);
    }
}

void print_colors(int* color, int size) {
    for (int i = 1; i <= size; i++) {
        printf("%d ", color[i]);
    }

    printf("\n");
}

int main() {
    unsigned int number_of_nodes = 6;
    EdgeVector* graph_edges = create_edge_vector();
    int* color = (int*) malloc(sizeof(int) * (number_of_nodes + 1));
    add_edge(graph_edges, 4, 6, 2);
    add_edge(graph_edges, 2, 4, 3);
    add_edge(graph_edges, 2, 6, 5);
    add_edge(graph_edges, 3, 5, 8);
    add_edge(graph_edges, 3, 6, 9);
    add_edge(graph_edges, 2, 5, 10);
    add_edge(graph_edges, 1, 3, 11);
    add_edge(graph_edges, 1, 2, 15);
    add_edge(graph_edges, 5, 6, 20);

    // le-am luat deja sortate, dar pentru generalitate am zis sa fac si sortarea
    qsort(graph_edges->edges, graph_edges->number_of_edges, sizeof(Edge), cmpfunc);
    for (int i = 1; i <= number_of_nodes; i++) {
        color[i] = i;
    }

    EdgeVector* tree_edges = create_edge_vector();
    printf("Colors each iteration:\n");
    for (int edge_index = 0; edge_index < graph_edges->number_of_edges; edge_index++) {
        print_colors(color, number_of_nodes);
        Edge edge = graph_edges->edges[edge_index];
        if (color[edge.u] != color[edge.v]) {
            add_edge(tree_edges, edge.u, edge.v, edge.cost);

            int c = color[edge.u];
            for (int i = 1; i <= number_of_nodes; i++) {
                if (color[i] == c) {
                    color[i] = color[edge.v];
                }
            }
        }
    }

    print_tree(tree_edges);
}