#include <stdio.h>
#include <stdlib.h>

int floorSearch(int* arr, int low, int high, int x) {
	if (arr[low] > x or low > high)
		return -1;

	if (arr[high] <= x)
		return high;
	
	int mid = (low + high) / 2;
	
	if (arr[mid] > x)
		if (arr[mid - 1] <= x)
			return mid - 1;
		else
			return floorSearch(arr, low, mid - 1, x);
	
	if (arr[mid + 1] > x)
		return mid;
	else
		return floorSearch(arr, mid + 1, high, x);
}

int main(int argc, char* argv[])
{
    int n = argc - 2;
    int* arr = (int*)malloc(sizeof(int) * n);
    for (int i = 0; i < n; i++) {
        arr[i] = atoi(argv[i + 1]);
    }
    int x = atoi(argv[n + 1]);
    printf("%d\n", floorSearch(arr, 0, n - 1, x));

    return 0;
}