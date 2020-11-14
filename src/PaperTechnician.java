/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

import utils.LoggerProcess;
import utils.Utility;

/**
 * Represents the paper technician, who refills the printer with papers
 */
public class PaperTechnician extends Technician {
    public PaperTechnician(String name, ThreadGroup threadGroup, Printer printer) {
        super(name, threadGroup, printer);
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                Utility.log(
                        LoggerProcess.PAPER_TECHNICIAN,
                        "Requested to refill paper",
                        null);

                ((LaserPrinter) printer).refillPaper();

                Utility.log(
                        LoggerProcess.PAPER_TECHNICIAN,
                        "Printer status. " + printer.toString(),
                        null);
                sleep(Utility.generateRandomDuration());
            } catch (InterruptedException e) {
                Utility.log(LoggerProcess.PAPER_TECHNICIAN, e.toString(), false);
            }
        }
    }
}
