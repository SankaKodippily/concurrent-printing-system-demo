/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

/**
 * Represents a Technician who overlooks the printer
 */
public abstract class Technician extends Thread {
    protected Printer printer;

    public Technician(String name, ThreadGroup threadGroup, Printer printer) {
        super(threadGroup, name);
        this.printer = printer;
    }
}
