/*
*	Created by Nan Mihai on 04.04.2020
*	Copyright (c) 2020 Nan Mihai. All rights reserved.
*	Laborator - Proiectarea algoritmilor (Grafuri)
*	Facultatea de Automatica si Calculatoare
*	Anul Universitar 2019-2020, Seria CD
*/
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "graph.h"

void explore(Graph g, int u, int* time);

void DFS_iterative(Graph g, int start) {
	Stack s = initStack(start);
	int* visited = (int*)calloc(g->V, sizeof(int));
	while (!isEmptyStack(s)) {
		int u = top(s);
		s = pop(s);
		if (visited[u] == 0) {
			printf("%d ", u);
			visited[u] = 1;
			List l;
			for (l = g->adjLists[u]; l != NULL; l = l->next) {
				if (visited[l->data.v] == 0) {
					s = push(s, l->data.v);
				}
			}
		}
	}

	freeStack(s);
	free(visited);
	printf("\n");
}

void DFS_recursive(Graph g) {
	int time = 0;
	for (int i = 0; i < g->V; i++) {
		if (g->visited[i] == 0) {
			explore(g, i, &time);
		}
	}

	printf("\n");
}

void explore(Graph g, int u, int* time) {
	(*time)++;
	g->start[u] = (*time);
	g->visited[u] = 1;
	List l;
	printf("%d ", u);
	for (l = g->adjLists[u]; l != NULL; l = l->next) {
		if (g->visited[l->data.v] == 0) {
			explore(g, l->data.v, time);
		}
	}

	(*time)++;
	g->end[u] = (*time);
}

/**
 * La functia iterativa se foloseste un stack si astfel merge recursiv
 * pe finalul listei, functia recursiva ia nodurile din
 * lista la rand asa ca merge recursiv pe inceputul listei
 * Acest lucru se observa comparand parcurgerea recursiva cu
 * parcurgerea iterativa cu startul 0
 */
void DFS_all_graph(Graph g) {
	printf("DFS iterativ:\n");
	for (int i = 0; i < g->V; i++) {
		DFS_iterative(g, i);
	}

	free(g->visited);
	g->visited = (int*)calloc(g->V, sizeof(int));
	printf("DFS recursiv:\n");
	DFS_recursive(g);
	printf("\n");
}

void BFS(Graph g, int start) {
	printf("BFS parcurgere:\n");
	int* visited = (int*)calloc(g->V, sizeof(int));
	int* dist = (int*)calloc(g->V, sizeof(int));
	int* p = (int*)calloc(g->V, sizeof(int));
	visited[start] = 1;
	dist[start] = 0;
	Queue q = initQueue(start);
	while (!isEmptyQueue(q)) {
		int u = first(q);
		q = dequeue(q);
		printf("%d ", u);
		List l;
		for (l = g->adjLists[u]; l != NULL; l = l->next) {
			int v = l->data.v;
			if (visited[v] == 0) {
				visited[v] = 1;
				dist[v] = dist[u] + 1;
				p[v] = u;
				q = enqueue(q, v);
			}
		}
	}

	/* nodurile nevizitate au distanta minima -1 */
	for (int i = 0; i < g->V; i++) {
		if (visited[i] == 0) {
			dist[i] = -1;
		}
	}

	printf("\nBFS distanta minima (i, d[i]):\n");
	for (int i = 1; i < g->V; i++) {
		printf("(%d, %d) ", i, dist[i]);
	}

	printf("\nBFS drumul minim de la u la 0:\n");
	for (int i = 1; i < g->V; i++) {
		if (dist[i] == -1) {
			printf("Nu exista drum de la %d la %d\n", 0, i);
			continue;
		}

		int current_node = i;
		printf("%d ", current_node);
		while (current_node != 0) {
			current_node = p[current_node];
			printf("%d ", current_node);
		}

		printf("\n");
	}

	printf("\n");
	free(visited);
	free(dist);
	free(p);
	freeQueue(q);
}

typedef struct nod {
	int index;
	int end_time;
} Nod;

int compare_nodes(const void * a, const void * b) {
	return ((*(Nod*)b).end_time - (*(Nod*)a).end_time);
}

/**
 * timpii de finalizare au fost deja calculati de la apelul
 * de DFS_iterative
 */
void top_sort(Graph g) {
	Nod* nodes = (Nod*)malloc(sizeof(Nod) * g->V);
	for (int i = 0; i < g->V; i++) {
		nodes[i].index = i;
		nodes[i].end_time = g->end[i];
	}

	qsort(nodes, g->V, sizeof(Nod), compare_nodes);

	printf("Sortare topologica:\n");
	for (int i = 0; i < g->V; i++) {
		printf("%d ", nodes[i].index);
	}

	free(nodes);
	printf("\n\n");
}

int main() {
	freopen("test1.in", "r", stdin);
	int V, type, i, x, y, M;
	scanf("%d %d", &V, &type);
	Graph graph = initGraph(V, type);
	scanf("%d", &M);
	for (i = 0; i < M; i++) {
		scanf("%d %d", &x, &y);
		graph = insertEdge(graph, x, y, 0);
	}

	DFS_all_graph(graph);
	BFS(graph, 0);
	top_sort(graph);
	printGraph(graph);
	drawGraph(graph, "graph1.dot");

	graph = freeGraph(graph);
	return 0;
}