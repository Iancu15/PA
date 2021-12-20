#include <stdio.h>
#include <stdlib.h>

// T(n) = T(n-1) + T(n-2) + theta(1)
// 2*T(n-2) + theta(1) <= T(n) <= 2*T(n-1) + theta(1)
// fac 2*T(n-2) + theta(1) cu metoda iteratiei
// 2 * T(n-2) = 4 * T(n-3) + 2*theta(1)
// 4 * T(n-3) = 8 * T(n-4) + 4*theta(1)
// ...
// 2^k * T(n-k) = 2^(k + 1) * T(n - (k + 1)) + 2^k * theta(1)
// ...
// 2^(h - 1) * T(n - (h-1)) = 2^h * T(n - h) + 2^(h-1) * theta(1)
// n - h = 1 => h = n - 1
// Insumand ajungem la T(n) = 2^h * T(1) + sum(theta(1), ... , 2^(h-1) * theta(1))
// T(1) il consideram theta (1) => 2^(n-1) + suma geometrica de 2^(i) pt 0...n-2
// T1(n) = 2^(n-1) + 1 * (2^(n-2) - 1)/(2 - 1) = 2^(n-1) + 2^(n-2) - 1 <= 
// <= 2^(n-1) + 2^(n-1) = 2*2^(n-1) = 2^n => (conf def cu const = 1) T1(n) apartine O(2^n)

// Daca facem pt 2*T(n-2) + theta(1) se va ajunge la forma
// 2^k * T(n-2*k) = 2^(k + 1) * T(n - 2*(k + 1)) + 2^k * theta(1)
// => 2^(h - 1) * T(n - 2*(h-1)) = 2^h * T(n - 2*h) + 2^(h-1) * theta(1)
// pt n - 2*h = 1 => 2*h + 1 = n => h = (n-1)/2
// => analog ca T2(n) = 2^(n-1)/2 + 1 * (2^((n-3)/2) - 1)/(2 - 1) = 2^((n-1)/2) + 2^((n-3)/2) - 1
// <= 2 * 2^((n-1)/2) = 2^((n+1)/2) => T2(n) apartine O(2^((n+1)/2))
// deci T(n) este intre O(2^((n+1)/2)) si O(2^n)
int fibonacci(int n) {
    if (n == 0)
        return 0;

    if (n == 1)
        return 1;
    
    return fibonacci(n-1) + fibonacci(n-2);
}

// e un for ce are n-1 iteratii < 1 * n => conform def ca apartine O(n), mult
// mai bun decat varianta recursiva
int fibonacci_iterativ(int n, int* v) {
    v[0] = 0;
    if (n == 0)
        return 0;
    
    v[1] = 1;
    for (int i = 2; i <= n; i++) {
        v[i] = v[i - 1] + v[i - 2];
    }

    return v[n];
}

int main() {
    int n;
    scanf("%d", &n);
    printf("%d\n", fibonacci(n));

    int* v = malloc(sizeof(int) * (n + 1));
    printf("%d\n", fibonacci_iterativ(n, v));
    free(v);
}