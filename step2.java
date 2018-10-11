import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class step2 {

    // button and LED states
    public static enum PRIMARY_BUTTON_MODE{BLINKING, DIMMING}
    public static enum LED_01_BLINK_INCREASE{ON, OFF}
    public static enum LED_01_BLINK_DELAY{2000, 1500, 1000, 500, 250}
    public static enum LED_02_BLINK_DELAY{2000, 1500, 1000, 500, 250}
    public static enum LED_02_BRIGHTNESS{L1, L2, L3}

    // button and LED state variables
    public static PRIMARY_BUTTON_MODE primaryButtonMode;
    public static LED_01_BLINK_INCREASE blinkIncreaseLED1;
    public static LED_01_BLINK_DELAY blinkDelayLED1;
    public static LED_02_BLINK_DELAY blinkDelayLED2;
    public static LED_02_BRIGHTNESS brightnessLED2;

    public static void main(String args[]) throws InterruptedException {
        System.out.println("<--Step2--> GPIO Listeners Test ... started.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio input pins with internal pull down resistors enabled
        final GpioPinDigitalInput primaryButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput toggleButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);

        // set shutdown states for button input pins
        primaryButton.setShutdownOptions(true);
        toggleButton.setShutdownOptions(true);

        // provision gpio output pins and turn on
        final GpioPinDigitalOutput LED_01 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26, "LED_01", PinState.HIGH);
        final GpioPinDigitalOutput LED_02_L1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "LED_02_L1", PinState.HIGH);
        final GpioPinDigitalOutput LED_02_L2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20, "LED_02_L2", PinState.LOW);
        final GpioPinDigitalOutput LED_02_L3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "LED_02_L3", PinState.LOW);

        // set shutdown states for LED pins
        LED_01.setShutdownOptions(true, PinState.LOW);
        LED_02_L1.setShutdownOptions(true, PinState.LOW);
        LED_02_L2.setShutdownOptions(true, PinState.LOW);
        LED_02_L3.setShutdownOptions(true, PinState.LOW);

        // create and register primary button gpio pin listener
        primaryButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display primary button mode on console
                System.out.println(" --> Current PRIMARY_BUTTON_MODE: " + primaryButtonMode);

                // display pin state on console
                System.out.println(" --> PRIMARY BUTTON GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                // perform primary button action
                switch (primaryButtonMode) {
                  case BLINKING:
                    // update blink increase state
                  break;
                  case DIMMING:
                    // update brightness level state
                    // call function to update brightness
                  break;
                  default:
                  break;
                }
            }

        });

        // create and register toggle button gpio pin listener
        toggleButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display primary button mode on console
                System.out.println(" --> Current PRIMARY_BUTTON_MODE: " + primaryButtonMode);

                // display pin state on console
                System.out.println(" --> TOGGLE BUTTON GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

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

                // display primary button mode on console
                System.out.println("|>| PRIMARY_BUTTON_MODE changed to: " + primaryButtonMode);
            }

        });

        System.out.println(" ... See the listener feedback here in the console:");

        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}
