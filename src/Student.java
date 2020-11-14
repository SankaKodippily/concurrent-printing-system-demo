/*
Author: Senthuran Ambalavanar | 2015215 | w1608452
 */

import utils.LoggerProcess;
import utils.Utility;

import java.util.Random;

/**
 * Represents a student who uses the printer to print documents
 */
public class Student extends Thread {
    private Printer printer;

    public Student(String name, ThreadGroup threadGroup, Printer printer) {
        super(threadGroup, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            String documentName = "doc_" + (i + 1);
            Document document = new Document(
                    generateDocumentID(this.getName(), documentName),
                    documentName,
                    generateRandomPageCount());

            Utility.log(
                    LoggerProcess.STUDENT,
                    "[" + this.getName() + "] Requested to print: " + document,
                    null);

            printer.printDocument(document);

            Utility.log(
                    LoggerProcess.STUDENT,
                    "Printer status. " + printer.toString(),
                    null);
            try {
                sleep(Utility.generateRandomDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates document id with the given student name and the document name
     *
     * @param studentName  Name of the student
     * @param documentName Name of the document
     * @return Document ID
     */
    private String generateDocumentID(String studentName, String documentName) {
        return studentName + "_" + documentName;
    }

    /**
     * Generates a random page count, within the range of 1 to 20 (inclusive), for documents
     *
     * @return Random page count
     */
    private int generateRandomPageCount() {
        return new Random().nextInt(19) + 1;
    }
}
