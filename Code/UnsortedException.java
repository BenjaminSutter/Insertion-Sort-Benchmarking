/*
 * File: UnsortedException.java
 * Author: Ben Sutter
 * Date: February 5th, 2020
 * Purpose: Create a named excption that will display the string passed if an exception is caught
 */

public class UnsortedException extends Exception {

    public UnsortedException(String errorMessage) {
        super(errorMessage);
    }

}
