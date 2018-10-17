import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.util.CommandArgumentParser;

public class Step3 {
	
	// GPIO variables
	public static GpioController gpio;
	public static GpioPinDigitalInput primaryButton;
	
	public Step3() {
		// create gpio controller
		gpio = GpioFactory.getInstance();

		// provision gpio input pins with internal pull down resistors enabled
		primaryButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, PinPullResistance.PULL_DOWN);
		
		// set shutdown states for button input pins
		primaryButton.setShutdownOptions(true);
	}
  
	public static void main(String args[]) throws InterruptedException {

    new Step3();

    // keep program running until user aborts (CTRL-C)
    while(true) {
		
        Thread.sleep(500);
    }

    // stop all GPIO activity/threads by shutting down the GPIO controller
    // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
    // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
  }
}
