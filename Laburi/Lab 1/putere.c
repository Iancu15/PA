#include <stdio.h>

// Complexitate theta(n), un for simplu care
// are mereu n pasi
int putere_nerecursiv(int x, int n) {
    int number = 1;
    for (int i = 0; i < n; i++) {
        number *= x;
    }

    return number;
}

// T(n) = T(n-1) + theta(1)
// Complexitatea este theta(n) pentru ca
// o sa fie n apeluri de complexitate theta(1)
// care adunate dau theta(n)
int putere_recursiv(int x, int n) {
    if (n == 0)
        return 1;
    
    return x * putere_nerecursiv(x, n-1);
}

// 2*T((n-1)/2) + theta(1) <= T(n) <= 2*T(n/2) + theta(1)
// in cel mai rau caz o sa fie toate numere pare adica
// 2*T(n/2) + theta(1) si in cel mai bun caz toate numere
// impare adica 2*T((n-1)/2) + theta(1)
// 2*T(n/2) + theta(1) se face cu teorema master
// log2 2 = 1 => n^1 => se incadreaza in cazul 1 pentru esp = 1/2
// => 2*T(n/2) + theta(1) are complexitatea theta(n^1)
// => sigur T(n) apartine O(n)
// daca se face 2*T((n-1)/2) + theta(1) cu metoda iteratiei si da
// theta(n) inseamna ca pot restrange T(n) la theta(n)
int ex3_ineficient(int x, int n) {
    if (n == 0)
        return 1;
    
    if (n % 2 == 0) {
        return ex3_ineficient(x, n/2) * ex3_ineficient(x, n/2);
    } else {
        return x * ex3_ineficient(x, (n-1)/2) * ex3_ineficient(x, (n-1)/2);
    }
}

// T((n-1)/2) + theta(1) <= T(n) <= T(n/2) + theta(1)
// De data asta da log2 1 = 0 => n^0 = 1 => cazul 2 pt k = 0 =>
// T(n/2) + theta(1) are complexitatea theta(1 * logn) =>
// T(n) are complexitatea O(logn) => mult mai eficient decat celelalte
// 2 variante
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

int main() {
    int x, n;
    scanf("%d", &x);
    scanf("%d", &n);
    printf("%d\n", putere_nerecursiv(x, n));
    printf("%d\n", putere_recursiv(x, n));
    printf("%d\n", ex3_ineficient(x, n));
    printf("%d\n", ex3_eficient(x, n));
}