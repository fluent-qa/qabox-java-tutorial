# Time Complexity Cheatsheet

- [O(1)](#constant---o1)
- [O(n)](#linear---on)
- [O(n^2)](#quadratic---on2)
- [O(log(n))](#logarithmic---ologn)
- [O(2^n)](#exponential---o2n)
- [O(n!)](#on)
- [O(n log(n))](#linear-logarithmic---on-logn)

##  Constant - O(1)

An algorithm has constant time complexity if it takes the same time regardless of the number of inputs.

For example, if we want to get the first item of an array, it doesn't matter how big the array size is.

- Hashmap lookup
- Array access and update
- Pushing and Popping elements from Stack
- Finding and applying math formula

```java
int a = 10;
int b = 20;
int c = 30;
int sum = a + b + c;
```


## Linear - O(n)

Linear time typically means looping through a linear data structure a constant number of times.

- Going through array/linked list
- Two pointers
- Some types of greedy
- Tree/graph traversal
- Stack/Queue

```java
for (int i = 1; i <= n; i++) {
  // constant time code
}

for (int j = 1; j <= n; j++) {
  // constant time code
}
```


## Quadratic - O(n^2)

If an algorithm's time complexity is quadratic, it means that the runtime of the algorithm is directly proportional to the square of the size of the input.

- Nested loops, e.g., visiting each matrix entry
- Shortest path between two nodes in a graph
- Many brute force solutions

```java
for (int i = 1; i <= n; i++) {
  for (int j = 1; j <= n; j++) {
    // constant time code
  }
}
```


## Logarithmic - O(log(n))

`log(n)` grows **VERY** slowly.

`log(1,000,000)` is only about 20.

In fact, lookup by primary key in a relational database is `log(n)`
(many mainstream relational databases such as Postgres use **B-trees** for indexing by default, and search in B-tree is `log(n)`).

- Binary search or variant
- Balanced binary search tree lookup
- Processing the digits of a number

The size of the input gets split into half with each iteration of the function.

The following code is `O(log(n))` because `n` is halved each iteration, so the amount of work is logarithmic:

```java
int n = 100000000;
while (n > 0) {
  // some constant operation
  n /= 2;
}
```


## Exponential - O(2^n)

The runtime of the algorithm gets doubled after every addition in the input.

**Grows very rapidly**. Often requires **memoization** to avoid repeated computations and reduce complexity.

- **Combinatorial** problems, backtracking, e.g. subsets
- Tower of Hanoi problem
- Often involves **recursion** and is harder to analyze time complexity at first sight
- Typically, for `n ≤ 25`

A recursive Fibonacci algorithm is `O(2^n)` because for any `fib(i)` where `i > 1`, we call `fib(i - 1)` and `fib(i - 2)`.

```java
public static int fib(int n) {
  if (n == 0 || n == 1) {
    return 1;
  }
  return fib(n - 1) + fib(n - 2);
}
```


## O(n!)

Grows insanely rapidly. Often requires memoization to avoid repeated computations and reduce complexity.

- Combinatorial problems, backtracking, e.g. permutations
- Often involves recursion and is harder to analyze time complexity at first sight
- Typically for `n ≤ 12`


## Linear Logarithmic - O(n log(n))

- Sorting. The default sorting algorithm's expected runtime in all mainstream languages is `n log(n)`.
  For example, Java uses a variant of **Merge sort** for object sorting and a variant of **Quick Sort** for primitive type sorting.

- Divide and conquer with a linear time merge operation.
  Divide is normally `log(n)`, and if merge is `O(n)` then the overall runtime is `O(n log(n))`.
  An example problem is smaller numbers to the right.
