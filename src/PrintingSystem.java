/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

import utils.LoggerProcess;
import utils.Utility;

/**
 * Represents the entire printing system, that is composed with a printer(resource), students, and technicians
 */
public class PrintingSystem {
    public static void main(String[] args) {
        // Create thread groups
        ThreadGroup students = new ThreadGroup("students");
        Utility.log(
                LoggerProcess.PRINTING_SYSTEM,
                "Created ThreadGroup: \"" + students.getName() + "\"",
                null);

        ThreadGroup technicians = new ThreadGroup("technicians");
        Utility.log(
                LoggerProcess.PRINTING_SYSTEM,
                "Created ThreadGroup: \"" + technicians.getName() + "\"",
                null);

        // Create printer (resource)
        LaserPrinter printer = new LaserPrinter("printer001", students);
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Initialised Printer", true);

        // Create student threads
        Student student1 = new Student("John", students, printer);
        Utility.log(
                LoggerProcess.PRINTING_SYSTEM,
                "Initialised student: \"" + student1.getName() + "\"",
                null);

        Student student2 = new Student("Doe", students, printer);
        Utility.log(
                LoggerProcess.PRINTING_SYSTEM,
                "Initialised student: \"" + student2.getName() + "\"",
                null);

        Student student3 = new Student("Tom", students, printer);
        Utility.log(
                LoggerProcess.PRINTING_SYSTEM,
                "Initialised student: \"" + student3.getName() + "\"",
                null);

        Student student4 = new Student("Harry", students, printer);
        Utility.log(
                LoggerProcess.PRINTING_SYSTEM,
                "Initialised student: \"" + student4.getName() + "\"",
                null);

        // Create technician threads
        Technician paperTechnician = new PaperTechnician("PaperTechnician", technicians, printer);
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Initialised paper technician", null);

        Technician tonerTechnician = new TonerTechnician("TonerTechnician", technicians, printer);
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Initialised toner technician", null);

        // Start all the threads
        student1.start();
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Started student: \"" + student1.getName() + "\"", true);

        student2.start();
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Started student: \"" + student2.getName() + "\"", true);

        student3.start();
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Started student: \"" + student3.getName() + "\"", true);

        student4.start();
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Started student: \"" + student4.getName() + "\"", true);

        paperTechnician.start();
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Started paper technician", true);

        tonerTechnician.start();
        Utility.log(LoggerProcess.PRINTING_SYSTEM, "Started toner technician", true);

        try {
            student1.join();
            Utility.log(LoggerProcess.PRINTING_SYSTEM, student1.getName() + " completed execution", true);
            student2.join();
            Utility.log(LoggerProcess.PRINTING_SYSTEM, student2.getName() + " completed execution", true);
            student3.join();
            Utility.log(LoggerProcess.PRINTING_SYSTEM, student3.getName() + " completed execution", true);
            student4.join();
            Utility.log(LoggerProcess.PRINTING_SYSTEM, student4.getName() + " completed execution", true);
            paperTechnician.join();
            Utility.log(LoggerProcess.PRINTING_SYSTEM, "Paper technician completed execution", true);
            tonerTechnician.join();
            Utility.log(LoggerProcess.PRINTING_SYSTEM, "Toner technician completed execution", true);

            Utility.log(LoggerProcess.PRINTING_SYSTEM, "Tasks completed. " + printer.toString(), true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
