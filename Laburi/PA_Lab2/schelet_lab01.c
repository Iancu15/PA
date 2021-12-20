/*
*	Created by Nan Mihai on 23.02.2020
*	Copyright (c) 2020 Nan Mihai. All rights reserved.
*	Laborator 1 - Proiectarea algoritmilor (Divide et Impera)
*	Facultatea de Automatica si Calculatoare
*	Anul Universitar 2019-2020, Seria CD
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "tree.h"

#define pa_assert(message, test) \
	do { \
		if (!(test)) \
			return message; \
	} while (0)
	
#define pa_run_test(test, score) \
	do { \
		char *message = test(); \
		tests_run++; \
		if (message) \
			return message; \
		else \
			total_score += score; \
	} while (0)

int tests_run = 0;
int total_score = 0;

int find(int *arr1, int *arr2, int left, int right, int index);

/*
*	Problema 1
*   arr1 - primul vector sortat crescător
*   arr2 - al doilea vector sortat crescător
*   size - numărul de elemente comune din cei doi vectori
*/
// un apel de functie de theta(logn) => complexitate theta(logn)
int find_missing_element(int *arr1, int *arr2, int size) {
	// implicit index-ul este ultimul element din vectorul mai mare
	// pentru a trata cazul special in care elementul eliminat
	// e fix ultimul element(moment in care nu merge niciodata pe ramura else in find
	// si astfel nu se modifica index-ul)
	find(arr1, arr2, 0, size - 1, size);
}

// formula de recursivitate este T(n) = T(n/2) + O(1) =>
// => cazul 2 al teoremei master pentru k = 0 =>
// => complexitate theta(n^0 * logn) = theta(logn)
int find(int *arr1, int *arr2, int left, int right, int index) {
	if (left > right)
		return index;

	int mid = left + (right - left)/2;
	if (arr1[mid] == arr2[mid]) {
		return find(arr1, arr2, mid+1, right, index);
	} else {
		return find(arr1, arr2, left, mid-1, mid);
	}
}

/*
*	Problema 2
*   arr - vectorul de cuvinte
*   size - dimensiunea vectorului de cuvinte
*/
char* solution(char **arr, int left, int right);

char *longest_common_prefix(char **arr, int size) {
	return solution(arr, 0, size - 1);
}

// un for ce merge pana la maxim minimul dintre lungimea
// lui word1 si word2 => complexitate de O(n), unde
// n este min(strlen(word1), strlen(word2))
char* common_prefix(char* word1, char* word2) {
	// nu conteaza daca iau word1, word2, pentru o caut prefixul comun
	// si astfel lungimea maxima a prefixului este minimul dintre cele 2
	int len = strlen(word1);
	char* prefix = (char*) malloc(len * sizeof(char));
	int i;
	for (i = 0; i < len; i++) {
		if (word1[i] != word2[i]) {
			break;
		}

		prefix[i] = word1[i];
	}

	prefix[i] = '\0';
	return prefix;
}

// complexitate T(n) = 2*T(n/2) + O(m), unde
// n este lungimea vectorului si m este dimensiunea
// celui mai mare prefix ce ar putea fi returnat
// de common_prefix conform input-ului
// log 2 2 = 1 => n^1
// In cazul in care n > m, atunci este primul caz
// al teoremei master si rezulta theta(n)
// In caz in care n < m, atunci e cazul 3 al teoremei
// 2*f(n/2) <= c*f(n) => 2 * n/2 <= c * n =>
// n <= c * n, nu exista un c mai mic care sa respecte
// nu se poate aplica teorema master pe acest caz
char* solution(char **arr, int left, int right) {
	if (left == right)
		return arr[left];
	int mid = left + (right - left)/2;
	char* result1 = solution(arr, left, mid);
	char* result2 = solution(arr, mid+1, right);
	return common_prefix(result1, result2);
}

/*
*	Problema 3
*   inOrder - vectorul care conține parcurgerea în inordine
*   preOrder - vectorul care  conține parcurgerea în preordine
*   inLeft - indicele primului element din vectorul inOrder
*   inRight - indicele ultimului element din vectorul inOrder
*   preIndex - indicele elementului din vectorul preOrder care urmează să fie adăugat
*/
// cautare binara e O(logn)
int cautare_binara(int *arr, int x, int left, int right) {
	if (left > right)
		return -1;
	
	int mid = left + (right - left)/2;
	if (arr[mid] == x) {
		return mid;
	} else if(arr[mid] < x) {
		return cautare_binara(arr, x, mid+1, right);
	} else {
		return cautare_binara(arr, x, left, mid-1);
	}
}

// complexitate O(nlogn) pentru ca mereu o sa fie procesate cele n noduri
// intr-o anume iteratie si in momentul in care e procesat acel nod se va
// face un apel de cautare binara care e O(logn)
Tree make_binary_tree(int *inOrder, int *preOrder, int inLeft, int inRight, int *preIndex) {
	if (inLeft > inRight) 
		return 0;
	
	Tree root = createTree(preOrder[*preIndex]);
	*preIndex += 1;
	int root_index = cautare_binara(inOrder, root->value, inLeft, inRight);
	root->left = make_binary_tree(inOrder, preOrder, inLeft, root_index-1, preIndex);
	root->right = make_binary_tree(inOrder, preOrder, root_index + 1, inRight, preIndex);

	return root;
}

/*
*	Problema 4
*   (x, y) - coordonatele celulei pentru care dorim să aflăm valoarea
*/
// l-am luat de la labul 1, complexitate calculata acolo de O(logn)
int ex3_eficient(int x, int n) {
    if (n == 0)
        return 1;
    
    if (n % 2 == 0) {
        int number = ex3_eficient(x, n/2);
        return number * number;
    } else {
        int number = ex3_eficient(x, (n-1)/2);
        return x * number * number;
    }
}

// Este apelata pentru fiecare step in intr-un cadran inferior
// stim ca pentru intrarea n sunt n straturi de cadrane si astfel
// n apeluri recursive
// La fiecare apel se apeleaza functia ex3_eficient de complexitate
// O(logn), asa ca complexitatea lui ZParcurgere este O(nlogn)
int ZParcurgere(int n, int x, int y) {
	if (n == 1) {
		if (x == 1 && y == 1) {
			return 1;
		} else if (x == 1 && y == 2) {
			return 2;
		} else if (x == 2 && y == 1) {
			return 3;
		} else {
			return 4;
		}
	}

	int half_size = ex3_eficient(2, n-1);
	int pos_power = half_size * half_size;

	if (x >= 1 && x <= half_size) {
		if (y >= 1 && y <= half_size) {
			return ZParcurgere(n-1, x, y);
		} else {
			return pos_power + ZParcurgere(n-1, x, y - half_size);
		}
	} else {
		if (y >= 1 && y <= half_size) {
			return 2 * pos_power + ZParcurgere(n-1, x - half_size, y);
		} else {
			return 3 * pos_power + ZParcurgere(n-1, x - half_size, y - half_size);
		}
	}
}


/*
*	Bonus 1
*   n - dimensiunea șirului magic pe care dorim să îl determinăm
*/
int *beautiful_array(int n) {
	int *result = malloc(n * sizeof(int));
	int i = 0;
	for (i = 0; i < n; i++) {
		result[i] = i+1;
	}
	return result;
}

/*
*	Bonus 2
*	arr - vectorul cu elemente
*	size - dimensiunea vectorului
*/
int peak_value(int *arr, int size) {
	return -1;
}

static char *test_problema1() {
	int v1[] = {1, 2, 3, 4, 5};
	int v2[] = {1, 2, 3, 4, 5, 6};
	pa_assert("Problema1 - Test1 picat", find_missing_element(v1, v2, 5) == 5);
	pa_assert("Problema1 - Test2 picat", find_missing_element(v2, v1, 5) == 5);
	int v3[] = {1, 2, 4, 5};
	pa_assert("Problema1 - Test3 picat", find_missing_element(v1, v3, 4) == 2);
	int v4[] = {2, 3, 4, 5};
	pa_assert("Problema1 - Test4 picat", find_missing_element(v1, v4, 4) == 0);
	int v5[] = {1, 2, 3, 5};
	pa_assert("Problema1 - Test5 picat", find_missing_element(v1, v5, 4) == 3);
	return 0;
}

static char *test_problema2() {
	char **arr1;
	int i;
	arr1 = malloc(3 * sizeof(char*));
	arr1[0] = strdup("ana");
	arr1[1] = strdup("ana are");
	arr1[2] = strdup("ana are mere");
	pa_assert("Problema2 - Test1 picat", !strcmp(longest_common_prefix(arr1, 3),"ana"));
	for (i = 0; i < 3; i++)
		free(arr1[i]);
	free(arr1);
	arr1 = malloc(5 * sizeof(char*));
	arr1[0] = strdup("abcd");
	arr1[1] = strdup("acdb");
	arr1[2] = strdup("abcdefgh");
	arr1[3] = strdup("abcdefghijk");
	arr1[4] = strdup("ab");
	pa_assert("Problema2 - Test2 picat", !strcmp(longest_common_prefix(arr1, 5),"a"));
	arr1[1][1] = 'b';
	pa_assert("Problema2 - Test3 picat", !strcmp(longest_common_prefix(arr1, 5),"ab"));
	for (i = 0; i < 5; i++)
		free(arr1[i]);
	free(arr1);
	return 0;
}

int checkBST(Tree node) { 
	if (node == NULL) 
		return 1;
	if (node->left != NULL && node->left->value > node->value)
		return 0; 
	if (node->right != NULL && node->right->value < node->value) 
		return 0; 
	if (!checkBST(node->left) || !checkBST(node->right)) 
		return 0; 
	return 1; 
}

void bst_print_dot_aux(Tree node, FILE* stream) {
    static int nullcount = 0;

    if (node->left) {
        fprintf(stream, "    %d -> %d;\n", node->value, node->left->value);
        bst_print_dot_aux(node->left, stream);
    }
    if (node->right) {
        fprintf(stream, "    %d -> %d;\n", node->value, node->right->value);
        bst_print_dot_aux(node->right, stream);
    }
}

void bst_print_dot(Tree tree, FILE* stream, int type) {
    fprintf(stream, "digraph BST {\n");
    if (type == 1)
    	fprintf(stream, "    node [fontname=\"Arial\", shape=circle, style=filled, fillcolor=red];\n");
    else
    	fprintf(stream, "    node [fontname=\"Arial\", shape=circle, style=filled, fillcolor=blue];\n");
    if (!tree)
        fprintf(stream, "\n");
    else if (!tree->right && !tree->left)
        fprintf(stream, "    %d;\n", tree->value);
    else
        bst_print_dot_aux(tree, stream);
    fprintf(stream, "}\n");
}

static char *test_problema3() {
	int preOrder1[] = {4, 1, 0, 3, 2, 6, 5, 8, 7, 9};
	int inOrder1[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	int index = 0;
	Tree root = make_binary_tree(inOrder1, preOrder1, 0, 9, &index);
	FILE *fout = fopen("test.dot", "w");
	bst_print_dot(root, fout, 1);
	fclose(fout);
	system("dot test.dot | neato -n -Tpng -o prob3_test1.png");
	pa_assert("Problema3 - Test1 picat", checkBST(root) && root != NULL && root->value == 4);
	root = freeTree(root);

	int preOrder2[] = {10, 7, 5, 6, 9, 8, 20, 15, 13, 18, 40};
	int inOrder2[] = {5, 6, 7, 8, 9, 10, 13, 15, 18, 20, 40};
	index = 0;
	root = make_binary_tree(inOrder2, preOrder2, 0, 10, &index);
	fout = fopen("test.dot", "w");
	bst_print_dot(root, fout, 1);
	fclose(fout);
	system("dot test.dot | neato -n -Tpng -o prob3_test2.png");
	pa_assert("Problema3 - Test2 picat", checkBST(root) && root != NULL && root->value == 10);
	root = freeTree(root);

	int preOrder3[] = {10, 7, 5, 4, 3, 2, 6, 9, 8, 20, 15, 13, 14, 18, 17, 16, 19, 40, 50, 60, 70};
	int inOrder3[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 40, 50, 60, 70};
	index = 0;
	root = make_binary_tree(inOrder3, preOrder3, 0, 20, &index);
	fout = fopen("test.dot", "w");
	bst_print_dot(root, fout, 1);
	fclose(fout);
	system("dot test.dot | neato -n -Tpng -o prob3_test3.png");
	pa_assert("Problema3 - Test3 picat", checkBST(root) && root != NULL && root->value == 10);
	root = freeTree(root);

	return 0;
}

static char *test_problema4() {
	pa_assert("Problema4 - Test1 picat", ZParcurgere(1, 2, 1) == 3);
	pa_assert("Problema4 - Test2 picat", ZParcurgere(5, 10, 20) == 392);
	pa_assert("Problema4 - Test3 picat", ZParcurgere(4, 5, 7) == 53);
	pa_assert("Problema4 - Test4 picat", ZParcurgere(4, 15, 9) == 233);
	pa_assert("Problema4 - Test5 picat", ZParcurgere(3, 1, 8) == 22);
	return 0;
}

int checkBeautiful(int *arr, int size) {
	int i;
	for (i = 1; i < size - 1; i++) {
		if (arr[i] * 2 == arr[i-1] + arr[i + 1])
			return 0;
	}
	return 1;
}

static char *test_problema5() {
	int *arr1 = beautiful_array(5);
	pa_assert("Bonus 1 - Test1 picat", checkBeautiful(arr1, 5));
	free(arr1);
	arr1 = beautiful_array(10);
	pa_assert("Bonus 1 - Test2 picat", checkBeautiful(arr1, 10));
	free(arr1);
	arr1 = beautiful_array(50);
	pa_assert("Bonus 1 - Test3 picat", checkBeautiful(arr1, 50));
	free(arr1);
	arr1 = beautiful_array(100);
	pa_assert("Bonus 1 - Test4 picat", checkBeautiful(arr1, 100));
	free(arr1);
	return 0;
}

static char *test_problema6() {
	int arr1[] = {1, 2, 3, 4, 5, 4, 3};
	pa_assert("Problema6 - Test1 picat", peak_value(arr1, 7) == 5);
	int arr2[] = {1, 10, 9, 8, 7, 6, 5, 4, 3, 2};
	pa_assert("Problema6 - Test2 picat", peak_value(arr2, 10) == 10);
	int arr3[] = {1, 2, 3, 4, 5, 6, 7, 8, 10, 9};
	pa_assert("Problema6 - Test3 picat", peak_value(arr3, 10) == 10);
	int arr4[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3};
	pa_assert("Problema6 - Test3 picat", peak_value(arr4, 17) == 10);
	return 0;
}

static char *all_tests() {
	pa_run_test(test_problema1, 2);
	pa_run_test(test_problema2, 2);
	pa_run_test(test_problema3, 3);
	pa_run_test(test_problema4, 3);
	pa_run_test(test_problema5, 4);
	pa_run_test(test_problema6, 3);
	return 0;
}

static char *selective_tests(int argc, char **argv) {
	int i;
	int viz[7] = {0};
	for (i = 1; i < argc; i++) {
		if (viz[atoi(argv[i])]) {
			continue;
		}
		if (!strcmp(argv[i], "1")) {
			viz[1] = 1;
			pa_run_test(test_problema1, 2);
		} else if (!strcmp(argv[i], "2")) {
			viz[2] = 1;
			pa_run_test(test_problema2, 1);
		} else if (!strcmp(argv[i], "3")) {
			viz[3] = 1;
			pa_run_test(test_problema3, 2);
		} else if (!strcmp(argv[i], "4")) {
			viz[4] = 1;
			pa_run_test(test_problema4, 2);
		} else if (!strcmp(argv[i], "5")) {
			viz[5] = 1;
			pa_run_test(test_problema5, 4);
		} else if (!strcmp(argv[i], "6")) {
			viz[6] = 1;
			pa_run_test(test_problema6, 3);
		}
	}
	return 0;
}

int main(int argc, char **argv) {
	srand(time(NULL));
	char *result;
	if (argc == 1) {
		result = all_tests();
		if (result != 0) {
			printf("%s\n", result);
		} else {
			printf("Toate testele au trecut! Felicitari!\n");
		}
	} else {
		result = selective_tests(argc, argv);
		if (result != 0) {
			printf("%s\n", result);
		} else {
			printf("Toate testele selectate au trecut!\n");
		}
	}
	printf("Punctajul obtinut este: %d\n", total_score);
	printf("Teste rulate: %d\n", tests_run);
	return result != 0;
}
