/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

import utils.LoggerProcess;
import utils.Utility;

/**
 * Represents the toner technician, who replaces the cartridge of the printer
 */
public class TonerTechnician extends Technician {
    public TonerTechnician(String name, ThreadGroup threadGroup, Printer printer) {
        super(name, threadGroup, printer);
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                Utility.log(
                        LoggerProcess.TONER_TECHNICIAN,
                        "Requested to replace toner",
                        null);

                ((LaserPrinter) printer).replaceTonerCartridge();

                Utility.log(
                        LoggerProcess.TONER_TECHNICIAN,
                        "Printer status. " + printer.toString(),
                        null);
                sleep(Utility.generateRandomDuration());
            } catch (InterruptedException e) {
                Utility.log(LoggerProcess.TONER_TECHNICIAN, e.toString(), false);
            }
        }
    }
}
