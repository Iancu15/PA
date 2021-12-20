#include <stdlib.h>
#include <stdio.h>
#include <string.h>

void swap_string(char* string, int index1, int index2) {
    char character = string[index1];
    string[index1] = string[index2];
    string[index2] = character;
}

void generate_strings(char* previousString, int index, int size) {
    if (index == size) {
        printf("%s\n", previousString);
    }

    for (int i = index; i < size; i++) {
        char* currentString = strdup(previousString);
        swap_string(currentString, index, i);
        generate_strings(currentString, index + 1, size);
    }
}

int main(int argc, char* argv[]) {
    char* string = argv[1];
    generate_strings(string, 0, strlen(string));
}