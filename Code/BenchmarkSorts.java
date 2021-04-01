/*
 * File: BeenchmarkSorts.java
 * Author: Ben Sutter
 * Date: February 5th, 2020
 * Purpose: Perform 50 runs of each data set size, keeping track of critical counts and time elapsed of each.
 * Stores all data into a 2d array, that 2d array is then written to afile for BenchmarkReport to process.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BenchmarkSorts {

    private Random random = new Random();//Gives the ability to
    private InsertionSort insortion = new InsertionSort();

    //Holds the data for putting to file later, int array has size of 51 because index 1 holds size of arrays
    int[][] recursiveCountArray = new int[10][51];
    long[][] recursiveTimeArray = new long[10][50];

    int[][] iterativeCountArray = new int[10][51];
    long[][] iterativeTimeArray = new long[10][50];

    //Creates the arrays and preforms the iterative and recursive sorts on them.
    private void createData() {
        
        //Runs 10 times for each different data size
        for (int n = 0; n < 10; n++) {
            //Creates the data sizes in increments of 100
            int arraySize = (n + 1) * 100;
            //Sets the first index of each array to the size of the array to keep track of it
            recursiveCountArray[n][0] = arraySize;
            iterativeCountArray[n][0] = arraySize;

            //Creates 50 data sets based on arraySize
            for (int i = 0; i < 50; i++) {

                //Creates two data sets, one for recursive, the other for iterative
                int[] recursiveArray = new int[arraySize];
                int[] iterativeArray = new int[arraySize];

                //Populates the arrays with random values
                for (int j = 0; j < recursiveArray.length; j++) {
                    //Every number randomly generated can have a value up to 2,147,483,647
                    int r = random.nextInt(Integer.MAX_VALUE);
                    //Ensures both data sets will have the same values in at the same index for control purposes
                    recursiveArray[j] = r;
                    iterativeArray[j] = r;
                }
                try {

                    insortion.recursiveSort(recursiveArray); //Perform recursive sort
                    recursiveCountArray[n][i + 1] = insortion.getCount();//Get count from recursive run
                    recursiveTimeArray[n][i] = insortion.getTime();//Get time from recursive run

                    insortion.iterativeSort(iterativeArray);//Perform recursive sort (also resets value of count and time elapsed)
                    iterativeCountArray[n][i + 1] = insortion.getCount();//Get count from iterative run
                    iterativeTimeArray[n][i] = insortion.getTime();//Get count from recursive run

                    //Exception is thrown when arrays are not sorted
                } catch (UnsortedException e) {
                    System.out.println(e);
                }
            }
        }
    }

    //Writes all data to a file for interpreting in BenchmarkReport
    private void dataToFile(String name, int[][] countArray, long[][] timeArray) throws IOException {

        BufferedWriter out = null;
        out = new BufferedWriter(new FileWriter(name));
        //Runs 10 times because there are 10 different data sizes
        for (int i = 0; i < 10; i++) {
            //Start each line with the size of the data set
            out.write(countArray[i][0] + ", ");
            //Adds writes the count value, then the time value
            for (int j = 0; j < 50; j++) {
                out.write(String.valueOf(countArray[i][j + 1]) + ", ");
                out.write(String.valueOf(timeArray[i][j]));
                //Unless it's the last loop, add a comma to seperate
                if (j < 49) {
                    out.write(", ");
                }
            }
            out.newLine();//New line for next loop
        }
        //End file writing
        out.flush();
        out.close();
    }

    //Simplifies the method to make it easier to call in name and distinguish from iterative
    public void recursionToFile() throws IOException {

        dataToFile("Recursive Results.txt", recursiveCountArray, recursiveTimeArray);

    }

    //Simplifies the method to make it easier to call in name and distinguish from recursive
    public void iterationToFile() throws IOException {

        dataToFile("Iterative Results.txt", iterativeCountArray, iterativeTimeArray);

    }

    public static void main(String[] args) {

        try {
            //Warms up the JVM by creating a class and then running createData 200 times 
            //Tried in intervals of 100, 200, 500, and 1000 and there was little to no difference above 200 so I stuck with 200
            for (int i = 0; i < 200; i++) {
                BenchmarkSorts warmup = new BenchmarkSorts();
                warmup.createData();
            }
            //After warmup is done, do one last run to write to file
            BenchmarkSorts b = new BenchmarkSorts();
            b.createData();
            b.recursionToFile();
            b.iterationToFile();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
