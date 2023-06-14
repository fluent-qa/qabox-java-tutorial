# Time Complexity Of A Computer Program

## What is Time Complexity and What is its need?

Suppose, your friend picked a number between 1 to 1000 and asked you to guess the number.

- if your guess is correct, he will tell you that it is _correct_ otherwise

- if your guess is bigger than his number he would tell you that it is _too big_, and

- if it is smaller than his number then he would tell you that it is _too small_

Here are some ways by which we could find the number:

1. We can guess each number from 1 to 1000, and see if it is correct.
2. We can pick the middle number, if he says _too big_ then we know for sure that the number is on the left side so we can discard the right side, similarly if he says _too small_ then we can discard the left side.
   We can repeat the same process until he says it is _correct_.


## Which way is better?

The 2nd way is way better than the 1st way.
In the worst case, the 1st way would take **1000 guesses** before we get the correct number (if the number is 1000), while the 2nd way would only take **10 guesses** in the worst case (this is because at every guess we discard one of the halves).

The time complexity of the first way is `O(n)` and that of the second way is `O(log n)`.

How do we compare the algorithms which are written in two different languages, running on two different machines?
This is exactly why the concept of time complexity was introduced.

> Time complexity is defined as the amount of time taken by an algorithm to run, as a function of the length of the input.

Since the algorithm's performance may vary with different types of input data, hence for an algorithms we usually use the **_worst-case Time complexity_** because that is the maximum time taken for any input size.


## Calculating Time Complexity

The most common metric for calculating time complexity is the **Big O** notation.
This removes all constant factors so that the running time can be estimated in relation to `n`, as `n` approaches infinity.

In general, you can think of it like this:

```
statement;
```

Above we have a single statement. Its Time Complexity will be **Constant**, i.e. `O(1)`.
The running time of the statement will not change in relation to `n`.

```java
for(i=0; i < n; i++)
{
    statement;
}
```

The time complexity for the above algorithm will be **Linear**, i.e. `O(n)`.
The running time of the loop is directly proportional to `n`.
When `n` doubles, so does the running time.

```java
for(i=0; i < n; i++) 
{
    for(j=0; j < n; j++)
    { 
        statement;
    }
}
```

The time complexity for the above code will be **Quadratic**, i.e. `O(n²)`.
The running time of the two loops is proportional to the square of `n`.
When `n` doubles, the running time increases by `n * n`.

```java
while(low <= high)
{
    mid = (low + high) / 2;
    if (target < list[mid])
        high = mid - 1;
    else if (target > list[mid])
        low = mid + 1;
    else break;
}
```

This is a typical _divide-and-conquer_ algorithm.
This algorithm will have a **Logarithmic** Time Complexity, i.e. `O(log n)`.

```java
void quicksort(int list[], int left, int right)
{
    int pivot = partition(list, left, right);
    quicksort(list, left, pivot - 1);
    quicksort(list, pivot + 1, right);
}
```

Taking the previous algorithm forward, above we have a small logic of Quick Sort.
Now in Quick Sort, we divide the list into halves every time, but we repeat the iteration `n` times (where `n` is the size of list).
Hence, time complexity will be `O(n * log n)`.
The running time consists of `n` loops (iterative or recursive) that are logarithmic, thus the algorithm is a **combination of linear and logarithmic**.

> In general, doing something with every item in one dimension is **linear**, doing something with every item in two dimensions is **quadratic**, and dividing the working area in half is **logarithmic**.
