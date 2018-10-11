public class Step2Test {

  public static int LED_01_COUNTER;
  public static int LED_02_COUNTER;

  public Step2Test() {
    System.out.println("Step2Test initializer calling InitializeStuff()");
    InitializeStuff();
  }

  public void InitializeStuff() {
    LED_01_COUNTER = 0;
    LED_02_COUNTER = 0;
    System.out.println("InitializeStuff() called.");
  }

  private static void LED1() {
    while(true) {
      // blink
      LED_01_COUNTER += 1;
      System.out.println("LED01 Blink #" + LED_01_COUNTER);

      // sleep
      try {
        System.out.println("LED1Thread sleeping for 3 seconds");
        Thread.sleep(3000);
      } catch (InterruptedException ex) { }
    }
  }

  private static void LED2() {
    while(true) {
      // blink
      LED_02_COUNTER += 1;
      System.out.println("LED02 Blink #" + LED_02_COUNTER);

      // sleep
      try {
        System.out.println("LED2Thread sleeping for 3.15 seconds");
        Thread.sleep(3150);
      } catch (InterruptedException ex) { }
    }
  }

  public static void main(String args[]) throws InterruptedException {

    System.out.println("Creating Step2Test.");
    new Step2Test();

    // new thread for LED 1
    Thread LED1Thread = new Thread() {
      @Override
      public void run(){
        LED1();
      }
    };
    System.out.println("Starting new thread for LED1.");
    LED1Thread.start();

    // new thread for LED 2
    Thread LED2Thread = new Thread() {
      @Override
      public void run(){
        LED2();
      }
    };
    System.out.println("Starting new thread for LED2.");
    LED2Thread.start();

    // keep program running until user aborts (CTRL-C)
    while(true) {
      System.out.println("--> main loop runnning.");
      Thread.sleep(1000);
    }
  }

}
