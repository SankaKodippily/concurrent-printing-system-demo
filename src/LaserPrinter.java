/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

import utils.LoggerProcess;
import utils.Utility;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents the laser printer resource, which is shared within the printing system
 */
public class LaserPrinter implements ServicePrinter {
    private String printerId;
    private int paperLevel;
    private int tonerLevel;
    private int noOfDocumentsPrinted;
    private ThreadGroup userGroup;

    // Reentrant lock with fairness enabled
    private Lock lock = new ReentrantLock(true);
    private Condition condition = lock.newCondition();

    public LaserPrinter(String printerId, ThreadGroup userGroup) {
        this.printerId = printerId;
        this.paperLevel = Full_Paper_Tray;
        this.tonerLevel = Full_Toner_Level;
        this.noOfDocumentsPrinted = 0;
        this.userGroup = userGroup;
    }


    @Override
    public void replaceTonerCartridge() {
        lock.lock();
        try {
            // Repeatedly check for the need to replace toner cartridge, in 5 seconds time interval
            while (tonerLevel > Minimum_Toner_Level) {
                // Check if printer has finished serving all the userGroup
                if (isPrinterUsageComplete()) {
                    Utility.log(
                            LoggerProcess.PRINTER,
                            "Usage of the printer is complete. No need to replace toner cartridge",
                            false);
                    break;
                } else {
                    Utility.log(LoggerProcess.PRINTER, toString(), null);
                    Utility.log(
                            LoggerProcess.PRINTER,
                            "Toner has not reached the minimum level to be refilled. Waiting to check again",
                            false);
                    condition.await(5, TimeUnit.SECONDS);
                }
            }

            // Replace toner cartridge if necessary
            if (tonerLevel < Minimum_Toner_Level) {
                tonerLevel = Full_Toner_Level;
                Utility.log(
                        LoggerProcess.PRINTER,
                        "Replaced toner cartridge",
                        true);
                Utility.log(LoggerProcess.PRINTER, toString(), null);
            }

            condition.signalAll();
        } catch (InterruptedException e) {
            Utility.log(LoggerProcess.PRINTER, e.toString(), false);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void refillPaper() {
        lock.lock();
        try {
            // Repeatedly check for the need to refill paper, in 5 seconds time interval
            while (paperLevel + SheetsPerPack > Full_Paper_Tray) {
                if (isPrinterUsageComplete()) {
                    Utility.log(
                            LoggerProcess.PRINTER,
                            "Usage of the printer is complete. No need to refill paper",
                            false);
                    break;
                } else {
                    Utility.log(LoggerProcess.PRINTER, toString(), null);
                    Utility.log(
                            LoggerProcess.PRINTER,
                            "Refilling paper will exceed the capacity. Waiting to check again",
                            false);
                    condition.await(5, TimeUnit.SECONDS);
                }
            }

            // Refill paper if necessary
            if (paperLevel + SheetsPerPack <= Full_Paper_Tray) {
                paperLevel += SheetsPerPack;
                Utility.log(
                        LoggerProcess.PRINTER,
                        "Refilled paper",
                        true);
                Utility.log(LoggerProcess.PRINTER, toString(), null);
            }

            condition.signalAll();
        } catch (InterruptedException e) {
            Utility.log(LoggerProcess.PRINTER, e.toString(), false);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void printDocument(Document document) {
        lock.lock();

        try {
            Utility.log(LoggerProcess.PRINTER, toString(), null);

            // When resources are insufficient, wait until resources become sufficient
            while (paperLevel < document.getNumberOfPages() || tonerLevel < document.getNumberOfPages()) {
                String message;
                if (paperLevel < document.getNumberOfPages()) {
                    message = "Insufficient Papers. Waiting until refilled";
                } else if (tonerLevel < document.getNumberOfPages()) {
                    message = "Insufficient Toner level. Waiting until cartridge is replaced";
                } else {
                    message = "Insufficient Papers & Toner level. Waiting until they become available";
                }
                Utility.log(LoggerProcess.PRINTER, message, false);
                condition.await();
            }

            // Print document if resources are sufficient
            if (paperLevel >= document.getNumberOfPages() && tonerLevel >= document.getNumberOfPages()) {
                paperLevel -= document.getNumberOfPages();
                tonerLevel -= document.getNumberOfPages();
                noOfDocumentsPrinted++;
                Utility.log(
                        LoggerProcess.PRINTER,
                        "Printed document: " + document, true);
                Utility.log(LoggerProcess.PRINTER, toString(), null);
            }

            condition.signalAll();
        } catch (InterruptedException e) {
            Utility.log(LoggerProcess.PRINTER, e.toString(), false);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "[ PrinterID: " + printerId +
                ", Paper Level: " + paperLevel +
                ", Toner Level: " + tonerLevel +
                ", Documents Printed: " + noOfDocumentsPrinted + " ]";
    }

    /**
     * Checks whether the usage of the printer is over,
     * in order to avoid waiting in time intervals and re-checking for replacing toner cartridge / refilling paper
     *
     * @return Whether the usage of the printer is over or not
     */
    private boolean isPrinterUsageComplete() {
        return userGroup.activeCount() == 0;
    }
}
