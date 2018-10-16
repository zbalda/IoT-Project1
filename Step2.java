import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.util.CommandArgumentParser;

public class Step2 {

  // button and LED states
  public static enum PRIMARY_BUTTON_MODE{BLINKING, DIMMING};
  public static enum LED_01_BLINK_INCREASE{ON, OFF};
  public static enum LED_02_BRIGHTNESS{L1, L2, L3, L4};
  public static final int[] LED_01_BLINK_DELAY = {800, 600, 400, 250, 125};
  public static final int[] LED_02_BLINK_DELAY = {800, 600, 400, 250, 125};
  public static final int LED_BLINK_COUNTER_MAX = 3;

  // button and LED state variables
  public static PRIMARY_BUTTON_MODE primaryButtonMode;
  public static LED_01_BLINK_INCREASE blinkIncreaseLED1;
  public static LED_02_BRIGHTNESS brightnessLED2;
  public static int blinkDelayIndexLED1;
  public static int blinkDelayIndexLED2;
  public static int blinkCounterLED1;
  public static int blinkCounterLED2;
  public static int blinkTime;

  // GPIO variables
  public static GpioController gpio;
  public static GpioPinDigitalInput primaryButton;
  public static GpioPinDigitalInput toggleButton;
  public static GpioPinDigitalOutput LED_01;
  public static Pin LED2pin;
  public static GpioPinPwmOutput LED2pwm;

  public Step2() {
    System.out.println("Initializing GPIO.");
    InitializeGPIO();
    System.out.println("Initializing States.");
    InitializeStates();
    System.out.println("Initializing Button Listeners.");
    InitializeButtonListeners();
  }

  public void InitializeGPIO() {
    // create gpio controller
    gpio = GpioFactory.getInstance();

    // provision gpio input pins with internal pull down resistors enabled
    primaryButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, PinPullResistance.PULL_DOWN);
    toggleButton  = gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, PinPullResistance.PULL_DOWN);

    // set shutdown states for button input pins
    primaryButton.setShutdownOptions(true);
    toggleButton .setShutdownOptions(true);

    // provision LED01 gpio output pin and set shutdown state
    LED_01 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "LED_01", PinState.LOW);
    LED_01.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    
    // privision LED02 gpio output pin and pwm
    LED2pin = CommandArgumentParser.getPin(RaspiPin.class, RaspiPin.GPIO_01);
    LED2pwm = gpio.provisionPwmOutputPin(LED2pin);
  }

  public void InitializeStates() {
    // set initial button and LED states
    primaryButtonMode = PRIMARY_BUTTON_MODE.BLINKING;
    blinkIncreaseLED1 = LED_01_BLINK_INCREASE.ON;
    brightnessLED2 = LED_02_BRIGHTNESS.L1;
    blinkDelayIndexLED1 = 0;
    blinkDelayIndexLED2 = 0;
    blinkCounterLED1 = 0;
    blinkCounterLED2 = 0;
    blinkTime = 200;
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
        // only on button down
        if(event.getState() == PinState.HIGH){
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
					brightnessLED2 = LED_02_BRIGHTNESS.L4;
				  break;
				  case L4:
					brightnessLED2 = LED_02_BRIGHTNESS.L1;
				  break;
				  default:
				  break;
				}
			  break;
			  default:
			  break;
			}
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
        // only on button down
        if(event.getState() == PinState.HIGH){
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
		}

        // display primary button mode after changes
        System.out.println(" AFTER:");
        System.out.println("   --> PRIMARY_BUTTON_MODE: " + primaryButtonMode);
      }
    });
  }

  private static void LED1() {
    while(true) {
      switch (blinkIncreaseLED1) {
        case ON:
          // update blink counter
          blinkCounterLED1 += 1;

          if(blinkCounterLED1 > LED_BLINK_COUNTER_MAX){
            blinkCounterLED1 = 1;

            // update blink delay
            blinkDelayIndexLED1 += 1;
            if(blinkDelayIndexLED1 >= LED_01_BLINK_DELAY.length) {
              blinkDelayIndexLED1 = 0;
            }
          }
        break;
        default:
        break;
      }

      // pulse LED 1
      LED_01.pulse(blinkTime, true);
      
      // sleep
      try {
        Thread.sleep(LED_01_BLINK_DELAY[blinkDelayIndexLED1]);
      } catch (InterruptedException ex) {
        System.out.println("Tried to sleep LED1 but couldn't.");
      }
    }
  }

  private static void LED2() {
    while(true) {
      // update blink counter
      blinkCounterLED2 += 1;

      if(blinkCounterLED2 > LED_BLINK_COUNTER_MAX) {
        blinkCounterLED2 = 1;

        // update blink delay
        blinkDelayIndexLED2 += 1;
        if(blinkDelayIndexLED2 >= LED_02_BLINK_DELAY.length) {
          blinkDelayIndexLED2 = 0;
        }
      }

      // pulse LED 2
      switch (brightnessLED2) {
        case L1:
          LED2pwm.setPwm(50);
          try {
			Thread.sleep(blinkTime);
		  } catch (InterruptedException ex) {
			System.out.println("Tried to blink LED2 but couldn't.");
		  }
		  LED2pwm.setPwm(0);
        break;
        case L2:
          LED2pwm.setPwm(300);
          try {
			Thread.sleep(blinkTime);
		  } catch (InterruptedException ex) {
			System.out.println("Tried to blink LED2 but couldn't.");
		  }
		  LED2pwm.setPwm(0);
        break;
        case L3:
          LED2pwm.setPwm(600);
          try {
			Thread.sleep(blinkTime);
		  } catch (InterruptedException ex) {
			System.out.println("Tried to blink LED2 but couldn't.");
		  }
		  LED2pwm.setPwm(0);
        break;
        case L4:
          LED2pwm.setPwm(1000);
          try {
			Thread.sleep(blinkTime);
		  } catch (InterruptedException ex) {
			System.out.println("Tried to blink LED2 but couldn't.");
		  }
		  LED2pwm.setPwm(0);
        break;
        default:
        break;
      }

      // sleep
      try {
        Thread.sleep(LED_02_BLINK_DELAY[blinkDelayIndexLED2]);
      } catch (InterruptedException ex) {
        System.out.println("Tried to sleep LED2 but couldn't.");
      }
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
