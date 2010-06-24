# Introduction

An implementation of the java.util.Queue interface using only stacks internally. In order to maintain performant for both poll and offer operations, two stacks are maintained internally -- poll and offer. When being offerred elements, all elements in poll stack are flipped onto the offer stack, and the new element is pushed onto the offer stack. When being polled, all elements in the offer stack are flipped onto the poll stack, and the head element is popped off of the poll stack. At any given time, all elements are completely contained in either the offer (in order of insertion) or poll (in reverse order of insertion) stack. This approach is tuned to be performant when a large number of sequential offer operations are followed by a large number of poll operations.

Unit and performance test suites are provided to verify the proper operation of the implementation and the performance of it under the conditions described above.

# Requirements

The following are the requirements to build and run QueueStack and its associated test suites:

   * Java 1.5 or above
   * Ant 1.7 or above

It should run on Linux and Windows-based systems.  However, it has only been tested on Mac OS  10.5.

# Building and Running

From the root directory of the project, execute the following command:

	ant clean test

This action will result in output similar to following:

	Buildfile: build.xml

	clean:
	   [delete] Deleting directory /Users/jburwell/Documents/workspace/queue-stack/build

	init:
	    [mkdir] Created dir: /Users/jburwell/Documents/workspace/queue-stack/build

	compile:
	    [mkdir] Created dir: /Users/jburwell/Documents/workspace/queue-stack/build/classes
	    [javac] Compiling 1 source file to /Users/jburwell/Documents/workspace/queue-stack/build/classes

	test:
	    [javac] Compiling 3 source files to /Users/jburwell/Documents/workspace/queue-stack/build/classes
	     [echo] Running Unit Tests
	   [testng] [Parser] Running:
	   [testng]   Ant suite
	   [testng] 
	   [testng] 
	   [testng] ===============================================
	   [testng] Ant suite
	   [testng] Total tests run: 14, Failures: 0, Skips: 0
	   [testng] ===============================================
	   [testng] 
	     [echo] Running Performance Tests
	   [testng] [Parser] Running:
	   [testng]   Ant suite
	   [testng] 
	   [testng] Offer for 1 elements is 95000 nanoseconds per element and 95000 nanoseconds total.
	   [testng] Poll for 1 elements is 14000 nanoseconds per element and 14000 nanoseconds total
	   [testng] Offer for 10 elements is 2000 nanoseconds per element and 20000 nanoseconds total.
	   [testng] Poll for 10 elements is 2600 nanoseconds per element and 26000 nanoseconds total
	   [testng] Offer for 100 elements is 1010 nanoseconds per element and 101000 nanoseconds total.
	   [testng] Poll for 100 elements is 3460 nanoseconds per element and 346000 nanoseconds total
	   [testng] Offer for 1000 elements is 1565 nanoseconds per element and 1565000 nanoseconds total.
	   [testng] Poll for 1000 elements is 3179 nanoseconds per element and 3179000 nanoseconds total
	   [testng] Offer for 10000 elements is 171 nanoseconds per element and 1713000 nanoseconds total.
	   [testng] Poll for 10000 elements is 752 nanoseconds per element and 7526000 nanoseconds total
	   [testng] 
	   [testng] ===============================================
	   [testng] Ant suite
	   [testng] Total tests run: 5, Failures: 0, Skips: 0
	   [testng] ===============================================
	   [testng] 

	BUILD SUCCESSFUL
	Total time: 3 seconds
