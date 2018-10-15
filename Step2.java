import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Step2 {

  // button and LED states
  public static final enum PRIMARY_BUTTON_MODE{BLINKING, DIMMING}
  public static final enum LED_01_BLINK_INCREASE{ON, OFF}
  public static final enum LED_02_BRIGHTNESS{L1, L2, L3}
  public static final enum LED_01_BLINK_DELAY{2000, 1500, 1000, 500, 250}
  public static final enum LED_02_BLINK_DELAY{2000, 1500, 1000, 500, 250}

  // button and LED state variables
  public static PRIMARY_BUTTON_MODE primaryButtonMode;
  public static LED_01_BLINK_INCREASE blinkIncreaseLED1;
  public static LED_02_BRIGHTNESS brightnessLED2;
  public static LED_01_BLINK_DELAY blinkDelayLED1;
  public static LED_02_BLINK_DELAY blinkDelayLED2;

  // GPIO variables
  public static final GpioController gpio;
  public static final GpioPinDigitalInput primaryButton;
  public static final GpioPinDigitalInput toggleButton;
  public static final GpioPinDigitalOutput LED_01;
  public static final GpioPinDigitalOutput LED_02_L1;
  public static final GpioPinDigitalOutput LED_02_L2;
  public static final GpioPinDigitalOutput LED_02_L3;

  public Step2() {
    InitializeGPIO();
    System.out.println("GPIO Initialized.");
    InitializeStates();
    System.out.println("States Initialized.");
    InitializeButtonListeners();
    System.out.println("Button Listeners Initialized.");
  }

  public void InitializeGPIO() {
    // create gpio controller
    gpio = GpioFactory.getInstance();

    // provision gpio input pins with internal pull down resistors enabled
    primaryButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, PinPullResistance.PULL_DOWN);
    toggleButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_19, PinPullResistance.PULL_DOWN);

    // set shutdown states for button input pins
    primaryButton.setShutdownOptions(true);
    toggleButton.setShutdownOptions(true);

    // provision gpio output pins and turn on
    LED_01 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "LED_01", PinState.HIGH);
    LED_02_L1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "LED_02_L1", PinState.HIGH);
    LED_02_L2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20, "LED_02_L2", PinState.LOW);
    LED_02_L3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "LED_02_L3", PinState.LOW);

    // set shutdown states for LED pins
    LED_01.setShutdownOptions(true, PinState.LOW);
    LED_02_L1.setShutdownOptions(true, PinState.LOW);
    LED_02_L2.setShutdownOptions(true, PinState.LOW);
    LED_02_L3.setShutdownOptions(true, PinState.LOW);
  }

  public void InitializeStates() {
    // set initial button and LED states
    primaryButtonMode = PRIMARY_BUTTON_MODE.BLINKING;
    blinkIncreaseLED1 = LED_01_BLINK_INCREASE.ON;
    brightnessLED2 = LED_02_BRIGHTNESS.L3;
    blinkDelayLED1 = LED_01_BLINK_DELAY.2000;
    blinkDelayLED2 = LED_02_BLINK_DELAY.2000;
  }

  public void InitializeButtonListeners() {
    // create and register primary button gpio pin listener
    primaryButton.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display primary button pin state
        System.out.println("\nPRIMARY BUTTON GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

        // display state variables before changes
        System.out.println(" BEFORE:");
        System.out.println("   --> PRIMARY_BUTTON_MODE: " + primaryButtonMode);
        System.out.println("   --> LED_01_BLINK_INCREASE: " + blinkIncreaseLED1);
        System.out.println("   --> LED_02_BRIGHTNESS: " + brightnessLED2);

        // perform primary button action
        switch (primaryButtonMode) {
          case BLINKING:
            // update LED 1 blinking increase state
            switch (blinkIncreaseLED1) {
              case ON:
                blinkIncreaseLED1 = LED_01_BLINK_INCREASE.OFF;
              break;
              case OFF:
                blinkIncreaseLED1 = LED_01_BLINK_INCREASE.ON;
              break;
              default:
              break;
            }
          break;
          case DIMMING:
            // update LED 2 brightness level state
            switch (brightnessLED2) {
              case L1:
                brightnessLED2 = LED_02_BRIGHTNESS.L2;
              break;
              case L2:
                brightnessLED2 = LED_02_BRIGHTNESS.L3;
              break;
              case L3:
                brightnessLED2 = LED_02_BRIGHTNESS.L1;
              break;
              default:
              break;
            }
          break;
          default:
          break;
        }

        // display state variables after changes
        System.out.println(" AFTER:");
        System.out.println("   --> PRIMARY_BUTTON_MODE: " + primaryButtonMode);
        System.out.println("   --> LED_01_BLINK_INCREASE: " + blinkIncreaseLED1);
        System.out.println("   --> LED_02_BRIGHTNESS: " + brightnessLED2);
      }
    });

    // create and register toggle button gpio pin listener
    toggleButton.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display toggle button pin state
        System.out.println("\nTOGGLE BUTTON GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

        // display primary button mode before changes
        System.out.println(" BEFORE:");
        System.out.println("   --> PRIMARY_BUTTON_MODE: " + primaryButtonMode);

        // change the function of the primary button
        switch (primaryButtonMode) {
          case BLINKING:
            primaryButtonMode = PRIMARY_BUTTON_MODE.DIMMING;
          break;
          case DIMMING:
            primaryButtonMode = PRIMARY_BUTTON_MODE.BLINKING;
          break;
          default:
          break;
        }

        // display primary button mode after changes
        System.out.println(" AFTER:");
        System.out.println("   --> PRIMARY_BUTTON_MODE: " + primaryButtonMode);
      }
    });
  }

  private static void LED1() {
    while(true) {
      // pulse LED 1
      LED_01.pulse(400, true);

      // sleep
      try {
        Thread.sleep(blinkDelayLED1);
      } catch (InterruptedException ex) { }

      // update sleep delay
      switch (blinkIncreaseLED1) {
        case ON:
          // TODO: update sleep delay. Every 3 ticks or so it should iterate to the next delay.
        break;
        default:
        break;
      }
    }
  }

  private static void LED2() {
    while(true) {
      // pulse LED 2
      switch (brightnessLED2) {
        case L1:
          LED_02_L1.pulse(400, true);
        break;
        case L2:
          LED_02_L2.pulse(400, true);
        break;
        case L3:
          LED_02_L3.pulse(400, true);
        break;
        default:
        break;
      }

      // sleep
      try {
        Thread.sleep(blinkDelayLED2);
      } catch (InterruptedException ex) { }

      // TODO: update sleep delay. Every 3 ticks or so it should iterate to the next delay.
    }
  }

  public static void main(String args[]) throws InterruptedException {

    new Step2();

    // new thread for LED 1
    Thread LED1Thread = new Thread() {
      @Override
      public void run(){
        LED1();
      }
    };
    LED1Thread.start();
    System.out.println("LED1Thread Started.");

    // new thread for LED 2
    Thread LED2Thread = new Thread() {
      @Override
      public void run(){
        LED2();
      }
    };
    LED2Thread.start();
    System.out.println("LED2Thread Started.");

    // keep program running until user aborts (CTRL-C)
    while(true) {
        Thread.sleep(500);
    }

    // stop all GPIO activity/threads by shutting down the GPIO controller
    // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
    // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
  }
}
