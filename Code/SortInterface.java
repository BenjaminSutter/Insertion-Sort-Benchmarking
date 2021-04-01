/*
 * File: SortInterface.java
 * Author: Ben Sutter
 * Date: February 5th, 2020
 * Purpose: Create an interface for implementation in InsertionSort
 */

interface SortInterface {

    void recursiveSort(int[] array) throws UnsortedException;

    void iterativeSort(int[] array) throws UnsortedException;

    int getCount();

    long getTime();

}
