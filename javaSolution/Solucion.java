
/*
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
*/

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class Philosopher extends Thread {
  /*
   * This class contains the behavior implementation for each philosopher, using
   * threads.
   */

  String name;
  ArrayList<ReentrantLock> forks;
  static public boolean execution = false;
  final long startTime = System.currentTimeMillis();

  // Default values:
  long minThinkingTime = 100L;
  long maxThinkingTime = 800L;
  long minEatingTime = 100L;
  long maxEatingTime = 200L;

  Philosopher(String name, ArrayList<ReentrantLock> forks) {
    /*
     * Constructor Input: - name: list with names - forks: list lock objects (1 per
     * name)
     */
    this.name = name;
    this.forks = forks;
  }

  public void run() {
    /*
     * Threading function. Runs the required methods to comply with expected
     * philosopher behavior. "self.execution" is checked to confirm program is still
     * in execution.
     */
    while (execution) {
      try {
        think();
        takeForks();
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void takeForks() throws InterruptedException {
    /*
     * Take forks function. The thread tries to acquire the two forks (lock objects)
     * in its fork list. Once the eat() function has been executed, the forks can be
     * released.
     */
    forks.get(0).lock();
    System.out.println(String.format("%d ms - %s takes fork 1.", (System.currentTimeMillis() - startTime), this.name));
    forks.get(1).lock();
    System.out.println(String.format("%d ms - %s takes fork 2.", (System.currentTimeMillis() - startTime), this.name));
    eat();
    forks.get(0).unlock();
    forks.get(1).unlock();
  }

  private void eat() throws InterruptedException {
    /*
     * Eat function. Thread goes to sleep a random amount of time, limited by the
     * default min and max eating times.
     */
    long generatedLong = minEatingTime + (long) (Math.random() * (maxEatingTime - minEatingTime));
    System.out.println(String.format("%d ms - %s eats for %d ms.", (System.currentTimeMillis() - startTime), this.name,
        generatedLong));
    Thread.sleep(generatedLong);
  }

  private void think() throws InterruptedException {
    /*
     * Eat function. Thread goes to sleep a random amount of time, limited by the
     * default min and max thinking times.
     */
    long generatedLong = minThinkingTime + (long) (Math.random() * (maxThinkingTime - minThinkingTime));
    System.out.println(String.format("%d ms - %s thinks for %d ms.", (System.currentTimeMillis() - startTime),
        this.name, generatedLong));
    Thread.sleep(generatedLong);
    System.out.println(String.format("%d ms - %s is hungry.", (System.currentTimeMillis() - startTime), this.name));
  }
}

class FileXML {
  public static void create(String xmlFilePath) {
    /*
     * Function in charge of creating an xml file with default name values. Only ran
     * when no number of philosopher and time of execution is provided by the user,
     * and no xml file is detected.
     */
    try {
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
      // root element
      Element root = document.createElement("data");
      document.appendChild(root);

      // philosophers element:
      Element philosophers = document.createElement("philosophers");
      root.appendChild(philosophers);

      // individual philosophers:
      Element philosopher1 = document.createElement("philosopher");
      philosopher1.appendChild(document.createTextNode("Aristotle"));
      philosophers.appendChild(philosopher1);

      Element philosopher2 = document.createElement("philosopher");
      philosopher2.appendChild(document.createTextNode("Kant"));
      philosophers.appendChild(philosopher2);

      Element philosopher3 = document.createElement("philosopher");
      philosopher3.appendChild(document.createTextNode("Buddha"));
      philosophers.appendChild(philosopher3);

      Element philosopher4 = document.createElement("philosopher");
      philosopher4.appendChild(document.createTextNode("Marx"));
      philosophers.appendChild(philosopher4);

      Element philosopher5 = document.createElement("philosopher");
      philosopher5.appendChild(document.createTextNode("Russel"));
      philosophers.appendChild(philosopher5);

      // create the xml file
      // transform the DOM Object to an XML File
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(document);
      StreamResult streamResult = new StreamResult(new File(xmlFilePath));

      // If you use
      // StreamResult result = new StreamResult(System.out);
      // the output will be pushed to the standard output ...
      // You can use that for debugging
      transformer.transform(domSource, streamResult);
      System.out.println("Done creating XML File");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<String> readFile(String fileName) {
    /*
     * Function in carge of reading the contents of the xml file. Only ran when no
     * number of philosopher and time of execution is provided by the user, and no
     * xml file is detected. OUTPUT: list with name data for each philosopher
     */
    ArrayList<String> names = new ArrayList<String>();
    try {
      File inputFile = new File(fileName);
      if (!inputFile.exists()) {
        create(fileName);
      }

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();
      NodeList nList = doc.getElementsByTagName("philosopher");

      for (int i = 0; i < nList.getLength(); i++) {
        Node nNode = nList.item(i);
        Element eElement = (Element) nNode;
        names.add(eElement.getTextContent());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return names;
  }
}

public class Solucion {
  // Main Class

  public static void main(String[] args) {
    int numPhilosophers = 5;
    int defaultExecTime = 30000;
    final String defaultFileName = "philosophers.xml";

    ArrayList<ReentrantLock> forks = new ArrayList<ReentrantLock>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Philosopher> philosophers = new ArrayList<Philosopher>();

    // Argument logic and handling
    /*
     * Try to get usable valuables from arguments provided by the user. Except:
     * finish program and print message.
     */
    try {
      if (args.length > 1) {
        defaultExecTime = Integer.parseInt(args[0]) * 1000;
        numPhilosophers = Integer.parseInt(args[1]);
        for (int i = 0; i < numPhilosophers; i++) {
          names.add(Integer.toString(i));
        }
      } else {
        // Generate names if default case is detected:
        names = FileXML.readFile(defaultFileName);
      }
    } catch (Exception e) {
      System.out.println("Invalid options. Use:");
      System.out.println("Solucion <exec_time> <num_philosophers>");
      System.exit(0);
    }

    // Creating lock objects to be used as resources (forks):
    for (int i = 0; i < numPhilosophers; i++) {
      forks.add(new ReentrantLock());
    }

    // Populating a list with philosophers:
    for (int i = 0; i < numPhilosophers - 1; i++) {
      ArrayList<ReentrantLock> auxForks = new ArrayList<ReentrantLock>(forks.subList(i, i + 2));
      philosophers.add(new Philosopher(names.get(i), auxForks));
    }

    // Special case: Adding last philosopher to list. (List Fork order changes)
    ArrayList<ReentrantLock> auxForks2 = new ArrayList<ReentrantLock>();
    auxForks2.add(forks.get(0));
    auxForks2.add(forks.get(forks.size() - 1));
    philosophers.add(new Philosopher(names.get(philosophers.size()), auxForks2));

    // Thread execution can begin
    Philosopher.execution = true;
    for (int i = 0; i < philosophers.size(); i++) {
      // System.out.println(philosophers.get(i).name);
      philosophers.get(i).start();
    }
    try {
      Thread.sleep(defaultExecTime);
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }

    // Thread execution stops
    Philosopher.execution = false;
  }
}