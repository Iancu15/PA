#include <iostream>
using namespace std;
 
// A dynamic programming based function to find nth
// Catalan number
unsigned long int catalanDP(unsigned int n)
{
    // Table to store results of subproblems
    unsigned long int catalan[n + 1];
 
    // Initialize first two values in table
    catalan[0] = 1;
 
    // Fill entries in catalan[] using recursive formula
    for (int i = 1; i <= n; i++) {
        catalan[i] = 0;
        for (int j = 0; j < i; j++)
            catalan[i] += catalan[j] * catalan[i - j - 1];
    }
 
    // Return last entry
    return catalan[n];
}
 
// Driver code
int main(int argc, char* argv[])
{
    int n = atoi(argv[1]);
    cout << catalanDP(n) << "\n";
    return 0;
}