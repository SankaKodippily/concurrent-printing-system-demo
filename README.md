# Concurrent Printing System - Demo

Demonstration of a concurrent printing system, that has several students that share a printer to print documents, and two technicians that refill it with paper and toner cartridges.

Contents:
* [Java Code](src)
* [FSP Processes](Printer.lts)

The Java code uses Re-entrant Locks. [PrintingSystem](src/PrintingSystem.java) class has the main method, which can be used to run the demo.

The corresponding FSP processes are as follows: 

```javascript
const MAX_SHEETS = 3 // Maximum number of sheets in a printer

set PRINT_ACTIONS = {acquirePrint, print, acquireRefill, refill, release}

// Initialise printer with given number of sheets and continue
PRINTER (PAPER_COUNT = MAX_SHEETS) = PRINTER[PAPER_COUNT],
PRINTER[p : 0..PAPER_COUNT] = 
	if (p > 0)
	then (acquirePrint -> print -> release -> PRINTER[p-1])
	else (acquireRefill -> refill -> release -> PRINTER[MAX_SHEETS]).

// Initialise student and print given number of documents
STUDENT(DOCUMENT_COUNT = 1) = STUDENT[DOCUMENT_COUNT],
STUDENT[d : 1..DOCUMENT_COUNT] = (
	acquirePrint -> print[d] ->
		if (d > 1)
		then (release -> STUDENT[d-1])
		else (release -> END) // Prints the last document
) + PRINT_ACTIONS / {print/print[d:1..DOCUMENT_COUNT]}. // Exposes print[d] actions as just "print"

TECHNICIAN = (
	acquireRefill -> refill -> release -> TECHNICIAN |
	wait -> TECHNICIAN
) + PRINT_ACTIONS.

||PRINTING_SYSTEM = (s3: STUDENT(3) || s2: STUDENT(2) || t: TECHNICIAN || {s3, s2, t} :: PRINTER).

```

Implemented for the coursework of the module: _Concurrent Programming (6SENG002W)_ in 2018, at Informatics Institute of Technology (IIT), Sri Lanka, in collaboration with University of Westminster.

Author: Senthuran Ambalavanar ([github.com/senthuran16](https://github.com/senthuran16))