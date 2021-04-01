/*
 * File: BenchmarkReport.java
 * Author: Ben Sutter
 * Date: February 5th, 2020
 * Purpose: Read a file genrated by BenchmarkSorts.java, preform mean and coefficient of variances calculations.
 * After preforming the calculations, display all data on a JTable
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class BenchmarkReport {

    boolean isEmpty = true;//Won't show GUI and will print message if file is empty
    String fileName;//Used to name the JTable window
    double[][] dataArray = new double[10][5]; //Holds all the data values to convert to string
    String[][] dataStringArray = new String[10][5]; //Holds all the converted values to show in JTable

    //Code for JTable taken from: https://www.geeksforgeeks.org/java-swing-jtable/
    JFrame frame;
    JTable table;

    public BenchmarkReport() {
        //Opens the file which populates dataArray
        openFile();

        //If file is empty or blank, inform the user. Otherwise, create JTable and GUI
        if (isEmpty) {
            System.out.println("File not chosen or file is empty");
        } else {
            //Fills dataStringArray with all the values to store in the JTable
            populateStringArray();

            // Frame initiallization 
            frame = new JFrame();
            // Set's frame title based off of what the name of the file opened was 
            frame.setTitle(fileName + "'s Benchmark Report");
            // Column Names 
            String[] columnNames = {"Size", "Avg Count", "Coef Count", "Avg Time", "Coef Time"};
            // Initializing the JTable 
            table = new JTable(dataStringArray, columnNames);
            table.setBounds(30, 40, 200, 300);
            // adding it to JScrollPane 
            JScrollPane sp = new JScrollPane(table);
            frame.add(sp);
            // Frame Size 
            frame.setSize(500, 225);
            // Frame Visible = true 
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

    }//End GUI

    //Given the 2D double array that holds the values, convert them to a string array for displaying
    public String[] dataToTable(double[] values) {
        DecimalFormat df = new DecimalFormat("#0.00");//Limits decimil places to 2
        String[] dataStrings = new String[5];//Creates a string array to hold data
        //Sets the first value of the array to the size of data set, stripped of decimal points
        dataStrings[0] = new DecimalFormat("#").format(values[0]);
        for (int i = 1; i < values.length; i++) {

            //If index is even, it is the coef count so add a percent sign.
            if (i % 2 == 0) {
                dataStrings[i] = df.format(values[i]) + "%";
            } else {
                dataStrings[i] = df.format(values[i]);
            }

        }
        return dataStrings;
    }

    //Populates the 2d string array to display data (5 x 10
    public void populateStringArray() {
        for (int i = 0; i < dataStringArray.length; i++) {
            dataStringArray[i] = dataToTable(dataArray[i]);
        }
    }

    //Adds all numbers in array and then divides them by length to find mean
    public double getMean(double[] array) {
        double totalSum = 0;
        for (int i = 0; i < array.length; i++) {
            totalSum += array[i];
        }
        return totalSum / array.length;
    }

    //Calculates standard deviation, code found from https://www.programiz.com/java-programming/examples/standard-deviation
    public double getStandardDeviation(double[] array) {
        double standardDev = 0;
        double mean = getMean(array);
        for (double num : array) {
            standardDev += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDev / (array.length - 1));
    }

    //Finds Coefficient of Variation, th formula is (Standard Deviation/Mean) * 100
    public double getCoefficientOfVariation(double[] array) {
        double mean = getMean(array);
        double standardDeviation = getStandardDeviation(array);
        return (standardDeviation / mean) * 100;
    }

    private void openFile() {
        try {
            //Create a JFileChooser so the user can select which file to parse.
            JFileChooser jfc = new JFileChooser("This doesn't matter");
            jfc.setCurrentDirectory(new File("."));
            int userApproval = jfc.showOpenDialog(null);//Initializes variable for approval later on
            if (userApproval == JFileChooser.APPROVE_OPTION) {//Changes userApproval to start program if a file is chosen
                File theChosenOne = jfc.getSelectedFile();
                BufferedReader reader = new BufferedReader(new FileReader(theChosenOne));//Creates new reader to read the selected file
                Scanner dataReader = new Scanner(reader);

                //Gets name of selected file to show in GUI
                fileName = theChosenOne.getName();
                //Creates arrays to store incoming values, after each loop their values are overriden
                double[] countArray = new double[50];
                double[] timeArray = new double[50];
                //Keeps track of loops to determine where in the 2d array everything should go. Typecasted to double for easier tracking and parameter passing.
                int count = 0;
                while (dataReader.hasNext()) {
                    isEmpty = false;
                    //Grab each line from the file, strip it of white space
                    String incomingLine = dataReader.nextLine().replaceAll("\\s", "");
                    //Breaks up the string into integers, Line of code found from https://stackoverflow.com/a/35765098
                    int[] numbers = Arrays.stream(incomingLine.split(",")).mapToInt(Integer::parseInt).toArray();

                    //Indexes are used to keep track of indexes of individual arrays because the array size is 100, and each array is only supposed to get 50 values
                    int countIndex = 0;
                    int timeIndex = 0;
                    //Loop starts at 1 to skip index 0 (holds size of data sets), if index is even, move variable to countArray, if odd go to timeArray
                    for (int i = 1; i < 101; i++) {
                        if (i % 2 == 0) {
                            //If int is even, add it to timeArray then increase index
                            timeArray[countIndex] = numbers[i];
                            countIndex++;
                        } else {
                            //If int is odd, add it to countArray then increase index
                            countArray[timeIndex] = numbers[i];
                            timeIndex++;
                        }
                    }
                    //Fills up the 2d dataStringArray array with values
                    dataArray[count][0] = numbers[0];//Size of array, first character on each line
                    dataArray[count][1] = getMean(countArray);//mean of countArray
                    dataArray[count][2] = getCoefficientOfVariation(countArray);//coefficient of variation for countArray
                    dataArray[count][3] = getMean(timeArray);//mean of timeArray
                    dataArray[count][4] = getCoefficientOfVariation(timeArray);//coefficient of variation for TimeArray

                    //Increase count to keep track of which index the values are going to
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Problem with file \n" + e);//Prints the exception message
        }
    }

    public static void main(String[] args) {
        BenchmarkReport br = new BenchmarkReport();
    }

}
