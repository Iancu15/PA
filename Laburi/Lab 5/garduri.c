#include <stdlib.h>
#include <stdio.h>
#include <time.h>

long fencesIter(long length, long height) {
    if (length <= 0 || height <= 0) {
        return 0;
    }

    long *dp = (long*)malloc((length + 1) * sizeof(long));
    for (long i = 1; i < height && i <= length; i++) {
        dp[i] = 1;
    }

    if (length >= height) {
        dp[height] = 2;
    }

    for (int i = height + 1; i <= length; i++) {
        dp[i] = dp[i - 1] + dp[i - height];
    }

    long result = dp[length];
    free(dp);
    return result;
}

long** mul(long **a, long **b, long size) {
    long **result = (long**)malloc(sizeof(long*) * size);
    for (int i = 0; i < size; i++) {
        result[i] = (long*)calloc(size, sizeof(long));
    }

    for (int i = 0; i < size; i++) {
        for (long j = 0; j < size; j++) {
            for (long k = 0; k < size; k++) {
                result[i][j] += a[i][k] * b[k][j];
            }
        }
    }

    return result;
}

long** identityMatrix(long size) {
    long **I = (long**)malloc(sizeof(long*) * size);
    for (int i = 0; i < size; i++) {
        I[i] = (long*)calloc(size, sizeof(long));
        I[i][i] = 1;
    }

    return I;
}

long** powerIterative(long **a, long n, long size) {
    long **res = identityMatrix(size);
    while (n > 0) {
        if (n % 2 == 0) {
            a = mul(a, a, size);
            n /= 2;
        } else {
            res = mul(res, a, size);
            n--;
        }
    }

    return res;
}

long** powerRecursive(long **a, long n, long size) {
    if (n == 0) {
        return identityMatrix(size);
    }

    if (n % 2 == 0) {
        long** M = powerRecursive(a, n/2, size);
        return mul(M, M, size);
    } else {
        long** M = powerRecursive(a, (n-1)/2, size);
        return mul(a, mul(M, M, size), size);
    }
}

long* mulVectorNMatrix(long *a, long **b, long size) {
    long *result = (long*)calloc(size, sizeof(long));
    for (int i = 0; i < size; i++) {
        for (long j = 0; j < size; j++) {
            result[i] += a[j] * b[i][j];
        }
    }

    return result;
}

long fencesMatrix(long length, long height, long** (*power)(long**, long, long)) {
    if (length <= 0 || height <= 0) {
        return 0;
    }

    if (length < height) {
        return 1;
    }

    long **M = (long**)malloc(sizeof(long*) * height);
    for (int i = 0; i < height; i++) {
        M[i] = (long*)calloc(height, sizeof(long));
    }

    M[0][height - 1] = 1;
    M[height - 1][height - 1] = 1;
    for (int i = 1; i < height; i++) {
        M[i][i - 1] = 1;
    }

    M = power(M, length - height, height);

    long *S = (long*)malloc(sizeof(long) * height);
    for (int i = 0; i < height - 1; i++) {
        S[i] = 1;
    }
    S[height - 1] = 2;

    long *result = mulVectorNMatrix(S, M, height);
    return result[height - 1];
}

void printMatrix(long** a, int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            printf("%lu ", a[i][j]);
        }

        printf("\n");
    }
}

long main(long argc, char *argv[]) {
    long length = atoi(argv[1]);
    long height = atoi(argv[2]);
    clock_t start_t, end_t, total_t;
    start_t = clock();
    long numberOfCombIter = fencesIter(length, height);
    end_t = clock();
    printf("Time iterative: %f\n", (double)(end_t - start_t) / CLOCKS_PER_SEC);

    start_t = clock();
    long numberOfCombMatrix = fencesMatrix(length, height, powerIterative);
    end_t = clock();
    // daca foloseam o variabila total_t sa stochez timpul ce trb afisat
    // imi afisa 0.0000
    printf("Time matrix with iterative power function: %f\n", (double)(end_t - start_t) / CLOCKS_PER_SEC);

    start_t = clock();
    fencesMatrix(length, height, powerRecursive);
    end_t = clock();
    printf("Time matrix with recursive power function: %f\n", (double)(end_t - start_t) / CLOCKS_PER_SEC);

    printf("Iterative: %lu\n", numberOfCombIter);
    printf("Matrix: %lu\n", numberOfCombMatrix);
}