#include <iostream>
#include <math.h>
using namespace std;

void egyptian_fractionsv2(long nr, long dr)
{
    long unit_dr = ceil((double)dr/nr);
    nr = nr * unit_dr - dr;
    dr *= unit_dr;

    while (unit_dr > 0) {
        cout << "1/" << unit_dr << " + ";
        printf("%ld %ld %ld\n", unit_dr, nr, dr);
        unit_dr = ceil((double)dr/nr);
        nr = nr * unit_dr - dr;
        dr *= unit_dr;
    }
}

void printEgyptian(int nr, int dr)
{
    // If either numerator or denominator is 0
    if (dr == 0 || nr == 0)
        return;
 
    // If numerator divides denominator, then simple division
    // makes the fraction in 1/n form
    if (dr%nr == 0)
    {
        cout << "1/" << dr/nr;
        return;
    }
 
    // If denominator divides numerator, then the given number
    // is not fraction
    if (nr%dr == 0)
    {
        cout << nr/dr;
        return;
    }
 
    // If numerator is more than denominator
    if (nr > dr)
    {
        cout << nr/dr << " + ";
        printEgyptian(nr%dr, dr);
        return;
    }
 
    // We reach here dr > nr and dr%nr is non-zero
    // Find ceiling of dr/nr and print it as first
    // fraction
    int n = dr/nr + 1;
    cout << "1/" << n << " + ";
 
    // Recur for remaining part
    printEgyptian(nr*n-dr, dr*n);
 }
 
// Driver Program
int main(int argc, char* argv[])
{
    long nr = atoi(argv[1]);
    long dr = atoi(argv[2]);
    cout << "Egyptian Fraction Representation of "
         << nr << "/" << dr << " is\n ";
    egyptian_fractionsv2(nr, dr);
    //printEgyptian(nr, dr);
    cout << "\n";
    return 0;
}