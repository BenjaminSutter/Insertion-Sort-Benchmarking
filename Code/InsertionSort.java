/*
 * File: InsertionSort.java
 * Author: Ben Sutter
 * Date: February 5th, 2020
 * Purpose: Implements SortInterface. Holds iterative and recursive insertionSorts and keeps track of critical criticalCount and time elapsed.
 */

import java.util.Arrays;

public class InsertionSort implements SortInterface {

    int criticalCount = 0;//Keeps track of critical criticalCount for each sort
    long timeElapsed = 0;//Keeps track of time elapsed for each sort

    //Recursive insertionSort and it's associated methods taken from Homework 3
    @Override
    public void recursiveSort(int[] array) throws UnsortedException {

        long start = System.nanoTime();//Starts right before the recursion begins
        insert(array, 1);
        long end = System.nanoTime();//Endsafter the recursion is finished
        timeElapsed = (end - start);
        //If array is not sorted, throw exception

        if (!isSorted(array)) {
            throw new UnsortedException("Array not sorted\n" + Arrays.toString(array));
        }
    }

    //Method used in recursiveSort, taken from Homework3
    void insert(int[] array, int i) {

        //Keeps looping through the array starting at index 1, and ending at length of array
        if (i < array.length) {
            int value = array[i];
            int j = shift(array, value, i);//Pass it to shift to make sure it is sorted
            array[j] = value;
            insert(array, i + 1);
        }

    }

    int shift(int[] array, int value, int i) {
        
        int insert = i;
        //If sorted, skip this loop, if not shift element to sort
        if (i > 0 && array[i - 1] > value) {
            //Critical criticalCount here because this is called the most (recursively called here and also called from insert)
            //Decided to put it in the if loop rather than outside
            criticalCount++;
            array[i] = array[i - 1];
            insert = shift(array, value, i - 1);
        }
        return insert;
    }

    //Iterative code found from:https://www.techiedelight.com/insertion-sort-iterative-recursive/
    @Override
    public void iterativeSort(int[] array) throws UnsortedException {

        long start = System.nanoTime();//Start counting right before the for loop where everything happens
        // Start from the second element (assume element at index 0 is already sorted)
        for (int i = 1; i < array.length; i++) {
            int value = array[i];
            int j = i;

            // Find the index j within the sorted subset arr[0..i-1]
            // where element arr[i] belongs
            while (j > 0 && array[j - 1] > value) {
                array[j] = array[j - 1];
                j--;
                criticalCount++;//I determined that the nested loop would be the critical operation
            }

            // Note that sub-array arr[j..i-1] is shifted to
            // the right by one position i.e. arr[j+1..i]
            array[j] = value;
        }
        long end = System.nanoTime();//Stop counting right after the for loop when everything ends
        timeElapsed = (end - start);//Set timeElapsed value to time taken
        //If array is not sorted, throw exception
        if (!isSorted(array)) {
            throw new UnsortedException("Array not sorted\n" + Arrays.toString(array));
        }
    }

    //Runs through the entire list passed through it
    public boolean isSorted(int[] sortedList) {

        for (int i = 1; i < sortedList.length; i++) {
            if (sortedList[i] < sortedList[i - 1]) {
                return false;
            }
        }
        return true;
    }

    @Override //Returns critical criticalCount of sort
    public int getCount() {
        
        //Copies the variable, resets the variable for the next data set and returns the copy
        int copy = criticalCount;
        criticalCount = 0;
        return copy;

    }

    @Override //Returns time lapsed of sort
    public long getTime() {
        
        //Copies the variable, resets the variable for the next data set and returns the copy
        long copy = timeElapsed;
        timeElapsed = 0;
        return copy;
    }

}
