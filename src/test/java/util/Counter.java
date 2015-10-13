package util;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 13.10.2015
 */
public class Counter {
    private static int counter = 0;

    public static synchronized int getNextNumber() {
        return counter++;
    }
}