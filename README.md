# Philosopher's Problem
An implementation of the solution provided by Dijkstra for the Philosopher's Problem.

This solution assigns a different priority to each fork; the fork with the highest priority 
will be the first one that a philosopher will try to take. Priority is defined by the lowest 
value between the two available choices.

  P: Philosopher
  F: Fork
    
  F1 - P1 - F2 - P2 - F3 - P3 - F4 - P4 - F5 - P5

As each philosopher will try to take the fork with the lowest value, a list of two forks 
can be passed to each Philosopher object, the fork in index 0 being the one with the 
highest priority and the fork in index 1 having the lowest. Considering this, only the
last Philosopher object will also place the same fork as the first Philosopher in the 
index 0 of its list of available forks.

## Python solution:

Run using the **Python 3** interpreter.

The user can input arguments:
**python3 filosofos.py <exec_time> <num_philosophers>**

Arguments must be integers. An error is displayed otherwise.

If no argument is provided, the program runs using a default number of 5 philosophers, whose
names a read from the xml file: **philosophers.xml**. If no file exists, a new one is created.

![Python Execution](https://github.com/RedRichard/Philosopher-s-Problem/blob/master/programScreenshots/pythonExecution.png)

## Java solution:
Existing compiled files created with: **javac 1.8.0_252**

Compile using the **makefile**, with the command **make** in a terminal.
Run using: **java Solucion**

File can also be compiled directly using: **javac Solucion.java**

The user can input arguments:
**java Solucion <exec_time> <num_philosophers>**

Arguments must be integers. An error is displayed otherwise.

If no argument is provided, the program runs using a default number of 5 philosophers, whose
names a read from the xml file: **philosophers.xml**. If no file exists, a new one is created.

![Java Execution](https://github.com/RedRichard/Philosopher-s-Problem/blob/master/programScreenshots/javaExecution.png)
