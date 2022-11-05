#include<iostream>
using namespace std;
const int MAX = 1e5 + 10;
int a[MAX];
int main() {
	int n;
	cin >> n;
	for (int i = 0; i < n; ++i)
		cin >> a[i];
	int sum = 0;
	for (int i = 1; i < n; i += 2)
		sum += a[i];
	cout << sum;
	return 0;
}

