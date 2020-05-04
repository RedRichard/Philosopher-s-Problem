"""
    The following is an implementation of the solution provided by Dijkstra himself.
    This solution assigns a different priority to each fork; the fork with the highest
    priority will be the first one that a philosopher will try to take. Priority is
    defined by the lowest value between the two available choices.

    P: Philosopher
    F: Fork

    F1 - P1 - F2 - P2 - F3 - P3 - F4 - P4 - F5 - P5

    As each philosopher will try to take the fork with the lowest value, a list of two
    forks can be passed to each Philosopher object, the fork in index 0 being the one
    with the highest priority and the fork in index 1 having the lowest.
    Considering this, only the last Philosopher object will also place the same
    fork as the first Philosopher in the index 0 of its available forks.
"""

import sys
import getopt
import threading
import time
import random

# XML libraries
from xml.dom import minidom
import xml.etree.ElementTree as ET

from os import path

# Default values
num_philosophers = 5        # Default number of philosophers
min_thinking_time = 100     # Min thinking time: 100 ms
max_thinking_time = 800     # Max thinking time: 800 ms
min_eating_time = 100       # Min eating time: 100 ms
max_eating_time = 200       # Max eating time: 200 ms
default_exec_time = 30      # Default execution time: 30s
start_time = time.time()    # Time of program execution

philosophers_file_name = "philosophers.xml"     # Default xml file name


class Philosopher(threading.Thread):
    """
    This class contains the behavior implementation for each philosopher, using threads.
    """
    execution = True

    # Expected "forks" is a list of two elements
    def __init__(self, name, forks):
        """
        Constructor
        Input: 
        - name: list with names
        - forks: list lock objects (1 per name)
        """
        threading.Thread.__init__(self)
        self.name = name
        self.forks = forks

    def run(self):
        """
        Threading function. Runs the required methods to comply with
        expected philosopher behavior.
        "self.execution" is checked to confirm program is still in execution.
        """
        while(self.execution):
            self.think()
            self.take_forks()

    def take_forks(self):
        """
        Take forks function.
        The thread tries to acquire the two forks (lock objects) in its fork list.
        Once the eat() function has been executed, the forks can be released.
        """
        forks[0].acquire(True)
        print("{}s - {} takes fork 1.\n".format(
            time.time()-start_time, self.name))
        forks[1].acquire(True)
        print("{}s - {} takes fork 2.\n".format(
            time.time()-start_time, self.name))
        self.eat()
        forks[0].release()
        forks[1].release()

    def eat(self):
        """
        Eat function.
        Thread goes to sleep a random amount of time, limited
        by the default min and max eating times.
        """
        eating_time = random.uniform(
            min_eating_time, max_eating_time)
        print("{}s - {} eats for {}ms.\n".format(
            time.time()-start_time, self.name, eating_time))
        time.sleep(eating_time/1000)

    def think(self):
        """
        Eat function.
        Thread goes to sleep a random amount of time, limited
        by the default min and max thinking times.
        """
        think_time = random.uniform(
            min_thinking_time, max_thinking_time)
        print("{}s - {} thinks for {}ms.\n".format(
            time.time()-start_time, self.name, think_time))
        time.sleep(think_time/1000)
        print("{}s - {} is hungry.\n".format(
            time.time()-start_time, self.name))


def create_xml():
    """
    Function in charge of creating an xml file with default name
    values. Only ran when no number of philosopher and time of 
    execution is provided by the user, and no xml file is
    detected.
    """
    try:
        data = ET.Element('data')
        philosophers = ET.SubElement(data, 'philosophers')
        philosopher1 = ET.SubElement(philosophers, 'philosopher')
        philosopher2 = ET.SubElement(philosophers, 'philosopher')
        philosopher3 = ET.SubElement(philosophers, 'philosopher')
        philosopher4 = ET.SubElement(philosophers, 'philosopher')
        philosopher5 = ET.SubElement(philosophers, 'philosopher')
        philosopher1.text = "Aristotle"
        philosopher2.text = "Kant"
        philosopher3.text = "Buddha"
        philosopher4.text = "Marx"
        philosopher5.text = "Russel"

        # create a new XML file with the results
        myfile = ET.ElementTree(data)
        myfile.write(philosophers_file_name)
    except:
        raise


def readFile():
    """
    Function in carge of reading the contents of the xml file.
    Only ran when no number of philosopher and time of 
    execution is provided by the user, and no xml file is
    detected.
    OUTPUT: list with name data for each philosopher
    """
    try:
        mydoc = minidom.parse(philosophers_file_name)
    except Exception:
        create_xml()
        mydoc = minidom.parse(philosophers_file_name)
    finally:
        philosophers = mydoc.getElementsByTagName('philosopher')
        return [philosopher.firstChild.data for philosopher in philosophers]


if __name__ == "__main__":
    """
    Main function
    """
    # Argument logic and handling:
    args = sys.argv

    """
    Try to get usable valuables from arguments provided by the user.
    Except: finish program and print message.
    """
    try:
        if(len(args) > 1):
            default_exec_time = int(args[1])
            num_philosophers = int(args[2])
            philosopherNames = [i for i in range(1, num_philosophers+1)]
        else:
            # Generate names if default case is detected:
            philosopherNames = readFile()
    except:
        print("Invalid options. Use:")
        print('solucion.py <exec_time> <num_philosophers>')
        sys.exit()

    # Creating lock objects to be used as resources (forks):
    forks = [threading.Lock() for n in range(num_philosophers)]

    # Populating a list with philosophers:
    philosophers = [Philosopher(philosopherNames[i], forks[i:i+2])
                    for i in range(num_philosophers-1)]

    # Special case: Adding last philosopher to list. (List Fork order changes)
    philosophers += [Philosopher(philosopherNames[-1], [forks[0], forks[-1]])]

    # Thread execution can begin
    Philosopher.execution = True
    for philosopher in philosophers:
        philosopher.start()

    time.sleep(default_exec_time)

    # Thread execution stops
    Philosopher.execution = False
