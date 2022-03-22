package synchronization;

public class ThreadDemo extends Thread 
{

  private Thread thread;
  private String name;
  PrintDemo printDemo; /* This is the shared object between threads */

  ThreadDemo( String name,  PrintDemo printDemo){
       this.name = name;
        printDemo = printDemo;
  }
  public void run() {
	/*We use the keyword synchronized over the object printDemo
	* because printDemo is a shared object and we want to access
	* it atomically, ie. one thread only accesses the shared
	* object at a time, thus, avoiding interleaving threads
	* which may lead to inconsistent state of the shared object,
	* data structure etc
	*/


    synchronized(printDemo) {
      printDemo.printCount();
    }
    System.out.println("Thread " + name + " exiting.");
  }

  public void start ()
  {
	/*Just a showcase that we can also overload the start
	* method, otherwise use 
	* "AThreadObjectYouCreated.start()"
	*/
    System.out.println("Starting " + name);
    if (thread == null)
    {
      thread = new Thread (this, name);
      thread.start ();
    }
  }
}

--------------------------------------------------------------------------------

package synchronization;

class PrintDemo {

   public void printCount(){
    try {
         for(int i = 5; i > 0; i--) {
            System.out.println("Counter   ---   "  + i );
         }
     } catch (Exception e) {
         System.out.println("Thread  interrupted.");
     }
   }
}

--------------------------------------------------------------------------------
package threadCreation;

public class SimpleThread extends Thread {
  String name;

  public SimpleThread(String input) {
    this.name = input;
  }

  public void run() {
    for (int i = 0; i < 10; i++) {
      System.out.println(name + " " + i);
      try {
        sleep((int) (Math.random() * 500));
      } catch (InterruptedException e) {}
    }
  }

  public static void main(String[] args) {
    SimpleThread thread1 = new SimpleThread("Thread1");
    SimpleThread thread2 = new SimpleThread("Thread2");


    /* Wrong way to start threads - NEVER DO*/
    //new Thread(thread1).run();
    //new Thread(thread2).run();
    /* Correct way to start threads */
    new Thread(thread1).start();
    new Thread(thread2).start();


    
    /* PROSOXH OXI:*/
  //  t = new threadCreation.SimpleThread("Distributed");
  //  t.run(); //runs like method, first completes then returns
  //  t2 = new threadCreation.SimpleThread("Systems");
  //  t2.run();
  //  Calling run will be considered as method
  //  and you must wait to complete
  }
}


--------------------------------------------------------------------------------

package threadCreation;

public class SimpleThreadRunnable implements Runnable {
  String name;

  public SimpleThreadRunnable(String name) {
    this.name = name;
  }

  public void run() {
    for (int i = 0; i < 10; i++) {
      System.out.println(name + " " + i);
      try {
        Thread.sleep((int) (Math.random() * 500));
      } catch (InterruptedException e) {}
    }
  }

  public static void main(String[] args) {
    SimpleThreadRunnable thread1 = new SimpleThreadRunnable("Thread1");
    SimpleThreadRunnable thread2 = new SimpleThreadRunnable("Thread2");


    /* Wrong way to start threads - NEVER DO*/
    //new Thread(thread1).run();
    //new Thread(thread2).run();
    /* Correct way to start threads */
    new Thread(thread1).start();
    new Thread(thread2).start();
    // We create a thread object giving a runnable as a parameter
    // and we call the start method of the created object



  }
}


--------------------------------------------------------------------------------
package threadCreation;

class TestMultiPriority extends Thread {
    String name;

    public TestMultiPriority(String s) {
        this.name = s;
    }

    public void run() {
        System.out.println("running thread name is: " + Thread.currentThread().getName());
        System.out.println("running thread priority is:" + Thread.currentThread().getPriority() + " " + Thread.currentThread().getName());

        for (int i = 0; i < 10; i++) {
            System.out.println(name + " " + i);
            try {
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException e) {}
        }
    }

    public static void main(String args[]) {
        TestMultiPriority thread1 = new TestMultiPriority("Thread 1");
        TestMultiPriority thread2 = new TestMultiPriority("Thread 2");

        thread1.setPriority(Thread.MIN_PRIORITY);
        thread2.setPriority(Thread.MAX_PRIORITY);

        thread1.start();
        System.out.println("Thread1 started...");

//        try {
//            thread1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("");

        thread2.start();
        System.out.println("Thread2 started...");
    }
}     